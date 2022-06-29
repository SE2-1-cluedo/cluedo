package at.moritzmusel.cluedo.network;

import android.content.Context;
import android.net.sip.SipSession;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import at.moritzmusel.cluedo.communication.NetworkCommunicator;
import at.moritzmusel.cluedo.entities.Character;
import at.moritzmusel.cluedo.network.pojo.GameState;
import at.moritzmusel.cluedo.network.pojo.Question;
import at.moritzmusel.cluedo.entities.Player;

public class Network {
    private static final String TAG = "Networking ";
    private static final FirebaseDatabase fb = FirebaseDatabase.getInstance("https://cluedo-b12c1-default-rtdb.europe-west1.firebasedatabase.app/");
    private static final DatabaseReference database = fb.getReference();
    private static final DatabaseReference games = database.child("games");
    private static String currentGameID;
    private static Character currentCharacter = Character.getFirstCharacter();
    private static GameState gameState;
    private static FirebaseUser currentUser;
    private static final int[] killer = new int[3];
    private static NetworkCommunicator networkCommunicator = NetworkCommunicator.getInstance();
    private static final ValueEventListener turnFlagListener = new ValueEventListener() {

        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            //start Game
            String start = (String) snapshot.child("startGame").getValue();
            assert start != null;
            if(start.equals("start")) {
                NetworkCommunicator.getInstance().setStartGame(true);
                NetworkCommunicator.getInstance().notifyList();
            }
            //get PlayerTurn
            getGameState().setPlayerTurn((String)snapshot.child("player-turn").getValue(),false);

            //getMagnify
            String[] magnify = ((String) Objects.requireNonNull(snapshot.child("magnify").getValue())).split(" ");
            if(magnify.length == 2)
                getGameState().setMagnify(magnify,false);

            //get Question
            String[] question = ((String) Objects.requireNonNull(snapshot.child("question").getValue())).split(" ");
            if(question.length == 4) {
                System.out.println("Question listened");
                int[] numbers = new int[]{Integer.parseInt(question[1]), Integer.parseInt(question[2]), Integer.parseInt(question[3])};
                getGameState().setAskQuestion(new Question(question[0],numbers),false);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {}
    };
    private static final ValueEventListener weaponsListener = new ValueEventListener() {

        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            //get weapons
            String[] weapons = ((String) Objects.requireNonNull(snapshot.getValue())).split(" ");
            int[] weaponPositions = Stream.of(weapons)
                    .mapToInt(Integer::parseInt).toArray();
            getGameState().setWeaponPositions(weaponPositions,false);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {}
    };
    private static final ValueEventListener resultListener = new ValueEventListener() {

        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            //get Winner/Loser
            String winner = (String) Objects.requireNonNull(snapshot.child("winner").getValue());
            if(winner.length() > 0)
            getGameState().setWinner(winner,false);

            String loser = (String) Objects.requireNonNull(snapshot.child("loser").getValue());
            if(loser.length() > 0)
            getGameState().setLoser(loser,false);

            String framed = (String) Objects.requireNonNull(snapshot.child("framed").getValue());
            if(framed.length() > 0)
                getGameState().setFramed(framed, false);

            String framer = (String) Objects.requireNonNull(snapshot.child("framer").getValue());
            if(framed.length() > 0)
                getGameState().setFramer(framer, false);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
    private static final ValueEventListener playerListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            //get Players
            ArrayList<Player> playerList = new ArrayList<>();
            boolean playerChanged = false;
            for (DataSnapshot snap : snapshot.getChildren()) {
                if (snap.child("character").exists() && snap.child("position").exists() && snap.child("cards").exists()) {
                        Player p = new Player(snap.getKey());
                        playerChanged = true;

                        //set character
                        NetworkCommunicator.getInstance().setCharacterChanged(true);
                        p.setPlayerCharacterName(Character.valueOf((String) Objects.requireNonNull(snap.child("character").getValue())));

                       //setPosition
                        p.setPositionOnBoard(Integer.parseInt((String) Objects.requireNonNull(snap.child("position").getValue())));

                        //set owned Cards
                        String[] ownedCards = ((String) (Objects.requireNonNull(snap.child("cards").getValue()))).split(" ");
                        if (!Objects.equals(ownedCards[0], "")) {

                            p.setPlayerOwnedCards((ArrayList<Integer>) Arrays.stream(Stream.of(ownedCards)
                                    .mapToInt(Integer::parseInt).toArray()).boxed().collect(Collectors.toList()));
                        }
                    playerList.add(p);
                }
            }
            if (playerChanged)
                getGameState().setPlayerState(playerList, false);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {}
    };
    private static final ValueEventListener turnOrderListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            String[] turnOrder = ((String) Objects.requireNonNull(snapshot.getValue())).split(" ");
            if(turnOrder.length > 1)
                gameState.setTurnOrder(turnOrder,false);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
    private static final ValueEventListener eliminatedListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            //set known Cards
            String[] knownCards = ((String) Objects.requireNonNull(snapshot.getValue())).split(" ");
            if (!Objects.equals(knownCards[0], "")) {
                gameState.setEliminatedCards(Arrays.stream(Stream.of(knownCards)
                        .mapToInt(Integer::parseInt).toArray()).boxed().collect(Collectors.toList()),false);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    public static DatabaseReference getDatabaseReference(){
        return games;
    }
    public static Context ctx;

    //Wird aufgerufen wenn eine Lobby erstellt wird
    //DONE
    //TODO: Fix wrong date being asserted into db (Offset of 10h 15min)
    public static String createLobby(FirebaseUser user) {
        if (user == null) {
            Log.e(TAG, "Dont forget to authenticate and pass your Firebase user before calling `createLobby()`!");
            return null;
        }
        if (getCurrentGameID() != null) {
            Log.e(TAG, "Cant create game if you are already inside one! Call leaveLobby() first.");
            return null;
        }
        setCurrentUser(user);
        Date current = Calendar.getInstance().getTime();
        String gameID = intToChars(current.hashCode());
        setCurrentGameID(gameID);

        DatabaseReference game = database.child("games").child(gameID);
        SimpleDateFormat formated = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        formated.setTimeZone(TimeZone.getTimeZone("GMT"));
        game.child("timestamp").setValue(formated.format(current));

        //add Turn Flag path
        gameState = GameState.getInstance();
        DatabaseReference turnFlag = game.child("turn-flag");
        turnFlag.child("question").setValue("");
        turnFlag.child("player-turn").setValue(getCurrentUser().getUid());
        turnFlag.child("magnify").setValue("");
        turnFlag.child("startGame").setValue("waiting");
        turnFlag.addValueEventListener(turnFlagListener);

        //add eliminated-Cards
        game.child("cards-eliminated").setValue("");
        game.child("cards-eliminated").addValueEventListener(eliminatedListener);

        //add result path
        DatabaseReference result = game.child("result");
        result.child("winner").setValue("");
        result.child("loser").setValue("");
        result.child("framed").setValue("");
        result.child("framer").setValue("");
        result.addValueEventListener(resultListener);

        gameState.setWeaponPositions(gameState.getWeaponPositions(),true);

        //set killer
        gameState.setKiller(createKiller());

        game.child("turn-order").setValue("");
        game.child("turn-order").addValueEventListener(turnOrderListener);
        //add players path
        game.child("killer").setValue(gameState.getKillerAsString());
        DatabaseReference p = game.child("players").child(user.getUid());
        p.child("cards").setValue("");
        p.child("position").setValue(String.valueOf(new SecureRandom().nextInt(9)+1));
        p.child("character").setValue(getCurrentCharacter().name());

        game.child("weapon-positions").addValueEventListener(weaponsListener);
        game.child("players").addValueEventListener(playerListener);

        return gameID;
    }

    private static int[] createKiller(){
        int counter = 0;
        for(int i = 0; i < 3; i++){
            if(i == 2)
                killer[i] = new SecureRandom().nextInt(9) + counter;
            else {
                killer[i] = new SecureRandom().nextInt(6) + counter;
                counter += 6;
            }
        }
        return killer;
    }

    public static Character getCurrentCharacter() {
        return currentCharacter;
    }

    public static DatabaseReference getCurrentGame(){
        return games.child(getCurrentGameID());
    }

    //Wird aufgerufen nachdem eine Lobby erstellt wurde. Es wird der Nutzer, welcher die Lobby erstellt hat hinzugefügt
    //Parameter 3 "Player" enthält die bereits
    public static void joinLobby(FirebaseUser user, String gameID) {
        setCurrentGameID(gameID);
        setCurrentUser(user);

        ArrayList<String> joinedCharacters = new ArrayList<>();
        getCurrentGame().get().addOnCompleteListener(task -> {
            if (!task.isSuccessful())
                Log.e("firebase", "Error getting data", task.getException());
            else {
                for(DataSnapshot snap: task.getResult().child("players").getChildren()) {
                    joinedCharacters.add((String) snap.child("character").getValue());
                }
                while(joinedCharacters.contains(currentCharacter.name())){
                    currentCharacter = currentCharacter.getNextCharacter();
                }
                    DatabaseReference p = games.child(gameID).child("players").child(user.getUid());
                    p.child("character").setValue(currentCharacter.name());
                    p.child("cards").setValue("");
                    p.child("position").setValue(String.valueOf(new SecureRandom().nextInt(9)+1));

                    gameState = GameState.getInstance();
                    gameState.setKiller(killer);

                    getCurrentGame().child("players").addValueEventListener(playerListener);
                    getCurrentGame().child("weapon-positions").addValueEventListener(weaponsListener);
                    getCurrentGame().child("turn-flag").addValueEventListener(turnFlagListener);
                    getCurrentGame().child("result").addValueEventListener(resultListener);
                    getCurrentGame().child("turn-order").addValueEventListener(turnOrderListener);
                    getCurrentGame().child("cards-eliminated").addValueEventListener(eliminatedListener);
            }
        });


        //check if FB user and game exists
        Log.i(TAG, ("joinLobby() called params:"+ user +" "+ (getCurrentGameID() == null)));

    }


    public static void leaveLobby(FirebaseUser user, String gameID) {
        detachListeners();

        currentCharacter = Character.MISS_SCARLETT;
        OnDataRetreive onDataRetreive = new OnDataRetreive() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                DatabaseReference player = games.child(gameID).child("players").child(user.getUid());
                if (dataSnapshot!= null && dataSnapshot.exists() && getCurrentGameID() != null){
                    player.removeValue();
                    setCurrentGameID(null);
                    gameState.reset();
                    networkCommunicator.reset();
                    setGameState(null);
                }
            }

            @Override
            public void onFailure(Object error) {
                Log.e(TAG, ((Exception)error).getMessage());
            }
        };
        checkIfGameExists(gameID, onDataRetreive);
    }

    public static void startGame(String gameID, List<Player> list) {
        DatabaseReference game = games.child(gameID);
        StringBuilder sbTurnOrder = new StringBuilder();
        String[] turnOrder = new String[list.size()];
        //set turnOrder
        for(int i = 0; i < list.size(); i++){
            sbTurnOrder.append(list.get(i).getPlayerId()).append(" ");
            turnOrder[i] = list.get(i).getPlayerId();
        }

        game.child("turn-order").setValue(sbTurnOrder.toString().trim());
        gameState.setTurnOrder(turnOrder,true);

        game.child("turn-flag").child("startGame").setValue("start");

        gameState.setPlayerState(list,false);
        gameState.assignCards();
    }

    //Für Karten zum Ausscheiden hinzu
    public static void updatePlayerEliminationCard(FirebaseUser user, int cardID, String gameID) {
        DatabaseReference eliminations = games.child(gameID).child("players").child(user.getUid()).child("elimination");
        eliminations.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String eliminatedCards = (String) dataSnapshot.getValue();
                if (eliminatedCards != null && eliminatedCards.equals(""))
                    eliminations.setValue(String.valueOf(cardID));
                else
                    eliminations.setValue(eliminatedCards + ", " + cardID);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, error.getMessage());
            }
        });
    }

    public static void updateGameState(String gameID) {
        //TODO: Update GameState class on every database update
        getGameState().getCardState();
        getGameState().getPlayerState();
    }

    private static String intToChars(long number) {
        StringBuilder rt = new StringBuilder();
        char[] tmp = (String.valueOf(number) + ThreadLocalRandom.current().nextLong(10000, 99999)).toCharArray();
        for (int i = 6; i < tmp.length; ) {
            if (i < (tmp.length - 2)) {
                long nmbr = (long) tmp[i] + (long) tmp[i + 1] - 94;
                char t;
                if (nmbr <= 26) {
                    t = (char) (nmbr + 65);
                    rt.append(t);
                    i += 2;
                } else {
                    t = (char) (tmp[i] + 65);
                    rt.append(t);
                    i++;
                }
            } else {
                i++;
            }
        }
        return rt.toString();
    }

    public static String getCurrentGameID() {
        return currentGameID;
    }

    private static void setCurrentGameID(String currentGameID) {
        Network.currentGameID = currentGameID;
    }

    public static void initGameState(GameState gameState)   {
        if (getCurrentGameID() != null && Network.gameState == null)
            Network.gameState = gameState;
    }

    private static GameState getGameState() {
        return gameState;
    }

    private static void setGameState(GameState state) {
        Network.gameState = state;
    }

    public static FirebaseUser getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(FirebaseUser currentUser) {
        Network.currentUser = currentUser;
    }

    private static void checkIfGameExists(String gameID, OnDataRetreive onDataRetreive) {
        Log.i(TAG, "checkIfGameExists() called params:"+gameID);
        if(gameID == null || gameID.isEmpty()) {
            onDataRetreive.onSuccess(null);
            onDataRetreive.onFailure(new Exception("GameID cannot be null or empty!"));
        }else {
            games.child(gameID).get().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) onDataRetreive.onFailure(task.getException());
                else onDataRetreive.onSuccess(task.getResult());
            });
        }
    }

    public static void signInAnonymously(FirebaseAuth mAuth) {
        if (mAuth != null) {
            mAuth.signInAnonymously().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                } else {
                    Log.w(TAG, "signInAnonymously:failure", task.getException());
                }
            });
        }
    }

    public static Context getCtx() {
        return ctx;
    }

    public static void setCtx(Context ctx) {
        Network.ctx = ctx;
    }

    public static void test(){
        //Log.i(TAG, ""+checkIfGameExists(getCurrentGameID()));
    }
    private static void detachListeners(){
        getCurrentGame().child("cards-eliminated").removeEventListener(eliminatedListener);
        getCurrentGame().child("players").removeEventListener(playerListener);
        getCurrentGame().child("weapon-positions").removeEventListener(weaponsListener);
        getCurrentGame().child("turn-flag").removeEventListener(turnFlagListener);
        getCurrentGame().child("result").removeEventListener(resultListener);
        getCurrentGame().child("turn-order").removeEventListener(turnOrderListener);
    }
    public static void deleteGame(){
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            detachListeners();
            games.child(getCurrentGameID()).removeValue();
        }, 5000);
    }
}
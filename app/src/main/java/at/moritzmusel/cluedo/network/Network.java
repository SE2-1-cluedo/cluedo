package at.moritzmusel.cluedo.network;

import android.content.Context;
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
        //add killer to database
        gameState.setKiller(createKiller());
        gameState.setWeaponPositions(gameState.getWeaponPositions(),true);
        game.child("turn-order").setValue("");
        game.child("killer").setValue(gameState.getKillerAsString());
        //add player path
        DatabaseReference p = game.child("players").child(user.getUid());
        p.child("cards").setValue("");
        p.child("cards-eliminated").setValue("");
        p.child("position").setValue(String.valueOf(new SecureRandom().nextInt(9)+1));
        p.child("character").setValue(getCurrentCharacter().name());

        gamestate_databaseListener();
        return gameID;
    }

    private static int[] createKiller(){
        int[] killer = new int[3];
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

    private static void gamestate_databaseListener(){
        games.child(getCurrentGameID()).addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get PlayerTurn
              getGameState().setPlayerTurn((String)snapshot.child("turn-flag").child("player-turn").getValue(),false);

              //get Question
              String[] question = ((String) Objects.requireNonNull(snapshot.child("turn-flag").child("question").getValue())).split(" ");
              if(question.length == 4) {
                  int[] numbers = new int[]{Integer.parseInt(question[1]), Integer.parseInt(question[2]), Integer.parseInt(question[3])};
                  getGameState().setAskQuestion(new Question(question[0],numbers),false);
              }

              //get weapons
              String[] weapons = ((String) Objects.requireNonNull(snapshot.child("weapon-positions").getValue())).split(" ");
                int[] weaponPositions = Stream.of(weapons)
                        .mapToInt(Integer::parseInt).toArray();
              getGameState().setWeaponPositions(weaponPositions,false);

              //get Players
                ArrayList<Player> playerList = new ArrayList<>();
                boolean playerChanged = false;
                for (DataSnapshot snap: snapshot.child("players").getChildren()){
                    Player p = new Player(snap.getKey());
                    //set character
                    if(snap.child("character").exists()) {
                        playerChanged = true;
                        p.setPlayerCharacterName(Character.valueOf((String) Objects.requireNonNull(snap.child("character").getValue())));
                    }
                    //set position
                    if(snap.child("position").exists()) {
                        playerChanged = true;
                        p.setPositionOnBoard(Integer.parseInt((String) Objects.requireNonNull(snap.child("position").getValue())));
                    }
                    //set owned Cards
                    if(snap.child("cards").exists()) {
                        String[] ownedCards = ((String) (Objects.requireNonNull(snap.child("cards").getValue()))).split(" ");
                        if (!Objects.equals(ownedCards[0], "")) {
                            playerChanged = true;
                            p.setPlayerOwnedCards((ArrayList<Integer>) Arrays.stream(Stream.of(ownedCards)
                                    .mapToInt(Integer::parseInt).toArray()).boxed().collect(Collectors.toList()));
                        }
                    }
                    //set known Cards
                    if(snap.child("cards-eliminated").exists()) {
                        String[] knownCards = ((String) Objects.requireNonNull(snap.child("cards-eliminated").getValue())).split(" ");
                        if (!Objects.equals(knownCards[0], "")) {
                            playerChanged = true;
                            p.setCardsKnownThroughQuestions((ArrayList<Integer>) Arrays.stream(Stream.of(knownCards)
                                    .mapToInt(Integer::parseInt).toArray()).boxed().collect(Collectors.toList()));
                        }
                    }
                    playerList.add(p);
                }
                if(playerChanged)
                 getGameState().setPlayerState(playerList,false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public static DatabaseReference getCurrentGame(){
        return games.child(getCurrentGameID());
    }

    //Wird aufgerufen nachdem eine Lobby erstellt wurde. Es wird der Nutzer, welcher die Lobby erstellt hat hinzugefügt
    //Parameter 3 "Player" enthält die bereits
    public static void joinLobby(FirebaseUser user, String gameID) {
        //check if FB user and game exists
        //Log.i(TAG, ("joinLobby() called params:"+ user +" "+ (getCurrentGameID() == null) +" "+ checkIfGameExists(gameID)));
        OnDataRetreive onDataRetreive = new OnDataRetreive() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if (user != null && getCurrentGameID() == null) {
                        setCurrentGameID(gameID);
                        setCurrentUser(user);
                        currentCharacter = currentCharacter.getNextCharacter();
                        DatabaseReference p = games.child(gameID).child("players").child(user.getUid());
                        p.child("cards").setValue("");
                        p.child("cards-eliminated").setValue("");
                        p.child("position").setValue(String.valueOf(new SecureRandom().nextInt(9)+1));
                        p.child("character").setValue(currentCharacter.name());
                        gamestate_databaseListener();
                    } else {
                        throw new IllegalStateException("Make sure you are not already in a game when joining!");
                    }
                }
            }

            @Override
            public void onFailure(Object error) {
                Log.e(TAG, ((Exception)error).getMessage());
            }
        };
        checkIfGameExists(gameID, onDataRetreive);
    }

    public static void leaveLobby(FirebaseUser user, String gameID) {
        OnDataRetreive onDataRetreive = new OnDataRetreive() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                DatabaseReference player = games.child(gameID).child("players").child(user.getUid());
                if (dataSnapshot!= null && dataSnapshot.exists() && getCurrentGameID() != null && user != null){
                    player.removeValue();
                    setCurrentGameID(null);
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

        //set turnOrder
        for(Player p: list)
            sbTurnOrder.append(p.getPlayerId()).append(" ");

        game.child("turn-order").setValue(sbTurnOrder.toString().trim());

        game.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful())
                Log.e("firebase", "Error getting data", task.getException());
            else {
                String[] turnOrder = String.valueOf(task.getResult().child("turn-order").getValue()).split(" ");
                gameState.setTurnOrder(turnOrder);
                for (Player p : list)
                    p.setPositionOnBoard(Integer.parseInt((String) Objects.requireNonNull(task.getResult().child("players").child(p.getPlayerId()).child("position").getValue())));

                gameState.setPlayerState(list,true);
                gameState.setWeaponPositions(gameState.getWeaponPositions(),true);
                setGameState(gameState);
                gameState.assignCards();
            }
        });
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

    private static String intToChars(int number) {
        StringBuilder rt = new StringBuilder();
        char[] tmp = (Integer.toString(number) + ThreadLocalRandom.current().nextInt(10000, 99999)).toCharArray();
        for (int i = 0; i < tmp.length; ) {
            if (i < (tmp.length - 2)) {
                int nmbr = (int) tmp[i] + (int) tmp[i + 1] - 94;
                char t;
                if (nmbr <= 26) {
                    t = (char) (nmbr + 64);
                    rt.append(t);
                    i += 2;
                } else {
                    t = (char) (tmp[i] + 64);
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
}
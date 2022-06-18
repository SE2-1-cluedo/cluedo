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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import at.moritzmusel.cluedo.entities.Character;
import at.moritzmusel.cluedo.network.pojo.CardState;
import at.moritzmusel.cluedo.network.pojo.GameState;
import at.moritzmusel.cluedo.network.pojo.Killer;
import at.moritzmusel.cluedo.network.pojo.Question;
import at.moritzmusel.cluedo.entities.Player;

public class Network {
    private static final String TAG = "Networking ";
    private static final FirebaseDatabase fb = FirebaseDatabase.getInstance("https://cluedo-b12c1-default-rtdb.europe-west1.firebasedatabase.app/");
    private static final DatabaseReference database = fb.getReference();
    private static final DatabaseReference games = database.child("games");
    private static String currentGameID;
    private static GameState gameState;
    private static FirebaseUser currentUser;
    private static Context ctx;

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
        DatabaseReference game = games.child(gameID);
        SimpleDateFormat formated = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        formated.setTimeZone(TimeZone.getTimeZone("GMT"));
        game.child("timestamp").setValue(formated.format(current));
        //add Turn Flag path

        DatabaseReference turnFlag = game.child("turn-flag");
        turnFlag.child("question").setValue("");
        turnFlag.child("player-turn").setValue("");
        //add players path
        game.child("weapon-positions").setValue("");
        game.child("turn-order").setValue("");
        game.child("killer").setValue("");
        DatabaseReference p = game.child("players").child(user.getUid());
        p.child("cards").setValue("");
        p.child("cards-eliminated").setValue("");
        p.child("position").setValue("");
        p.child("character").setValue("");
        games.child("JHCIEMJ").addValueEventListener(new ValueEventListener() {
            AtomicBoolean bool = new AtomicBoolean(false);
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && !bool.get()){
                    joinLobby(user.getUid(), gameID);
                    bool.set(true);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        return gameID;
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
                for (DataSnapshot snap: snapshot.child("players").getChildren()){
                    Player p = new Player(snap.getKey(), Character.valueOf((String) snap.child("character").getValue()));
                    //set position
                    p.setPositionOnBoard(Integer.parseInt((String) Objects.requireNonNull(snap.child("position").getValue())));
                    //set owned Cards
                    String[] ownedCards = ((String) (Objects.requireNonNull(snap.child("cards").getValue()))).split(" ");
                    p.setPlayerOwnedCards((ArrayList<Integer>)Arrays.stream(Stream.of(ownedCards)
                            .mapToInt(Integer::parseInt).toArray()).boxed().collect(Collectors.toList()));
                    //set known Cards
                    String[] knownCards = ((String) Objects.requireNonNull(snap.child("cards-eliminated").getValue())).split(" ");
                    if(!Objects.equals(knownCards[0],""))
                    p.setCardsKnownThroughQuestions((ArrayList<Integer>)Arrays.stream(Stream.of(knownCards)
                            .mapToInt(Integer::parseInt).toArray()).boxed().collect(Collectors.toList()));

                    playerList.add(p);
                }
                getGameState().setPlayerState(playerList,false);

                System.out.println("Ohh something changed");
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
    public static boolean joinLobby(String user, String gameID) {
        //check if FB user and game exists
        //Log.i(TAG, "getCurrentGameID() " + getCurrentGameID() + ", user " + user.getUid() + ", gameExists() " + checkIfGameExists(gameID));
        //TODO: Why the fuk funktioniert checkIfGameExists() nicht wenn man lobby joined??!?!
        if (user != null && (getCurrentGameID() == null || getCurrentGameID().equals(gameID)) /*&& checkIfGameExists()*/) {
            setCurrentGameID(gameID);
            //etCurrentUser(user);
            DatabaseReference p = games.child(gameID).child("players").child(user);
            p.child("cards").setValue("");
            p.child("cards-eliminated").setValue("");
            p.child("position").setValue("");
            p.child("character").setValue("");
            //TODO: SET GAMESTATE
            return true;
        } else {
            throw new IllegalStateException("Make sure you are not already in a game when joining!");
        }
    }

    public static boolean leaveLobby(FirebaseUser user, String gameID) {
        //check if user is in a game and if FB user and game exists
        //Log.i(TAG + " leaveLobby() ", "getCurrentGameID() " + getCurrentGameID() + ", user " + user.getUid() + ", gameExists() " + checkIfGameExists(gameID));
        if (getCurrentGameID() != null && user != null && checkIfGameExists(gameID)) {
            setCurrentGameID(null);
            setGameState(null);
            games.child(gameID).child("players").child(user.getUid()).removeValue();
            return true;
        } else {
            return false;
        }
    }

    public static void startGame(String gameID, List<Player> list, Killer killer) {
        DatabaseReference game = games.child(gameID);
        StringBuilder sbTurnOrder = new StringBuilder();
        game.child("killer").setValue(killer.toString());

        //Entferne die 3 Karten aus dem Pool->anschließend die restlichen karten im Kreis verteilen (18Karten)
        //ERROR: Hier werden daten überschrieben
        //TODO: INIT @GameState class
        List<CardState> cardStates = new ArrayList<>(39);
        for (int i = 0; i < 39; i++) {
            cardStates.add(new CardState(i + 1));
        }
        //set turnOrder
        for(Player p: list)
            sbTurnOrder.append(p.getPlayerId()).append(" ");

        game.child("turn-order").setValue(sbTurnOrder.toString().trim());

        game.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful())
                Log.e("firebase", "Error getting data", task.getException());
            else {
                String[] turnOrder = String.valueOf(task.getResult().child("turn-order").getValue()).split(" ");
                gameState = new GameState(list, null, new Question(null, null), killer,ctx,turnOrder);
                gameState.setPlayerState(list,true);
                gameState.setPlayerTurn(turnOrder[0],true);
                gameState.setWeaponPositions(gameState.getWeaponPositions(),true);
                setGameState(gameState);
                gamestate_databaseListener();
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

    private static boolean checkIfGameExists(String gameID) {
        AtomicBoolean gameExists = new AtomicBoolean(false);
        games.child(gameID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("FML", "FMLL");
                if(snapshot.exists() && !gameExists.get() && false){
                    Log.i(TAG + " checkIfGameExists() ", gameExists.get() + " " +String.valueOf(snapshot));
                    gameExists.set(true);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, String.valueOf(error));
            }
        });
        Log.i(TAG, String.valueOf(gameExists.get()));
        return gameExists.get();
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

    public static void setCtx(Context ctx) {
        Network.ctx = ctx;
    }

    public static Context getCtx() {
        return ctx;
    }

    public static void testMETHOD(){
        games.child("CNTKENG").child("players").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i(TAG + " line 272", String.valueOf(snapshot.getChildrenCount()));
                int i = 0;
                /*
                for (DataSnapshot player : snapshot.getChildren()) {
                    givePlayerCards(player.getKey(), list.get(i), gameID);
                    sbTurnOrder.append(player.getValue());
                    i++;
                }
                 */
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}
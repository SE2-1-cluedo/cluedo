package at.moritzmusel.cluedo.network;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

import at.moritzmusel.cluedo.network.pojo.CardState;
import at.moritzmusel.cluedo.network.pojo.GameState;
import at.moritzmusel.cluedo.network.pojo.Killer;
import at.moritzmusel.cluedo.network.pojo.Player;
import at.moritzmusel.cluedo.network.pojo.Question;

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
        DatabaseReference game = database.child("games").child(gameID);
        SimpleDateFormat formated = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        formated.setTimeZone(TimeZone.getTimeZone("GMT"));
        game.child("timestamp").setValue(formated.format(current));
        //add Turn Flag path
        DatabaseReference turnFlag = game.child("turn-flag");
        turnFlag.child("question").setValue("");
        turnFlag.child("player-turn").setValue("");
        //add players path
        game.child("players").setValue("");
        game.child("turn-order").setValue("");
        game.child("killer").setValue("");
        games.child(gameID).addValueEventListener(new ValueEventListener() {
            AtomicBoolean bool = new AtomicBoolean(false);
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && !bool.get()){
                    joinLobby(user, gameID);
                    bool.set(true);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        return gameID;
    }

    //Wird aufgerufen nachdem eine Lobby erstellt wurde. Es wird der Nutzer, welcher die Lobby erstellt hat hinzugefügt
    //Parameter 3 "Player" enthält die bereits
    public static boolean joinLobby(FirebaseUser user, String gameID) {
        //check if FB user and game exists
        //Log.i(TAG, "getCurrentGameID() " + getCurrentGameID() + ", user " + user.getUid() + ", gameExists() " + checkIfGameExists(gameID));
        //TODO: Why the fuk funktioniert checkIfGameExists() nicht wenn man lobby joined??!?!
        if (user != null && (getCurrentGameID() == null || getCurrentGameID().equals(gameID)) /*&& checkIfGameExists()*/) {
            setCurrentGameID(gameID);
            setCurrentUser(user);
            DatabaseReference p = games.child(gameID).child("players").child(user.getUid());
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
        game.child("players").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;
                for (DataSnapshot player : snapshot.getChildren()) {
                    givePlayerCards(player.getKey(), list.get(i), gameID);
                    sbTurnOrder.append(player.getValue());
                    i++;
                }
                game.child("players").updateChildren(new HashMap<>()).addOnCompleteListener(new OnCompleteListener<Void>(){
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //TODO: Update local gamestate
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.getMessage());
            }
        });
        //TODO: INIT @GameState class
        List<CardState> cardStates = new ArrayList<>(21);
        for (int i = 0; i < 21; i++) {
            cardStates.add(new CardState(i + 1));
        }
        //TODO: playerTurn
        initGameState(new GameState(list, cardStates, new Question(null, null), killer, "", getCtx()));
        getGameState().setCardState(null);
        getGameState().setPlayerState(null);

        //set turnOrder
        game.child("turn-order").setValue(sbTurnOrder.toString());
    }

    //Gib Spieler Karten
    public static void givePlayerCards(String userID, Player player, String gameID) {
        Log.i(TAG, userID + " " + player.getCardsAsString());
        games.child(gameID).child("players").child(userID).child("cards").setValue(player.getCardsAsString());
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
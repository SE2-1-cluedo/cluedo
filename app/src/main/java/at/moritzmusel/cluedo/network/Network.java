package at.moritzmusel.cluedo.network;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import at.moritzmusel.cluedo.network.pojo.Card;
import at.moritzmusel.cluedo.network.pojo.CardState;
import at.moritzmusel.cluedo.network.pojo.GameState;
import at.moritzmusel.cluedo.network.pojo.Killer;
import at.moritzmusel.cluedo.network.pojo.Player;

public class Network {
    private static String TAG = "Networking ";
    private static FirebaseDatabase fb = FirebaseDatabase.getInstance("https://cluedo-b12c1-default-rtdb.europe-west1.firebasedatabase.app/");
    private static DatabaseReference database = fb.getReference();
    private static DatabaseReference games = database.child("games");
    private static String currentGameID;
    private static GameState gameState;

    //Wird aufgerufen wenn eine Lobby erstellt wird
    //DONE
    public static String createLobby(FirebaseUser user) {
        if(user==null){
            Log.e(TAG, "Dont forget to authenticate and pass your Firebase user before calling `createLobby()`!");
            return null;
        }
        java.sql.Timestamp current = new java.sql.Timestamp(System.currentTimeMillis());
        String gameID = intToChars(current.hashCode());
        setCurrentGameID(gameID);
        DatabaseReference game = database.child("games").child(gameID);
        //add Turn Flag path
        DatabaseReference turnFlag = game.child("turn-flag");
        turnFlag.child("question").setValue("");
        turnFlag.child("player-turn").setValue("");
        turnFlag.child("killer").setValue("");
        //add players path
        game.child("players").setValue("");
        game.child("turn-order").setValue("");
        return gameID;
    }

    //Wird aufgerufen nachdem eine Lobby erstellt wurde. Es wird der Nutzer, welcher die Lobby erstellt hat hinzugefügt
    //Parameter 3 "Player" enthält die bereits
    public static boolean joinLobby(FirebaseUser user, String gameID) {
        setCurrentGameID(gameID);
        if (user == null) return false;
        DatabaseReference p = games.child(gameID).child("players").child(user.getUid());
        p.child("cards").setValue("");
        p.child("cards-eliminated").setValue("");
        p.child("position").setValue("");
        p.child("character").setValue("");
        return true;
    }

    public static boolean leaveLobby(FirebaseUser user, String gameID) {
        if (user == null) return false;
        setCurrentGameID("");
        games.child(gameID).child("players").child(user.getUid()).removeValue();
        return true;
    }

    public static void startGame(String gameID, List<Player> list, Killer killer) {
        DatabaseReference game = games.child(gameID);
        game.child("killer").setValue(killer.getCardsAsString());
        //Entferne die 3 Karten aus dem Pool->anschließend die restlichen karten im Kreis verteilen (18Karten)
        game.child("cards").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;
                for (DataSnapshot child : snapshot.getChildren()) {
                    givePlayerCards(child.getKey(), list.get(i), gameID);
                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.getMessage());
            }
        });
        //TODO: INIT @GameState class
        initGameState(new GameState());
        getGameState().setCardState(null);
        getGameState().setPlayerState(null);
    }

    //Gib Spieler Karten
    public static void givePlayerCards(String userID, Player player, String gameID) {
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
                    Log.i(TAG, String.valueOf(t));
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

    public static void initGameState(GameState gameState) {
        if (!getCurrentGameID().equals("") && Network.gameState == null)
            Network.gameState = gameState;
    }

    public static GameState getGameState() {
        return gameState;
    }
}

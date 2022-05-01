package at.moritzmusel.cluedo.network;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import at.moritzmusel.cluedo.network.pojo.Card;
import at.moritzmusel.cluedo.network.pojo.Player;

public class Network {
    private static String TAG = "Networking ";
    private static FirebaseDatabase fb = FirebaseDatabase.getInstance("https://cluedo-b12c1-default-rtdb.europe-west1.firebasedatabase.app/");
    private static DatabaseReference database = fb.getReference();
    private static DatabaseReference games = database.child("games");
    private static String currentGameID;


    //Wird aufgerufen wenn eine Lobby erstellt wird
    public static String createLobby(FirebaseUser user) {
        if(user==null){
            Log.e(TAG, "Dont forget to authenticate and pass your Firebase user before calling `create Lobby`!");
            return null;
        }
        java.sql.Timestamp current = new java.sql.Timestamp(System.currentTimeMillis());
        String gameID = intToChars(current.hashCode());
        currentGameID = gameID;
        DatabaseReference game = database.child("games").child(gameID);
        //add Turn Flag path
        DatabaseReference turnFlag = game.child("turn-flag");
        turnFlag.child("question").setValue("");
        turnFlag.child("player-turn").setValue("");
        turnFlag.child("killer").setValue("");
        //add players path
        game.child("players").setValue("");
        return gameID;
    }
    //Wird aufgerufen nachdem eine Lobby erstellt wurde. Es wird der Nutzer, welcher die Lobby erstellt hat hinzugefügt
    //Parameter 3 "Player" enthält die bereits
    public static boolean joinLobby(FirebaseUser user, String gameID, Player player){
        if(user== null) return false;
        DatabaseReference p = games.child(gameID).child("players").child(user.getUid());
        /*
        p.child("cards").setValue(player.getCardsAsString());
        p.child("cards-eliminated").setValue(player.getEliminatedCardsAsString());
         */
        return true;
    }

    public static boolean leaveLobby(FirebaseUser user, String gameID){
        if(user== null) return false;
        games.child(gameID).child("players").child(user.getUid()).removeValue();
        return true;
    }
    //TODO: Logik hier für das Verteilen von Karten
    public static void startGame(String gameID){
        DatabaseReference game = games.child(gameID);
        //Setze killer
        game.child("killer").setValue("");
        //Entferne die 3 Karten aus dem Pool->anschließend die restlichen karten im Kreis verteilen (18Karten)
        game.child("cards").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child : snapshot.getChildren()){
                    //TODO: Cards distribute Logic
                    givePlayerCards(child.getKey(), /*Logik für karten verteilen*/new int[]{1,2,3}, gameID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.getMessage());
            }
        });
    }
    //Gib Spieler Karten
    public static void givePlayerCards(String uuid, int[] cardIDs, String gameID){
        games.child(gameID).child("players").child(uuid).child("cards").setValue(cardIDs);
    }
    //Für Karten zum Ausscheiden hinzu
    public static void addPlayerEliminationCard(FirebaseUser user, int cardID, String gameID){
        DatabaseReference eliminations = games.child(gameID).child("players").child(user.getUid()).child("elimination");
        eliminations.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String eliminatedCards = (String) dataSnapshot.getValue();
                eliminations.setValue(eliminatedCards + " " + cardID);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, error.getMessage());
            }
        });
    }



    private static String intToChars(int number) {
        String rt = "";
        char[] tmp = (Integer.toString(number)+ ThreadLocalRandom.current().nextInt(10000, 99999)).toCharArray();
        for (int i = 0; i < tmp.length;) {
            if(i < (tmp.length - 2)) {
                int nmbr = (int) tmp[i] + (int) tmp[i + 1] - 94;
                char t;
                if (nmbr <= 26) {
                    t = (char) (nmbr + '@');
                    rt += t;
                    i += 2;
                } else {
                    t = (char) (tmp[i] + '@');
                    rt += t;
                    i++;
                }
            }else{
                i++;
            }
        }
        return rt;
    }

    public static String getCurrentGameID() {
        return currentGameID;
    }
}

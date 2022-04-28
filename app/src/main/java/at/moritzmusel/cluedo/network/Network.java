package at.moritzmusel.cluedo.network;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Network {
    private static String TAG = "Networking ";
    private static FirebaseDatabase fb = FirebaseDatabase.getInstance("https://cluedo-b12c1-default-rtdb.europe-west1.firebasedatabase.app/");
    private static DatabaseReference database = fb.getReference();

    public static void initDB(){
        if(fb == null || database == null){
            fb = FirebaseDatabase.getInstance("https://cluedo-b12c1-default-rtdb.europe-west1.firebasedatabase.app/");
            database = fb.getReference();
        }
    }
    public static String createLobby() {
        if(database==null){
            Log.e(TAG, "Dont forget to call initDB()!");
            return null;
        }
        java.sql.Timestamp current = new java.sql.Timestamp(System.currentTimeMillis());
        String gameid = "gameid-" + intToChars(current.hashCode());
        DatabaseReference game = database.child("games").child(gameid);
        game.child("Turn-Flag");
        game.child("Players");
        game.child("Killer");
        return gameid;
    }
    public static void joinLobby(){

    }
    public static void leaveLobby(){

    }
    public static void startGame(){

    }
    public static void loginAnonymousUser(String username){

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
}

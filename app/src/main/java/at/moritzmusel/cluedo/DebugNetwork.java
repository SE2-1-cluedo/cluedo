package at.moritzmusel.cluedo;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import at.moritzmusel.cluedo.network.Network;
import at.moritzmusel.cluedo.network.data.QuestionCards;
import at.moritzmusel.cluedo.network.pojo.Card;
import at.moritzmusel.cluedo.network.pojo.Killer;
import at.moritzmusel.cluedo.network.pojo.Player;

public class DebugNetwork extends AppCompatActivity {

    private static final String TAG_DEBUG = "DEBUG Activity ~";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_network);
        //init auth
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        Network.signInAnonymously(mAuth);
        FirebaseUser user = mAuth.getCurrentUser();
        //init cards
        List<Player> list = new ArrayList<>();
        //player
        List<Card> card1 = new ArrayList<>();
        card1.add(new Card(2));
        card1.add(new Card(5));
        card1.add(new Card(3));
        card1.add(new Card(12));
        list.add(new Player(card1));
        //killer
        List<Card> killer = new ArrayList<>();
        killer.add(new Card(1));
        killer.add(new Card(7));
        killer.add(new Card(16));

        final Button btn = findViewById(R.id.create);
        btn.setOnClickListener(click -> {
            Network.setCtx(this);
            Network.createLobby(user);
            Network.startGame(Network.getCurrentGameID(), list, new Killer(killer));
            //Network.leaveLobby(user, Network.getCurrentGameID());
        });
        /*
        TextView view = findViewById(R.id.debug);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) if (Network.getCurrentGameID() != null) break;
                Log.i(TAG_DEBUG, Network.getCurrentGameID());
                view.setText(Network.getCurrentGameID());
            }
        }).start();*/
    }
}
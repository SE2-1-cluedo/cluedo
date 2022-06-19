package at.moritzmusel.cluedo;

import static at.moritzmusel.cluedo.network.Network.getCurrentGameID;
import static at.moritzmusel.cluedo.network.Network.testMETHOD;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;


import at.moritzmusel.cluedo.communication.NetworkCommunicator;
import at.moritzmusel.cluedo.entities.Player;
import at.moritzmusel.cluedo.network.Network;
import at.moritzmusel.cluedo.network.pojo.GameState;

public class DebugNetwork extends AppCompatActivity {

    private static final String TAG_DEBUG = "DEBUG Activity ~";
    private Network net;
    public GameState gm;

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
        ArrayList<Integer> card1 = new ArrayList<>();
        ArrayList<Integer> card2 = new ArrayList<>();
        card1.add(2);
        card1.add(5);
        card1.add(3);
        card1.add(12);


        card2.add(1);
        card2.add(8);
        card2.add(13);
        card2.add(19);


        final Button btn = findViewById(R.id.create);
        btn.setOnClickListener(click -> {
            Network.setCtx(this);
            Network.createLobby(user);
            list.add(new Player(user.getUid()));
           // list.add(new Player("shuekilogb3eht58sze2n19ht36z",card2));
            //gm = new GameState(list,null,null,killer,null,this);
            //Network.leaveLobby(user, Network.getCurrentGameID());
        });

        final Button btn3 = findViewById(R.id.test2);
        btn3.setOnClickListener(click -> {
            //list.add(new Player("shuekilogb3eht58sze2n19ht36z"));
          // Network.joinLobby("shuekilogb3eht58sze2n19ht36z",getCurrentGameID());
        });

        final Button btn2 = findViewById(R.id.test);
        btn2.setOnClickListener(click -> {
            Network.startGame(getCurrentGameID(),list);
            NetworkCommunicator.getInstance().register(() -> {
                System.out.println("Something changed");
            });
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
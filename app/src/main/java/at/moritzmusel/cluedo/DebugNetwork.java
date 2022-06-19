package at.moritzmusel.cluedo;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import at.moritzmusel.cluedo.entities.Player;
import at.moritzmusel.cluedo.network.Network;

public class DebugNetwork extends AppCompatActivity {

    private static final String TAG_DEBUG = "DEBUG Activity ~";
    private String game = "";
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
        card1.add(new Card(1, "", CardType.ROOM));
        card1.add(new Card(1, "", CardType.ROOM));
        card1.add(new Card(1, "", CardType.ROOM));
        card1.add(new Card(1, "", CardType.ROOM));
        list.add(new Player("qsd"));
        //killer
        List<Card> killer = new ArrayList<>();
        killer.add(new Card(1, "", CardType.ROOM));
        killer.add(new Card(1, "", CardType.ROOM));
        killer.add(new Card(1, "", CardType.ROOM));

        Button btn1 = findViewById(R.id.create),btn2=findViewById(R.id.start), btn3 = findViewById(R.id.leave);
        btn1.setOnClickListener(click -> {
            Network.setCtx(this);
            //
            game = Network.createLobby(user);
        });
        btn2.setOnClickListener(click -> {
            Network.setCtx(this);
            Network.joinLobby(user, game);
        });
        btn3.setOnClickListener(click -> {
            Network.setCtx(this);
            Network.leaveLobby(user, Network.getCurrentGameID());
        });
    }
}
package at.moritzmusel.cluedo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import at.moritzmusel.cluedo.network.Network;

public class LobbyDecisionActivity extends AppCompatActivity implements View.OnClickListener {

    private Button create_lobby;
    private Button join_lobby;
    private Button back_to_main;
    private String game = "";
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_decision);
        create_lobby = findViewById(R.id.btn_create_lobby);
        create_lobby.setOnClickListener(this);
        join_lobby = findViewById(R.id.btn_join_lobby);
        join_lobby.setOnClickListener(this);
        back_to_main = findViewById(R.id.btn_back_to_main);
        back_to_main.setOnClickListener(this);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        Network.signInAnonymously(mAuth);
        user = mAuth.getCurrentUser();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_create_lobby){
            Network.setCtx(this);
            game = Network.createLobby(user);
            Intent i = new Intent(LobbyDecisionActivity.this, CreateLobbyActivity.class);
            i.putExtra("decision",true);
            i.putExtra("game_id",game);
            i.putExtra("user",user);
            startActivity(i);
        }
        if(view.getId() == R.id.btn_join_lobby){
            Intent i = new Intent(LobbyDecisionActivity.this, JoinLobbyActivity.class);
            i.putExtra("game_id",game);
            i.putExtra("user",user);
            startActivity(i);
        }
        if(view.getId() == R.id.btn_back_to_main){
            finish();
        }
    }
}
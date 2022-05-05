package at.moritzmusel.cluedo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LobbyDecisionActivity extends AppCompatActivity implements View.OnClickListener {

    private Button create_lobby;
    private Button join_lobby;
    private Button back_to_main;

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
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_create_lobby){
            Intent i = new Intent(LobbyDecisionActivity.this, CreateLobbyActivity.class);
            startActivity(i);
        }
        if(view.getId() == R.id.btn_join_lobby){
            Intent i = new Intent(LobbyDecisionActivity.this, JoinLobbyActivity.class);
            startActivity(i);
        }
        if(view.getId() == R.id.btn_back_to_main){
            //Intent i = new Intent(LobbyDecisionActivity.this, MainActivity.class);
            //startActivity(i);
            finish();
        }
    }
}
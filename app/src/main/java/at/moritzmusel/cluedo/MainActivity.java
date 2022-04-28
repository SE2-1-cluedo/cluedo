package at.moritzmusel.cluedo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button startGame;
    private Button gamerules;
    private Button closeApp;
    private Button commands;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startGame = findViewById(R.id.btn_startgame);
        startGame.setOnClickListener(this);
        gamerules = findViewById(R.id.btn_gamerules);
        gamerules.setOnClickListener(this);
        closeApp = findViewById(R.id.btn_close_app);
        closeApp.setOnClickListener(this);
        commands = findViewById(R.id.btn_commands);
        commands.setOnClickListener(this);

        //button.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, BoardActivity.class)));
        // Test push ch
        Log.i("Test", "Test");

    }
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_startgame){
            Intent i = new Intent(MainActivity.this, LobbyDecisionActivity.class);
            startActivity(i);
        }
        if(view.getId() == R.id.btn_gamerules){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=S_WEloTcKmI")));
        }
        if(view.getId() == R.id.btn_commands){
            Intent i = new Intent(MainActivity.this, CommandsActivity.class);
            startActivity(i);
        }
        if(view.getId() == R.id.btn_close_app){
            finish();
            System.exit(0);
        }
    }


}
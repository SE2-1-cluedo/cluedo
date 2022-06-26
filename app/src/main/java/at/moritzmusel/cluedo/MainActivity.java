package at.moritzmusel.cluedo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;

import android.net.Uri;
import android.view.View;

import android.widget.Button;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startGame = findViewById(R.id.btn_startgame);
        startGame.setOnClickListener(this);
        Button gameRules = findViewById(R.id.btn_gamerules);
        gameRules.setOnClickListener(this);
        Button closeApp = findViewById(R.id.btn_close_app);
        closeApp.setOnClickListener(this);
        Button commands = findViewById(R.id.btn_commands);
        commands.setOnClickListener(this);

    }

    /**
     * start game calls the LobbyDecisionActivity
     * game rules show a youtube video
     * commands show the app specific information
     * close app closes app
     */
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

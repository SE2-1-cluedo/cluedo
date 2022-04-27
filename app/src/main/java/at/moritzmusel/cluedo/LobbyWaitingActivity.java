package at.moritzmusel.cluedo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class LobbyWaitingActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView playerlist;
    private ArrayList<String> playerItems = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private int playerCounter = 1;
    private TextView game_id;
    private Button back;
    private EditText number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_waiting);
        back = findViewById(R.id.btn_back_to_join_lobby);
        back.setOnClickListener(this);

        game_id = findViewById(R.id.txt_lobby_id);
        number = findViewById(R.id.txt_enter_id);

        playerlist = findViewById(R.id.playerlist);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, playerItems);
        playerlist.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_back_to_join_lobby){
            Intent i = new Intent(LobbyWaitingActivity.this, JoinLobbyActivity.class);
            startActivity(i);
        }
    }
    public void addPlayer(View view) {
        //Muss über das Netzwerk die Spieler in die Liste hinzufügen.
        playerItems.add("player "+playerCounter++);
        adapter.notifyDataSetChanged();
    }
    public void setGameId(){
        //Über das Netzwerk die Id für das Spiel bekommen und anzeigen.
        number.setText("12345");
    }
}
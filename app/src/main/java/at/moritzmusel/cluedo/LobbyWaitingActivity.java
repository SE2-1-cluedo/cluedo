package at.moritzmusel.cluedo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class LobbyWaitingActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView playerlist;
    private ArrayList<String> playerItems = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private int playerCounter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_waiting);
        final Button back = findViewById(R.id.btn_back_to_join_lobby);
        back.setOnClickListener(this);
        TextView game_id = findViewById(R.id.txt_lobby_id);
        TextView number = findViewById(R.id.txt_enter_id);
        game_id.setText(number.getText());

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
        playerItems.add("player "+playerCounter++);
        adapter.notifyDataSetChanged();
    }
}
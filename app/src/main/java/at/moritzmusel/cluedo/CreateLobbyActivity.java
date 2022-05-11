package at.moritzmusel.cluedo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CreateLobbyActivity extends AppCompatActivity implements View.OnClickListener{

    private ListView playerlist;
    private ArrayList<String> playerItems = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private int playerCounter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lobby);

        final Button send_link = findViewById(R.id.btn_send_link);
        send_link.setOnClickListener(this);

        final Button start = findViewById(R.id.btn_lobby_start);
        start.setOnClickListener(this);

        final Button back = findViewById(R.id.btn_back);
        back.setOnClickListener(this);

        TextView join_id = findViewById(R.id.txt_lobbyid);
        join_id.setText(getGameID());

        playerlist = findViewById(R.id.playerlist);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, playerItems);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, playerItems);
        playerlist.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_send_link){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, getGameID());//Hier wird dann der Einladungslink weitergesendet.
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);

            addPlayer(view);

        }
        if(view.getId() == R.id.btn_lobby_start){
            //select the character screen
            Intent i = new Intent(CreateLobbyActivity.this, BoardActivity.class);
            startActivity(i);
        }
        if(view.getId() == R.id.btn_back){
            //Intent i = new Intent(CreateLobbyActivity.this, LobbyDecisionActivity.class);
            //startActivity(i);
            finish();
        }
    }

    public String getGameID() {
        //Schnittstelle mit dem Netzwerk um die id zu bekommen.
        String id = "12345";//Nur zum sehen ob es geht
        return id;
    }

    public void addPlayer(View view) {
        playerItems.add("player "+playerCounter++);
        adapter.notifyDataSetChanged();
        vibrate(300);
    }

    public void vibrate(int duration){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(duration);
    }
}
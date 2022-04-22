package at.moritzmusel.cluedo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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

        playerlist = findViewById(R.id.playerlist);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, playerItems);
        playerlist.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_send_link){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");//Hier wird dann der Einladungslink weitergesendet.
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);

            addPlayer(view);

        }
        if(view.getId() == R.id.btn_startgame){
            //select the character screen

        }
    }

    public void addPlayer(View view) {
        playerItems.add("player "+playerCounter++);
        adapter.notifyDataSetChanged();
    }
}
package at.moritzmusel.cluedo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import at.moritzmusel.cluedo.entities.Character;

public class CreateLobbyActivity extends AppCompatActivity implements View.OnClickListener{

    private ListView playerlist;
    private TextView lobby_title;
    private ArrayList<String> playerItems = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private int playerCounter = 1;
    private Button send_link;
    private Button start;
    private Button back;
    private boolean decision;
    private Character c;
    private TextView character_name;
    private ImageView character_picture;

    /**
     * Creates and initialises all buttons and lists
     * Also changes the design according to the decision boolean
     * true nothing has to change
     * false to the join lobby where they have to wait for the host
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lobby);

        lobby_title = findViewById(R.id.txt_create_lobby);

        //Intent intent = getIntent();
        decision = getIntent().getExtras().getBoolean("decision");
        //checkCreateOrJoin(decision);

        send_link = findViewById(R.id.btn_send_link);
        send_link.setOnClickListener(this);

        start = findViewById(R.id.btn_lobby_start);
        start.setOnClickListener(this);

        back = findViewById(R.id.btn_back);
        back.setOnClickListener(this);

        TextView join_id = findViewById(R.id.txt_lobbyid);
        join_id.setText(getGameID());

        playerlist = findViewById(R.id.playerlist);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, playerItems);
        playerlist.setAdapter(adapter);

        c = Character.getFirstCharacter();
        character_name = findViewById(R.id.txt_character_name);
        character_picture = findViewById(R.id.img_character);
        addPlayer(playerlist);
        setCharacter();



        if(decision) {
            //if the player entered through the creation button
        }else{
           start.setClickable(false);
           //start.setBackgroundColor(getColor(R.color.gray));
           start.setBackground(getResources().getDrawable(android.R.drawable.progress_horizontal));
           start.setText(R.string.waiting);
           send_link.setVisibility(View.INVISIBLE);
           lobby_title.setText(R.string.lobby);
        }
    }

    /**
     * Sets the character for the player in the lobby
     */
    private void setCharacter() {
        character_name.setText(c.name());
        setImage();
        c = c.getNextCharacter();
    }

    /**
     * Sets the image in the lobby according to the character
     */
    private void setImage() {
        switch(c) {
            case DR_ORCHID:
                character_picture.setImageResource(R.drawable.orchid);
                break;
            case COLONEL_MUSTARD:
                character_picture.setImageResource(R.drawable.mustard);
                break;
            case MISS_SCARLETT:
                character_picture.setImageResource(R.drawable.scarlett);
                break;
            case PROFESSOR_PLUM:
                character_picture.setImageResource(R.drawable.plum);
                break;
            case MRS_PEACOCK:
                character_picture.setImageResource(R.drawable.peacock);
                break;
            case REVEREND_GREEN:
                character_picture.setImageResource(R.drawable.green);
                break;
        }
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

        }
        if(view.getId() == R.id.btn_lobby_start){
            //select the character screen
            Intent i = new Intent(CreateLobbyActivity.this, BoardActivity.class);
            startActivity(i);
        }
        if(view.getId() == R.id.btn_back){
            finish();
        }
    }

    /**
     * gets the game id for the current game
     * @return String with id
     */
    public String getGameID() {
        if(decision){
            //Schnittstelle mit dem Netzwerk um die id zu bekommen.
            String id = "12345";//Nur zum sehen ob es geht
            return id;
        }else{
            Intent intent = getIntent();
            String id_from_joinlobby = intent.getStringExtra(Intent.EXTRA_TEXT);
            return id_from_joinlobby;
            //game_id.setText(id_from_joinlobby);
        }

    }

    public void network(){
        //Adds the player according to the database
        //Also checks the number of player and only allows up to six and at least three.
    }

    /**
     * Adds the player string to the list
     * @param view the view with list
     */
    public void addPlayer(View view) {
        playerItems.add(playerCounter++ + " " + c.name());
        adapter.notifyDataSetChanged();
        vibrate(500);
    }

    /**
     * Used to check if the decision boolean worked
     */
    /*
    public void checkCreateOrJoin(boolean decision){
        new AlertDialog.Builder(CreateLobbyActivity.this)
                .setTitle("Did it work?")
                .setMessage("Join = false, Create = true: " + decision)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
        vibrate(300);
    }*/

    /**
     * lets the phone vibrate for the time of the duration
     * @param duration seconds you want it to vibrate
     */
    public void vibrate(int duration){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(duration);
    }
}
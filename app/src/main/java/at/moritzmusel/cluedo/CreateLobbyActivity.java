package at.moritzmusel.cluedo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import at.moritzmusel.cluedo.communication.NetworkCommunicator;
import at.moritzmusel.cluedo.entities.Character;
import at.moritzmusel.cluedo.entities.Player;
import at.moritzmusel.cluedo.network.Network;
import at.moritzmusel.cluedo.network.pojo.GameState;

public class CreateLobbyActivity extends AppCompatActivity implements View.OnClickListener{

    private ListView list_playerlist;
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
    private String game_id;
    private List<Player> player_list;
    FirebaseUser user;
    NetworkCommunicator networkCommunicator = NetworkCommunicator.getInstance();
    private GameState gamestate;

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
        gamestate = GameState.getInstance();

        lobby_title = findViewById(R.id.txt_create_lobby);

        //Intent intent = getIntent();
        decision = getIntent().getExtras().getBoolean("decision");
        user = (FirebaseUser) getIntent().getExtras().get("user");
        //checkCreateOrJoin(decision);
        //user = game_state.getPlayerState();

        send_link = findViewById(R.id.btn_send_link);
        send_link.setOnClickListener(this);

        start = findViewById(R.id.btn_lobby_start);
        start.setOnClickListener(this);

        back = findViewById(R.id.btn_back);
        back.setOnClickListener(this);

        TextView join_id = findViewById(R.id.txt_lobbyid);
        join_id.setText(getGameID());

        list_playerlist = findViewById(R.id.playerlist);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, playerItems);
        list_playerlist.setAdapter(adapter);

        /*for (Player p : game_state.getPlayerState()) {
            if(p.getPlayerId().equals(user.getUid())){
                addPlayer(list_playerlist);
            }
        }*/

        character_name = findViewById(R.id.txt_character_name);
        character_picture = findViewById(R.id.img_character);
        //addPlayer(list_playerlist);


        networkCommunicator.register(() -> {
            if(networkCommunicator.isPlayerChanged()){
                player_list = gamestate.getPlayerState();
                System.out.println(player_list);
                for (Player p : gamestate.getPlayerState()) {
                    if(p.getPlayerId().equals(user.getUid())){
                        c = p.getPlayerCharacterName();
                        character_name.setText(c.name());
                        setImage();
                    }
                    playerItems.add(playerCounter++ + " " + p.getPlayerCharacterName());
                    //c.getNextCharacter();
                    adapter.notifyDataSetChanged();
                    vibrate(500);

                }
            }
        });

        //setCharacter();


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
            Network.setCtx(this);
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(CreateLobbyActivity.this);
            builder.setTitle("Attention!");
            builder.setMessage("If you leave the Lobby now, you will have to create a new one.");
            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Network.leaveLobby(user, game_id);
                    finish();
                }
            });

            builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create();
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    /**
     * gets the game id for the current game
     * @return String with id
     */
    public String getGameID() {
        if(decision){
            game_id = getIntent().getExtras().getString("game_id");
            return game_id;
            //Schnittstelle mit dem Netzwerk um die id zu bekommen.
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
     * lets the phone vibrate for the time of the duration
     * @param duration seconds you want it to vibrate
     */
    public void vibrate(int duration){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(duration);
    }
}
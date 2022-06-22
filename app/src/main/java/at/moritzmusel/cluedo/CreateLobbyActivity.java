package at.moritzmusel.cluedo;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import java.util.HashSet;
import java.util.List;

import at.moritzmusel.cluedo.communication.NetworkCommunicator;
import at.moritzmusel.cluedo.entities.Character;
import at.moritzmusel.cluedo.entities.Player;
import at.moritzmusel.cluedo.network.Network;
import at.moritzmusel.cluedo.network.pojo.GameState;

public class CreateLobbyActivity extends AppCompatActivity implements View.OnClickListener{

    private final ArrayList<String> playerItems = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private int playerCounter = 1;
    private TextView lobby_title;
    private Button start;
    private Button back;
    private boolean decision;
    private Character c;
    private TextView character_name;
    private ImageView character_picture;
    private String game_id;
    private List<Player> player_list;
    FirebaseUser user;
    NetworkCommunicator networkCommunicator;
    private GameState gamestate;

    /**
     * Creates and initialises all buttons and lists
     * Also changes the design according to the Character Miss Scarlett
     * true nothing has to change
     * false to the join lobby where they have to wait for the host
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lobby);
        gamestate = GameState.getInstance();
        networkCommunicator = NetworkCommunicator.getInstance();

        lobby_title = findViewById(R.id.txt_create_lobby);

        decision = getIntent().getExtras().getBoolean("decision");
        user = (FirebaseUser) getIntent().getExtras().get("user");

        Button send_link = findViewById(R.id.btn_send_link);
        send_link.setOnClickListener(this);

        start = findViewById(R.id.btn_lobby_start);
        start.setOnClickListener(this);

        back = findViewById(R.id.btn_back);
        back.setOnClickListener(this);

        TextView join_id = findViewById(R.id.txt_lobbyid);
        join_id.setText(getGameID());

        ListView list_playerlist = findViewById(R.id.playerlist);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, playerItems);
        list_playerlist.setAdapter(adapter);

        character_name = findViewById(R.id.txt_character_name);
        character_picture = findViewById(R.id.img_character);

        managePlayerList();
    }

    /**
     * If a player creates, leaves or joins lobby it will update the list
     */
    public void managePlayerList(){
        networkCommunicator.register(() -> {
            //playerlist genau gleiche viele wie in der Gamestate
            playerItems.clear();
            if(networkCommunicator.isPlayerChanged()) {
                player_list = gamestate.getPlayerState();
                if (networkCommunicator.isCharacterChanged()) {
                    for (Player p : player_list) {
                        if (p.getPlayerId().equals(user.getUid())) {
                            setCharacter(p);
                            setLobby();
                            checkNumberOfPlayers();
                        }
                        addPlayerWithoutDuplicates(p);
                    }
                    networkCommunicator.setCharacterChanged(false);
                }
                networkCommunicator.setPlayerChanged(false);
            }
            //checks if the game starts so the board can be called
            if(networkCommunicator.isStartGame()){
                Intent i = new Intent(CreateLobbyActivity.this, BoardActivity.class);
                startActivity(i);
            }
        });

    }

    /**
     * If the player is the host (Miss Scarlett) and there are at least three to six
     * the start button will be clickable
     */
    private void checkNumberOfPlayers(){
        if(c == Character.MISS_SCARLETT){
            if(playerItems.size() < 3 || playerItems.size() > 6){
                start.setClickable(false);
                start.setBackground(getResources().getDrawable(android.R.drawable.progress_horizontal));
            }
            else{
                start.setClickable(true);
                start.setBackground(getResources().getDrawable(R.drawable.custom_button));
            }
        }
    }

    /**
     * Adds the player if it isn't in the list
     * @param p the player
     */
    private void addPlayerWithoutDuplicates(Player p){
        if (!playerItems.contains(p.getPlayerCharacterName().name())) {
            playerItems.add(p.getPlayerCharacterName().name());
            adapter.notifyDataSetChanged();
            vibrate(500);
        }
    }

    /**
     * Because the characters for the players will be automatically assign by the network
     * and if your are Miss Scarlett then you are the host that can start the game.
     */
    private void setLobby() {
        if(c == Character.MISS_SCARLETT){
            createUI();
        }else{
            joinUI();
        }
    }

    /**
     * If somebody enters through the join lobby the Activity will change to the Lobby
     */
    public void joinUI(){
        start.setClickable(false);
        start.setBackground(getResources().getDrawable(android.R.drawable.progress_horizontal));
        start.setText(R.string.waiting);
        lobby_title.setText(R.string.lobby);
    }

    /**
     * If somebody enters through the create lobby the Activity will change to the Lobby that can start the game
     */
    public void createUI(){
        start.setClickable(true);
        start.setBackground(getResources().getDrawable(R.drawable.custom_button));
        start.setText(R.string.start);
        lobby_title.setText(R.string.create_lobby);
    }

    /**
     * Same back for the system and the button
     */
    public void onBackPressed(){
        back();
    }

    /**
     * Sets the character for the player in the lobby
     */
    private void setCharacter(Player p) {
        c = p.getPlayerCharacterName();
        character_name.setText(c.name());
        setImage();
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

    /**
     * Whats happen when you click the buttons
     * Send link = sending the gameid per text
     * back = leaves lobby
     * Start will start the game
     * @param view this
     */
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
            Network.startGame(getGameID(),player_list);
            Intent i = new Intent(CreateLobbyActivity.this, BoardActivity.class);
            startActivity(i);
        }
        if(view.getId() == R.id.btn_back){
            back();
        }
    }

    /**
     * resets the network so the list of players can be properly shown
     */
    @Override
    protected void onPause() {
        super.onPause();
        gamestate.reset();
        networkCommunicator.reset();
    }

    /**
     * If you press back you will leave the lobby
     * If the player is the host an alert shows
     * that informs him that he leaves the lobby
     */
    public void back(){
        if(decision){
            Network.setCtx(this);
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(CreateLobbyActivity.this);
            builder.setTitle("Attention!");
            builder.setMessage("If you leave the Lobby now, you will have to create a new one.");
            builder.setNeutralButton("OK", (dialog, which) -> {
                Network.leaveLobby(user, getGameID());
                finish();
            });
            builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());
            builder.create();
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }else{
            Network.setCtx(this);
            Network.leaveLobby(user, getGameID());
            finish();
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
        }else{
            Intent intent = getIntent();
            return intent.getStringExtra(Intent.EXTRA_TEXT);
        }

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
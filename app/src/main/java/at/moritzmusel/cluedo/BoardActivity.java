package at.moritzmusel.cluedo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.IntStream;


import at.moritzmusel.cluedo.communication.GameplayCommunicator;
import at.moritzmusel.cluedo.communication.NetworkCommunicator;
import at.moritzmusel.cluedo.communication.SuspicionCommunicator;
import at.moritzmusel.cluedo.game.Dice;
import at.moritzmusel.cluedo.network.Network;
import at.moritzmusel.cluedo.network.pojo.GameState;
import at.moritzmusel.cluedo.sensor.ShakeDetector;
import at.moritzmusel.cluedo.game.Gameplay;
import at.moritzmusel.cluedo.entities.Character;

public class BoardActivity extends AppCompatActivity {

    private View decorView;
    private View playerCardsView;
    private AllTheCards allCards;
    private float x1;
    private float y1;
    private GameState gameState;
    private SuspicionCommunicator susCommunicator;
    private GameplayCommunicator gameplayCommunicator;
    private NetworkCommunicator netCommunicator;
    static final int MIN_SWIPE_DISTANCE = 150;
    private final ArrayList<ImageButton> allArrows = new ArrayList<>();
    private Dice dice;
    private Gameplay gp1;
    private int newPosition;
    private LinkedList<Card> allGameCards;
    private final ArrayList<Button> secrets = new ArrayList<>();
    private final ArrayList<ImageView> allWeapons = new ArrayList<>();
    private final ArrayList<String> weaponNames = new ArrayList<>(Arrays.asList("dagger","candlestick","revolver","rope", "pipe","wrench"));
    private EvidenceCards evidenceCards;

    private SensorManager mSensorManager;
    private Sensor accel;
    private ShakeDetector shakeDetector;

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        decorView = getWindow().getDecorView();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        gp1 = Gameplay.getInstance();

        setContentView(R.layout.test_board2);
        ConstraintLayout constraint = findViewById(R.id.constraintLayout);

        // Gets all children of the layout and puts all arrows in the arrayList and all positions in the hashMap
        for (int i = 1; i <= constraint.getChildCount(); i++) {
            View test = constraint.getChildAt(i);

            if(test instanceof Button && getResources().getResourceEntryName(test.getId()).contains("secret"))
                secrets.add((Button) test);

            else if(test instanceof  ImageView && getResources().getResourceEntryName(test.getId()).contains("w_"))
                allWeapons.add((ImageView) test);

             else if (test instanceof ImageButton && !getResources().getResourceEntryName(test.getId()).contains("cardView"))
                allArrows.add((ImageButton) test);
        }

        constraint.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        // Layout has happened here.
                            for (int i = 0; i < gp1.getPlayers().size(); i++) {
                                    String player = gp1.getPlayers().get(i).getPlayerCharacterName().name().split("[_]")[1].toLowerCase();
                                    findViewById(createPlayer(player)).setVisibility(View.VISIBLE);
                                    String name = player + "_";
                                    Button startPlace = findViewById(createRoomDestination(name, gp1.getPlayers().get(i).getPositionOnBoard()));
                                    findViewById(createPlayer(player)).setX(startPlace.getX());
                                    findViewById(createPlayer(player)).setY(startPlace.getY());
                                }
                            for(int i = 0; i < gp1.getWeaponsPos().length; i++){
                                    String str = "w_"+weaponNames.get(i);
                                    Button startPosition = findViewById(createRoomDestination("weapon", gp1.getWeaponsPos()[i]));
                                    findViewById(getResources().getIdentifier(str,"id",getPackageName())).setX(startPosition.getX());
                                    findViewById(getResources().getIdentifier(str,"id",getPackageName())).setY(startPosition.getY());
                            }
                        newPosition = gp1.findPlayerByCharacterName(gp1.getCurrentPlayer()).getPositionOnBoard();
                        // Don't forget to remove your listener when you are done with it.
                        constraint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });

        gameState = GameState.getInstance();

        susCommunicator = SuspicionCommunicator.getInstance();

        gameplayCommunicator = GameplayCommunicator.getInstance();
        gameplayCommunicator.register(() -> {
            if(gameplayCommunicator.isMoved()){
                System.out.println("Now we refreshed");
//                refreshBoard();
                gameplayCommunicator.setMoved(false);
                netCommunicator.setPositionChanged(false);
            }
            if(gameplayCommunicator.isSuspicion()){
                //onCardViewClick();
            }
            if(gameplayCommunicator.isTurnChange()){
                notifyCurrentPlayer();
                if(gp1.checkIfPlayerIsOwn())
                callDice();
                gameplayCommunicator.setTurnChange(false);
            }
        });

        netCommunicator = NetworkCommunicator.getInstance();
        netCommunicator.setPositionChanged(false);
        netCommunicator.register(() -> {
           if(netCommunicator.isHasLost()) {
                //call loser dialog
           }
           if(netCommunicator.isHasWon()){
                //call winner dialog
           }
        });

        allCards = new AllTheCards();
        allGameCards = allCards.getGameCards();

        evidenceCards = new EvidenceCards();

//        ImageButton cardView = findViewById(R.id.cardView);
//        cardView.setOnClickListener(v -> {
//            gameplayCommunicator.setMoved(true);
//            gameplayCommunicator.notifyList();
//        });

        ImageView image = new ImageView(this);
        image.setImageResource(R.drawable.cardback);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        shakeDetector = new ShakeDetector();

        if(gp1.checkIfPlayerIsOwn()){
            System.out.println("This device");
            callDice();
        }
    }

    /**
     * Creates an AlertDialog which informs the player that a card is owned by somebody or the murderer
     * uses the methods of the EvidenceCards-Class
     */
    private void rolledMagnifyingGlass() {
        if(dice.getNumberRolled() == 4){
            AlertDialog.Builder builder = new AlertDialog.Builder(BoardActivity.this);
            builder.setTitle("What is going on?");
            builder.setMessage("You rolled the magnifying glass." + "\n"
                    + "A evidence card has been drawn." + "\n"
                    + "It is revealed that the Card: " + evidenceCards.getDrawnCard().getDesignation() + "\n"
                    + "is owned by: " + evidenceCards.getPlayer());

            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
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

    private void refreshBoard() {
        System.out.println("Should have moved now");
        for (int i = 0; i < gp1.getPlayers().size(); i++) {
            String player = gp1.getPlayers().get(i).getPlayerCharacterName().name().split("[_]")[1].toLowerCase();
            String name = player + "_";
            Button startPlace = findViewById(createRoomDestination(name, gp1.getPlayers().get(i).getPositionOnBoard()));
            findViewById(createPlayer(player)).setX(startPlace.getX());
            findViewById(createPlayer(player)).setY(startPlace.getY());
        }
        for(int i = 0; i < gp1.getWeaponsPos().length; i++){
            String str = "w_"+weaponNames.get(i);
            Button startPosition = findViewById(createRoomDestination("weapon", gp1.getWeaponsPos()[i]));
            findViewById(getResources().getIdentifier(str,"id",getPackageName())).setX(startPosition.getX());
            findViewById(getResources().getIdentifier(str,"id",getPackageName())).setY(startPosition.getY());
        }
    }




    /**
     * Creates an alert with a dice action, which is shakeable and clickable.
     */
    @SuppressLint("InflateParams")
    private void callDice() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(BoardActivity.this, R.style.AlertDialogStyle);
        builder.setTitle("Throw Dice");

        LayoutInflater inflater = getLayoutInflater();
        View dice_layout = inflater.inflate(R.layout.custom_dialog, null);
        builder.setView(dice_layout);

        View diceView = dice_layout.findViewById(R.id.dialogDice);
        dice = new Dice((ImageView) diceView);
        diceView.setOnClickListener(v -> {
            dice.throwDice();
            diceRolled();
        });

        mSensorManager.registerListener(shakeDetector, accel, SensorManager.SENSOR_DELAY_UI);

        builder.setCancelable(false);

        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();

        shakeDetector.setOnShakeListener(count -> {
            if(count < 2){
                dice.throwDice();
                diceRolled();
            }
//            new Thread(){
//                public void run() {
//                    try {
//                        Thread.sleep(2000);
//                        runOnUiThread(alertDialog::cancel);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }.start();
    });
    }


    @Override
    public void onRestart() {
        super.onRestart();
        if(susCommunicator.getHasSuspected() || susCommunicator.getHasAccused()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
//            gameState.setAskQuestion(new Question("TestPlayer",new int[]{2,9,15}),false);
            if(susCommunicator.getHasAccused())
                builder.setMessage("This is your one and only accusation, are you sure about it?");
            else
            builder.setMessage("You suspected: ");

            setPlayerCardImages();
            builder.setView(playerCardsView);

            builder.setPositiveButton("Yes, proceed", (dialog, which) -> {
                switchWeapon(susCommunicator.getWeapon().toLowerCase());
                moveSuspectedPlayer(susCommunicator.getCharacter());
                //moveCharacter
                if(susCommunicator.getHasSuspected())
                    showSuspicionResult();
                    //do something to ask the next player about your suspicion
                else if (susCommunicator.getHasAccused())
                    System.out.println("Accused");
                    //check with murder cards either player wins or is out
                susCommunicator.setHasSuspected(false);
                susCommunicator.setHasAccused(false);
                dialog.cancel();
            });
            builder.setNegativeButton("No, I want to change", (dialog, which) -> {
                susCommunicator.setHasSuspected(false);
                susCommunicator.setHasAccused(false);
                Intent intent = new Intent(BoardActivity.this, SuspicionActivity.class);
                startActivity(intent);
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void showSuspicionResult(){
        AlertDialog.Builder builder = new AlertDialog.Builder(BoardActivity.this);

        String[] playerWithSusCard = gp1.getPlayerForSuspectedCardsDEMO(gameState.getAskQuestion().getCards());
        //if(gp1.findPlayerByCharacterName(gp1.getCurrentPlayer()).getPlayerId().equals(Network.getCurrentUser().getUid())){
        if(true){
            if(playerWithSusCard.length == 1)
                builder.setMessage("The cards are owned by "+playerWithSusCard[0]);
            else
            builder.setMessage("The Card "+allCards.getGameCards().get(Integer.parseInt(playerWithSusCard[1])).getDesignation()+" is owned by "+playerWithSusCard[0]);
        } else {
            if(playerWithSusCard.length == 1)
            builder.setMessage("The cards are owned by "+playerWithSusCard[0]);
            else {
                builder.setMessage("One or more cards are owned by "+playerWithSusCard[0]);
            }
        }

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        new Thread(){
            public void run() {
                try {
                    Thread.sleep(5000);
                    runOnUiThread(() -> {
                        alertDialog.dismiss();
                        gp1.endTurn();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        }

    /**
     * sets the layout for the card alert
     * initialises the ImageViews of the cards and sets
     * the right image for the views.
     */
    @SuppressLint("InflateParams")
    private void setPlayerCardImages() {
        LayoutInflater factory = LayoutInflater.from(BoardActivity.this);
        playerCardsView = factory.inflate(R.layout.image_show_cards, null);
        LinearLayout linearH0 = playerCardsView.findViewById(R.id.linearH0);
        LinearLayout linearH1 = playerCardsView.findViewById(R.id.linearH1);
        ArrayList<ImageView> card_list = new ArrayList<>();

        for(int i = 0; i < linearH0.getChildCount()*2; i++){
            if(i < 3)
            card_list.add((ImageView) linearH0.getChildAt(i));
            else
            card_list.add((ImageView) linearH1.getChildAt(i-3));
        }

        ArrayList<Integer> card_ids = getPlayerCardIds();

        if(card_ids.size() <= 6){
            for(int i = 0; i < card_ids.size();i++){
                setPlayerCard(card_list.get(i), card_ids.get(i));
            }
            for(int i = card_ids.size(); i < card_list.size();i++){
                card_list.get(i).setVisibility(View.GONE);
            }
        }
        else{
            for(int i = 0; i < 6;i++){
                setPlayerCard(card_list.get(i), card_ids.get(i));
            }
        }


    }

    /**
     * Connects with the network to get the cardids for the images
     * @return Array with three to six ids for the images
     */
    private ArrayList<Integer> getPlayerCardIds(){
        ArrayList<Integer> card_ids;
        if(susCommunicator.getHasSuspected() || susCommunicator.getHasAccused())
            card_ids = new ArrayList<>(Arrays.asList(allCards.findIdWithName(susCommunicator.getCharacter()),allCards.findIdWithName(susCommunicator.getWeapon()),newPosition+11));
        else if(gameplayCommunicator.isSuspicion())
            card_ids = new ArrayList<>(Arrays.asList(gameState.getAskQuestion().getCards()[0],gameState.getAskQuestion().getCards()[1],gameState.getAskQuestion().getCards()[2]));
        else
            card_ids = gp1.findPlayerById(Network.getCurrentUser().getUid()).getPlayerOwnedCards();

        return card_ids;
    }

    /**
     * Sets the picture connected to the id on the imageView card
     * @param card the imageView we want to set the picture on
     * @param id for finding the picture
     */
    private void setPlayerCard(ImageView card, int id){
        switch(id) {
            case 0:
                card.setImageResource(R.drawable.scarlett_card);
                break;
            case 1:
                card.setImageResource(R.drawable.plum_card);
                break;
            case 2:
                card.setImageResource(R.drawable.green_card);
                break;
            case 3:
                card.setImageResource(R.drawable.peacock_card);
                break;
            case 4:
                card.setImageResource(R.drawable.mustard_card);
                break;
            case 5:
                card.setImageResource(R.drawable.orchid_card);
                break;
            case 6:
                card.setImageResource(R.drawable.dagger);
                break;
            case 7:
                card.setImageResource(R.drawable.candlestick);
                break;
            case 8:
                card.setImageResource(R.drawable.revolver);
                break;
            case 9:
                card.setImageResource(R.drawable.rope);
                break;
            case 10:
                card.setImageResource(R.drawable.pipe);
                break;
            case 11:
                card.setImageResource(R.drawable.wrench);
                break;
            case 12:
                card.setImageResource(R.drawable.lounge_card);
                break;
            case 13:
                card.setImageResource(R.drawable.conservatory_card);
                break;
            case 14:
                card.setImageResource(R.drawable.ballroom_card);
                break;
            case 15:
                card.setImageResource(R.drawable.dining_card);
                break;
            case 16:
                card.setImageResource(R.drawable.kitchen_card);
                break;
            case 17:
                card.setImageResource(R.drawable.library_card);
                break;
            case 18:
                card.setImageResource(R.drawable.billiard_card);
                break;
            case 19:
                card.setImageResource(R.drawable.study_card);
                break;
            case 20:
                card.setImageResource(R.drawable.hall_card);
                break;
            default:
                card.setImageResource(R.drawable.cardback);
        }
    }

    /**
     * Called when dice gets rolled. Removes dice clickListener, resets stepsTaken in Gameplay, creates a alert for the magnifying glass method
     * and calls the move methode
     */
    public void diceRolled() {
        rolledMagnifyingGlass();
        gp1.setStepsTaken(0);
        movePlayerWithArrows();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
            decorView.setSystemUiVisibility(hideSystemBars());
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    private int hideSystemBars() {
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }

    /**
     * Creates a popUp message if the android backButton is pressed
     */
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Are you sure you want to exit?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        builder.setNegativeButton("No, I'm definitely not", (dialog, which) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Ends the turn and sends a message who the next player is
     */
    private void notifyCurrentPlayer(){
        newPosition = gp1.findPlayerByCharacterName(gp1.getCurrentPlayer()).getPositionOnBoard();
        Toast.makeText(this,"It is now "+gp1.getCurrentPlayer().name()+"'s turn", Toast.LENGTH_SHORT).show();
    }

    /**
     * called by the arrows, searches for current player and the arrow that has been clicked and calls
     * the move function afterwards
     *
     * @param v the button that has been clicked
     */
    public void move(View v) {

        View destination;
        View mover = getMover();
        String character = getResources().getResourceEntryName(mover.getId())+"_";

        switch (getResources().getResourceEntryName(v.getId())) {

            case "secret_1":
            case "secret_2":
                if(gp1.isAllowedToUseSecretPassage()) {
                        destination = findViewById(createRoomDestination(character, 7));
                        gp1.setStepsTaken(dice.getNumberRolled()-1);
                        moveAnimation(mover, destination);
                    }
                    break;

            case "secret_3":
               if(gp1.isAllowedToUseSecretPassage()) {
                        destination = findViewById(createRoomDestination(character, 3));
                        gp1.setStepsTaken(dice.getNumberRolled()-1);
                        moveAnimation(mover, destination);
                    }
                    break;

            case "lounge_btn_right":
            case "ballroom_btn_left":
            case "kitchen_btn_up":
                destination = findViewById(createRoomDestination(character,2));
                    moveAnimation(mover, destination);
                    break;

            case "lounge_btn_down":
            case "billiard_btn_up":

            case "kitchen_btn_left":
                destination = findViewById(createRoomDestination(character,4));
                    moveAnimation(mover, destination);
                    break;

            case "dining_btn_up":
            case "conservatory_btn_left":
                destination = findViewById(createRoomDestination(character,1));
                    moveAnimation(mover, destination);
                    break;

            case "dining_btn_right":
            case "library_btn_left":
            case "conservatory_btn_down":
            case "study_btn_up":
                destination = findViewById(createRoomDestination(character,5));
                    moveAnimation(mover, destination);
                    break;

            case "dining_btn_down":
            case "study_btn_left":
                destination = findViewById(createRoomDestination(character,7));
                    moveAnimation(mover, destination);
                    break;

            case "billiard_btn_right":
            case "hall_btn_left":
            case "kitchen_btn_down":
                destination = findViewById(createRoomDestination(character,8));
                    moveAnimation(mover, destination);
                    break;

            case "ballroom_btn_down":
            case "hall_btn_up":
            case "kitchen_btn_right":
                destination = findViewById(createRoomDestination(character,6));
                    moveAnimation(mover, destination);
                    break;

            case "library_btn_up":
            case "conservatory_btn_right":
                destination = findViewById(createRoomDestination(character,3));
                    moveAnimation(mover, destination);
                    break;

            case "library_btn_down":
            case "study_btn_right":
                destination = findViewById(createRoomDestination(character,9));
                    moveAnimation(mover, destination);
                    break;
        }
    }

    /**
     * When called, sets all arrows on the board invisible
     */
    private void setArrowsInvisible() {
        for (ImageButton b : allArrows)
            b.setVisibility(View.INVISIBLE);
    }

    /**
     * When called all secret Passages are activated
     */
    private void activateSecrets(){
        for(Button b : secrets)
            b.setVisibility(View.VISIBLE);
    }

    /**
     * When called all secret Passages are deactivated
     */
    private void deactivateSecrets(){
        for(Button b : secrets)
            b.setVisibility(View.INVISIBLE);
    }

    /**
     * gets called in moveMethode to find the View of the current Player
     * @return the View (Icon) of the current player
     */
    private View getMover(){
        return findViewById(getResources().getIdentifier(gp1.getCurrentPlayer().name().split("[_]")[1].toLowerCase(), "id", getPackageName()));
    }

    /**
     *
     * @param character the name of the currentPlayer
     * @param room the room he wants to move to
     * @return the id of the destination, which is a combination of character and room
     */
    private int createRoomDestination(String character, int room){
        character+=room;
        newPosition = room;
        return getResources().getIdentifier(character, "id", getPackageName());
    }

    /**
     *
     * @param room the integer of the room we want the weapon to move to
     * @return the id of the weaponsPosition (View)
     */
    private int createWeaponDestination(int room){
        return getResources().getIdentifier("weapon"+room, "id", getPackageName());
    }

    /**
     *
     * @param player the name of the player we want to find the View of
     * @return the id of the player (View)
     */
    private int createPlayer(String player){
        return getResources().getIdentifier(player, "id",getPackageName());
    }

    /**
     * This method moves the suspected Player to the room of the suspecting player
     * @param player the name of the suspected player
     */
    private void moveSuspectedPlayer(String player){
        StringBuilder myName = new StringBuilder(player.toUpperCase());
        myName.setCharAt(player.indexOf(" "), '_');

        player = player.split("[ ]")[1].toLowerCase();
        ImageView calledPlayer = findViewById(getResources().getIdentifier(player,"id",getPackageName()));
        player += "_";

        if(calledPlayer.getVisibility() == View.VISIBLE && gp1.findPlayerByCharacterName(Character.valueOf(myName.toString())).getPositionOnBoard() != newPosition){
            calledPlayer.setX(findViewById(createRoomDestination(player,newPosition)).getX());
            calledPlayer.setY(findViewById(createRoomDestination(player,newPosition)).getY());
            gp1.findPlayerByCharacterName(Character.valueOf(myName.toString())).setPositionOnBoard(newPosition);
        }
    }

    /**
     * Gets called by the suspicion to bring the weapon to the currentRoom
     * @param str the name of the weapon that's been moved
     */
    private void switchWeapon(String str) {
        String weapon = "w_"+str;
        if (IntStream.of(gp1.getWeaponsPos()).noneMatch(x -> x==newPosition)) {
            for (ImageView IV:allWeapons) {
                if (getResources().getResourceEntryName(IV.getId()).equals(weapon)) {
                    IV.setX(findViewById(createWeaponDestination(newPosition)).getX());
                    IV.setY(findViewById(createWeaponDestination(newPosition)).getY());
                    for(String s: weaponNames) {
                       if(s.equals(str)){
                           int[] pos = gp1.getWeaponsPos();
                           pos[weaponNames.indexOf(s)] = newPosition;
                           gp1.setWeaponsPos(pos);
                       }
                    }
                    break;
                }
            }
        } else {
            String otherWeapon = weaponNames.get(ArrayUtils.indexOf(gp1.getWeaponsPos(),newPosition));
            ImageView otherWeaponView = findViewById(getResources().getIdentifier("w_"+otherWeapon, "id", getPackageName()));
            for (ImageView IV: allWeapons) {
                if (getResources().getResourceEntryName(IV.getId()).equals(weapon)) {
                    IV.setX(findViewById(createWeaponDestination( newPosition)).getX());
                    IV.setY(findViewById(createWeaponDestination(newPosition)).getY());
                    for(String s: weaponNames) {
                        if(s.equals(str)){
                            int[] pos = gp1.getWeaponsPos();
                            otherWeaponView.setX(findViewById(createWeaponDestination(pos[weaponNames.indexOf(s)])).getX());
                            otherWeaponView.setY(findViewById(createWeaponDestination(pos[weaponNames.indexOf(s)])).getY());
                            int help = pos[weaponNames.indexOf(otherWeapon)];
                            pos[weaponNames.indexOf(otherWeapon)] = pos[weaponNames.indexOf(s)];
                            pos[weaponNames.indexOf(s)] = help;
                            gp1.setWeaponsPos(pos);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * moves a ImageView from one position to another and calls the method canMove from Gameplay
     *
     * @param mover       the view that's been moved
     * @param destination the position we want to move to
     */
    private void moveAnimation(View mover, View destination) {

        mover.setX(destination.getX());
        mover.setY(destination.getY());

        gp1.updatePlayerPosition(newPosition);

        setArrowsInvisible();
        gp1.canMove();
        movePlayerWithArrows();
    }

    /**
     * checks if a player is allowed to move and then shows the arrows to the adjacent rooms
     * also reassigns the clickListener to the dice if player is finished with moving
     *
     */
    private void movePlayerWithArrows() {
        if (gp1.checkIfPlayerIsOwn()) {
            if (gp1.findPlayerByCharacterName(gp1.getCurrentPlayer()).getIsAbleToMove()) {
                activateSecrets();
                switch (newPosition) {
                    case 1:
                        findViewById(R.id.lounge_btn_down).setVisibility(View.VISIBLE);

                        findViewById(R.id.lounge_btn_right).setVisibility(View.VISIBLE);
                        break;

                    case 2:
                        findViewById(R.id.conservatory_btn_down).setVisibility(View.VISIBLE);

                        findViewById(R.id.conservatory_btn_left).setVisibility(View.VISIBLE);

                        findViewById(R.id.conservatory_btn_right).setVisibility(View.VISIBLE);
                        break;

                    case 3:
                        findViewById(R.id.ballroom_btn_left).setVisibility(View.VISIBLE);

                        findViewById(R.id.ballroom_btn_down).setVisibility(View.VISIBLE);
                        break;

                    case 4:
                        findViewById(R.id.dining_btn_up).setVisibility(View.VISIBLE);

                        findViewById(R.id.dining_btn_right).setVisibility(View.VISIBLE);

                        findViewById(R.id.dining_btn_down).setVisibility(View.VISIBLE);
                        break;

                    case 5:
                        findViewById(R.id.kitchen_btn_up).setVisibility(View.VISIBLE);

                        findViewById(R.id.kitchen_btn_right).setVisibility(View.VISIBLE);

                        findViewById(R.id.kitchen_btn_down).setVisibility(View.VISIBLE);

                        findViewById(R.id.kitchen_btn_left).setVisibility(View.VISIBLE);
                        break;

                    case 6:
                        findViewById(R.id.library_btn_left).setVisibility(View.VISIBLE);

                        findViewById(R.id.library_btn_up).setVisibility(View.VISIBLE);

                        findViewById(R.id.library_btn_down).setVisibility(View.VISIBLE);
                        break;

                    case 7:
                        findViewById(R.id.billiard_btn_up).setVisibility(View.VISIBLE);

                        findViewById(R.id.billiard_btn_right).setVisibility(View.VISIBLE);
                        break;

                    case 8:
                        findViewById(R.id.study_btn_up).setVisibility(View.VISIBLE);

                        findViewById(R.id.study_btn_left).setVisibility(View.VISIBLE);

                        findViewById(R.id.study_btn_right).setVisibility(View.VISIBLE);
                        break;

                    case 9:
                        findViewById(R.id.hall_btn_up).setVisibility(View.VISIBLE);

                        findViewById(R.id.hall_btn_left).setVisibility(View.VISIBLE);
                        break;
                }

            } else {
                deactivateSecrets();
            }
        }
    }

    /**
     * if button is clicked or the screen is swiped horizontally, the current cards of the player are shown
     */
    public void onCardViewClick() {

        AlertDialog.Builder builder = new AlertDialog.Builder(BoardActivity.this,R.style.AlertDialogStyle);
        if(gameplayCommunicator.isSuspicion())
            builder.setTitle(gameState.getAskQuestion().getAskPerson()+" suspected");
        else
        builder.setTitle("My Cards");

        setPlayerCardImages();
        builder.setView(playerCardsView);

        if(!gameplayCommunicator.isSuspicion()) {
            builder.setNeutralButton("OK", (dialog, which) -> dialog.cancel());
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else {
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            new Thread(){
                public void run() {
                    try {
                        Thread.sleep(3000);
                        runOnUiThread(() -> {
                            gameplayCommunicator.setSuspicion(false);
                            alertDialog.dismiss();
                            showSuspicionResult();
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    /**
     * starts the Notepad Activity for the swipe motion
     */
    public void startNotepad(){
        Intent intent = new Intent(this, NotepadActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }
    /**
     * starts the Suspicion Activity for the swipe motion
     */
    public void startSuspicion(){
        Intent intent = new Intent(this, SuspicionActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }

    /**
     * EventListener für Swipe-Event to start either the Notepad, Suspicion or the card alert
     */
    @Override
    public boolean onTouchEvent (MotionEvent touchEvent){

        switch(touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;

            case MotionEvent.ACTION_UP:
                float x2 = touchEvent.getX();
                float y2 = touchEvent.getY();
                float swipeRight = x2 -x1, swipeLeft = x1- x2, swipe = y1- y2;

                if(swipeRight > MIN_SWIPE_DISTANCE){
                    startNotepad();
                } else if(swipeLeft > MIN_SWIPE_DISTANCE){
                    startSuspicion();
                }else if (swipe > MIN_SWIPE_DISTANCE){
                    onCardViewClick();
                }
                break;
        }
        return false;
    }

}
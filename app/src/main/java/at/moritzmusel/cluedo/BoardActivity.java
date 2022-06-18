package at.moritzmusel.cluedo;

import static at.moritzmusel.cluedo.entities.Character.DR_ORCHID;
import static at.moritzmusel.cluedo.entities.Character.MRS_PEACOCK;
import static at.moritzmusel.cluedo.entities.Character.PROFESSOR_PLUM;
import static at.moritzmusel.cluedo.entities.Character.REVEREND_GREEN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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

import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import at.moritzmusel.cluedo.entities.Player;
import at.moritzmusel.cluedo.game.Dice;
import at.moritzmusel.cluedo.sensor.OnSwipeTouchListener;
import at.moritzmusel.cluedo.sensor.ShakeDetector;
import at.moritzmusel.cluedo.game.Gameplay;

public class BoardActivity extends AppCompatActivity{
    private AllTheCards allcards;
    private OnSwipeTouchListener swipeTouchListener;
    private float x1, x2, y1, y2;
    static final int MIN_SWIPE_DISTANCE = 150;
    private final ArrayList<ImageButton> allArrows = new ArrayList<>();
    private Dice dice;
    private Gameplay gp1;
    private int newPosition;
    ArrayList<Button> allPositions = new ArrayList<>();
    ArrayList<ImageView> allPlayers = new ArrayList<>();
    ArrayList<Button> secrets = new ArrayList<>();
    ArrayList<ImageView> allWeapons = new ArrayList<>();
    Integer[] help = {1,2,3,4,5,6,7,8,9};
    List<Integer> helpList = Arrays.asList(help);
    HashMap<Integer,String> freeWeaponPlaces = new HashMap<>();
    private View playerCardsView;
    private EvidenceCards evidenceCards;
    private ImageView image;
    private View dice_layout;
    //private ImageView diceView;
    private View decorView, diceView;

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
        decorView.setOnSystemUiVisibilityChangeListener(visibility -> {
            if (visibility == 0)
                decorView.setSystemUiVisibility(hideSystemBars());
        });

        Player Player2 = new Player(2, REVEREND_GREEN);
        Player Player3 = new Player(3, PROFESSOR_PLUM);
        Player Player4 = new Player(4, MRS_PEACOCK);
        Player Player5 = new Player(5, DR_ORCHID);
        ArrayList<Player> playersEven = new ArrayList<>(Arrays.asList(Player2,Player3, Player4, Player5));

        gp1 = new Gameplay(playersEven);
        gp1.decidePlayerWhoMovesFirst();

        setContentView(R.layout.test_board2);
        ConstraintLayout constraint = findViewById(R.id.constraintLayout);

        // Gets all children of the layout and puts all arrows in the arrayList and all positions in the hashMap
        for (int i = 1; i <= constraint.getChildCount(); i++) {
            View test = constraint.getChildAt(i);

            if(test instanceof Button && getResources().getResourceEntryName(test.getId()).contains("secret"))
                secrets.add((Button) test);

            else if(test instanceof  ImageView && getResources().getResourceEntryName(test.getId()).contains("w_"))
                allWeapons.add((ImageView) test);

            else if (test instanceof Button && getResources().getResourceEntryName(test.getId()).matches("[a-z_]+[0-9]+"))
                allPositions.add((Button) test);

             else if (test instanceof ImageButton && !getResources().getResourceEntryName(test.getId()).contains("cardView"))
                allArrows.add((ImageButton) test);

             else if (test instanceof ImageView && getResources().getResourceEntryName(test.getId()).matches("^[a-z]+$"))
                allPlayers.add((ImageView) test);

        }

        constraint.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        // Layout has happened here.
                            System.out.println("Everything rendered");
                            SecureRandom r = new SecureRandom();
                            for (int i = 0; i < gp1.getPlayers().size(); i++) {
                                for (int j = 0; j < allPlayers.size(); j++) {
                                    if (getResources().getResourceEntryName(allPlayers.get(j).getId()).equals(gp1.getPlayers().get(i).getPlayerCharacterName().name().split("[_]")[1].toLowerCase())) {
                                        if (findViewById(allPlayers.get(i).getId()).getVisibility() == View.INVISIBLE) {
                                            findViewById(allPlayers.get(j).getId()).setVisibility(View.VISIBLE);
                                            String name = getResources().getResourceEntryName(allPlayers.get(j).getId()) + "_";
                                            int room = r.nextInt(9) + 1;
                                            Button startPlace = findViewById(createRoomDestination(name, room));
                                            findViewById(allPlayers.get(j).getId()).setX(startPlace.getX());
                                            findViewById(allPlayers.get(j).getId()).setY(startPlace.getY());
                                            gp1.getPlayers().get(i).setPositionOnBoard(room);
                                            System.out.println(getResources().getResourceEntryName(startPlace.getId()));
                                        }
                                    }
                                }
                            }
                        Collections.shuffle(helpList);
                            helpList.toArray(help);
                            for(int i = 0; i < help.length; i++){
                                if(i < allWeapons.size()) {
                                    Button startPosition = findViewById(createRoomDestination("weapon", help[i]));
                                    findViewById(allWeapons.get(i).getId()).setX(startPosition.getX());
                                    findViewById(allWeapons.get(i).getId()).setY(startPosition.getY());
                                    freeWeaponPlaces.put(help[i],getResources().getResourceEntryName(allWeapons.get(i).getId()));
                                } else
                                    freeWeaponPlaces.put(help[i],null);
                            }
                        newPosition = gp1.findPlayerByCharacterName(gp1.getCurrentPlayer()).getPositionOnBoard();
                        // Don't forget to remove your listener when you are done with it.
                        constraint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });


        /*diceView = findViewById(R.id.dialogDice);
        dice = new Dice((ImageView) diceView);
        diceView.setOnClickListener(v -> {
            dice.throwDice();
            diceRolled();
        });*/


        allcards = new AllTheCards();
        allcards.getGameCards();

        evidenceCards = new EvidenceCards();

        //ImageButton cardView = findViewById(R.id.cardView);
        //cardView.setOnClickListener(this);

        image = new ImageView(this);
        image.setImageResource(R.drawable.cardback);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        shakeDetector = new ShakeDetector();

        callDice();
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



    /**
     * Creates an alert with a dice action, which is shakeable and clickable.
     */
    private void callDice(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(BoardActivity.this,R.style.AlertDialogStyle);
        builder.setTitle("Throw Dice");

        LayoutInflater inflater = getLayoutInflater();
        dice_layout = inflater.inflate(R.layout.custom_dialog, null);
        builder.setView(dice_layout);

        diceView = dice_layout.findViewById(R.id.dialogDice);
        dice = new Dice((ImageView) diceView);

        mSensorManager.registerListener(shakeDetector, accel, SensorManager.SENSOR_DELAY_UI);

        builder.setCancelable(false);

        /*builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });*/

        android.app.AlertDialog alertDialog = builder.create();

        alertDialog.show();

        shakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count){
                if(count < 2){
                    dice.throwDice();
                    diceRolled();
                    final Timer timer2 = new Timer();
                    timer2.schedule(new TimerTask() {
                        public void run() {
                            alertDialog.dismiss();
                            timer2.cancel(); //this will cancel the timer of the system
                        }
                    }, 2000);
                }
            }
        });



    }

    /**
     * Called when dice gets rolled. Removes dice clickListener, resets stepsTaken in Gameplay, creates a alert for the magnifying glass method
     * and calls the move methode
     */
    public void diceRolled() {
        //diceView.setOnClickListener(v -> Toast.makeText(this,"You already rolled the dice!", Toast.LENGTH_SHORT).show());
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
        newPosition = gp1.findPlayerByCharacterName(gp1.endTurn()).getPositionOnBoard();
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

        if (mover.getX() < findViewById(R.id.border_left).getX()) {
            switch (getResources().getResourceEntryName(v.getId())) {
                case "secret_1":
                    if(gp1.isAllowedToUseSecretPassage()) {
                        destination = findViewById(createRoomDestination(character, 7));
                        gp1.setStepsTaken(dice.getNumberRolled()-1);
                        moveAnimation(mover, destination, null, false);
                    }
                    break;
                case "secret_3":
                    if(gp1.isAllowedToUseSecretPassage()) {
                        destination = findViewById(createRoomDestination(character, 3));
                        gp1.setStepsTaken(dice.getNumberRolled()-1);
                        moveAnimation(mover, destination, null, false);
                    }
                    break;
                case "lounge_btn_right":
                    destination = findViewById(createRoomDestination(character,2));
                    moveAnimation(mover, destination, null, false);
                    break;
                case "lounge_btn_down":
                case "billiard_btn_up":
                    destination = findViewById(createRoomDestination(character,4));
                    moveAnimation(mover, destination, findViewById(R.id.median_left), true);
                    break;
                case "dining_btn_up":
                    destination = findViewById(createRoomDestination(character,1));
                    moveAnimation(mover, destination, findViewById(R.id.median_left), true);
                    break;
                case "dining_btn_right":
                    destination = findViewById(createRoomDestination(character,5));
                    moveAnimation(mover, destination, null, false);
                    break;
                case "dining_btn_down":
                    destination = findViewById(createRoomDestination(character,7));
                    moveAnimation(mover, destination, findViewById(R.id.median_left), true);
                    break;
                case "billiard_btn_right":
                    destination = findViewById(createRoomDestination(character,8));
                    moveAnimation(mover, destination, findViewById(R.id.median_left), true);
                    break;
            }
        } else if (mover.getX() > findViewById(R.id.border_right).getX()) {
            switch (getResources().getResourceEntryName(v.getId())) {
                case "secret_2":
                    if(gp1.isAllowedToUseSecretPassage()) {
                        destination = findViewById(createRoomDestination(character, 7));
                        gp1.setStepsTaken(dice.getNumberRolled()-1);
                        moveAnimation(mover, destination, null, false);
                    }
                    break;
                case "ballroom_btn_left":
                    destination = findViewById(createRoomDestination(character,2));
                    moveAnimation(mover, destination, null, false);
                    break;
                case "ballroom_btn_down":
                case "hall_btn_up":
                    destination = findViewById(createRoomDestination(character,6));
                    moveAnimation(mover, destination, findViewById(R.id.median_right), true);
                    break;
                case "library_btn_up":
                    destination = findViewById(createRoomDestination(character,3));
                    moveAnimation(mover, destination, findViewById(R.id.median_right), true);
                    break;
                case "library_btn_left":
                    destination = findViewById(createRoomDestination(character,5));
                    moveAnimation(mover, destination, null, false);
                    break;
                case "library_btn_down":
                    destination = findViewById(createRoomDestination(character,9));
                    moveAnimation(mover, destination, findViewById(R.id.median_right), true);
                    break;
                case "hall_btn_left":
                    destination = findViewById(createRoomDestination(character,8));
                    moveAnimation(mover, destination, null, false);
                    break;
            }
        } else {
            switch (getResources().getResourceEntryName(v.getId())) {
                case "conservatory_btn_left":
                    destination = findViewById(createRoomDestination(character,1));
                    moveAnimation(mover, destination, null, false);
                    break;
                case "conservatory_btn_down":
                case "study_btn_up":
                    destination = findViewById(createRoomDestination(character,5));
                    moveAnimation(mover, destination, findViewById(R.id.median_center), true);
                    break;
                case "conservatory_btn_right":
                    destination = findViewById(createRoomDestination(character,3));
                    moveAnimation(mover, destination, null, false);
                    break;
                case "kitchen_btn_up":
                    destination = findViewById(createRoomDestination(character,2));
                    moveAnimation(mover, destination, findViewById(R.id.median_center), true);
                    break;
                case "kitchen_btn_right":
                    destination = findViewById(createRoomDestination(character,6));
                    moveAnimation(mover, destination, null, false);
                    break;
                case "kitchen_btn_down":
                    destination = findViewById(createRoomDestination(character,8));
                    moveAnimation(mover, destination, findViewById(R.id.median_center), true);
                    break;
                case "kitchen_btn_left":
                    destination = findViewById(createRoomDestination(character,4));
                    moveAnimation(mover, destination, null, false);
                    break;
                case "study_btn_left":
                    destination = findViewById(createRoomDestination(character,7));
                    moveAnimation(mover, destination, null, false);
                    break;
                case "study_btn_right":
                    destination = findViewById(createRoomDestination(character,9));
                    moveAnimation(mover, destination, null, false);
                    break;
            }
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

    private int createWeaponDestination(int room){
        return getResources().getIdentifier("weapon"+room, "id", getPackageName());
    }

    /**
     * Gets called by the suspicion to bring the weapon to the currentRoom
     * @param str the name of the weapon that's been moved
     */
    public void switchWeapon(String str) {
        String weapon = "w_"+str;
        if (freeWeaponPlaces.get(newPosition) == null) {
            for (ImageView IV:allWeapons) {
                if (getResources().getResourceEntryName(IV.getId()).equals(weapon)) {
                    IV.setX(findViewById(createWeaponDestination(newPosition)).getX());
                    IV.setY(findViewById(createWeaponDestination(newPosition)).getY());
                    for(int s: freeWeaponPlaces.keySet()) {
                        if (Objects.equals(freeWeaponPlaces.get(s), weapon)) {
                            freeWeaponPlaces.put(s, null);
                            freeWeaponPlaces.put(newPosition, weapon);
                            System.out.println(freeWeaponPlaces.get(s));
                            System.out.println(freeWeaponPlaces.get(newPosition));
                            break;
                        }
                    }
                    break;
                }
            }
        } else {
            String otherWeapon = freeWeaponPlaces.get(newPosition);
            ImageView otherWeaponView = findViewById(getResources().getIdentifier(otherWeapon, "id", getPackageName()));
            for (ImageView IV: allWeapons) {
                if (getResources().getResourceEntryName(IV.getId()).equals(weapon)) {
                    IV.setX(findViewById(createWeaponDestination( newPosition)).getX());
                    IV.setY(findViewById(createWeaponDestination(newPosition)).getY());
                    for(int s: freeWeaponPlaces.keySet()) {
                        if(Objects.equals(freeWeaponPlaces.get(s), weapon)){
                            otherWeaponView.setX(findViewById(createWeaponDestination(s)).getX());
                            otherWeaponView.setY(findViewById(createWeaponDestination(s)).getY());
                            freeWeaponPlaces.put(s,otherWeapon);
                            freeWeaponPlaces.put(newPosition,weapon);
                            System.out.println(newPosition);
                            System.out.println(s);
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
     * @param median      important for the path for the animation
     * @param vertical    if true we follow the path, if false we move in a strait line
     */
    private void moveAnimation(View mover, View destination, View median, boolean vertical) {

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
                notifyCurrentPlayer();
                callDice();
            }
        }

    /**
     * if button is clicked or the screen is swiped horizontally, the current cards of the player are shown
     */
    public void onCardViewClick() {

        AlertDialog.Builder builder = new AlertDialog.Builder(BoardActivity.this,R.style.AlertDialogStyle);
        builder.setTitle("My Cards");

        setPlayerCardImages();
        builder.setView(playerCardsView);

        builder.setNeutralButton("OK", (dialog, which) -> dialog.cancel());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * sets the layout for the card alert
     * initialises the ImageViews of the cards and sets
     * the right image for the views.
     */
    private void setPlayerCardImages() {
        LayoutInflater factory = LayoutInflater.from(BoardActivity.this);
        playerCardsView = factory.inflate(R.layout.image_show_cards, null);

        ImageView card1 = playerCardsView.findViewById(R.id.myCard1);
        ImageView card2 = playerCardsView.findViewById(R.id.myCard2);
        ImageView card3 = playerCardsView.findViewById(R.id.myCard3);
        ImageView card4 = playerCardsView.findViewById(R.id.myCard4);
        ImageView card5 = playerCardsView.findViewById(R.id.myCard5);
        ImageView card6 = playerCardsView.findViewById(R.id.myCard6);

        ArrayList<ImageView> card_list = new ArrayList<>();
        card_list.add(card1);
        card_list.add(card2);
        card_list.add(card3);
        card_list.add(card4);
        card_list.add(card5);
        card_list.add(card6);

        int[] card_ids = getPlayerCardIds();

        if(card_ids.length <= 6){
            for(int i = 0; i < card_ids.length;i++){
                setPlayerCard(card_list.get(i), card_ids[i]);
            }
            for(int i = card_ids.length; i < card_list.size();i++){
                card_list.get(i).setVisibility(View.GONE);
            }
        }
        else{
            for(int i = 0; i < 6;i++){
                setPlayerCard(card_list.get(i), card_ids[i]);
            }
        }


    }

    /**
     * Connects with the network to get the cardids for the images
     * @return Array with three to six ids for the images
     */
    private int[] getPlayerCardIds(){
        int[] id;
        //Hier mit Netzwerk verknüpfen
        id = new int[]{
                allcards.getGameCards().get(0).getId(),
                allcards.getGameCards().get(7).getId(),
                allcards.getGameCards().get(8).getId(),
                allcards.getGameCards().get(20).getId(),
                allcards.getGameCards().get(10).getId(),
                allcards.getGameCards().get(18).getId()};
        return id;
    }

    /**
     * sets the image for the ImageView according to the id
     * @param card ImageView to set the image
     * @param id to get the right image
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
                card.setImageResource(R.drawable.hall_card);
                break;
            case 13:
                card.setImageResource(R.drawable.lounge_card);
                break;
            case 14:
                card.setImageResource(R.drawable.dining_card);
                break;
            case 15:
                card.setImageResource(R.drawable.kitchen_card);
                break;
            case 16:
                card.setImageResource(R.drawable.ballroom_card);
                break;
            case 17:
                card.setImageResource(R.drawable.conservatory_card);
                break;
            case 18:
                card.setImageResource(R.drawable.billiard_card);
                break;
            case 19:
                card.setImageResource(R.drawable.library_card);
                break;
            case 20:
                card.setImageResource(R.drawable.study_card);
                break;
            default:
                card.setImageResource(R.drawable.cardback);
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
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();
                float swipeRight = x2-x1, swipeLeft = x1-x2;

                if(swipeRight > MIN_SWIPE_DISTANCE){
                    startNotepad();
                } else if(swipeLeft > MIN_SWIPE_DISTANCE){
                    startSuspicion();
                }else{
                    onCardViewClick();
                }
                break;
        }
        return false;
    }


    /*
    private int clickCount;
    private long start_time;
    static final int MAX_DURATION = 500;
    /**
     * EventListener für Swipe-Event to start either the Notepad, Suspicion or the card alert
     *//*
    @Override
    public boolean onTouchEvent (MotionEvent touchEvent){
        switch(touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                start_time = System.currentTimeMillis();
                clickCount++;
                break;

            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();
                float swipeRight = x2-x1, swipeLeft = x1-x2;

                if(swipeRight > MIN_SWIPE_DISTANCE){
                    startNotepad();
                } else if(swipeLeft > MIN_SWIPE_DISTANCE){
                    startSuspicion();
                }else if(clickCount == 1){
                    start_time = System.currentTimeMillis();
                }else if(clickCount == 2){
                    long duration = System.currentTimeMillis() - start_time;
                    if(duration <= MAX_DURATION){
                        onCardViewClick();
                        clickCount = 0;
                        start_time = 0;
                        duration = 0;
                    }else{
                        clickCount = 1;
                        start_time = System.currentTimeMillis();
                    }
                }
                break;
        }
        return false;
    }*/

}
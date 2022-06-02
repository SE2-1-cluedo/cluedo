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

import android.os.Bundle;
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

import at.moritzmusel.cluedo.entities.Player;
import at.moritzmusel.cluedo.game.Dice;
import at.moritzmusel.cluedo.game.Gameplay;

public class BoardActivity extends AppCompatActivity {

    private View decorView, diceView;
    private AllTheCards allCards;
    private float x1;
    static final int MIN_SWIPE_DISTANCE = 150;
    private final ArrayList<ImageButton> allArrows = new ArrayList<>();
    Dice dice;
    private Gameplay gp1;
    private int newPosition;
    ArrayList<Button> allPositions = new ArrayList<>();
    ArrayList<ImageView> allPlayers = new ArrayList<>();
    ArrayList<Button> secrets = new ArrayList<>();
    ArrayList<ImageView> allWeapons = new ArrayList<>();
    Integer[] help = {1,2,3,4,5,6,7,8,9};
    List<Integer> helpList = Arrays.asList(help);
    HashMap<Integer,String> freeWeaponPlaces = new HashMap<>();

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

        allCards = new AllTheCards();

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

        diceView = findViewById(R.id.diceView);
        dice = new Dice((ImageView) diceView);
        diceView.setOnClickListener(v -> {
            dice.throwDice();
            diceRolled();
        });

        ImageButton cardView = findViewById(R.id.cardView);
        cardView.setVisibility(View.VISIBLE);
        cardView.setOnClickListener(v -> onCardViewClick());
    }

    /**
     * Called when dice gets rolled. Removes dice clickListener, resets stepsTaken in Gameplay
     * and calls the move methode
     */
    public void diceRolled() {
        diceView.setOnClickListener(v -> Toast.makeText(this,"You already rolled the dice!", Toast.LENGTH_SHORT).show());
        gp1.setStepsTaken(0);
        movePlayerWithArrows();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
            decorView.setSystemUiVisibility(hideSystemBars());
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
                diceView.setOnClickListener(v -> {
                    dice.throwDice();
                    diceRolled();
                });
                System.out.println("Has moved enough");
                deactivateSecrets();
                notifyCurrentPlayer();
            }
        }

    /**
     * if button is clicked, the current cards of the player are shown
     */
    public void onCardViewClick() {
            AlertDialog.Builder builder = new AlertDialog.Builder(BoardActivity.this);
            builder.setTitle("My Cards");

            final String[] items = {allCards.getGameCards().get(0).getDesignation(),allCards.getGameCards().get(10).getDesignation(),allCards.getGameCards().get(18).getDesignation()};
            //Später vielleicht mit den Bildern
            //Nur Demo brauche Methode um die eigentlichen Karten zu bekommen
            builder.setItems(items, (dialog, item) -> {

            });
            builder.setNeutralButton("OK", (dialog, which) -> dialog.cancel());
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
    }

    /**
     * starts a new Activity to see the notepad
     */
    public void startNotepad(){
        Intent intent = new Intent(this, NotepadActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }

    /**
     * starts a new Activity to call an accusation or suspicion
     */
    public void startSuspicion(){
        Intent intent = new Intent(this, SuspicionActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }

    //EventListener für Swipe-Event
    @Override
    public boolean onTouchEvent (MotionEvent touchEvent){
        switch(touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                float y1 = touchEvent.getY();
                break;

            case MotionEvent.ACTION_UP:
                float x2 = touchEvent.getX();
                float y2 = touchEvent.getY();
                float swipeRight = x2 -x1,
                        swipeLeft = x1- x2;

                if(swipeRight > MIN_SWIPE_DISTANCE){
                    startNotepad();
                } else if(swipeLeft > MIN_SWIPE_DISTANCE){
                    startSuspicion();
                }
                break;
        }
        return false;
    }
}
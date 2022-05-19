package at.moritzmusel.cluedo;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;

import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import at.moritzmusel.cluedo.entities.Character;
import at.moritzmusel.cluedo.game.Dice;
import at.moritzmusel.cluedo.game.Gameplay;

public class BoardActivity extends AppCompatActivity {

    private View decorView, diceView;

    ArrayList<ImageButton> allArrows = new ArrayList<>();
    Dice dice;
    Gameplay gp1;
    private int newPosition, counter;
    ArrayList<Button> allPositions = new ArrayList<>();
    ArrayList<ImageView> allPlayers = new ArrayList<>();
    ArrayList<Button> secrets = new ArrayList<>();


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
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

        gp1 = new Gameplay();

        setContentView(R.layout.test_board2);
        ConstraintLayout constraint = findViewById(R.id.constraintLayout);

        // Gets all children of the layout and puts all arrows in the arrayList and all positions in the hashMap
        for (int i = 1; i <= constraint.getChildCount(); i++) {
            View test = constraint.getChildAt(i);

            if(test instanceof Button && getResources().getResourceEntryName(test.getId()).contains("secret"))
                secrets.add((Button) test);

            else if (test instanceof Button && getResources().getResourceEntryName(test.getId()).matches("[a-z_]+[0-9]+"))
                allPositions.add((Button) test);

             else if (test instanceof ImageButton)
                allArrows.add((ImageButton) test);

             else if (test instanceof ImageView && getResources().getResourceEntryName(test.getId()).matches("^[a-z]+$"))
                allPlayers.add((ImageView) test);

        }
        /*
            for(int i = 1; i <= gp1.getPlayers().size(); i++) {
                for (int j = 0; j <= allPlayers.size(); j++) {
                    if (getResources().getResourceEntryName(allPlayers.get(j).getId()).equals(gp1.getPlayers().get(i).getPlayerCharacterName().name().split("[_]")[1].toLowerCase())) {

                    }
                }
            }
*/
        newPosition = 1;

        diceView = findViewById(R.id.diceView);
        dice = new Dice((ImageView) diceView);
        diceView.setOnClickListener(v -> {
            dice.throwDice();
            diceRolled();
        });
    }

    public void diceRolled() {
        diceView.setOnClickListener(v -> System.out.println("Has been removed"));
        counter = 0;
        movePlayerWithArrows(newPosition);
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
     * called by the arrows, searches for current player and the arrow that has been clicked and calls
     * the move function afterwards
     *
     * @param v the button that has been clicked
     */
    public void move(View v) {

        View destination;
        //View mover = getMover();
        View mover = findViewById(R.id.green);
        //String character = getResources().getResourceEntryName(mover.getId())+"_";
        String character = "green_";

        if (mover.getX() < findViewById(R.id.border_left).getX()) {
            switch (getResources().getResourceEntryName(v.getId())) {
                case "secret_1":
                    if(mover.getX() == findViewById(createDestination(character,1)).getX()) {
                        destination = findViewById(createDestination(character, 7));
                        counter=dice.getNumberRolled()-1;
                        moveAnimation(mover, destination, null, false);
                    }
                    break;
                case "secret_3":
                    if(mover.getX() == findViewById(createDestination(character,7)).getX()) {
                        destination = findViewById(createDestination(character, 3));
                        counter=dice.getNumberRolled()-1;
                        moveAnimation(mover, destination, null, false);
                    }
                    break;
                case "lounge_btn_right":
                    destination = findViewById(createDestination(character,2));
                    moveAnimation(mover, destination, null, false);
                    break;
                case "lounge_btn_down":
                case "billiard_btn_up":
                    destination = findViewById(createDestination(character,4));
                    moveAnimation(mover, destination, findViewById(R.id.median_left), true);
                    break;
                case "dining_btn_up":
                    destination = findViewById(createDestination(character,1));
                    moveAnimation(mover, destination, findViewById(R.id.median_left), true);
                    break;
                case "dining_btn_right":
                    destination = findViewById(createDestination(character,5));
                    moveAnimation(mover, destination, null, false);
                    break;
                case "dining_btn_down":
                    destination = findViewById(createDestination(character,7));
                    moveAnimation(mover, destination, findViewById(R.id.median_left), true);
                    break;
                case "billiard_btn_right":
                    destination = findViewById(createDestination(character,8));
                    moveAnimation(mover, destination, findViewById(R.id.median_left), true);
                    break;
            }
        } else if (mover.getX() > findViewById(R.id.border_right).getX()) {
            switch (getResources().getResourceEntryName(v.getId())) {
                case "secret_2":
                    if(mover.getX() == findViewById(createDestination(character,3)).getX()) {
                        destination = findViewById(createDestination(character, 7));
                        counter=dice.getNumberRolled()-1;
                        moveAnimation(mover, destination, null, false);
                    }
                    break;
                case "ballroom_btn_left":
                    destination = findViewById(createDestination(character,2));
                    moveAnimation(mover, destination, null, false);
                    break;
                case "ballroom_btn_down":
                case "hall_btn_up":
                    destination = findViewById(createDestination(character,6));
                    moveAnimation(mover, destination, findViewById(R.id.median_right), true);
                    break;
                case "library_btn_up":
                    destination = findViewById(createDestination(character,3));
                    moveAnimation(mover, destination, findViewById(R.id.median_right), true);
                    break;
                case "library_btn_left":
                    destination = findViewById(createDestination(character,5));
                    moveAnimation(mover, destination, null, false);
                    break;
                case "library_btn_down":
                    destination = findViewById(createDestination(character,9));
                    moveAnimation(mover, destination, findViewById(R.id.median_right), true);
                    break;
                case "hall_btn_left":
                    destination = findViewById(createDestination(character,8));
                    moveAnimation(mover, destination, null, false);
                    break;
            }
        } else {
            switch (getResources().getResourceEntryName(v.getId())) {
                case "conservatory_btn_left":
                    destination = findViewById(createDestination(character,1));
                    moveAnimation(mover, destination, null, false);
                    break;
                case "conservatory_btn_down":
                case "study_btn_up":
                    destination = findViewById(createDestination(character,5));
                    moveAnimation(mover, destination, findViewById(R.id.median_center), true);
                    break;
                case "conservatory_btn_right":
                    destination = findViewById(createDestination(character,3));
                    moveAnimation(mover, destination, null, false);
                    break;
                case "kitchen_btn_up":
                    destination = findViewById(createDestination(character,2));
                    moveAnimation(mover, destination, findViewById(R.id.median_center), true);
                    break;
                case "kitchen_btn_right":
                    destination = findViewById(createDestination(character,6));
                    moveAnimation(mover, destination, null, false);
                    break;
                case "kitchen_btn_down":
                    destination = findViewById(createDestination(character,8));
                    moveAnimation(mover, destination, findViewById(R.id.median_center), true);
                    break;
                case "kitchen_btn_left":
                    destination = findViewById(createDestination(character,4));
                    moveAnimation(mover, destination, null, false);
                    break;
                case "study_btn_left":
                    destination = findViewById(createDestination(character,7));
                    moveAnimation(mover, destination, null, false);
                    break;
                case "study_btn_right":
                    destination = findViewById(createDestination(character,9));
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

    private void activateSecrets(){
        for(Button b : secrets)
            b.setVisibility(View.VISIBLE);
    }

    private void deactivateSecrets(){
        for(Button b : secrets)
            b.setVisibility(View.INVISIBLE);
    }

    private View getMover(){
        switch (gp1.getCurrentPlayer().name()){
            case "MISS_SCARLET":
                return findViewById(R.id.scarlett);

            case "COLONEL_MUSTARD":
                return findViewById(R.id.mustard);

            case "DR_ORCHID":
                return findViewById(R.id.orchid);

            case "REVEREND_GREEN":
                return findViewById(R.id.green);

            case "MRS_PEACOCK":
                return findViewById(R.id.peacock);

            case "PROFESSOR_PLUM":
                return findViewById(R.id.plum);

            default: return null;
        }
    }

    private int createDestination(String character, int room){
        character+=room;
        newPosition = room;
        return getResources().getIdentifier(character, "id", getPackageName());
    }

    private void switchWeapon(){}

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

        setArrowsInvisible();
        //gp1.canMove();
        counter++;
        movePlayerWithArrows(newPosition);
    }

    /**
     * checks if a player is allowed to move and afterwards the rooms around the player if there is enough room too move in the next room if it is
     * then show a clickable arrow if not do nothing
     *
     * @param room input the position where the player is currently standing
     */
    private void movePlayerWithArrows(int room) {

        //if (gp1.findPlayerByCharacterName(gp1.getCurrentPlayer()).getIsAbleToMove()) {
        if (counter < dice.getNumberRolled()) {
            activateSecrets();
            switch (room) {
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
        }

    }

    /**
     * checks which room-Button has been clicked and
     * changes the layout to the specific room
     * @param v the button that has been clicked
     */
    public void onButtonClick(View v) {

        //System.out.println(getResources().getResourceEntryName(v.getId())+" has been clicked");
        /*
        switch(getResources().getResourceEntryName(v.getId())){
            case "lounge_button":
                setContentView(R.layout.activity_room_lounge);
                break;
            case "conservatory_button":
                setContentView(R.layout.activity_room_conservatory);
                break;
            case "ballroom_button":
                setContentView(R.layout.activity_room_ballroom);
                break;
            case "dining_button":
                setContentView(R.layout.activity_room_dining);
                break;
            case "kitchen_button":
                setContentView(R.layout.activity_room_kitchen);
                break;
            case "library_button":
                setContentView(R.layout.activity_room_library);
                break;
            case "billiard_button":
                setContentView(R.layout.activity_room_billiard);
                break;
            case "study_button":
                setContentView(R.layout.activity_room_study);
                break;
            case "hall_button":
                setContentView(R.layout.activity_room_hall);
                break;
        }

         */
    }
}
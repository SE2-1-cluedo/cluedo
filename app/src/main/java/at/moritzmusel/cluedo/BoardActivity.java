package at.moritzmusel.cluedo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.res.Configuration;

import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;

import at.moritzmusel.cluedo.game.Dice;
import at.moritzmusel.cluedo.game.Gameplay;

public class BoardActivity extends AppCompatActivity {

    private View decorView, diceView;

    ArrayList<ImageButton> allArrows = new ArrayList<>();
    Dice dice;
    int counter;
    Gameplay gp1;
    private String oldPosition, newPosition;
    private final HashMap<String, Boolean> occupiedPositions = new HashMap<>();


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

        setContentView(R.layout.test_board2);
        ConstraintLayout constraint = findViewById(R.id.constraintLayout);

        // Gets all children of the layout and puts all arrows in the arrayList and all positions in the hashMap
        for (int i = 1; i <= constraint.getChildCount(); i++) {
            View test = constraint.getChildAt(i);

            if (test instanceof Button && getResources().getResourceEntryName(test.getId()).contains("position")) {
                occupiedPositions.put(getResources().getResourceEntryName(test.getId()), true);

            } else if (test instanceof ImageButton) {
                allArrows.add((ImageButton) test);
            }
        }

        newPosition = "lounge_position_1";
        gp1 = new Gameplay();

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

    /**
     * called by the arrows, searches for current player and the arrow that has been clicked and calls
     * the move function afterwards
     *
     * @param v the button that has been clicked
     */
    public void move(View v) {

        View mover = findViewById(R.id.white);
        View destination;
        /*
        switch (gp1.getCurrentPlayer().name()){
            case "MISS_SCARLET":
                mover = findViewById(R.id.scarlet);
                break;
            case "COLONEL_MUSTARD":
                mover = findViewById(R.id.mustard);
                break;
            case "MADAME_WHITE":
                mover = findViewById(R.id.white);
                break;
            case "REVEREND_GREEN":
                mover = findViewById(R.id.green);
                break;
            case "MRS_PEACOCK":
                mover = findViewById(R.id.peacock);
                break;
            case "PROFESSOR_PLUM":
                mover = findViewById(R.id.plum);
                break;
        }
*/
        //lastPosition = gp1.findPlayerByCharacterName(g1.getCurrentPlayer()).getPositionOnBoardString();
        oldPosition = newPosition;
        if (oldPosition != null && !oldPosition.equals("startPoint")) {
            occupiedPositions.replace(oldPosition, true);
        }

        if (mover.getX() < findViewById(R.id.border1).getX()) {
            switch (getResources().getResourceEntryName(v.getId())) {
                case "lounge_btn_right":
                    destination = Boolean.TRUE.equals(occupiedPositions.get("conservatory_position_1")) ? findViewById(R.id.conservatory_position_1) : findViewById(R.id.conservatory_position_2);
                    moveAnimation(mover, destination, null, false);
                    break;
                case "lounge_btn_down":
                case "billiard_btn_up":
                    destination = Boolean.TRUE.equals(occupiedPositions.get("dining_position_1")) ? findViewById(R.id.dining_position_1) : findViewById(R.id.dining_position_2);
                    moveAnimation(mover, destination, findViewById(R.id.median1), true);
                    break;
                case "dining_btn_up":
                    destination = Boolean.TRUE.equals(occupiedPositions.get("lounge_position_1")) ? findViewById(R.id.lounge_position_1) : findViewById(R.id.lounge_position_2);
                    moveAnimation(mover, destination, findViewById(R.id.median1), true);
                    break;
                case "dining_btn_right":
                    destination = Boolean.TRUE.equals(occupiedPositions.get("kitchen_position_1")) ? findViewById(R.id.kitchen_position_1) : findViewById(R.id.kitchen_position_2);
                    moveAnimation(mover, destination, null, false);
                    break;
                case "dining_btn_down":
                    destination = Boolean.TRUE.equals(occupiedPositions.get("billiard_position_1")) ? findViewById(R.id.billiard_position_1) : findViewById(R.id.billiard_position_2);
                    moveAnimation(mover, destination, findViewById(R.id.median1), true);
                    break;
                case "billiard_btn_right":
                    destination = Boolean.TRUE.equals(occupiedPositions.get("study_position_1")) ? findViewById(R.id.study_position_1) : findViewById(R.id.study_position_2);
                    moveAnimation(mover, destination, findViewById(R.id.median1), true);
                    break;
            }
        } else if (mover.getX() > findViewById(R.id.border2).getX()) {
            switch (getResources().getResourceEntryName(v.getId())) {
                case "ballroom_btn_left":
                    destination = Boolean.TRUE.equals(occupiedPositions.get("conservatory_position_1")) ? findViewById(R.id.conservatory_position_1) : findViewById(R.id.conservatory_position_2);
                    moveAnimation(mover, destination, null, false);
                    break;
                case "ballroom_btn_down":
                case "hall_btn_up":
                    destination = Boolean.TRUE.equals(occupiedPositions.get("library_position_1")) ? findViewById(R.id.library_position_1) : findViewById(R.id.library_position_2);
                    moveAnimation(mover, destination, findViewById(R.id.median3), true);
                    break;
                case "library_btn_up":
                    destination = Boolean.TRUE.equals(occupiedPositions.get("ball_position_1")) ? findViewById(R.id.ball_position_1) : findViewById(R.id.ball_position_2);
                    moveAnimation(mover, destination, findViewById(R.id.median3), true);
                    break;
                case "library_btn_left":
                    destination = Boolean.TRUE.equals(occupiedPositions.get("kitchen_position_1")) ? findViewById(R.id.kitchen_position_1) : findViewById(R.id.kitchen_position_2);
                    moveAnimation(mover, destination, null, false);
                    break;
                case "library_btn_down":
                    destination = Boolean.TRUE.equals(occupiedPositions.get("hall_position_1")) ? findViewById(R.id.hall_position_1) : findViewById(R.id.hall_position_2);
                    moveAnimation(mover, destination, findViewById(R.id.median3), true);
                    break;
                case "hall_btn_left":
                    destination = Boolean.TRUE.equals(occupiedPositions.get("study_position_1")) ? findViewById(R.id.study_position_1) : findViewById(R.id.study_position_2);
                    moveAnimation(mover, destination, null, false);
                    break;
            }
        } else {
            switch (getResources().getResourceEntryName(v.getId())) {
                case "conservatory_btn_left":
                    destination = Boolean.TRUE.equals(occupiedPositions.get("lounge_position_1")) ? findViewById(R.id.lounge_position_1) : findViewById(R.id.lounge_position_2);
                    moveAnimation(mover, destination, null, false);
                    break;
                case "conservatory_btn_down":
                case "study_btn_up":
                    destination = Boolean.TRUE.equals(occupiedPositions.get("kitchen_position_1")) ? findViewById(R.id.kitchen_position_1) : findViewById(R.id.kitchen_position_2);
                    moveAnimation(mover, destination, findViewById(R.id.median2), true);
                    break;
                case "conservatory_btn_right":
                    destination = Boolean.TRUE.equals(occupiedPositions.get("ball_position_1")) ? findViewById(R.id.ball_position_1) : findViewById(R.id.ball_position_2);
                    moveAnimation(mover, destination, null, false);
                    break;
                case "kitchen_btn_up":
                    destination = Boolean.TRUE.equals(occupiedPositions.get("conservatory_position_1")) ? findViewById(R.id.conservatory_position_1) : findViewById(R.id.conservatory_position_2);
                    moveAnimation(mover, destination, findViewById(R.id.median2), true);
                    break;
                case "kitchen_btn_right":
                    destination = Boolean.TRUE.equals(occupiedPositions.get("library_position_1")) ? findViewById(R.id.library_position_1) : findViewById(R.id.library_position_2);
                    moveAnimation(mover, destination, null, false);
                    break;
                case "kitchen_btn_down":
                    destination = Boolean.TRUE.equals(occupiedPositions.get("study_position_1")) ? findViewById(R.id.study_position_1) : findViewById(R.id.study_position_2);
                    moveAnimation(mover, destination, findViewById(R.id.median2), true);
                    break;
                case "kitchen_btn_left":
                    destination = Boolean.TRUE.equals(occupiedPositions.get("dining_position_1")) ? findViewById(R.id.dining_position_1) : findViewById(R.id.dining_position_2);
                    moveAnimation(mover, destination, null, false);
                    break;
                case "study_btn_left":
                    destination = Boolean.TRUE.equals(occupiedPositions.get("billiard_position_1")) ? findViewById(R.id.billiard_position_1) : findViewById(R.id.billiard_position_2);
                    moveAnimation(mover, destination, null, false);
                    break;
                case "study_btn_right":
                    destination = Boolean.TRUE.equals(occupiedPositions.get("hall_position_1")) ? findViewById(R.id.hall_position_1) : findViewById(R.id.hall_position_2);
                    moveAnimation(mover, destination, null, false);
                    break;
            }
        }
    }

    /**
     * When called, sets all arrows on the board invisible
     */
    private void setArrowsInvisible() {
        for (ImageButton b : allArrows) {
            b.setVisibility(View.INVISIBLE);
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

        // important for later animations
        /*
        Path path = new Path();
        if(vertical){
            path.lineTo(median.getX()-mover.getX(),0);
            path.lineTo(median.getX()-mover.getX(),destination.getY()-mover.getY());
            path.lineTo(destination.getX()-mover.getX(),destination.getY()-mover.getY());
            } else
                path.lineTo(destination.getX()-mover.getX(),0);
        */

        mover.setX(destination.getX());
        mover.setY(destination.getY());

        newPosition = getResources().getResourceEntryName(destination.getId());
        occupiedPositions.replace(newPosition, false);

        setArrowsInvisible();
        //gp1.canMove();
        counter++;
        movePlayerWithArrows(newPosition);
    }

    /**
     * checks if a player is allowed to move and afterwards the rooms around the player if there is enough room too move in the next room if it is
     * then show a clickable arrow if not do nothing
     *
     * @param position input the position where the player is currently standing
     */
    private void movePlayerWithArrows(String position) {

        //if (gp1.findPlayerByCharacterName(gp1.getCurrentPlayer()).getIsAbleToMove()) {
        if (counter < dice.getNumberRolled()) {

            switch (position) {
                case "lounge_position_1":
                case "lounge_position_2":

                    if (Boolean.TRUE.equals(occupiedPositions.get("dining_position_1")) || Boolean.TRUE.equals(occupiedPositions.get("dining_position_2"))) {
                        findViewById(R.id.lounge_btn_down).setVisibility(View.VISIBLE);
                    }
                    if (Boolean.TRUE.equals(occupiedPositions.get("conservatory_position_1")) || Boolean.TRUE.equals(occupiedPositions.get("conservatory_position_2"))) {
                        findViewById(R.id.lounge_btn_right).setVisibility(View.VISIBLE);
                    }
                    break;

                case "conservatory_position_1":
                case "conservatory_position_2":
                    if (Boolean.TRUE.equals(occupiedPositions.get("kitchen_position_1")) || Boolean.TRUE.equals(occupiedPositions.get("kitchen_position_2"))) {
                        findViewById(R.id.conservatory_btn_down).setVisibility(View.VISIBLE);
                    }
                    if (Boolean.TRUE.equals(occupiedPositions.get("lounge_position_1")) || Boolean.TRUE.equals(occupiedPositions.get("lounge_position_2"))) {
                        findViewById(R.id.conservatory_btn_left).setVisibility(View.VISIBLE);
                    }
                    if (Boolean.TRUE.equals(occupiedPositions.get("ball_position_1")) || Boolean.TRUE.equals(occupiedPositions.get("ball_position_2"))) {
                        findViewById(R.id.conservatory_btn_right).setVisibility(View.VISIBLE);
                    }
                    break;

                case "ball_position_1":
                case "ball_position_2":
                    if (Boolean.TRUE.equals(occupiedPositions.get("conservatory_position_1")) || Boolean.TRUE.equals(occupiedPositions.get("conservatory_position_2"))) {
                        findViewById(R.id.ballroom_btn_left).setVisibility(View.VISIBLE);
                    }
                    if (Boolean.TRUE.equals(occupiedPositions.get("library_position_1")) || Boolean.TRUE.equals(occupiedPositions.get("library_position_2"))) {
                        findViewById(R.id.ballroom_btn_down).setVisibility(View.VISIBLE);
                    }
                    break;

                case "dining_position_1":
                case "dining_position_2":
                    if (Boolean.TRUE.equals(occupiedPositions.get("lounge_position_1")) || Boolean.TRUE.equals(occupiedPositions.get("lounge_position_2"))) {
                        findViewById(R.id.dining_btn_up).setVisibility(View.VISIBLE);
                    }
                    if (Boolean.TRUE.equals(occupiedPositions.get("kitchen_position_1")) || Boolean.TRUE.equals(occupiedPositions.get("kitchen_position_2"))) {
                        findViewById(R.id.dining_btn_right).setVisibility(View.VISIBLE);
                    }
                    if (Boolean.TRUE.equals(occupiedPositions.get("billiard_position_1")) || Boolean.TRUE.equals(occupiedPositions.get("billiard_position_2"))) {
                        findViewById(R.id.dining_btn_down).setVisibility(View.VISIBLE);
                    }
                    break;

                case "kitchen_position_1":
                case "kitchen_position_2":
                    if (Boolean.TRUE.equals(occupiedPositions.get("conservatory_position_1")) || Boolean.TRUE.equals(occupiedPositions.get("conservatory_position_2"))) {
                        findViewById(R.id.kitchen_btn_up).setVisibility(View.VISIBLE);
                    }
                    if (Boolean.TRUE.equals(occupiedPositions.get("library_position_1")) || Boolean.TRUE.equals(occupiedPositions.get("library_position_2"))) {
                        findViewById(R.id.kitchen_btn_right).setVisibility(View.VISIBLE);
                    }
                    if (Boolean.TRUE.equals(occupiedPositions.get("study_position_1")) || Boolean.TRUE.equals(occupiedPositions.get("study_position_2"))) {
                        findViewById(R.id.kitchen_btn_down).setVisibility(View.VISIBLE);
                    }
                    if (Boolean.TRUE.equals(occupiedPositions.get("dining_position_1")) || Boolean.TRUE.equals(occupiedPositions.get("dining_position_2"))) {
                        findViewById(R.id.kitchen_btn_left).setVisibility(View.VISIBLE);
                    }
                    break;

                case "library_position_1":
                case "library_position_2":
                    if (Boolean.TRUE.equals(occupiedPositions.get("kitchen_position_1")) || Boolean.TRUE.equals(occupiedPositions.get("kitchen_position_2"))) {
                        findViewById(R.id.library_btn_left).setVisibility(View.VISIBLE);
                    }
                    if (Boolean.TRUE.equals(occupiedPositions.get("ball_position_1")) || Boolean.TRUE.equals(occupiedPositions.get("ball_position_2"))) {
                        findViewById(R.id.library_btn_up).setVisibility(View.VISIBLE);
                    }
                    if (Boolean.TRUE.equals(occupiedPositions.get("hall_position_1")) || Boolean.TRUE.equals(occupiedPositions.get("hall_position_2"))) {
                        findViewById(R.id.library_btn_down).setVisibility(View.VISIBLE);
                    }
                    break;

                case "billiard_position_1":
                case "billiard_position_2":
                    if (Boolean.TRUE.equals(occupiedPositions.get("dining_position_1")) || Boolean.TRUE.equals(occupiedPositions.get("dining_position_2"))) {
                        findViewById(R.id.billiard_btn_up).setVisibility(View.VISIBLE);
                    }
                    if (Boolean.TRUE.equals(occupiedPositions.get("study_position_1")) || Boolean.TRUE.equals(occupiedPositions.get("study_position_2"))) {
                        findViewById(R.id.billiard_btn_right).setVisibility(View.VISIBLE);
                    }
                    break;

                case "study_position_1":
                case "study_position_2":
                    if (Boolean.TRUE.equals(occupiedPositions.get("kitchen_position_1")) || Boolean.TRUE.equals(occupiedPositions.get("kitchen_position_2"))) {
                        findViewById(R.id.study_btn_up).setVisibility(View.VISIBLE);
                    }
                    if (Boolean.TRUE.equals(occupiedPositions.get("billiard_position_1")) || Boolean.TRUE.equals(occupiedPositions.get("billiard_position_2"))) {
                        findViewById(R.id.study_btn_left).setVisibility(View.VISIBLE);
                    }
                    if (Boolean.TRUE.equals(occupiedPositions.get("hall_position_1")) || Boolean.TRUE.equals(occupiedPositions.get("hall_position_2"))) {
                        findViewById(R.id.study_btn_right).setVisibility(View.VISIBLE);
                    }
                    break;

                case "hall_position_1":
                case "hall_position_2":
                    if (Boolean.TRUE.equals(occupiedPositions.get("library_position_1")) || Boolean.TRUE.equals(occupiedPositions.get("library_position_2"))) {
                        findViewById(R.id.hall_btn_up).setVisibility(View.VISIBLE);
                    }
                    if (Boolean.TRUE.equals(occupiedPositions.get("study_position_1")) || Boolean.TRUE.equals(occupiedPositions.get("study_position_2"))) {
                        findViewById(R.id.hall_btn_left).setVisibility(View.VISIBLE);
                    }
                    break;
            }

        } else {
            diceView.setOnClickListener(v -> {
                dice.throwDice();
                diceRolled();
            });
            System.out.println("Has moved enough");
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
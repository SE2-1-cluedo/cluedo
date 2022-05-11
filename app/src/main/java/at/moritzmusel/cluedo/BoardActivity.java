package at.moritzmusel.cluedo;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.res.Configuration;
import android.graphics.Path;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

import at.moritzmusel.cluedo.game.Dice;

public class BoardActivity extends AppCompatActivity {

    private View decorView;
    private Button hall;
    private Button diningRoom;
    private Button library;
    private Button ballRoom;
    private Button billiardRoom;
    private Button kitchen;
    private Button lounge;
    private Button studyRoom;
    private Button conservatory;

    private boolean lounge_position_1 = true;
    private boolean lounge_position_2 = true;
    private boolean kitchen_position_1 = true;
    private boolean kitchen_position_2 = true;
    private boolean conservatory_position_1 = true;
    private boolean conservatory_position_2 = true;
    private boolean hall_position_1 = true;
    private boolean hall_position_2 = true;
    private boolean diningRoom_position_1 = true;
    private boolean diningRoom_position_2 = true;
    private boolean ballRoom_position_1 = true;
    private boolean ballRoom_position_2 = true;
    private boolean library_position_1 = false;
    private boolean library_position_2 = false;
    private boolean study_position_1 = true;
    private boolean study_position_2 = true;
    private boolean billiardRoom_position_1 = true;
    private boolean billiardRoom_position_2 = true;


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility){
                if(visibility == 0)
                    decorView.setSystemUiVisibility(hideSystemBars());
            }
        });

        setContentView(R.layout.test_board2);

        hall = findViewById(R.id.hall_button);
        diningRoom = findViewById(R.id.dining_button);
        library = findViewById(R.id.library_button);
        ballRoom = findViewById(R.id.ballroom_button);
        billiardRoom = findViewById(R.id.billiard_button);
        kitchen = findViewById(R.id.kitchen_button);
        lounge = findViewById(R.id.lounge_button);
        studyRoom = findViewById(R.id.study_button);
        conservatory = findViewById(R.id.conservatory_button);


        ImageView diceView = findViewById(R.id.diceView);
        Dice dice = new Dice(diceView);
        diceView.setOnClickListener(view -> dice.throwDice());
        movePlayerWithArrows(1);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus)
            decorView.setSystemUiVisibility(hideSystemBars());
    }

    private int hideSystemBars(){
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }

    public void movePlayer(View v) {


        Button lounge_position_1 = findViewById(R.id.lounge_position_1);
        Button median1 = findViewById(R.id.median1);

        Path path = new Path();
        path.lineTo(median1.getX()-v.getX(),0);
        path.lineTo(median1.getX()-v.getX(), lounge_position_1.getY() - v.getY());
        path.lineTo(lounge_position_1.getX()-v.getX(),lounge_position_1.getY() - v.getY());


        ObjectAnimator animation = ObjectAnimator.ofFloat(v,"translationX","translationY",path);
        animation.setDuration(4000);
        animation.start();

    }

    /**
     * checks the rooms around the player if there is enough room too move in the next room if it is
     * then show a clickable arrow if not do nothing
     * @param position
     * input the position where the player is currently standing
     */
    private void movePlayerWithArrows(int position) {
        //find room durch parameter?

        //test hall
        Button playerPos = findViewById(R.id.hall_button);
        if (hall.equals(playerPos)) {
            if(library_position_1 || library_position_2){
                findViewById(R.id.hall_btn_up).setVisibility(View.VISIBLE);
            }
            if(study_position_1 || study_position_2){
                findViewById(R.id.hall_btn_left).setVisibility(View.VISIBLE);
            }
        }else if(lounge.equals(playerPos)){
            if(diningRoom_position_1 || diningRoom_position_2){
                findViewById(R.id.lounge_btn_down).setVisibility(View.VISIBLE);
            }
            if(conservatory_position_1 || conservatory_position_2){
                findViewById(R.id.lounge_btn_right).setVisibility(View.VISIBLE);
            }
        }
        else if(diningRoom.equals(playerPos)){
            if(diningRoom_position_1 || diningRoom_position_2){
                findViewById(R.id.lounge_btn_down).setVisibility(View.VISIBLE);
            }
            if(conservatory_position_1 || conservatory_position_2){
                findViewById(R.id.lounge_btn_right).setVisibility(View.VISIBLE);
            }
        }
        else if(billiardRoom.equals(playerPos)){
            if(diningRoom_position_1 || diningRoom_position_2){
                findViewById(R.id.billiard_btn_up).setVisibility(View.VISIBLE);
            }
            if(study_position_1 || study_position_2){
                findViewById(R.id.billiard_btn_right).setVisibility(View.VISIBLE);
            }
        }
        else if(conservatory.equals(playerPos)){
            if(kitchen_position_1 || kitchen_position_2){
                findViewById(R.id.conservatory_btn_down).setVisibility(View.VISIBLE);
            }
            if(lounge_position_1 || lounge_position_2){
                findViewById(R.id.conservatory_btn_left).setVisibility(View.VISIBLE);
            }
            if(ballRoom_position_1 || ballRoom_position_2){
                findViewById(R.id.conservatory_btn_right).setVisibility(View.VISIBLE);
            }
        }
        else if(studyRoom.equals(playerPos)){
            if(kitchen_position_1 || kitchen_position_2){
                findViewById(R.id.study_btn_up).setVisibility(View.VISIBLE);
            }
            if(billiardRoom_position_1 || billiardRoom_position_2){
                findViewById(R.id.study_btn_left).setVisibility(View.VISIBLE);
            }
            if(hall_position_1 ||hall_position_2){
                findViewById(R.id.study_btn_right).setVisibility(View.VISIBLE);
            }
        }
        else if(library.equals(playerPos)){
            if(kitchen_position_1 || kitchen_position_2){
                findViewById(R.id.library_btn_left).setVisibility(View.VISIBLE);
            }
            if(ballRoom_position_1 || ballRoom_position_2){
                findViewById(R.id.library_btn_up).setVisibility(View.VISIBLE);
            }
            if(hall_position_1 ||hall_position_2){
                findViewById(R.id.library_btn_down).setVisibility(View.VISIBLE);
            }
        }
        else if(ballRoom.equals(playerPos)){
            if(conservatory_position_1 || conservatory_position_2){
                findViewById(R.id.ballroom_btn_left).setVisibility(View.VISIBLE);
            }
            if(library_position_1 || library_position_2){
                findViewById(R.id.ballroom_btn_down).setVisibility(View.VISIBLE);
            }
        }
        else if(kitchen.equals(playerPos)){
            if(conservatory_position_1 || conservatory_position_2){
                findViewById(R.id.kitchen_btn_up).setVisibility(View.VISIBLE);
            }
            if(library_position_1 || library_position_2){
                findViewById(R.id.kitchen_btn_right).setVisibility(View.VISIBLE);
            }
            if(study_position_1 || study_position_2){
                findViewById(R.id.kitchen_btn_down).setVisibility(View.VISIBLE);
            }
            if(diningRoom_position_1 ||diningRoom_position_2){
                findViewById(R.id.kitchen_btn_left).setVisibility(View.VISIBLE);
            }
        }

    }

    public void onButtonClick(View v) {
        String room = getResources().getResourceEntryName(v.getId());

        //System.out.println(getResources().getResourceEntryName(v.getId())+" has been clicked");

        switch(room){
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
    }

    public void onButtonReturn(View v){
        setContentView(R.layout.test_board2);

        ImageView diceView = findViewById(R.id.diceView);
        Dice dice = new Dice(diceView);
        diceView.setOnClickListener(view -> dice.throwDice());
    }
}
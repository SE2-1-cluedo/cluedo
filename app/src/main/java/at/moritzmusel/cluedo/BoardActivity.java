package at.moritzmusel.cluedo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import at.moritzmusel.cluedo.game.Dice;

public class BoardActivity extends AppCompatActivity {

    private View decorView;

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

        ImageView diceView = findViewById(R.id.diceView);
        Dice dice = new Dice(diceView);
        diceView.setOnClickListener(view -> dice.throwDice());
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
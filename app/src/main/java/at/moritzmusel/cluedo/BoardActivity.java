package at.moritzmusel.cluedo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import at.moritzmusel.cluedo.game.Dice;

public class BoardActivity extends AppCompatActivity implements View.OnClickListener {
    private AllTheCards allcards;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_board2);

        allcards = new AllTheCards();
        allcards.getGameCards();

        ImageView diceView = findViewById(R.id.diceView);
        Dice dice = new Dice(diceView);
        diceView.setOnClickListener(view -> dice.throwDice());

        ImageButton cardView = findViewById(R.id.cardView);
        cardView.setOnClickListener(this);


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

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.cardView){
            AlertDialog alertDialog = new AlertDialog.Builder(BoardActivity.this).create();
            alertDialog.setTitle("My Cards");

            alertDialog.setMessage("");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }

    }
}
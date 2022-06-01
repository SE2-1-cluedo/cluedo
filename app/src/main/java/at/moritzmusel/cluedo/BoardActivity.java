package at.moritzmusel.cluedo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import at.moritzmusel.cluedo.game.Dice;

public class BoardActivity extends AppCompatActivity implements View.OnClickListener {
    private AllTheCards allcards;
    private float x1, x2, y1, y2;
    static final int MIN_SWIPE_DISTANCE = 150;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_board2);

        allcards = new AllTheCards();
        allcards.getGameCards();


        //diceView.setOnClickListener(view -> dice.throwDice());

        ImageButton cardView = findViewById(R.id.cardView);
        cardView.setOnClickListener(this);

        image = new ImageView(this);
        image.setImageResource(R.drawable.cardback);

        AlertDialog.Builder builder = new AlertDialog.Builder(BoardActivity.this);
        builder.setTitle("Throw Dice");

        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.custom_dialog, null);
        builder.setView(dialoglayout);


        builder.setPositiveButton("Roll", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*ImageView diceView = findViewById(R.id.img_dice);
                Dice dice = new Dice(diceView);
                diceView.setOnClickListener(view -> dice.throwDice());*/
            }
        });

        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        /*alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                setContentView(R.layout.test_board2);
                ImageView image = (ImageView) alertDialog.findViewById(R.id.diceView);
                Dice dice = new Dice(image);
                image.setOnClickListener(view -> dice.throwDice());

                Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.dice1);
                float imageWidthInPX = (float)image.getWidth();

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Math.round(imageWidthInPX),
                        Math.round(imageWidthInPX * (float)icon.getHeight() / (float)icon.getWidth()));
                image.setLayoutParams(layoutParams);

            }
        });*/


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
            AlertDialog.Builder builder = new AlertDialog.Builder(BoardActivity.this);
            builder.setTitle("My Cards");

            final String[] items = {allcards.getGameCards().get(0).getDesignation(),allcards.getGameCards().get(7).getDesignation(),allcards.getGameCards().get(20).getDesignation()};
            //Später vielleicht mit den Bildern
            //Nur Demo brauche Methode um die eigentlichen Karten zu bekommen
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {

                }
            });
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

    public void startNotepad(){
        Intent intent = new Intent(this, NotepadActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }
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
                y1 = touchEvent.getY();
                break;

            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();
                float swipeRight = x2-x1,
                        swipeLeft = x1-x2;

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
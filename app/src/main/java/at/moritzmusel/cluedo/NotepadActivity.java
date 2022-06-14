package at.moritzmusel.cluedo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import at.moritzmusel.cluedo.entities.Player;

public class NotepadActivity extends AppCompatActivity implements View.OnClickListener {

    CheckboxAdapter checkAdapter;
    boolean checked;
    at.moritzmusel.cluedo.Card card;
    float x1, x2;
    static final int MIN_SWIPE_DISTANCE = 150;

    private CheckBox pl1Scarlett;
    private CheckBox pl1Plum;
    private CheckBox pl1Green;
    private CheckBox pl1Peacock;
    private CheckBox pl1Mustard;
    private CheckBox pl1Orchid;
    private CheckBox pl1Dagger;
    private CheckBox pl1Candlestick;
    private CheckBox pl1Pistol;
    private CheckBox pl1Rope;
    private CheckBox pl1Pipe;
    private CheckBox pl1Wrench;
    private CheckBox pl1Hall;
    private CheckBox pl1Lounge;
    private CheckBox pl1Dining;
    private CheckBox pl1Kitchen;
    private CheckBox pl1Ballroom;
    private CheckBox pl1Conservatory;
    private CheckBox pl1Billiard;
    private CheckBox pl1Library;
    private CheckBox pl1Study;

    private EditText colScralettInput;
    private EditText colPlumInput;
    private EditText colGreenInput;
    private EditText colPeacockInput;
    private EditText colMustardInput;
    private EditText colOrchidInput;
    private EditText colDaggerInput;
    private EditText colCandlestickInput;
    private EditText colPistolInput;
    private EditText colRopeInput;
    private EditText colPipeInput;
    private EditText colWrenchInput;
    private EditText colHallInput;
    private EditText colLoungeInput;
    private EditText colDiningInput;
    private EditText colKitchenInput;
    private EditText colBallroomInput;
    private EditText colConservatoryInput;
    private EditText colBilliardInput;
    private EditText colLibraryInput;
    private EditText colStudyInput;

    private Button btn_closeNotepad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad_update);

        String[] stringArray = getResources().getStringArray(R.array.all);
        List<String> list = new ArrayList<>(Arrays.asList(stringArray));
        ListView listView = findViewById(R.id.listviewNotepad);
        checkAdapter = new CheckboxAdapter(this, list);
        listView.setAdapter(checkAdapter);

        Button btn_closeNotepad = findViewById(R.id.btn_closeNotepad);
        btn_closeNotepad.setOnClickListener(this);

        pl1Scarlett = findViewById(R.id.pl1Scarlett);
        pl1Plum = findViewById(R.id.pl1Plum);
        pl1Green = findViewById(R.id.pl1Green);
        pl1Peacock = findViewById(R.id.pl1Peackock);
        pl1Mustard = findViewById(R.id.pl1Mustard);
        pl1Orchid = findViewById(R.id.pl1Orchid);
        pl1Dagger = findViewById(R.id.pl1Dagger);
        pl1Candlestick = findViewById(R.id.pl1Candlestick);
        pl1Pistol = findViewById(R.id.pl1Pistole);
        pl1Rope = findViewById(R.id.pl1Rope);
        pl1Pipe = findViewById(R.id.pl1Pipe);
        pl1Wrench = findViewById(R.id.pl1Wrench);
        pl1Hall = findViewById(R.id.pl1Hall);
        pl1Lounge = findViewById(R.id.pl1Lounge);
        pl1Dining = findViewById(R.id.pl1Dining);
        pl1Kitchen = findViewById(R.id.pl1Kitchen);
        pl1Ballroom = findViewById(R.id.pl1Ballroom);
        pl1Conservatory = findViewById(R.id.pl1Conservatory);
        pl1Billiard = findViewById(R.id.pl1Billiard);
        pl1Library = findViewById(R.id.pl1Library);
        pl1Study = findViewById(R.id.pl1Study);

        colScralettInput = findViewById(R.id.colScarlettInput);
        colPlumInput = findViewById(R.id.colPlumInput);
        colGreenInput = findViewById(R.id.colGreenInput);
        colPeacockInput = findViewById(R.id.colPeacockInput);
        colMustardInput = findViewById(R.id.colMustardInput);
        colOrchidInput = findViewById(R.id.colOrchidInput);
        colDaggerInput = findViewById(R.id.colDaggerInput);
        colCandlestickInput = findViewById(R.id.colCandlestickInput);
        colPistolInput = findViewById(R.id.colPistolInput);
        colRopeInput = findViewById(R.id.colRopeInput);
        colPipeInput = findViewById(R.id.colPipeInput);
        colWrenchInput = findViewById(R.id.colWrenchInput);
        colHallInput = findViewById(R.id.colHallInput);
        colLoungeInput = findViewById(R.id.colLoungeInput);
        colDiningInput = findViewById(R.id.colDiningInput);
        colKitchenInput = findViewById(R.id.colKitchenInput);
        colBallroomInput = findViewById(R.id.colBallroomInput);
        colConservatoryInput = findViewById(R.id.colConservatoryInput);
        colBilliardInput = findViewById(R.id.colBilliardInput);
        colLibraryInput = findViewById(R.id.colLibraryInput);
        colStudyInput = findViewById(R.id.colStudyInput);

    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_closeNotepad) {
            finish();
          //  Intent back = new Intent(NotepadActivity.this, BoardActivity.class);
           // startActivity(back);
        }
    }

    public void setCheckedOwnedCards(Player player){
        for (int i = 0; i < player.getPlayerOwnedCards().size(); i++) {
            setCheckCard(player.getPlayerOwnedCards().get(i));
        }
    }

    @Override
    public boolean onTouchEvent (MotionEvent touchEvent){
        switch (touchEvent.getAction()) {

            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();

                float swipeLeft = x1 - x2;

                if (swipeLeft > MIN_SWIPE_DISTANCE) {
                    finish();
                    overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                }
                break;
        }
        return false;
    }


    public void setCheckCard(int id) {
        switch (id) {
            case 0:
                pl1Scarlett.setChecked(true);
                break;
            case 1:
                pl1Plum.setChecked(true);
                break;
            case 2:
                pl1Green.setChecked(true);
                break;
            case 3:
                pl1Peacock.setChecked(true);
                break;
            case 4:
                pl1Mustard.setChecked(true);
                break;
            case 5:
                pl1Orchid.setChecked(true);
                break;
            case 6:
                pl1Dagger.setChecked(true);
                break;
            case 7:
                pl1Candlestick.setChecked(true);
                break;
            case 8:
                pl1Pistol.setChecked(true);
                break;
            case 9:
                pl1Rope.setChecked(true);
                break;
            case 10:
                pl1Pipe.setChecked(true);
                break;
            case 11:
                pl1Wrench.setChecked(true);
                break;
            case 12:
                pl1Hall.setChecked(true);
                break;
            case 13:
                pl1Lounge.setChecked(true);
                break;
            case 14:
                pl1Dining.setChecked(true);
                break;
            case 15:
                pl1Kitchen.setChecked(true);
                break;
            case 16:
                pl1Ballroom.setChecked(true);
                break;
            case 17:
                pl1Conservatory.setChecked(true);
                break;
            case 18:
                pl1Billiard.setChecked(true);
                break;
            case 19:
                pl1Library.setChecked(true);
                break;
            case 20:
                pl1Study.setChecked(true);
                break;
        }


    }


}

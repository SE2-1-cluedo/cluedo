package at.moritzmusel.cluedo;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import at.moritzmusel.cluedo.network.pojo.Card;

public class NotepadActivity extends AppCompatActivity {

    at.moritzmusel.cluedo.Card card;

    private TextView colScarlett;
    private TextView colPlum;
    private TextView colGreen;
    private TextView colPeacock;
    private TextView colMustard;
    private TextView colOrchid;
    private TextView colDagger;
    private TextView colCandlestick;
    private TextView colPistol;
    private TextView colRope;
    private TextView colPipe;
    private TextView colWrench;
    private TextView colHall;
    private TextView colLounge;
    private TextView colDining;
    private TextView colKitchen;
    private TextView colBallroom;
    private TextView colConservatory;
    private TextView colBilliard;
    private TextView colLibrary;
    private TextView colStudy;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);

        colScarlett = findViewById(R.id.colScarlett);
        colPlum = findViewById(R.id.colPlum);
        colGreen = findViewById(R.id.colGreen);
        colPeacock = findViewById(R.id.colPeacock);
        colMustard = findViewById(R.id.colMustard);
        colOrchid = findViewById(R.id.colOrchid);
        colDagger = findViewById(R.id.colDagger);
        colCandlestick = findViewById(R.id.colCandlestick);
        colPistol = findViewById(R.id.colPistol);
        colRope = findViewById(R.id.colRope);
        colPipe = findViewById(R.id.colPipe);
        colWrench = findViewById(R.id.colWrench);
        colHall = findViewById(R.id.colHall);
        colLounge = findViewById(R.id.colLounge);
        colDining = findViewById(R.id.colDining);
        colKitchen = findViewById(R.id.colKitchen);
        colBallroom = findViewById(R.id.colBallroom);
        colConservatory = findViewById(R.id.colConservatory);
        colBilliard = findViewById(R.id.colBilliard);
        colLibrary = findViewById(R.id.colLibrary);
        colStudy = findViewById(R.id.colStudy);

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





    public int findId(View view){
        int idFound;
        switch (view.getId()) {
            case R.id.colScarlett:
               idFound =  0;
                break;
            case R.id.colPlum:
                idFound =  1;
                break;
            case R.id.colGreen:
                idFound = 2;
                break;
            case R.id.colPeacock:
                idFound = 3;
                break;
            case R.id.colMustard:
                idFound =  4;
                break;
            case R.id.colOrchid:
                idFound = 5;
                break;
            case R.id.colDagger:
                idFound = 6;
                break;
            case R.id.colCandlestick:
                idFound = 7;
                break;
            case R.id.colPistol:
                idFound = 8;
                break;
            case R.id.colRope:
                idFound = 9;
                break;
            case R.id.colPipe:
                idFound = 10;
                break;
            case R.id.colWrench:
                idFound = 11;
                break;
            case R.id.colHall:
                idFound = 12;
                break;
            case R.id.colLounge:
                idFound = 13;
                break;
            case R.id.colDining:
                idFound = 14;
                break;
            case R.id.colKitchen:
                idFound = 15;
                break;
            case R.id.colBallroom:
                idFound = 16;
                break;
            case R.id.colConservatory:
                idFound = 17;
                break;
            case R.id.colBilliard:
                idFound = 18;
                break;
            case R.id.colLibrary:
                idFound = 19;
                break;
            case R.id.colStudy:
                idFound  = 20;
                break;
            default:
                idFound = 99;
        }
        return idFound;
    }

    public void setCheckCard(Card card) {
        switch (card.getCardID()) {
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

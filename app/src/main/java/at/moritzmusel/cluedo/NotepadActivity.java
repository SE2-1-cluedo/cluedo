package at.moritzmusel.cluedo;

import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NotepadActivity extends AppCompatActivity {

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


    }


}

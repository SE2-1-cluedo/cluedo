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

    private CheckBox pl1Scarlett;
    private CheckBox pl2Scarlett;
    private CheckBox pl3Scarlett;
    private CheckBox pl4Scarlett;
    private CheckBox pl5Scarlett;
    private CheckBox pl6Scarlett;

    private CheckBox pl1Plum;
    private CheckBox pl2Plum;
    private CheckBox pl3Plum;
    private CheckBox pl4Plum;
    private CheckBox pl5Plum;
    private CheckBox pl6Plum;

    private CheckBox pl1Green;
    private CheckBox pl2Green;
    private CheckBox pl3Green;
    private CheckBox pl4Green;
    private CheckBox pl5Green;
    private CheckBox pl6Green;

    private CheckBox pl1Peacock;
    private CheckBox pl2Peacock;
    private CheckBox pl3Peacock;
    private CheckBox pl4Peacock;
    private CheckBox pl5Peacock;
    private CheckBox pl6Peacock;

    private CheckBox pl1Mustard;
    private CheckBox pl2Mustard;
    private CheckBox pl3Mustard;
    private CheckBox pl4Mustard;
    private CheckBox pl5Mustard;
    private CheckBox pl6Mustard;

    private CheckBox pl1Orchid;
    private CheckBox pl2Orchid;
    private CheckBox pl3Orchid;
    private CheckBox pl4Orchid;
    private CheckBox pl5Orchid;
    private CheckBox pl6Orchid;

    private CheckBox pl1Dagger;
    private CheckBox pl2Dagger;
    private CheckBox pl3Dagger;
    private CheckBox pl4Dagger;
    private CheckBox pl5Dagger;
    private CheckBox pl6Dagger;

    private CheckBox pl1Candlestick;
    private CheckBox pl2Candlestick;
    private CheckBox pl3Candlestick;
    private CheckBox pl4Candlestick;
    private CheckBox pl5Candlestick;
    private CheckBox pl6Candlestick;

    private CheckBox pl1Pistol;
    private CheckBox pl2Pistol;
    private CheckBox pl3Pistol;
    private CheckBox pl4Pistol;
    private CheckBox pl5Pistol;
    private CheckBox pl6Pistol;

    private CheckBox pl1Rope;
    private CheckBox pl2Rope;
    private CheckBox pl3Rope;
    private CheckBox pl4Rope;
    private CheckBox pl5Rope;
    private CheckBox pl6Rope;

    private CheckBox pl1Pipe;
    private CheckBox pl2Pipe;
    private CheckBox pl3Pipe;
    private CheckBox pl4Pipe;
    private CheckBox pl5Pipe;
    private CheckBox pl6Pipe;

    private CheckBox pl1Wrench;
    private CheckBox pl2Wrench;
    private CheckBox pl3Wrench;
    private CheckBox pl4Wrench;
    private CheckBox pl5Wrench;
    private CheckBox pl6Wrench;

    private CheckBox pl1Hall;
    private CheckBox pl2Hall;
    private CheckBox pl3Hall;
    private CheckBox pl4Hall;
    private CheckBox pl5Hall;
    private CheckBox pl6Hall;

    private CheckBox pl1Lounge;
    private CheckBox pl2Lounge;
    private CheckBox pl3Lounge;
    private CheckBox pl4Lounge;
    private CheckBox pl5Lounge;
    private CheckBox pl6Lounge;

    private CheckBox pl1Dining;
    private CheckBox pl2Dining;
    private CheckBox pl3Dining;
    private CheckBox pl4Dining;
    private CheckBox pl5Dining;
    private CheckBox pl6Dining;

    private CheckBox pl1Kitchen;
    private CheckBox pl2Kitchen;
    private CheckBox pl3Kitchen;
    private CheckBox pl4Kitchen;
    private CheckBox pl5Kitchen;
    private CheckBox pl6Kitchen;

    private CheckBox pl1Ballroom;
    private CheckBox pl2Ballroom;
    private CheckBox pl3Ballroom;
    private CheckBox pl4Ballroom;
    private CheckBox pl5Ballroom;
    private CheckBox pl6Ballroom;

    private CheckBox pl1Conservatory;
    private CheckBox pl2Conservatory;
    private CheckBox pl3Conservatory;
    private CheckBox pl4Conservatory;
    private CheckBox pl5Conservatory;
    private CheckBox pl6Conservatory;

    private CheckBox pl1Billiard;
    private CheckBox pl2Billiard;
    private CheckBox pl3Billiard;
    private CheckBox pl4Billiard;
    private CheckBox pl5Billiard;
    private CheckBox pl6Billiard;

    private CheckBox pl1Library;
    private CheckBox pl2Library;
    private CheckBox pl3Library;
    private CheckBox pl4Library;
    private CheckBox pl5Library;
    private CheckBox pl6Library;

    private CheckBox pl1Study;
    private CheckBox pl2Study;
    private CheckBox pl3Study;
    private CheckBox pl4Study;
    private CheckBox pl5Study;
    private CheckBox pl6Study;
    private CheckBox pl7Study;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);

        pl1Scarlett = findViewById(R.id.pl1Scarlett);
        pl2Scarlett = findViewById(R.id.pl1Scarlett);
        pl3Scarlett = findViewById(R.id.pl1Scarlett);
        pl4Scarlett = findViewById(R.id.pl1Scarlett);
        pl5Scarlett = findViewById(R.id.pl1Scarlett);
        pl6Scarlett = findViewById(R.id.pl1Scarlett);


        pl1Plum = findViewById(R.id.pl1Plum);
        pl2Plum = findViewById(R.id.pl1Plum);
        pl3Plum = findViewById(R.id.pl1Plum);
        pl4Plum = findViewById(R.id.pl1Plum);
        pl5Plum = findViewById(R.id.pl1Plum);
        pl6Plum = findViewById(R.id.pl1Plum);

        pl1Green = findViewById(R.id.pl1Green);
        pl2Green = findViewById(R.id.pl1Green);
        pl3Green = findViewById(R.id.pl1Green);
        pl4Green = findViewById(R.id.pl1Green);
        pl5Green = findViewById(R.id.pl1Green);
        pl6Green = findViewById(R.id.pl1Green);

        pl1Peacock = findViewById(R.id.pl1Peackock);
        pl2Peacock = findViewById(R.id.pl1Peackock);
        pl3Peacock = findViewById(R.id.pl1Peackock);
        pl4Peacock = findViewById(R.id.pl1Peackock);
        pl5Peacock = findViewById(R.id.pl1Peackock);
        pl6Peacock = findViewById(R.id.pl1Peackock);

        pl1Mustard = findViewById(R.id.pl1Mustard);
        pl2Mustard = findViewById(R.id.pl1Mustard);
        pl3Mustard = findViewById(R.id.pl1Mustard);
        pl4Mustard = findViewById(R.id.pl1Mustard);
        pl5Mustard = findViewById(R.id.pl1Mustard);
        pl6Mustard = findViewById(R.id.pl1Mustard);


        pl1Orchid = findViewById(R.id.pl1Orchid);
        pl2Orchid = findViewById(R.id.pl1Orchid);
        pl3Orchid = findViewById(R.id.pl1Orchid);
        pl4Orchid = findViewById(R.id.pl1Orchid);
        pl5Orchid = findViewById(R.id.pl1Orchid);
        pl6Orchid = findViewById(R.id.pl1Orchid);

        pl1Dagger = findViewById(R.id.pl1Dagger);
        pl2Dagger = findViewById(R.id.pl1Dagger);
        pl3Dagger = findViewById(R.id.pl1Dagger);
        pl4Dagger = findViewById(R.id.pl1Dagger);
        pl5Dagger = findViewById(R.id.pl1Dagger);
        pl6Dagger = findViewById(R.id.pl1Dagger);

        pl1Candlestick = findViewById(R.id.pl1Candlestick);
        pl2Candlestick = findViewById(R.id.pl1Candlestick);
        pl3Candlestick = findViewById(R.id.pl1Candlestick);
        pl4Candlestick = findViewById(R.id.pl1Candlestick);
        pl5Candlestick = findViewById(R.id.pl1Candlestick);
        pl6Candlestick = findViewById(R.id.pl1Candlestick);


        pl1Pistol = findViewById(R.id.pl1Pistole);
        pl2Pistol = findViewById(R.id.pl1Pistole);
        pl3Pistol = findViewById(R.id.pl1Pistole);
        pl4Pistol = findViewById(R.id.pl1Pistole);
        pl5Pistol = findViewById(R.id.pl1Pistole);
        pl6Pistol = findViewById(R.id.pl1Pistole);

        pl1Rope = findViewById(R.id.pl1Rope);
        pl2Rope = findViewById(R.id.pl1Rope);
        pl3Rope = findViewById(R.id.pl1Rope);
        pl4Rope = findViewById(R.id.pl1Rope);
        pl5Rope = findViewById(R.id.pl1Rope);
        pl6Rope = findViewById(R.id.pl1Rope);

        pl1Pipe = findViewById(R.id.pl1Pipe);
        pl2Pipe = findViewById(R.id.pl1Pipe);
        pl3Pipe = findViewById(R.id.pl1Pipe);
        pl4Pipe = findViewById(R.id.pl1Pipe);
        pl5Pipe = findViewById(R.id.pl1Pipe);
        pl6Pipe = findViewById(R.id.pl1Pipe);

        pl1Wrench = findViewById(R.id.pl1Wrench);
        pl2Wrench = findViewById(R.id.pl1Wrench);
        pl3Wrench = findViewById(R.id.pl1Wrench);
        pl4Wrench = findViewById(R.id.pl1Wrench);
        pl5Wrench = findViewById(R.id.pl1Wrench);
        pl6Wrench = findViewById(R.id.pl1Wrench);


        pl1Hall = findViewById(R.id.pl1Hall);
        pl2Hall = findViewById(R.id.pl1Hall);
        pl3Hall = findViewById(R.id.pl1Hall);
        pl4Hall = findViewById(R.id.pl1Hall);
        pl5Hall = findViewById(R.id.pl1Hall);
        pl6Hall = findViewById(R.id.pl1Hall);

        pl1Lounge = findViewById(R.id.pl1Lounge);
        pl2Lounge = findViewById(R.id.pl1Lounge);
        pl3Lounge = findViewById(R.id.pl1Lounge);
        pl4Lounge = findViewById(R.id.pl1Lounge);
        pl5Lounge = findViewById(R.id.pl1Lounge);
        pl6Lounge = findViewById(R.id.pl1Lounge);

        pl1Dining = findViewById(R.id.pl1Dining);
        pl2Dining = findViewById(R.id.pl1Dining);
        pl3Dining = findViewById(R.id.pl1Dining);
        pl4Dining = findViewById(R.id.pl1Dining);
        pl5Dining = findViewById(R.id.pl1Dining);
        pl6Dining = findViewById(R.id.pl1Dining);

        pl1Kitchen = findViewById(R.id.pl1Kitchen);
        pl2Kitchen = findViewById(R.id.pl1Kitchen);
        pl3Kitchen = findViewById(R.id.pl1Kitchen);
        pl4Kitchen = findViewById(R.id.pl1Kitchen);
        pl5Kitchen = findViewById(R.id.pl1Kitchen);
        pl6Kitchen = findViewById(R.id.pl1Kitchen);


        pl1Ballroom = findViewById(R.id.pl1Ballroom);
        pl2Ballroom = findViewById(R.id.pl1Ballroom);
        pl3Ballroom = findViewById(R.id.pl1Ballroom);
        pl4Ballroom = findViewById(R.id.pl1Ballroom);
        pl5Ballroom = findViewById(R.id.pl1Ballroom);
        pl6Ballroom = findViewById(R.id.pl1Ballroom);

        pl1Conservatory = findViewById(R.id.pl1Conservatory);
        pl2Conservatory = findViewById(R.id.pl1Conservatory);
        pl3Conservatory = findViewById(R.id.pl1Conservatory);
        pl4Conservatory = findViewById(R.id.pl1Conservatory);
        pl5Conservatory = findViewById(R.id.pl1Conservatory);
        pl6Conservatory = findViewById(R.id.pl1Conservatory);

        pl1Billiard = findViewById(R.id.pl1Billiard);
        pl2Billiard = findViewById(R.id.pl1Billiard);
        pl3Billiard = findViewById(R.id.pl1Billiard);
        pl4Billiard = findViewById(R.id.pl1Billiard);
        pl5Billiard = findViewById(R.id.pl1Billiard);
        pl6Billiard = findViewById(R.id.pl1Billiard);

        pl1Library = findViewById(R.id.pl1Library);
        pl2Library = findViewById(R.id.pl1Library);
        pl3Library = findViewById(R.id.pl1Library);
        pl4Library = findViewById(R.id.pl1Library);
        pl5Library = findViewById(R.id.pl1Library);
        pl6Library = findViewById(R.id.pl1Library);

        pl1Study = findViewById(R.id.pl1Study);
        pl2Study = findViewById(R.id.pl1Study);
        pl3Study = findViewById(R.id.pl1Study);
        pl4Study = findViewById(R.id.pl1Study);
        pl5Study = findViewById(R.id.pl1Study);
        pl6Study = findViewById(R.id.pl1Study);

    }


}

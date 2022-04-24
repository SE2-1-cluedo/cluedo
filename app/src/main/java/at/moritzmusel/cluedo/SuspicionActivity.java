package at.moritzmusel.cluedo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class SuspicionActivity extends AppCompatActivity implements View.OnClickListener {
    /*



   */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suspicion);

        //aktueller Raum
        TextView setRoom = findViewById(R.id.setRoom);


        //Button Auswahl Person
        ImageButton cmustard = findViewById(R.id.cmustard);
        ImageButton cgreen = findViewById(R.id.cgreen);
        ImageButton cpeacock = findViewById(R.id.cpeacock);
        ImageButton cscarlett = findViewById(R.id.cscarlett);
        ImageButton cmplum = findViewById(R.id.cplum);
        ImageButton cwhite = findViewById(R.id.cwhite);

        //Button Auswahl Waffe
        ImageButton cknife = findViewById(R.id.cknife);
        ImageButton cpipe = findViewById(R.id.cpipe);
        ImageButton ccandlestick = findViewById(R.id.ccandlestick);
        ImageButton cgun = findViewById(R.id.cgun);
        ImageButton crope = findViewById(R.id.crope);
        ImageButton cwrench = findViewById(R.id.cwrench);

        //Bestätigung des Verdachts
        Button submit = findViewById(R.id.submit);

    }

    @Override
    public void onClick(View view) {

    }
}
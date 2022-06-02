package at.moritzmusel.cluedo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_character);

        final Button btn_green = findViewById(R.id.btn_green);
        btn_green.setOnClickListener(this);
        final Button btn_plum = findViewById(R.id.btn_plum);
        btn_plum.setOnClickListener(this);
        final Button btn_peacock = findViewById(R.id.btn_peacock);
        btn_peacock.setOnClickListener(this);
        final Button btn_scarlett = findViewById(R.id.btn_scarlett);
        btn_scarlett.setOnClickListener(this);
        final Button btn_orchid = findViewById(R.id.btn_orchid);
        btn_orchid.setOnClickListener(this);
        final Button btn_mustard = findViewById(R.id.btn_mustard);
        btn_mustard.setOnClickListener(this);
    }

    //TODO Verknüpfe CHaracter mit Player / Wo soll die Seite eingefügt werden vor Lobby join/ nach Lobby join
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_green) {

        }
        if (view.getId() == R.id.btn_plum) {

        }
        if (view.getId() == R.id.btn_peacock) {

        }
        if (view.getId() == R.id.btn_scarlett) {

        }
        if (view.getId() == R.id.btn_orchid) {

        }
        if (view.getId() == R.id.btn_mustard) {

        }
    }
}
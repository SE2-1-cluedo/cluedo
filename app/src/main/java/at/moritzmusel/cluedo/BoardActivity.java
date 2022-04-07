package at.moritzmusel.cluedo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import at.moritzmusel.cluedo.game.Dice;

public class BoardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        ImageView diceView = findViewById(R.id.diceView);
        Dice dice = new Dice(diceView);
        diceView.setOnClickListener(view -> dice.throwDice());

    }
}
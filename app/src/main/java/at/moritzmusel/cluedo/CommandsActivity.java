package at.moritzmusel.cluedo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CommandsActivity extends AppCompatActivity implements View.OnClickListener {
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commands);
        back = findViewById(R.id.btn_back_commands);
        back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_back_commands){
            finish();
        }
    }
}
package at.moritzmusel.cluedo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class JoinLobbyActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText enter_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_lobby);
        final Button back = findViewById(R.id.btn_back_to_lobbydecision);
        back.setOnClickListener(this);
        final Button join = findViewById(R.id.btn_lobby_join);
        join.setOnClickListener(this);
        enter_id = findViewById(R.id.txt_enter_id);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_back_to_lobbydecision){
            //Intent i = new Intent(JoinLobbyActivity.this, LobbyDecisionActivity.class);
            //startActivity(i);
            finish();
        }
        if(view.getId() == R.id.btn_lobby_join){
            //if(getEnterId().equals(getGameId())){
                Intent i = new Intent(JoinLobbyActivity.this, LobbyWaitingActivity.class);
                startActivity(i);
            //}
        }
    }

    public String getGameId() {
        //Muss noch mit Netwerk verknüpt werden.
        String id = "12345";
        return id;
    }

    public String getEnterId(){
        return enter_id.getText().toString();
    }
}
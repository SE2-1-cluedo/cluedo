package at.moritzmusel.cluedo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
    private String enter;
    private String game_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_lobby);
        final Button back = findViewById(R.id.btn_back_to_lobbydecision);
        back.setOnClickListener(this);
        final Button join = findViewById(R.id.btn_lobby_join);
        join.setOnClickListener(this);
        enter_id = findViewById(R.id.txt_enter_id);
        enter = getEnterId();
        game_id = getIntent().getExtras().getString("game_id");
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_back_to_lobbydecision){
            //Intent i = new Intent(JoinLobbyActivity.this, LobbyDecisionActivity.class);
            //startActivity(i);
            finish();
        }
        if(view.getId() == R.id.btn_lobby_join){
            enter = getEnterId();
            //Checken ob die überhaupt exisitert
            if(enter.isEmpty() || !enter.equals(game_id)){
                new AlertDialog.Builder(JoinLobbyActivity.this)
                        .setTitle("ERROR")
                        .setMessage("The ENTER ID is false/empty")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
            else{
                Intent i = new Intent(JoinLobbyActivity.this, CreateLobbyActivity.class);
                i.putExtra("decision",false);
                i.putExtra(Intent.EXTRA_TEXT, enter);
                //false = join
                //true = create
                startActivity(i);
            }
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
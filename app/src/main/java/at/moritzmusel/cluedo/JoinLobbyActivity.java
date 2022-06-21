package at.moritzmusel.cluedo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

import org.w3c.dom.Text;

import java.util.Objects;

import at.moritzmusel.cluedo.communication.NetworkCommunicator;
import at.moritzmusel.cluedo.entities.Player;
import at.moritzmusel.cluedo.network.Network;
import at.moritzmusel.cluedo.network.pojo.GameState;

public class JoinLobbyActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText enter_id;
    private String enter;
    private String game_id;
    FirebaseUser user;
    private boolean right_game_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_lobby);
        final Button back = findViewById(R.id.btn_back_to_lobbydecision);
        back.setOnClickListener(this);
        final Button join = findViewById(R.id.btn_lobby_join);
        join.setOnClickListener(this);

        enter_id = findViewById(R.id.txt_enter_id);
        //game_id = Network.getCurrentGameID();
        user = (FirebaseUser) getIntent().getExtras().get("user");
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_back_to_lobbydecision){
            /*Intent i = new Intent(JoinLobbyActivity.this, LobbyDecisionActivity.class);
            startActivity(i);
            new AlertDialog.Builder(JoinLobbyActivity.this)
                    .setMessage("String: " + right_game_id)
                    .show();
            */
            finish();
        }

        if(view.getId() == R.id.btn_lobby_join){
            enter = getEnterId();
            Network.getDatabaseReference().get().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) Log.e("firebase", "Error getting data", task.getException());
                else {
                    right_game_id = task.getResult().child(enter).exists();
                    //Checken ob die überhaupt exisitert
                    if(!right_game_id && !enter.isEmpty()){
                        new AlertDialog.Builder(JoinLobbyActivity.this)
                                .setTitle("ERROR")
                                .setMessage("The ENTER ID is false/empty: " + right_game_id + enter)
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();
                    }
                    else{
                        Network.setCtx(this);
                        Network.joinLobby(user, enter);
                        Intent i = new Intent(JoinLobbyActivity.this, CreateLobbyActivity.class);
                        i.putExtra("decision",false);
                        //i.putExtra("game_id",game_id);
                        i.putExtra("user",user);
                        i.putExtra(Intent.EXTRA_TEXT, enter);
                        //false = join
                        //true = create
                        startActivity(i);
                    }
                }
            });

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
package at.moritzmusel.cluedo.activities;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.auth.FirebaseUser;

import at.moritzmusel.cluedo.R;
import at.moritzmusel.cluedo.network.Network;


public class JoinLobbyActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText enter_id;
    private String enter;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_lobby);
        final Button back = findViewById(R.id.btn_back_to_lobbydecision);
        back.setOnClickListener(this);
        final Button join = findViewById(R.id.btn_lobby_join);
        join.setOnClickListener(this);

        enter_id = findViewById(R.id.txt_enter_id);
        user = (FirebaseUser) getIntent().getExtras().get("user");
    }

    /**
     * back = close the activity
     * join = checks if the enter is not empty, exists and that the game hasn't started yet.
     * joins the lobby and send the decision, user and enter text to the next activity
     * @param view this
     */
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_back_to_lobbydecision){
            finish();
        }
        if(view.getId() == R.id.btn_lobby_join){
            enter = getEnterId();
            Network.getDatabaseReference().get().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) Log.e("firebase", "Error getting data", task.getException());
                else {
                    //Checken ob die überhaupt exisitert
                    if(enter.isEmpty() || !task.getResult().child(enter).exists()){
                        showDialog("You're input is either empty or false!");
                    }
                    else if(task.getResult().child(enter).child("turn-flag").child("startGame").getValue().equals("start")){
                        showDialog("The Game already started.");
                    }
                    else{
                        Network.setCtx(this);
                        Network.joinLobby(user, enter);
                        Intent i = new Intent(JoinLobbyActivity.this, CreateLobbyActivity.class);
                        i.putExtra("decision",false);
                        i.putExtra("user",user);
                        i.putExtra(Intent.EXTRA_TEXT, enter);
                        startActivity(i);
                    }
                }
            });
        }
    }

    /**
     * shows a dialog for an error for the lobby
     * @param text String Message
     */
    public void showDialog(String text){
        new AlertDialog.Builder(JoinLobbyActivity.this)
                .setTitle("ERROR")
                .setMessage(text)
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> {
                }).show();
    }

    /**
     * Gets the string that is entered
     * @return String Gameid
     */
    public String getEnterId(){
        return enter_id.getText().toString();
    }
}
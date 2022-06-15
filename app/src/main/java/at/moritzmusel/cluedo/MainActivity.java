package at.moritzmusel.cluedo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import at.moritzmusel.cluedo.network.Network;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG_AUTH = "AnonymousAuth";
    //Firebase Authentifizierung von Nutzern
    private FirebaseAuth mAuth;
    private Button startGame;
    private Button gamerules;
    private Button closeApp;
    private Button commands;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //authentifiziere Nutzer zur einzigartigen zuordnung von spielern
        mAuth = FirebaseAuth.getInstance();
        //end
        //Network instance, currently for testing

        Network n = new Network();
        //n.initDB();
        signInAnonymously();
        n.createLobby(mAuth.getCurrentUser());
        //end
        
        startGame = findViewById(R.id.btn_startgame);
        startGame.setOnClickListener(this);
        gamerules = findViewById(R.id.btn_gamerules);
        gamerules.setOnClickListener(this);
        closeApp = findViewById(R.id.btn_close_app);
        closeApp.setOnClickListener(this);
        commands = findViewById(R.id.btn_commands);
        commands.setOnClickListener(this);
        //Button button = findViewById(R.id.button);
        //button.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, BoardActivity.class)));
        // Test push ch
        Log.i("Test", "Test");
    }

    //On start checken ob Nutzer bereits angemeldet ist, falls nicht -> ui change entsprechend
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //placeholderMethod_updateUI(currentUser);
    }
    //Methode um Nutzer anonym anzumelden falls nicht angemeldet
    private void signInAnonymously() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG_AUTH, "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            placeholderMethod_updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG_AUTH, "signInAnonymously:failure", task.getException());
                            //Toast.makeText(AnonymousAuthActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            placeholderMethod_updateUI(null);
                        }
                    }
                });
    }

    //TODO: Logik hier falls Nutzer nicht eingeloggt ist
    //update ui falls nutzer bereits angemeldet ist, ansonsten zeige eine Anmelde fläche
    private void placeholderMethod_updateUI(FirebaseUser user){
        //Falls Nutzer eingeloggt->Überspringe Login Screen, ansonsten `signInAnonymously()`
        if(user != null){
            //Skippe hier Login Button oder so, keine Ahnung Bro
            Log.i(TAG_AUTH, user.getUid());
        }else{
           signInAnonymously();
        }
    }

    /**
     * Opens the next menu for the lobby.
     * Opens link to a youtube video.
     * You close the app after the button is clicked.
     * Opens the commands which have information for the players.
     */
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_startgame){
            Intent i = new Intent(MainActivity.this, LobbyDecisionActivity.class);
            startActivity(i);
        }
        if(view.getId() == R.id.btn_gamerules){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=S_WEloTcKmI")));
        }
        if(view.getId() == R.id.btn_commands){
            Intent i = new Intent(MainActivity.this, CommandsActivity.class);
            startActivity(i);
        }
        if(view.getId() == R.id.btn_close_app){
            finish();
            System.exit(0);
        }
    }
}

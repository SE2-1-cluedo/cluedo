package at.moritzmusel.cluedo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static final String TAG_AUTH = "AnonymousAuth";
    //Firebase Authentifizierung von Nutzern
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(this, DebugNetwork.class));
        Log.i(TAG_AUTH, "HALLO");

        /*
        //Button button = findViewById(R.id.button);
        //authentifiziere Nutzer zur einzigartigen zuordnung von spielern
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser m = mAuth.getCurrentUser();
        //end
        //Network instance, currently for testing
        //n.initDB();
        signInAnonymously();
        Network.createLobby(mAuth.getCurrentUser());
        Network.joinLobby(m, Network.getCurrentGameID());
        //end
        //button.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, BoardActivity.class)));
         */
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
                .addOnCompleteListener(this, task -> {
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
}
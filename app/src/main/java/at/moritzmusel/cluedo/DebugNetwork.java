package at.moritzmusel.cluedo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import at.moritzmusel.cluedo.network.Network;

public class DebugNetwork extends AppCompatActivity {

    private static final String TAG_DEBUG = "DEBUG Activity ~";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_network);
        Button btn = findViewById(R.id.create);
        btn.setOnClickListener(click->{
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            Network.signInAnonymously(mAuth);
            FirebaseUser user = mAuth.getCurrentUser();
            Log.i(TAG_DEBUG, String.valueOf(user));
            Network.createLobby(user);
        });
        TextView view = (TextView) findViewById(R.id.debug);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if(Network.getCurrentGameID()!=null)break;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.setText(Network.getCurrentGameID());
                    }
                });
            }
        }).start();
    }
}
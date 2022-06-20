package at.moritzmusel.cluedo;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import at.moritzmusel.cluedo.entities.Player;
import at.moritzmusel.cluedo.game.Gameplay;
import at.moritzmusel.cluedo.network.pojo.GameState;

public class NotepadActivity extends AppCompatActivity implements View.OnClickListener {

    CheckboxAdapter checkAdapter;
    float x1, x2;
    static final int MIN_SWIPE_DISTANCE = 150;
    NotepadData notepadData = new NotepadData();
    Gameplay gpl = Gameplay.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad_update);


        String[] stringArray = getResources().getStringArray(R.array.all);
        List<String> list = new ArrayList<>(Arrays.asList(stringArray));
        ListView listView = findViewById(R.id.listviewNotepad);
        checkAdapter = new CheckboxAdapter(this, list, notepadData);
        listView.setAdapter(checkAdapter);

        //setPlayersOwnedCards();
        //setQuitPlayersCards();

        Button btn_closeNotepad = findViewById(R.id.btn_closeNotepad);
        btn_closeNotepad.setOnClickListener(this);


    }

    public void onStart() {
        super.onStart();
        checkAdapter.loadState();


    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_closeNotepad) {
            checkAdapter.safeState();
            finish();
        }
    }


/*     //TODO autamtic setChecked QuitPlayersCards
       public void setQuitPlayersCards() {
           int [] cards = new int[];
           cards = Methode von Konstantin
           for (int i = 0; i < checkAdapter.checkboxItems.size(); i++) {
               for (int j = 0; j < cards.length; j++) {
                   if (Integer.valueOf(checkAdapter.checkboxItems.get(i)).equals(cards[j])) {
                       checkAdapter.cb1.setChecked(true);
                       checkAdapter.cb2.setChecked(true);
                       checkAdapter.cb3.setChecked(true);
                       checkAdapter.cb4.setChecked(true);
                       checkAdapter.cb5.setChecked(true);
                       checkAdapter.cb6.setChecked(true);
                   }
               }
           }
       }
  */
    //TODO autamtic setChecked PlayerOwnedCards
    public void setPlayersOwnedCards() {
        ArrayList<Integer> cards = new ArrayList<>();
        //cards = Methode von Robert
        for (int x = 0; x < checkAdapter.checkboxItems.size(); x++) {
            for (int j = 0; j < cards.size(); j++) {
                if (Integer.valueOf(checkAdapter.checkboxItems.get(x)).equals(cards.get(j))) {
                    checkAdapter.cb1.setChecked(true);
                    checkAdapter.cb2.setChecked(true);
                    checkAdapter.cb3.setChecked(true);
                    checkAdapter.cb4.setChecked(true);
                    checkAdapter.cb5.setChecked(true);
                    checkAdapter.cb6.setChecked(true);
                }
            }
        }
    }

    /**
     * With a swipe to the left you get back to the main board.
     */
    @Override
    public boolean onTouchEvent(MotionEvent touchEvent) {
        switch (touchEvent.getAction()) {

            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();

                float swipeLeft = x1 - x2;

                if (swipeLeft > MIN_SWIPE_DISTANCE) {
                    finish();
                    overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                }
                break;
        }
        return false;
    }

    public static class NotepadData {

        public static List<Boolean> checkboxStatecb1;
        public static List<Boolean> checkboxStatecb2;
        public static List<Boolean> checkboxStatecb3;
        public static List<Boolean> checkboxStatecb4;
        public static List<Boolean> checkboxStatecb5;
        public static List<Boolean> checkboxStatecb6;
        public static boolean active;

        public NotepadData() {
            super();
            if (!active) {
                checkboxStatecb1 = new ArrayList<Boolean>();
                checkboxStatecb2 = new ArrayList<Boolean>();
                checkboxStatecb3 = new ArrayList<Boolean>();
                checkboxStatecb4 = new ArrayList<Boolean>();
                checkboxStatecb5 = new ArrayList<Boolean>();
                checkboxStatecb6 = new ArrayList<Boolean>();
                this.active = true;
            }
        }

        public List<Boolean> getCheckboxStatecb1() {
            return checkboxStatecb1;
        }

        public void setCheckboxStatecb1(List<Boolean> checkboxStatecb1) {
            this.checkboxStatecb1 = checkboxStatecb1;
        }

        public List<Boolean> getCheckboxStatecb2() {
            return checkboxStatecb2;
        }

        public void setCheckboxStatecb2(List<Boolean> checkboxStatecb2) {
            this.checkboxStatecb2 = checkboxStatecb2;
        }

        public List<Boolean> getCheckboxStatecb3() {
            return checkboxStatecb3;
        }

        public void setCheckboxStatecb3(List<Boolean> checkboxStatecb3) {
            this.checkboxStatecb3 = checkboxStatecb3;
        }

        public List<Boolean> getCheckboxStatecb4() {
            return checkboxStatecb4;
        }

        public void setCheckboxStatecb4(List<Boolean> checkboxStatecb4) {
            this.checkboxStatecb4 = checkboxStatecb4;
        }

        public List<Boolean> getCheckboxStatecb5() {
            return checkboxStatecb5;
        }

        public void setCheckboxStatecb5(List<Boolean> checkboxStatecb5) {
            this.checkboxStatecb5 = checkboxStatecb5;
        }

        public List<Boolean> getCheckboxStatecb6() {
            return checkboxStatecb6;
        }

        public void setCheckboxStatecb6(List<Boolean> checkboxStatecb6) {
            this.checkboxStatecb6 = checkboxStatecb6;
        }
    }

}

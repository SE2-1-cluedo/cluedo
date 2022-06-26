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

                for (int i = 0; i < 22; i++) {
                    checkboxStatecb1.add(i, false);
                    checkboxStatecb2.add(i, false);
                    checkboxStatecb3.add(i, false);
                    checkboxStatecb4.add(i, false);
                    checkboxStatecb5.add(i, false);
                    checkboxStatecb6.add(i, false);
                }
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

        public void setAllCheckBoxInLine(int i) {
            getCheckboxStatecb1().set(i, true);
            getCheckboxStatecb2().set(i, true);
            getCheckboxStatecb3().set(i, true);
            getCheckboxStatecb4().set(i, true);
            getCheckboxStatecb5().set(i, true);
            getCheckboxStatecb6().set(i, true);
        }
    }

}

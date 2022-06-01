package at.moritzmusel.cluedo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NotepadActivity extends AppCompatActivity implements View.OnClickListener {

    CheckboxAdapter checkAdapter;
    boolean checked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad_update);

        String[] stringArray = getResources().getStringArray(R.array.all);
        List<String> list = new ArrayList<>(Arrays.asList(stringArray));
        ListView listView = findViewById(R.id.listviewNotepad);
        checkAdapter = new CheckboxAdapter(this, list);
        listView.setAdapter(checkAdapter);

        Button btn_closeNotepad = findViewById(R.id.btn_closeNotepad);
        btn_closeNotepad.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_closeNotepad) {
            Intent back = new Intent(NotepadActivity.this, BoardActivity.class);
            startActivity(back);
        }
    }
}

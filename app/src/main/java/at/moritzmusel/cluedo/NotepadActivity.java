package at.moritzmusel.cluedo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NotepadActivity extends AppCompatActivity implements View.OnClickListener {

    private List<String> list;
    private Button btn_closeNotepad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad_update);

        String[] stringArray = getResources().getStringArray(R.array.all);
        list = new ArrayList<>(Arrays.asList(stringArray));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.list_row, R.id.textNotepad, list);
        ListView listView = (ListView) findViewById(R.id.listviewNotepad);
        listView.setAdapter(arrayAdapter);


        btn_closeNotepad = findViewById(R.id.btn_closeNotepad);
        btn_closeNotepad.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_closeNotepad) {
            Intent back = new Intent(NotepadActivity.this, BoardActivity.class);
            startActivity(back);
        }
    }

    public void setCheckedOwnedCards() {

    }
}

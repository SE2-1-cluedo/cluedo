package at.moritzmusel.cluedo;

import static at.moritzmusel.cluedo.R.id.spinner_weapon;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class SuspicionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener  {

    private String [] possiblePersons;
    private String [] possibleWeapons;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suspicion);

        //aktueller Raum
        TextView setRoom = findViewById(R.id.setRoom);

        //Auswahl Person
        ArrayAdapter<CharSequence> adapterPerson = ArrayAdapter.createFromResource(this, R.array.spinner_person,android.R.layout.simple_spinner_item );
        Spinner person = (Spinner) findViewById(R.id.spinner_person);
        person.setOnItemSelectedListener(this);
        adapterPerson.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        person.setAdapter(adapterPerson);


        //Auswahl Waffe
        ArrayAdapter<CharSequence> adapterWeapon = ArrayAdapter.createFromResource(this, R.array.spinner_weapon,android.R.layout.simple_spinner_item );
        Spinner weapon = (Spinner) findViewById(spinner_weapon);
        weapon.setOnItemSelectedListener(this);
        adapterWeapon.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weapon.setAdapter(adapterWeapon);

        possiblePersons = getResources().getStringArray(R.id.spinner_person);
        possibleWeapons = getResources().getStringArray(R.id.spinner_weapon);


        //Bestätigung des Verdachts / Anklage
        Button accustion = findViewById(R.id.submit_Accusation);
        Button suspicion = findViewById(R.id.submit_Suspicion);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.submit_Suspicion){

        }if(view.getId() == R.id.submit_Accusation){

        }
    }
}
package at.moritzmusel.cluedo;

import static at.moritzmusel.cluedo.R.id.personSelect;
import static at.moritzmusel.cluedo.R.id.spinner_weapon;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import at.moritzmusel.cluedo.entities.Player;

public class SuspicionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String[] possiblePersons;
    private String[] possibleWeapons;
    private String[] roomsArray;
    private TextView currentRoom;
    private String selectedRoom;
    private String selectedPerson;
    private String selectedWeapon;
    private Button accusation;
    private Button suspicion;
    private TextView personSelect;
    private TextView weaponSelect;
    private TextView setRoom;
    private ArrayAdapter<CharSequence> adapterPerson;
    private ArrayAdapter<CharSequence> adapterWeapon;
    private Spinner person;
    private Spinner weapon;
    private Player player;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suspicion);


        //Dropdown Person
        adapterPerson = ArrayAdapter.createFromResource(this, R.array.person_array, android.R.layout.simple_spinner_item);
        person = (Spinner) findViewById(R.id.spinner_person);
        person.setOnItemSelectedListener(this);
        adapterPerson.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        person.setAdapter(adapterPerson);


        //Dropdown Waffe
        adapterWeapon = ArrayAdapter.createFromResource(this, R.array.weapons_array, android.R.layout.simple_spinner_item);
        weapon = (Spinner) findViewById(spinner_weapon);
        weapon.setOnItemSelectedListener(this);
        adapterWeapon.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weapon.setAdapter(adapterWeapon);

        //Resources
        possiblePersons = getResources().getStringArray(R.array.person_array);
        possibleWeapons = getResources().getStringArray(R.array.weapons_array);
        roomsArray = getResources().getStringArray(R.array.rooms_array);

        //Textfeld aktueller Raum
        currentRoom = findViewById(R.id.currentRoom);

        //Button Bestätigung des Verdachts / Anklage
        accusation = findViewById(R.id.submit_Accusation);
        accusation.setOnClickListener((new View.OnClickListener() {
            //TODO Button accusation Funktionalität
            @Override
            public void onClick(View view) {

            }
        }));
        suspicion = findViewById(R.id.submit_Suspicion);
        suspicion.setOnClickListener(new View.OnClickListener() {
            //TODO Button suspicion Funktionaität
            @Override
            public void onClick(View view) {

            }
        });

        //Textfelder Spielerauswahl
        personSelect = findViewById(R.id.personSelect);
        weaponSelect = findViewById(R.id.weaponSelect);

    }

    private void getSelectedRoom() {
        //TODO getCurrentRoom Funktionalität - mit Player/Network verknüpft?
        switch (player.getPositionOnBoard()) {
            case 12:
                currentRoom.setText("Hall");
                break;
            case 13:
                currentRoom.setText("Lounge");
                break;
            case 14:
                currentRoom.setText("Dining Room");
                break;
            case 15:
                currentRoom.setText("Kitchen");
                break;
            case 16:
                currentRoom.setText("Ballroom");
                break;
            case 17:
                currentRoom.setText("Conservatory");
                break;
            case 18:
                currentRoom.setText("Billiard Room");
                break;
            case 19:
                currentRoom.setText("Library");
                break;
            case 20:
                currentRoom.setText("Study");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        for (String possibleWeapon : possibleWeapons) {
            if (adapterView.getItemAtPosition(i).equals(possibleWeapon)) {
                selectedWeapon = adapterView.getItemAtPosition(i).toString();
                if (selectedWeapon.equals(possibleWeapons[0])) {
                    weaponSelect.setText(selectedWeapon);
                } else if (selectedWeapon.equals(possibleWeapons[1])) {
                    weaponSelect.setText(selectedWeapon);
                } else if (selectedWeapon.equals(possibleWeapons[2])) {
                    weaponSelect.setText(selectedWeapon);
                } else if (selectedWeapon.equals(possibleWeapons[3])) {
                    weaponSelect.setText(selectedWeapon);
                } else if (selectedWeapon.equals(possibleWeapons[4])) {
                    weaponSelect.setText(selectedWeapon);
                } else if (selectedWeapon.equals(possibleWeapons[5])) {
                    weaponSelect.setText(selectedWeapon);
                }
            }
        }

        for (String possiblePerson : possiblePersons) {
            if (adapterView.getItemAtPosition(i).equals(possiblePerson)) {
                selectedPerson = adapterView.getItemAtPosition(i).toString();
                if (selectedPerson.equals(possiblePersons[0])) {
                    personSelect.setText(selectedPerson);
                } else if (selectedPerson.equals(possiblePersons[1])) {
                    personSelect.setText(selectedPerson);
                } else if (selectedPerson.equals(possiblePersons[2])) {
                    personSelect.setText(selectedPerson);
                } else if (selectedPerson.equals(possiblePersons[3])) {
                    personSelect.setText(selectedPerson);
                } else if (selectedPerson.equals(possiblePersons[4])) {
                    personSelect.setText(selectedPerson);
                } else if (selectedPerson.equals(possiblePersons[5])) {
                    personSelect.setText(selectedPerson);
                }


            }
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Context context = getApplicationContext();
        CharSequence text = "You have to choose a weapon and a person!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


}
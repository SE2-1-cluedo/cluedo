package at.moritzmusel.cluedo.activities;

import static at.moritzmusel.cluedo.R.id.personSelect;
import static at.moritzmusel.cluedo.R.id.spinner_weapon;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import at.moritzmusel.cluedo.R;
import at.moritzmusel.cluedo.communication.SuspicionCommunicator;
import at.moritzmusel.cluedo.entities.Player;
import at.moritzmusel.cluedo.game.Gameplay;

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
    float x1, x2;
    Gameplay gp1;
    SuspicionCommunicator ca;
    static final int MIN_SWIPE_DISTANCE = 150;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suspicion);
        gp1 = Gameplay.getInstance();

        //Dropdown Person
        adapterPerson = ArrayAdapter.createFromResource(this, R.array.person_array, android.R.layout.simple_spinner_item);
        person = (Spinner) findViewById(R.id.spinner_person);
        person.setOnItemSelectedListener(this);
        adapterPerson.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        person.setAdapter(adapterPerson);


        //Dropdown Waffe
        adapterWeapon = ArrayAdapter.createFromResource(this, R.array.weapons_array, android.R.layout.simple_spinner_item);
        weapon = findViewById(spinner_weapon);
        weapon.setOnItemSelectedListener(this);
        adapterWeapon.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weapon.setAdapter(adapterWeapon);

        //Resources
        possiblePersons = getResources().getStringArray(R.array.person_array);
        possibleWeapons = getResources().getStringArray(R.array.weapons_array);
        roomsArray = getResources().getStringArray(R.array.rooms_array);

        //Textfeld aktueller Raum
        currentRoom = findViewById(R.id.currentRoom);
        int room = gp1.findPlayerByCharacterName(gp1.getCurrentPlayer()).getPositionOnBoard()-1;
        currentRoom.setText(roomsArray[room]);

        ca = SuspicionCommunicator.getInstance();

        //Button Bestätigung des Verdachts / Anklage
        accusation = findViewById(R.id.submit_Accusation);
        //TODO Button accusation Funktionalität
        accusation.setOnClickListener((view -> {
            ca.setCharacter(selectedPerson);
            ca.setWeapon(selectedWeapon);
            ca.setHasAccused(true);
            finish();
        }));
        suspicion = findViewById(R.id.submit_Suspicion);
        //TODO Button suspicion Funktionaität
        suspicion.setOnClickListener(view -> {
            ca.setCharacter(selectedPerson);
            ca.setWeapon(selectedWeapon);
            ca.setHasSuspected(true);
            finish();
        });

        //Textfelder Spielerauswahl
        personSelect = findViewById(R.id.personSelect);
        weaponSelect = findViewById(R.id.weaponSelect);

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
                } else {
                    String nothing = "You have to choose a weapon";
                    Toast.makeText(this, nothing, Toast.LENGTH_SHORT).show();
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
                } else {
                    String nothing = "You have to choose a person";
                    Toast.makeText(this, nothing, Toast.LENGTH_SHORT).show();
                }

            }
        }


    }

    @Override
    public boolean onTouchEvent (MotionEvent touchEvent){
        switch (touchEvent.getAction()) {

            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();

                float swipeRight = x2 - x1;

                if (swipeRight > MIN_SWIPE_DISTANCE) {
                    finish();
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }
                break;
        }
        return false;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }


}
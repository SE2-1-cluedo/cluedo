package at.moritzmusel.cluedo;

import static at.moritzmusel.cluedo.NotepadData.*;

import android.app.Activity;
import android.content.Context;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import at.moritzmusel.cluedo.entities.Player;

public class CheckboxAdapter extends ArrayAdapter {
    Context context;
    List<Boolean> checkboxState;
    List<String> checkboxItems;
    Player player;
    CheckBox cb1;
    CheckBox cb2;
    CheckBox cb3;
    CheckBox cb4;
    CheckBox cb5;
    CheckBox cb6;

    public CheckboxAdapter(Context context, List<String> resource) {
        super(context, R.layout.list_row, resource);

        this.context = context;
        this.checkboxItems = resource;
        this.checkboxState = new ArrayList<Boolean>(Collections.nCopies(resource.size(), true));
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.list_row, parent, false);
        TextView textView = convertView.findViewById(R.id.textNotepad);
        cb1 = convertView.findViewById(R.id.checkbox1);
        cb2 = convertView.findViewById(R.id.checkbox2);
        cb3 = convertView.findViewById(R.id.checkbox3);
        cb4 = convertView.findViewById(R.id.checkbox4);
        cb5 = convertView.findViewById(R.id.checkbox5);
        cb6 = convertView.findViewById(R.id.checkbox6);

        textView.setText(checkboxItems.get(position));

        return convertView;
    }

    public void setPlayersOwnedCards() {
        for (int i = 0; i < checkboxItems.size(); i++) {
            for (int j = 0; j < player.getPlayerOwnedCards().size(); j++) {
                if (Integer.valueOf(checkboxItems.get(i)).equals(player.getPlayerOwnedCards().get(j))) {
                    cb1.setChecked(true);
                    cb2.setChecked(true);
                    cb3.setChecked(true);
                    cb4.setChecked(true);
                    cb5.setChecked(true);
                    cb6.setChecked(true);
                }
            }
        }
    }
}
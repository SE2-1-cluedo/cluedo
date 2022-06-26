package at.moritzmusel.cluedo;

import android.app.Activity;
import android.app.UiModeManager;
import android.content.Context;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.core.widget.CompoundButtonCompat;

import java.util.ArrayList;
import java.util.List;

import at.moritzmusel.cluedo.entities.Player;
import at.moritzmusel.cluedo.game.Gameplay;

public class CheckboxAdapter extends ArrayAdapter {
    Context context;
    List<Boolean> checkboxStatecb1;
    List<Boolean> checkboxStatecb2;
    List<Boolean> checkboxStatecb3;
    List<Boolean> checkboxStatecb4;
    List<Boolean> checkboxStatecb5;
    List<Boolean> checkboxStatecb6;
    List<String> checkboxItems;
    NotepadActivity.NotepadData notepadData;
    Gameplay gpl;


    public CheckboxAdapter(Context context, List<String> resource, NotepadActivity.NotepadData notepadData) {
        super(context, R.layout.list_row, resource);

        this.context = context;
        this.checkboxItems = resource;
        this.checkboxStatecb1 = new ArrayList<>();
        this.checkboxStatecb2 = new ArrayList<>();
        this.checkboxStatecb3 = new ArrayList<>();
        this.checkboxStatecb4 = new ArrayList<>();
        this.checkboxStatecb5 = new ArrayList<>();
        this.checkboxStatecb6 = new ArrayList<>();

        for (int i = 0; i < this.getCount(); i++) {
            this.checkboxStatecb1.add(i, false);
            this.checkboxStatecb2.add(i, false);
            this.checkboxStatecb3.add(i, false);
            this.checkboxStatecb4.add(i, false);
            this.checkboxStatecb5.add(i, false);
            this.checkboxStatecb6.add(i, false);
        }

        this.notepadData = notepadData;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final CheckHolder holder;
        if (convertView == null) {

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_row, parent, false);

            holder = new CheckHolder();

            holder.textView = convertView.findViewById(R.id.textNotepad);
            holder.cb1 = convertView.findViewById(R.id.checkbox1);
            holder.cb2 = convertView.findViewById(R.id.checkbox2);
            holder.cb3 = convertView.findViewById(R.id.checkbox3);
            holder.cb4 = convertView.findViewById(R.id.checkbox4);
            holder.cb5 = convertView.findViewById(R.id.checkbox5);
            holder.cb6 = convertView.findViewById(R.id.checkbox6);

            gpl = Gameplay.getInstance();

            if (!gpl.getCardsOfPlayerOwn().isEmpty()) {
                int count = 0;
                for (int x = 0; x < checkboxItems.size(); x++) {
                    for (int j = 0; j < gpl.getCardsOfPlayerOwn().size(); j++) {
                        if (x == gpl.getCardsOfPlayerOwn().get(j)) {
                            notepadData.setAllCheckBoxInLine(gpl.getCardsOfPlayerOwn().get(j));
                        }
                    }
                    count++;
                }
            }

            List<Player> players = gpl.getPlayers();
            for (int i = 0; i < checkboxItems.size(); i++) {
                if(players.size()<4){
                    checkboxStatecb4.set(i,true);
                    holder.cb4.setChecked(checkboxStatecb4.get(4));
                    holder.cb4.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#808080")));
                    checkboxStatecb5.set(i,true);
                    holder.cb5.setChecked(checkboxStatecb5.get(5));
                    holder.cb5.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#808080")));
                    checkboxStatecb6.set(i,true);
                    holder.cb6.setChecked(checkboxStatecb6.get(6));
                    holder.cb6.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#808080")));
                }
                if(players.size()<5){
                    checkboxStatecb5.set(i,true);
                    holder.cb5.setChecked(checkboxStatecb5.get(5));
                    holder.cb5.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#808080")));
                    checkboxStatecb6.set(i,true);
                    holder.cb6.setChecked(checkboxStatecb6.get(6));
                    holder.cb6.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#808080")));

                }
                if(players.size()<6){
                    checkboxStatecb6.set(i,true);
                    holder.cb6.setChecked(checkboxStatecb6.get(6));
                    holder.cb6.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#808080")));

                }



            }

            convertView.setTag(holder);
        } else {
            holder = (CheckHolder)convertView.getTag();
    }
        holder.textView.setText(checkboxItems.get(position));
        holder.cb1.setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick (View view){
        if (holder.cb1.isChecked()) {
            checkboxStatecb1.set(position, true);
        } else {
            checkboxStatecb1.set(position, false);
        }

    }

    });
        holder.cb1.setChecked(checkboxStatecb1.get(position));

        holder.cb2.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View view){
        if (holder.cb2.isChecked()) {
            checkboxStatecb2.set(position, true);
        } else {
            checkboxStatecb2.set(position, false);
        }

    }
    });
        holder.cb2.setChecked(checkboxStatecb2.get(position));

        holder.cb3.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View view){
        if (holder.cb3.isChecked()) {
            checkboxStatecb3.set(position, true);
        } else {
            checkboxStatecb3.set(position, false);
        }

    }
    });
        holder.cb3.setChecked(checkboxStatecb3.get(position));

        holder.cb4.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View view){
        if (holder.cb4.isChecked()) {
            checkboxStatecb4.set(position, true);
        } else {
            checkboxStatecb4.set(position, false);
        }

    }
    });
        holder.cb4.setChecked(checkboxStatecb4.get(position));

        holder.cb5.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View view){
        if (holder.cb5.isChecked()) {
            checkboxStatecb5.set(position, true);
        } else {
            checkboxStatecb5.set(position, false);
        }

    }
    });
        holder.cb5.setChecked(checkboxStatecb5.get(position));

        holder.cb6.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View view){
        if (holder.cb6.isChecked()) {
            checkboxStatecb6.set(position, true);
        } else {
            checkboxStatecb6.set(position, false);
        }

    }
    });
        holder.cb6.setChecked(checkboxStatecb6.get(position));
        return convertView;
}

    public void safeState(){
        notepadData.setCheckboxStatecb1(this.checkboxStatecb1);
        notepadData.setCheckboxStatecb2(this.checkboxStatecb2);
        notepadData.setCheckboxStatecb3(this.checkboxStatecb3);
        notepadData.setCheckboxStatecb4(this.checkboxStatecb4);
        notepadData.setCheckboxStatecb5(this.checkboxStatecb5);
        notepadData.setCheckboxStatecb6(this.checkboxStatecb6);

    }

    public void loadState(){
        if(!notepadData.getCheckboxStatecb1().isEmpty()) {
            this.checkboxStatecb1 = notepadData.getCheckboxStatecb1();
        }
        if(!notepadData.getCheckboxStatecb2().isEmpty()) {
            this.checkboxStatecb2 = notepadData.getCheckboxStatecb2();
        }
        if(!notepadData.getCheckboxStatecb3().isEmpty()) {
            this.checkboxStatecb3 = notepadData.getCheckboxStatecb3();
        }
        if(!notepadData.getCheckboxStatecb4().isEmpty()) {
            this.checkboxStatecb4 = notepadData.getCheckboxStatecb4();
        }
        if(!notepadData.getCheckboxStatecb5().isEmpty()) {
            this.checkboxStatecb5 = notepadData.getCheckboxStatecb5();
        }
        if(!notepadData.getCheckboxStatecb6().isEmpty()) {
            this.checkboxStatecb6 = notepadData.getCheckboxStatecb6();
        }
    }



static class CheckHolder {

    TextView textView;
    CheckBox cb1;
    CheckBox cb2;
    CheckBox cb3;
    CheckBox cb4;
    CheckBox cb5;
    CheckBox cb6;

}
}
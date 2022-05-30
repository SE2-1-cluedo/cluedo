package at.moritzmusel.cluedo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import at.moritzmusel.cluedo.R;

public class CheckboxAdapter extends ArrayAdapter {
    Context context;
    List<Boolean> checkboxState;
    List<String> checkboxItems;
    List<Card> allCards;

    public CheckboxAdapter(Context context, List<String> resource) {
        super(context, R.layout.list_row, resource);

        this.context = context;
        this.checkboxItems = resource;
        this.checkboxState = new ArrayList<Boolean>(Collections.nCopies(resource.size(), true));
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.list_row, parent, false);
        TextView textView = (TextView) convertView.findViewById(R.id.textNotepad);
        CheckBox cb1 = (CheckBox) convertView.findViewById(R.id.checkbox1);
        CheckBox cb2 = (CheckBox) convertView.findViewById(R.id.checkbox2);
        CheckBox cb3 = (CheckBox) convertView.findViewById(R.id.checkbox3);
        CheckBox cb4 = (CheckBox) convertView.findViewById(R.id.checkbox4);
        CheckBox cb5 = (CheckBox) convertView.findViewById(R.id.checkbox5);
        CheckBox cb6 = (CheckBox) convertView.findViewById(R.id.checkbox6);

        textView.setText(checkboxItems.get(position));

        return convertView;
    }


    public void setChecked(boolean state, int position){
        checkboxState.set(position, state);
    }
}
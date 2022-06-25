package at.moritzmusel.cluedo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import at.moritzmusel.cluedo.entities.Character;
import at.moritzmusel.cluedo.entities.Player;

public class Dialogs {
    Dialog dialog;

    public Dialogs() {
    }

    /**
     * Shows a Dialog with the Winner Information.
     * @param ac Activity where it will be shown
     * @param winner Name of the Winner
     */
    public void callWinDialog(Activity ac, String winner){
        dialog = new Dialog(ac);
        dialog.setContentView(R.layout.win_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView img_close = dialog.findViewById(R.id.img_close);
        Button btn_winner = dialog.findViewById(R.id.btn_winner);

        TextView txt_winner = dialog.findViewById(R.id.txt_win);
        txt_winner.setText("The Winner is: " + winner);
        img_close.setOnClickListener(v -> {
            endGame(ac);
            dialog.dismiss();
        });
        btn_winner.setOnClickListener(v -> {
            //ac.finishAffinity();
            endGame(ac);
            dialog.dismiss();
        });
        dialog.show();
    }

    /**
     * Shows a Dialog with the Information that you lost.
     * @param ac Activity where it will be shown
     */
    public void callLoseDialog(Activity ac){
        dialog = new Dialog(ac);
        dialog.setContentView(R.layout.win_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView img_close = dialog.findViewById(R.id.img_close);
        Button btn_loser = dialog.findViewById(R.id.btn_winner);
        btn_loser.setBackground(ac.getDrawable(R.drawable.custom_button2));

        ImageView image = dialog.findViewById(R.id.img_win);
        image.setImageResource(R.drawable.red_lose);

        TextView txt_loser = dialog.findViewById(R.id.txt_win);
        txt_loser.setText("You made a" + "\n" + "false Accusation");

        TextView txt_lost = dialog.findViewById(R.id.txt_winner);
        txt_lost.setText("You Lost!");
        txt_lost.setTextColor(ac.getResources().getColor(R.color.red));

        img_close.setOnClickListener(v -> {
            endGame(ac);
            dialog.dismiss();
        });
        btn_loser.setOnClickListener(v -> {
            //ac.finishAffinity();
            endGame(ac);
            dialog.dismiss();
        });
        dialog.show();
    }

    public void endGame(Activity ac){
        Intent intent = new Intent(ac.getApplicationContext(),MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ac.startActivity(intent);
    }

    public void callCheatDialog(Activity ac, List<Player> players){
        final String[] framed = {""};

        dialog = new Dialog(ac);
        dialog.setContentView(R.layout.cheat_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView img_close = dialog.findViewById(R.id.img_close);
        Button btn_frame = dialog.findViewById(R.id.btn_frame);

        String[] player_characters = null;
        int i = 0;
        for(Player p:players){
            player_characters[i] = p.getPlayerCharacterName().name();
            i++;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ac,android.R.layout.simple_list_item_1,player_characters);

        ListView framable_players = (ListView)dialog.findViewById(R.id.list_framable_players);

        framable_players.setAdapter(adapter);

        framable_players.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //framed[0] = player_characters[position];
                Toast.makeText(ac, "You framed " + player_characters[position], Toast.LENGTH_LONG).show();
            }
        });

        TextView txt_frame = dialog.findViewById(R.id.txt_cheat_title);
        //txt_frame.setText("Frame: " + framed[0]);

        img_close.setOnClickListener(v -> {
            dialog.dismiss();
        });

        btn_frame.setOnClickListener(v -> {
            Toast.makeText(ac, "You framed " + framed[0], Toast.LENGTH_LONG).show();
            dialog.dismiss();
        });
        dialog.show();
    }



}

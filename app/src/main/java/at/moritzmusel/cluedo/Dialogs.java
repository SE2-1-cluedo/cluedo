package at.moritzmusel.cluedo;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import at.moritzmusel.cluedo.entities.Character;
import at.moritzmusel.cluedo.entities.Player;
import at.moritzmusel.cluedo.network.Network;

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
        dialog.setCancelable(false);
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
        dialog.setCancelable(false);

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

    public void callFrameDialog(Activity ac, List<Player> players, Player framer){
        dialog = new Dialog(ac);
        dialog.setContentView(R.layout.cheat_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView txt_frame = dialog.findViewById(R.id.txt_cheat_title);
        txt_frame.setText("Frame:");
        TextView txt_frame_name = dialog.findViewById(R.id.txt_frame_player_name);
        ImageView img_close = dialog.findViewById(R.id.img_close);
        Button btn_frame = dialog.findViewById(R.id.btn_frame);
        ArrayList<String> player_characters = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ac,android.R.layout.simple_list_item_1,player_characters);
        ListView frameable_players = (ListView)dialog.findViewById(R.id.list_framable_players);
        frameable_players.setAdapter(adapter);
        dialog.show();

        for(Player p: players){
            player_characters.add(p.getPlayerCharacterName().name());
            if(p.getPlayerId() == framer.getPlayerId()){
                player_characters.remove(framer.getPlayerCharacterName().name());
            }
            adapter.notifyDataSetChanged();
        }

        frameable_players.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(ac, "You framed " + player_characters.get(position), Toast.LENGTH_LONG).show();
                txt_frame_name.setText(player_characters.get(position));
            }
        });

        img_close.setOnClickListener(v -> {
            dialog.dismiss();
        });

        btn_frame.setOnClickListener(v -> {
            //gameplay methode die den wert auf gewählte ändert.
            //return txt_frame.getText();
            dialog.dismiss();
        });

    }

    public void callFramedDialog(Activity ac){
        dialog = new Dialog(ac);
        dialog.setContentView(R.layout.win_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //ImageView img_close = dialog.findViewById(R.id.img_close);
        Button btn_winner = dialog.findViewById(R.id.btn_winner);
        dialog.setCancelable(false);

        ImageView image = dialog.findViewById(R.id.img_win);
        image.setVisibility(View.GONE);

        TextView txt_countdown = dialog.findViewById(R.id.txt_win);
        TextView txt_framed_you = dialog.findViewById(R.id.txt_winner);
        txt_framed_you.setText("Somebody framed you!");
        txt_framed_you.setTextSize(24);
        btn_winner.setText("Deny");

        dialog.show();

        new CountDownTimer(11000, 1000) {
            @Override
            public void onTick(long l) {
                txt_countdown.setText("" + l/1000);
                btn_winner.setOnClickListener(v -> {
                    dialog.dismiss();
                    this.cancel();
                });
            }
            @Override
            public void onFinish() {
                dialog.dismiss();
                callLoseDialog(ac);

                //change to loser
                //alert.setMessage("end");
            }
        }.start();
    }




}

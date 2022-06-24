package at.moritzmusel.cluedo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
        img_close.setOnClickListener(v -> dialog.dismiss());
        btn_winner.setOnClickListener(v -> {
            ac.finish();
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

        img_close.setOnClickListener(v -> dialog.dismiss());
        btn_loser.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

}

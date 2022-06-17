package at.moritzmusel.cluedo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import at.moritzmusel.cluedo.game.Dice;
import at.moritzmusel.cluedo.sensor.ShakeDetector;

public class Dialogs {
    private Activity activity;
    private SensorManager mSensorManager;
    private View dice_layout;
    private View diceView;
    private Sensor accel;
    private Dice dice;
    private ShakeDetector shakeDetector;
    private EvidenceCards evidenceCards;
    Dialog dialog;

    public Dialogs() {
    }

    public Dialogs(Activity ac) {
        this.activity = ac;
    }

    public void callWinDialog(Activity ac, String winner){
        dialog = new Dialog(ac);
        dialog.setContentView(R.layout.win_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView img_close = dialog.findViewById(R.id.img_close);
        Button btn_winner = dialog.findViewById(R.id.btn_winner);

        TextView txt_winner = dialog.findViewById(R.id.txt_win);
        txt_winner.setText("The Winner is: " + winner);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_winner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

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

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_loser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * Creates an alert with a dice action, which is shakeable and clickable.
     */
    public void callDice(Activity ac){
        mSensorManager = (SensorManager) ac.getSystemService(Context.SENSOR_SERVICE);
        accel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        shakeDetector = new ShakeDetector();

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ac,R.style.AlertDialogStyle);
        builder.setTitle("Throw Dice");

        LayoutInflater inflater = ac.getLayoutInflater();
        dice_layout = inflater.inflate(R.layout.custom_dialog, null);
        builder.setView(dice_layout);

        diceView = dice_layout.findViewById(R.id.dialogDice);
        dice = new Dice((ImageView) diceView);


        mSensorManager.registerListener(shakeDetector, accel, SensorManager.SENSOR_DELAY_UI);

        builder.setCancelable(false);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void diceRolled(Activity ac) {
        diceView.setOnClickListener(v -> Toast.makeText(ac,"You already rolled the dice!", Toast.LENGTH_SHORT).show());
        rolledMagnifyingGlass(ac, dice);
    }

    public void rolledMagnifyingGlass(Activity ac, Dice dice) {
        if(dice.getNumberRolled() == 4){
            AlertDialog.Builder builder = new AlertDialog.Builder(ac);
            builder.setTitle("What is going on?");
            builder.setMessage("You rolled the magnifying glass." + "\n"
                    + "A evidence card has been drawn." + "\n"
                    + "It is revealed that the Card: " + evidenceCards.getDrawnCard().getDesignation() + "\n"
                    + "is owned by: " + evidenceCards.getPlayer());

            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create();
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }


}

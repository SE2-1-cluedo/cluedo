package at.moritzmusel.cluedo.game;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Random;

import at.moritzmusel.cluedo.R;

public class Dice {
    private int numberRolled;
    private ImageView diceView;

    /**
     * Simuliert die Logik eines Würfels, sowie die Würfel Animation
     * @param diceView die Image View des Würfels welcher der Spieler angezeigt
     *                 bekommt bevor dieser ihn würfelt. Diese wird dann durch ein
     *                 Bilde der gewürfelten zahl getauscht
     */
    public Dice(ImageView diceView){
        this.diceView = diceView;
    }

    /**
     * setzt den Würfel auf das ursprüngliche Image zurück
     * setzt die gewürfelte Zahl auf -1
     */
    public void resetDice(){
        diceView.setImageResource(R.drawable.dice3d);
        numberRolled = -1;
    }

    /**
     * Wirft den würfel. Dabei wird die Anmiation des Wurfes durchgeführt
     * und das Ergebnis wird auf der Image View bildlich dargestellt
     */

    public void throwDice()  {
        Random r = new Random();
        int i = r.nextInt(4)+1;
        Gameplay.rollDiceForPlayer(i);
        Animation animation = AnimationUtils.loadAnimation(diceView.getContext(), R.anim.rotate_dice);
        diceView.startAnimation(animation);
        switch (i){
            case 1:
                diceView.setImageResource(R.drawable.dice1);
                break;
            case 2:
                diceView.setImageResource(R.drawable.dice2);
                break;
            case 3:
                diceView.setImageResource(R.drawable.dice3);
                break;
            case 4:
                diceView.setImageResource(R.drawable.dice4);
                break;
            default:
                break;
        }
        numberRolled = i;
    }


    //------------------------------------------ Getter and Setter --------------------------------------------------------------------------//
    public ImageView getDiceView() {
        return diceView;
    }

    public void setDiceView(ImageView diceView) {
        this.diceView = diceView;
    }

    public int getNumberRolled() {
        return numberRolled;
    }

    public void setNumberRolled(int numberRolled) {
        this.numberRolled = numberRolled;
    }
}

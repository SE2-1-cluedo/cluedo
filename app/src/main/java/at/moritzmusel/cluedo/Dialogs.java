package at.moritzmusel.cluedo;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Activity;
import android.app.Dialog;
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

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

import at.moritzmusel.cluedo.communication.NetworkCommunicator;
import at.moritzmusel.cluedo.entities.Character;
import at.moritzmusel.cluedo.entities.Player;
import at.moritzmusel.cluedo.game.Gameplay;
import at.moritzmusel.cluedo.network.Network;
import at.moritzmusel.cluedo.network.pojo.GameState;

public class Dialogs {
    Gameplay gp1 = Gameplay.getInstance();
    GameState gameState = GameState.getInstance();
    NetworkCommunicator networkCommunicator = NetworkCommunicator.getInstance();

    public Dialogs() {
    }

    /**
     * Shows a Dialog with the Winner Information.
     * @param ac Activity where it will be shown
     * @param winner Name of the Winner
     */
    public void callWinDialog(Activity ac, String winner){
        Dialog dialog;
        dialog = new Dialog(ac);
        dialog.setContentView(R.layout.win_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

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
     * Shows a Dialog with the Information that you lost or made a wrong accusation.
     * @param ac Activity where it will be shown
     */
    public void callLoseDialog(Activity ac, String reason){
        Dialog dialog;
        dialog = new Dialog(ac);
        dialog.setContentView(R.layout.win_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        ImageView img_close = dialog.findViewById(R.id.img_close);
        Button btn_loser = dialog.findViewById(R.id.btn_winner);
        btn_loser.setBackground(ac.getDrawable(R.drawable.custom_button2));

        ImageView image = dialog.findViewById(R.id.img_win);
        image.setImageResource(R.drawable.red_lose);

        TextView txt_loser = dialog.findViewById(R.id.txt_win);
        txt_loser.setText(reason);

        TextView txt_lost = dialog.findViewById(R.id.txt_winner);
        txt_lost.setText("You Lose!");
        txt_lost.setTextColor(ac.getResources().getColor(R.color.red));

        if(reason.equals("You made a wrong Accusation!")){
            img_close.setVisibility(View.GONE);
            btn_loser.setOnClickListener(v -> {
                //ac.finishAffinity();
                gameState.setLoser(null,true);
                removeFromTurnOrder(Network.getCurrentUser().getUid());
                dialog.dismiss();
            });
        }else if(reason.equals("Somebody else made a wrong Accusation!")){
            txt_loser.setText(gp1.getCharacterByPlayerID(gameState.getLoser())  + " made a wrong Accusation");
            txt_lost.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
            img_close.setOnClickListener(v -> {
                dialog.dismiss();
            });
            btn_loser.setOnClickListener(v -> {
                dialog.dismiss();
            });
        }
        else{
            img_close.setOnClickListener(v -> {
                endGame(ac);
                dialog.dismiss();
            });
            btn_loser.setOnClickListener(v -> {
                endGame(ac);
                dialog.dismiss();
            });
        }
        dialog.show();
    }

    /**
     * Deletes a player id from the turnorder, so he can not do anything after a wrong accusation
     * @param uid Id to delete
     */
    private void removeFromTurnOrder(String uid) {
        String [] turnOrder = ArrayUtils.removeElement(gameState.getTurnOrder(), uid);
        gameState.setTurnOrder(turnOrder,true);
    }

    /**
     * If the the winner is set it will be used to switch to the beginning screen
     * @param ac Activity to be called
     */
    public void endGame(Activity ac){
        //Network.leaveLobby(Network.getCurrentUser(),Network.getCurrentGameID());
        Intent intent = new Intent(ac.getApplicationContext(),MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ac.startActivity(intent);
    }

    /**
     * A dialog to select a player to fame and end the game
     * @param ac Activity to be called
     * @param players list of all players
     * @param framer the player who frames another player
     */
    public void callFrameDialog(Activity ac, List<Player> players, Player framer){
        Dialog dialog;
        dialog = new Dialog(ac);
        dialog.setContentView(R.layout.cheat_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView txt_frame = dialog.findViewById(R.id.txt_cheat_title);
        txt_frame.setText("Frame:");
        TextView txt_frame_name = dialog.findViewById(R.id.txt_frame_player_name);
        ImageView img_close = dialog.findViewById(R.id.img_close);
        Button btn_frame = dialog.findViewById(R.id.btn_frame);
        btn_frame.setBackground(ac.getResources().getDrawable(android.R.drawable.progress_horizontal));
        ArrayList<String> player_characters = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ac,android.R.layout.simple_list_item_1,player_characters);
        ListView frameable_players = (ListView)dialog.findViewById(R.id.list_framable_players);
        frameable_players.setAdapter(adapter);
        dialog.show();

        for(Player p: players){
            player_characters.add(p.getPlayerCharacterName().name());
            if(p.getPlayerId() == framer.getPlayerId()){
                player_characters.remove(framer.getPlayerCharacterName().name());
                //gameState.setFramer(framer.getPlayerId().toString(), true);
            }
            adapter.notifyDataSetChanged();
        }

        frameable_players.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(ac, "You framed " + player_characters.get(position), Toast.LENGTH_LONG).show();
                btn_frame.setClickable(true);
                btn_frame.setBackground(ac.getResources().getDrawable(R.drawable.custom_button));
                txt_frame_name.setText(player_characters.get(position));
            }
        });

        img_close.setOnClickListener(v -> {
            dialog.dismiss();
        });

        btn_frame.setOnClickListener(v -> {
            for(Player p:gameState.getPlayerState()){
                if(p.getPlayerCharacterName().equals(getCharacterWithString(txt_frame_name.getText().toString()))){
                    gameState.setFramed(p.getPlayerId(),true);
                }
            }
            gameState.setFramer(framer.getPlayerId(),true);
            dialog.dismiss();
        });
    }

    /**
     * Gives a string and returns a Character
     * @param name Name of the Character
     * @return Character that equals the string
     */
    public Character getCharacterWithString(String name){
        if(name.equals(Character.MISS_SCARLETT.name())){
            return Character.MISS_SCARLETT;
        }
        else if(name.equals(Character.REVEREND_GREEN.name())){
            return Character.REVEREND_GREEN;
        }
        else if(name.equals(Character.PROFESSOR_PLUM.name())){
            return Character.PROFESSOR_PLUM;
        }
        else if(name.equals(Character.MRS_PEACOCK.name())){
            return Character.MRS_PEACOCK;
        }
        else if(name.equals(Character.COLONEL_MUSTARD.name())){
            return Character.COLONEL_MUSTARD;
        }
        else{
            return Character.DR_ORCHID;
        }
    }

    /**
     * A dialog is called with a countdown and if it reaches zero the game ends.
     * @param ac Activity to be called
     */
    public void callFramedDialog(Activity ac){
        Dialog dialog;
        dialog = new Dialog(ac);
        dialog.setContentView(R.layout.win_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //ImageView img_close = dialog.findViewById(R.id.img_close);
        Button btn_deny = dialog.findViewById(R.id.btn_winner);

        ImageView image = dialog.findViewById(R.id.img_win);
        image.setVisibility(View.GONE);

        TextView txt_countdown = dialog.findViewById(R.id.txt_win);
        TextView txt_framed_you = dialog.findViewById(R.id.txt_winner);
        txt_framed_you.setText("Somebody framed you!");
        txt_framed_you.setTextSize(24);
        btn_deny.setText("Deny");

        dialog.show();

        new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long l) {
                txt_countdown.setText("" + l/1000);
                btn_deny.setOnClickListener(v -> {
                    gameState.setFramed(null,true);
                    gameState.setFramer(null,true);
                    dialog.cancel();
                    this.cancel();
                });
            }

            @Override
            public void onFinish() {
                gameState.setWinner(gameState.getFramer(),true);
                gameState.setFramed(null,true);
                gameState.setFramer(null,true);
                dialog.dismiss();
                //callLoseDialog(ac, "You were framed!");
                //change to loser
                //alert.setMessage("end");
            }
        }.start();
    }





}

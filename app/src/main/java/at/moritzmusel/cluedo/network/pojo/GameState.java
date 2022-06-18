package at.moritzmusel.cluedo.network.pojo;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.moritzmusel.cluedo.entities.Player;
import at.moritzmusel.cluedo.network.Network;
import at.moritzmusel.cluedo.network.data.QuestionCards;

public class GameState {
    private List<Player> playerState;
    private List<Integer> cardState;
    private List<Card> questionCardStack;
    private Question askQuestion;
    private final Killer killer;
    private String playerTurn;
    private final String[] turnOrder;
    //Positions in Array -> {dagger - candlestick - revolver - rope - pipe - wrench}
    private int[] weaponPositions = new int[]{5,1,9,3,6,8};
    DatabaseReference dbRef;

    public GameState(List<Player> playerState, List<Integer> cardState, Question askQuestion, Killer killer, Context ctx, String[] turnOrder) {
        this.playerState = playerState;
        this.cardState = cardState;
        this.askQuestion = askQuestion;
        this.killer = killer;
        this.turnOrder = turnOrder;
        initQuestionCardsStack(ctx);
        dbRef = Network.getCurrentGame();
    }

    public List<Integer> getCardState() {
        return cardState;
    }

    public void setCardState(List<Integer> cardState, boolean database) {
        this.cardState = cardState;
        if (cardState == null && database)
            dbRef.child("players").child(Network.getCurrentUser().getUid()).child("cards").setValue("");
        else if(database){
            StringBuilder sB = new StringBuilder();
            for (int c : cardState)
                sB.append(c).append("");

            dbRef.child("players").child(Network.getCurrentUser().getUid()).child("cards").setValue(sB.toString().trim());
        }
    }

    public List<Player> getPlayerState() {
        return playerState;
    }

    public void setPlayerState(List<Player> playerState, boolean database) {
        this.playerState = playerState;
        if(playerState == null && database){
            List<String> players = new ArrayList<>();
            dbRef.child("players").get().addOnCompleteListener(task -> {
                if (!task.isSuccessful())
                    Log.e("firebase", "Error getting data", task.getException());
                else {
                    String[] splitter = String.valueOf(task.getResult().getValue()).split("[{ =]");
                    for(String s : splitter)
                        if (s.length() == 28)
                            players.add(s);

                    for(String player : players){
                        Map<String,Object> map = new HashMap<>();
                        map.put("cards","");
                        map.put("cards-eliminated","");
                        map.put("character","");
                        map.put("position","");
                        dbRef.child("players").child(player).updateChildren(map);
                    }
                }
            });
        } else if(database){
            for (Player p : playerState){
                dbRef.child("players").child(p.getPlayerId()).child("cards").setValue(p.getOwnedCardsAsString());
                dbRef.child("players").child(p.getPlayerId()).child("cards-eliminated").setValue(p.getKnownCardsAsString());
                dbRef.child("players").child(p.getPlayerId()).child("character").setValue(p.getPlayerCharacterName().name());
                dbRef.child("players").child(p.getPlayerId()).child("position").setValue(Integer.toString(p.getPositionOnBoard()));
            }
        }
    }

    public Question getAskQuestion() {
        return askQuestion;
    }

    public void setAskQuestion(Question askQuestion, boolean database) {
        this.askQuestion = askQuestion;
        if(askQuestion == null && database)
            dbRef.child("turn-flag").child("question").setValue("");
        else if(database){
            StringBuilder sB = new StringBuilder();
            sB.append(askQuestion.getAskPerson()).append(" ");
            for(int i : askQuestion.getCards())
                sB.append(i).append("");
            dbRef.child("turn-flag").child("question").setValue(sB.toString().trim());
        }
    }

    public Killer getKiller() {
        return killer;
    }

    public String getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(String playerTurn, boolean database) {
        this.playerTurn = playerTurn;
        if(playerTurn == null && database)
            dbRef.child("turn-flag").child("player-turn").setValue("");
        else if(database)
            dbRef.child("turn-flag").child("player-turn").setValue(playerTurn);
    }

    private void initQuestionCardsStack(Context ctx){
        QuestionCards qc = new QuestionCards(ctx);
        this.questionCardStack = qc.getQuestionCards();
    }

    public int[] getWeaponPositions() {
        return weaponPositions;
    }

    public void setWeaponPositions(int[] weaponPositions, boolean database) {
        this.weaponPositions = weaponPositions;
        if(weaponPositions == null && database)
            dbRef.child("weapon-positions").setValue("");
        else if(database){
            StringBuilder sB = new StringBuilder();
            for(int i: weaponPositions)
                sB.append(i).append(" ");

            dbRef.child("weapon-positions").setValue(sB.toString().trim());
        }
    }

    private String[] getTurnOrder(){
        return turnOrder;
    }
}

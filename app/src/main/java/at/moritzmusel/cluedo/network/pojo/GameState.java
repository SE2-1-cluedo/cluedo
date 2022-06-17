package at.moritzmusel.cluedo.network.pojo;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import at.moritzmusel.cluedo.network.data.QuestionCards;

public class GameState {
    private List<Player> playerState;
    private List<CardState> cardState;
    private List<Card> questionCardStack;
    private Question askQuestion;
    private Killer killer;
    private String playerTurn;

    public GameState(List<Player> playerState, List<CardState> cardState, Question askQuestion, Killer killer, String playerTurn, Context ctx) {
        this.playerState = playerState;
        this.cardState = cardState;
        this.askQuestion = askQuestion;
        this.killer = killer;
        this.playerTurn = playerTurn;
        //cardState = new ArrayList<>(29);
        initQuestionCardsStack(ctx);
    }

    public List<CardState> getCardState() {
        return cardState;
    }

    public void setCardState(List<CardState> cardState) {
        this.cardState = cardState;
    }

    public List<Player> getPlayerState() {
        return playerState;
    }

    public void setPlayerState(List<Player> playerState) {
        this.playerState = playerState;
    }

    public Question getAskQuestion() {
        return askQuestion;
    }

    public void setAskQuestion(Question askQuestion) {
        this.askQuestion = askQuestion;
    }

    public Killer getKiller() {
        return killer;
    }

    public void setKiller(Killer killer) {
        this.killer = killer;
    }

    public String getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(String playerTurn) {
        this.playerTurn = playerTurn;
    }

    private void initQuestionCardsStack(Context ctx){
        QuestionCards qc = new QuestionCards(ctx);
        this.questionCardStack = qc.getQuestionCards();
    }
}

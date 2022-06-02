package at.moritzmusel.cluedo.network.pojo;

import java.util.List;

public class GameState {
    private List<Player> playerState;
    private List<CardState> cardState;
    private Question askQuestion;
    private Killer killer;
    private String playerTurn;

    public GameState(List<Player> playerState, List<CardState> cardState, Question askQuestion, Killer killer, String playerTurn) {
        this.playerState = playerState;
        this.cardState = cardState;
        this.askQuestion = askQuestion;
        this.killer = killer;
        this.playerTurn = playerTurn;
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
}

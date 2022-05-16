package at.moritzmusel.cluedo.network.pojo;

import java.util.List;

public class GameState {
    private List<Player> playerState;
    private List<CardState> cardState;

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
}

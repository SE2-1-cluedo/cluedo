package at.moritzmusel.cluedo.entities;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String playerId;
    private int positionOnBoard = 1;
    private boolean isAbleToMove = false;
    private final Character playerCharacterName;
    private ArrayList<Integer> playerOwnedCards = new ArrayList<>();
    private ArrayList<Integer> cardsKnownThroughQuestions = new ArrayList<>();

    /**
     * @param playerId
     * Player ID assigned by the Server
     * @param playerCharacterName
     * Chosen by the player
     */
    public Player(String playerId, Character playerCharacterName) {
        this.playerId = playerId;
        this.playerCharacterName = playerCharacterName;
    }


    //----------------------------------Methods----------------------------//

    public void addOwnedCard(int card) {
        playerOwnedCards.add(card);
    }

    public void addKnownCard(int card){cardsKnownThroughQuestions.add(card);}

    public String getOwnedCardsAsString() {
        StringBuilder res = new StringBuilder();
        for (int c : playerOwnedCards) res.append(c).append(" ");
        return res.length() < 2 ? "" : res.toString().trim();
    }

    public String getKnownCardsAsString() {
        StringBuilder res = new StringBuilder();
        for (int c : cardsKnownThroughQuestions) res.append(c).append(" ");
        return res.length() < 2 ? "" : res.toString().trim();
    }

    @NonNull
    @Override
    public String toString() {
        return "Player{" +
                "cards=" + getOwnedCardsAsString() +
                ", eliminationCards=" + getKnownCardsAsString() +
                '}';
    }


    //----------------------------------Getter and Setter----------------------------//

    public String getPlayerId() {
        return playerId;
    }

    public int getPositionOnBoard() {
        return positionOnBoard;
    }

    public void setPositionOnBoard(int positionOnBoard) {
        this.positionOnBoard = positionOnBoard;
    }

    public Character getPlayerCharacterName() {
        return playerCharacterName;
    }

    public List<Integer> getPlayerOwnedCards() {
        return playerOwnedCards;
    }

    public void setPlayerOwnedCards(ArrayList<Integer> playerOwnedCards) {
        this.playerOwnedCards = playerOwnedCards;
    }

    public boolean getIsAbleToMove() {
        return isAbleToMove;
    }

    public void setAbleToMove(boolean ableToMove) {
        isAbleToMove = ableToMove;
    }

    public ArrayList<Integer> getCardsKnownThroughQuestions() {
        return cardsKnownThroughQuestions;
    }

    public void setCardsKnownThroughQuestions(ArrayList<Integer> cardsKnownThroughQuestions) {
        this.cardsKnownThroughQuestions = cardsKnownThroughQuestions;
    }
}

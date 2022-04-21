package at.moritzmusel.cluedo.entities;

import java.util.ArrayList;

public class Player {
    private int playerId;
    private int positionOnBoard;
    private String playerCharacterName;
    private ArrayList<Integer> playerOwnedCards;

    public Player(int playerId, int positionOnBoard,
                  String playerCharacterName,
                  ArrayList<Integer> playerOwnedCards) {
        this.playerId = playerId;
        this.positionOnBoard = positionOnBoard;
        this.playerCharacterName = playerCharacterName;
        this.playerOwnedCards = playerOwnedCards;
    }


    //----------------------------------Getter and Setter----------------------------//

    public int getPlayerId() {
        return playerId;
    }

    public int getPositionOnBoard() {
        return positionOnBoard;
    }

    public void setPositionOnBoard(int positionOnBoard) {
        this.positionOnBoard = positionOnBoard;
    }

    public String getPlayerCharacterName() {
        return playerCharacterName;
    }

    public void setPlayerCharacterName(String playerCharacterName) {
        this.playerCharacterName = playerCharacterName;
    }

    public ArrayList<Integer> getPlayerOwnedCards() {
        return playerOwnedCards;
    }

    public void setPlayerOwnedCards(ArrayList<Integer> playerOwnedCards) {
        this.playerOwnedCards = playerOwnedCards;
    }
}

package at.moritzmusel.cluedo.entities;

import java.util.ArrayList;

public class Player {
    private final int playerId;
    private int positionOnBoard;
    private boolean isAbleToMove = false;
    private final Character playerCharacterName;
    private final ArrayList<Integer> playerOwnedCards;

    public Player(int playerId, int positionOnBoard,
                  Character playerCharacterName,
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

    public Character getPlayerCharacterName() {
        return playerCharacterName;
    }

    public ArrayList<Integer> getPlayerOwnedCards() {
        return playerOwnedCards;
    }

    public boolean getIsAbleToMove() {
        return isAbleToMove;
    }

    public void setIsAbleToMove(boolean ableToMove) {
        isAbleToMove = ableToMove;
    }
}

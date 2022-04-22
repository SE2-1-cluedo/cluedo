package at.moritzmusel.cluedo.entities;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final int playerId;
    private int positionOnBoard = 1;
    private boolean isAbleToMove = false;
    private final Character playerCharacterName;
    private final List<Integer> playerOwnedCards;

    /**
     * @param playerId
     * Player ID assigned by the Server
     * @param playerCharacterName
     * Choosen by the player
     * @param playerOwnedCards
     * Cluedo Cards owned by the players referred through their unique ID
     */
    public Player(int playerId,
                  Character playerCharacterName,
                  List<Integer> playerOwnedCards) {
        this.playerId = playerId;
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

    public List<Integer> getPlayerOwnedCards() {
        return playerOwnedCards;
    }

    public boolean getIsAbleToMove() {
        return isAbleToMove;
    }

    public void setIsAbleToMove(boolean ableToMove) {
        isAbleToMove = ableToMove;
    }
}

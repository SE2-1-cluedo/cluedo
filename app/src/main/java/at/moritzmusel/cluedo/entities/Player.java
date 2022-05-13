package at.moritzmusel.cluedo.entities;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final int playerId;
    private int positionOnBoard = 1;
    private String positionOnBoardString = "startPoint";
    private boolean isAbleToMove = false;
    private final Character playerCharacterName;
    private final ArrayList<Integer> playerOwnedCards = new ArrayList<>();

    /**
     * @param playerId
     * Player ID assigned by the Server
     * @param playerCharacterName
     * Choosen by the player
     */
    public Player(int playerId,
                  Character playerCharacterName) {
        this.playerId = playerId;
        this.playerCharacterName = playerCharacterName;
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

    public void setPlayerCard(int playerCard) {
        playerOwnedCards.add(playerCard);
    }

    public boolean getIsAbleToMove() {
        return isAbleToMove;
    }

    public void setIsAbleToMove(boolean ableToMove) {
        isAbleToMove = ableToMove;
    }

    public String getPositionOnBoardString() {
        return positionOnBoardString;
    }

    public void setPositionOnBoardString(String positionOnBoardString) {
        this.positionOnBoardString = positionOnBoardString;
    }
}

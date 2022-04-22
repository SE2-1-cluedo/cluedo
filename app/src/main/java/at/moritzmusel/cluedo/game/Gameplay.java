package at.moritzmusel.cluedo.game;

import java.util.List;

import at.moritzmusel.cluedo.entities.Character;
import at.moritzmusel.cluedo.entities.Player;

public class Gameplay {
    private static int numDice;
    private Character currentPlayer;
    private final List<Player> players;

    /**
     * @param players
     *      all the Players in the Session
     */
    public Gameplay(List<Player> players) {
        this.players = players;
    }

    /**
     * Called after the Player ends his/her turn
     */
    public Character endTurn(){
        Player player = findPlayerByCharacterName(currentPlayer);
        player.setIsAbleToMove(false);
        decideCharacterWhoMovesNext();
        return currentPlayer;
    }

    /**
     * Calculates the position of the Player after the dice was thrown depending on if
     * he moved right or left
     * dice was thrown and
     * @param direction
     *  1 (move right)     /    0 (move left)
     */
    public void movePlayer(byte direction){
        Player player = findPlayerByCharacterName(currentPlayer);
        player.setIsAbleToMove(true);
        if(player.getIsAbleToMove()) {
            int newPosition = calculatePosition(player.getPositionOnBoard(), direction, numDice);
            player.setPositionOnBoard(newPosition);
            //movePlayerUi(player)
            //dont allow dice throw again
        }
    }

    /**
     * Takes the result after the Player throw the dice and safes it in a variable
     * @param numberRolled
     * Takes the result after the dice throw
     */
    public static void rollDiceForPlayer(int numberRolled){
       numDice = numberRolled;
    }

    /**
     * Decides which Player/Character is able to move first
     */
    public void DecidePlayerWhoMovesFirst(){
        currentPlayer = Character.Miss_Scarlet;
        while(true) {
            Player firstPlayer = findPlayerByCharacterName(currentPlayer);
            if(firstPlayer == null){
                currentPlayer = currentPlayer.getNextCharacter();
            }else {
                currentPlayer = firstPlayer.getPlayerCharacterName();
                break;
            }
        }

    }

    private void decideCharacterWhoMovesNext(){
        while(true) {
            assert currentPlayer != null;
            currentPlayer= currentPlayer.getNextCharacter();
            Player player = findPlayerByCharacterName(currentPlayer);
            if(player != null){
                currentPlayer = player.getPlayerCharacterName();
                break;
            }
        }
    }

    /**
     *
     * @param character
     * Find the Character belonging to a player if not return null
     * @return
     * Player who playing as the character
     */
    public Player findPlayerByCharacterName(Character character){
        int countCharacters = 1;
        while(true) {
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getPlayerCharacterName() == character) {
                    return players.get(i);
                }else if (countCharacters > 6) {
                    return null;
                }
            }
            assert character != null;
            countCharacters++;
        }
    }

    /**
     * @param position
     * the current position of the player on board
     * @param direction
     * if the player wants to move right(1) or left(0)
     * @param diceNum
     * the number thrown by the player
     * @return
     * final position that the position is a valid field on the board
     */
    private int calculatePosition(int position, byte direction, int diceNum){
        int finalPosition;
        if(direction == 1){
            finalPosition = position + diceNum <= 9 ? diceNum + position : (diceNum + position) - 9;
        }else{
            finalPosition = position - diceNum > 0 ? position - diceNum : (position - diceNum) + 9;
        }
        return finalPosition;
    }

    public static void setNumDice(int numDice) {
        Gameplay.numDice = numDice;
    }

    public Character getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Character currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}

package at.moritzmusel.cluedo.game;

import java.util.List;
import java.util.Random;

import at.moritzmusel.cluedo.entities.Character;
import at.moritzmusel.cluedo.entities.Player;

public class Gameplay {
    private static int numDice;
    private static boolean isAbleToMove = false;
    private Character currentPlayer;
    private static final Random rand = new Random();
    private final List<Player> players;

    /**
     * @param players
     *      all the Players in the Session
     */
    public Gameplay(List<Player> players) {
        this.players = players;
    }

    public void endTurn(){
        currentPlayer = currentPlayer.nextPlayer();
        isAbleToMove = false;
    }

    public void movePlayer(){
        if(isAbleToMove){
            Player player = findPlayerByCharacterName(currentPlayer);
            player.setPositionOnBoard(player.getPositionOnBoard() + numDice);
            //movePlayerUi(player)
        }
    }
    public static void rollDiceForPlayer(int numberRolled){
       numDice = numberRolled;
       isAbleToMove = true;
    }
    public void DecidePlayerWhoMovesFirst(){
        currentPlayer = Character.Miss_Scarlet;
        Player firstPlayer = findPlayerByCharacterName(currentPlayer);
        while(true) {
            if(firstPlayer != null){
                currentPlayer = firstPlayer.getPlayerCharacterName();
                break;
            }else {
                assert currentPlayer != null;
                currentPlayer = currentPlayer.nextPlayer();
            }
        }

    }

    public Player findPlayerByCharacterName(Character character){
        Player player = null;
        for (int i = 0; i < players.size(); i++) {
            if(players.get(i).getPlayerCharacterName().equals(character)) {
                player = players.get(i);
                break;
            }
        }
       return player;
    }
}

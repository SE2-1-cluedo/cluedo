package at.moritzmusel.cluedo.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import at.moritzmusel.cluedo.entities.Character;
import at.moritzmusel.cluedo.entities.Player;

import static at.moritzmusel.cluedo.entities.Character.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.ArrayList;

public class GameplayTest {

    Player Player1;
    Player Player2;
    Player Player3;
    Player Player4;
    Player Player5;
    Gameplay gameOdd;
    Gameplay gameEven;
    ArrayList<Player> playersOdd;
    ArrayList<Player> playersEven;

    @Before
    public void setUp() {
        Player1 = new Player(1, MISS_SCARLET);
        Player2 = new Player(2, THE_REVEREND_GREEN);
        Player3 = new Player(3, PROFESSOR_PLUM);
        Player4 = new Player(4, MRS_PEACOCK);
        Player5 = new Player(5, MRS_WHITE);
        playersOdd = new ArrayList<>(Arrays.asList(Player1, Player2, Player3, Player4, Player5));
        playersEven = new ArrayList<>(Arrays.asList(Player2,Player3, Player4, Player5));
        gameOdd = new Gameplay(playersOdd);
        gameEven = new Gameplay(playersEven);
        Gameplay.setNumDice(3);
    }

    @Test
    public void endTurn() {
        gameOdd.decidePlayerWhoMovesFirst();
        Character currentPlayer = gameOdd.endTurn();
        assertEquals(MRS_WHITE, currentPlayer);
    }

    @Test
    public void endTurnOfLastPlayer() {
        gameOdd.setCurrentPlayer(PROFESSOR_PLUM);
        Character currentPlayer = gameOdd.endTurn();
        assertEquals(MISS_SCARLET, currentPlayer);
    }

    @Test
    public void endTurnPlayerNotMoving() {
        gameOdd.decidePlayerWhoMovesFirst();
        endTurn();
        Assert.assertFalse(Player1.getIsAbleToMove());
    }

    @Test
    public void movePlayerOverTheStartRight() {
        gameOdd.decidePlayerWhoMovesFirst();
        gameOdd.findPlayerByCharacterName(gameOdd.getCurrentPlayer()).setPositionOnBoard(9);
        gameOdd.movePlayer((byte) 1);
        assertEquals(3, gameOdd.findPlayerByCharacterName(gameOdd.getCurrentPlayer()).getPositionOnBoard());
    }

    @Test
    public void movePlayerOverTheStartLeft() {
        gameOdd.decidePlayerWhoMovesFirst();
        gameOdd.findPlayerByCharacterName(gameOdd.getCurrentPlayer()).setPositionOnBoard(2);
        gameOdd.movePlayer((byte) 0);
        assertEquals(8, gameOdd.findPlayerByCharacterName(gameOdd.getCurrentPlayer()).getPositionOnBoard());
    }

    @Test
    public void move2PlayersRightWithSameDiceResult() {
        int player1Pos, player2Pos;
        gameOdd.decidePlayerWhoMovesFirst();
        gameOdd.movePlayer((byte) 1);
        player1Pos = gameOdd.findPlayerByCharacterName(gameOdd.getCurrentPlayer()).getPositionOnBoard();
        gameOdd.endTurn();
        gameOdd.movePlayer((byte) 1);
        player2Pos = gameOdd.findPlayerByCharacterName(gameOdd.getCurrentPlayer()).getPositionOnBoard();
        assertEquals(player1Pos, player2Pos);
    }

    @Test
    public void move2PlayersLeftWithSameDiceResult() {
        int player1Pos, player2Pos;
        gameOdd.decidePlayerWhoMovesFirst();
        gameOdd.movePlayer((byte) 0);
        player1Pos = gameOdd.findPlayerByCharacterName(gameOdd.getCurrentPlayer()).getPositionOnBoard();
        gameOdd.endTurn();
        gameOdd.movePlayer((byte) 0);
        player2Pos = gameOdd.findPlayerByCharacterName(gameOdd.getCurrentPlayer()).getPositionOnBoard();
        assertEquals(player1Pos, player2Pos);
    }


    @Test
    public void currentPlayerIsNotAllowedToUseSecretPassage() {
        gameOdd.decidePlayerWhoMovesFirst();
        assertFalse(gameOdd.isAllowedToUseSecretPassage());
    }

    @Test
    public void decidePlayerWhoMovesFirst() {
        gameOdd.decidePlayerWhoMovesFirst();
        assertEquals(MISS_SCARLET, gameOdd.getCurrentPlayer());
    }

    @Test
    public void decidePlayerWhoMovesWhenScarlettIsNotPlayed() {
        gameEven = new Gameplay(playersEven);
        gameEven.decidePlayerWhoMovesFirst();
        assertEquals(MRS_WHITE, gameEven.getCurrentPlayer());
    }

    @Test
    public void findPlayerByCharacterName() {
        assertEquals(Player3, gameOdd.findPlayerByCharacterName(PROFESSOR_PLUM));
    }

    @Test
    public void findCharacterWhoIsNotInGame() {
        Assert.assertNull(gameEven.findPlayerByCharacterName(COLONEL_MUSTARD));
    }

    @Test
    public void checkIfTheGeneratedCluedoCardsAreUnique(){
        gameOdd.generateCluedoCards();
        ArrayList<Integer> allPlayerCards = new ArrayList<>();
        ArrayList<Integer> compared = new ArrayList<>();
        boolean check = false;

        allPlayerCards.addAll(Player1.getPlayerOwnedCards());
        allPlayerCards.addAll(Player2.getPlayerOwnedCards());
        allPlayerCards.addAll(Player3.getPlayerOwnedCards());
        allPlayerCards.addAll(Player4.getPlayerOwnedCards());
        allPlayerCards.addAll(Player5.getPlayerOwnedCards());

        compared.add(allPlayerCards.get(0));
        for(int i = 1; i < allPlayerCards.size(); i++){
            int toCheck = allPlayerCards.get(i);
            if(compared.contains(toCheck) && toCheck > 0 && toCheck < 27){
                check = true;
                break;
            }
            compared.add(toCheck);
        }
        assertFalse(check);
    }

    @Test
    public void checkIfCluedoCardsAreEvenDistributedForEvenPlayers(){
        gameEven.generateCluedoCards();
        assertEquals(7,Player2.getPlayerOwnedCards().size());
        assertEquals(7,Player3.getPlayerOwnedCards().size());
        assertEquals(6,Player4.getPlayerOwnedCards().size());
        assertEquals(6,Player5.getPlayerOwnedCards().size());
    }

    @Test
    public void checkIfCluedoCardsAreEvenDistributedForOddPlayers(){
        gameOdd.generateCluedoCards();
        assertEquals(6,Player1.getPlayerOwnedCards().size());
        assertEquals(5,Player2.getPlayerOwnedCards().size());
        assertEquals(5,Player3.getPlayerOwnedCards().size());
        assertEquals(5,Player4.getPlayerOwnedCards().size());
        assertEquals(5,Player5.getPlayerOwnedCards().size());
    }
}
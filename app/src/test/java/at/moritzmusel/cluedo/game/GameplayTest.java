package at.moritzmusel.cluedo.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import at.moritzmusel.cluedo.AllTheCards;
import at.moritzmusel.cluedo.entities.Character;
import at.moritzmusel.cluedo.entities.Player;

import static at.moritzmusel.cluedo.entities.Character.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class GameplayTest {

    Player Player1;
    Player Player2;
    Player Player3;
    Player Player4;
    Player Player5;
    Gameplay gameOdd;

    @Before
    public void setUp() {
        gameOdd = Gameplay.getInstance();
        Player1 = new Player("1");
        Player2 = new Player("2");
        Player3 = new Player("3");
        Player4 = new Player("4");
        Player5 = new Player("5");
    }

    @Test
    public void endTurn() {
        gameOdd.decidePlayerWhoMovesFirst();
        Character currentPlayer = gameOdd.endTurn();
        assertEquals(DR_ORCHID, currentPlayer);
    }

    @Test
    public void endTurnOfLastPlayer() {
        gameOdd.setCurrentPlayer(PROFESSOR_PLUM);
        Character currentPlayer = gameOdd.endTurn();
        assertEquals(MISS_SCARLETT, currentPlayer);
    }


    @Test
    public void currentPlayerIsNotAllowedToUseSecretPassage() {
        gameOdd.decidePlayerWhoMovesFirst();
        gameOdd.findPlayerByCharacterName(gameOdd.getCurrentPlayer()).setPositionOnBoard(2);
        assertFalse(gameOdd.isAllowedToUseSecretPassage());
    }

    @Test
    public void decidePlayerWhoMovesFirst() {
        gameOdd.decidePlayerWhoMovesFirst();
        assertEquals(MISS_SCARLETT, gameOdd.getCurrentPlayer());
    }

    /*
    @Test
    public void decidePlayerWhoMovesWhenScarlettIsNotPlayed() {
        gameEven.decidePlayerWhoMovesFirst();
        assertEquals(DR_ORCHID, gameEven.getCurrentPlayer());
    }*/

    @Test
    public void findPlayerByCharacterName() {
        assertEquals(Player3, gameOdd.findPlayerByCharacterName(PROFESSOR_PLUM));
    }

    /*
    @Test
    public void findCharacterWhoIsNotInGame() {
        Assert.assertNull(gameEven.findPlayerByCharacterName(COLONEL_MUSTARD));
    }*/

    @Test
    public void checkIfTheGeneratedCluedoCardsAreUnique(){
        //gameOdd.createNewGame();
        ArrayList<Integer> allPlayerCards = new ArrayList<>();
        boolean check = false;
        allPlayerCards.addAll(Player1.getPlayerOwnedCards());
        allPlayerCards.addAll(Player2.getPlayerOwnedCards());
        allPlayerCards.addAll(Player3.getPlayerOwnedCards());
        allPlayerCards.addAll(Player4.getPlayerOwnedCards());
        allPlayerCards.addAll(Player5.getPlayerOwnedCards());
        int size = allPlayerCards.size();

        for(int i = 0; i < size; i++){
            int toCheck = allPlayerCards.get(i);
            if(allPlayerCards.size() == 1){
                break;
            }
            allPlayerCards.remove((Integer) toCheck);
            size--;
            if(allPlayerCards.contains(toCheck) && toCheck >= 0 && toCheck <= 20){
                check = true;
                break;
            }
        }
        assertFalse(check);
    }

    /*
    @Test
    public void checkIfCluedoCardsAreEvenDistributedForEvenPlayers(){
        gameEven.createNewGame();
        assertEquals(5,Player2.getPlayerOwnedCards().size());
        assertEquals(5,Player3.getPlayerOwnedCards().size());
        assertEquals(4,Player4.getPlayerOwnedCards().size());
        assertEquals(4,Player5.getPlayerOwnedCards().size());
    }*/


    @Test
    public void checkIfCluedoCardsAreEvenDistributedForOddPlayers() {
        //gameOdd.createNewGame();
        assertEquals(4, Player1.getPlayerOwnedCards().size());
        assertEquals(4, Player2.getPlayerOwnedCards().size());
        assertEquals(4, Player3.getPlayerOwnedCards().size());
        assertEquals(3, Player4.getPlayerOwnedCards().size());
        assertEquals(3, Player5.getPlayerOwnedCards().size());
    }

    @Test
    public void checkIfClueCardsAreGenerated(){
        gameOdd.generateClueCards();
        assertEquals(30,gameOdd.getClueCards().size());
    }

    @Test
    public void checkIfClueCardsAreUnique(){
        gameOdd.generateClueCards();
        ArrayList<Integer> clueCards = new ArrayList<>(gameOdd.getClueCards());
        boolean check = false;
        int toCheck;
        int size = clueCards.size();

        for(int i = 0; i < size; i++){
            toCheck = clueCards.get(i);
            if(clueCards.size() == 1){
                break;
            }
            clueCards.remove((Integer) toCheck);
            size--;
            if(clueCards.contains(toCheck) && toCheck >= 0 && toCheck <= 20){
                check = true;
                break;
            }
        }
        assertFalse(check);
    }

    @Test
    public void drawClueCardAfterDiceThrow4() {
        Gameplay.setNumDice(4);
        gameOdd.decidePlayerWhoMovesFirst();
        gameOdd.generateClueCards();
        gameOdd.updatePlayerPosition(1);
        gameOdd.drawClueCard();
        assertEquals(29,gameOdd.getClueCards().size());
    }

    @Test
    public void drawLastClueCard(){
        ArrayList<Integer> clueCards = new ArrayList<>();
        clueCards.add(1);
        gameOdd.decidePlayerWhoMovesFirst();
        gameOdd.generateClueCards();
        gameOdd.setClueCards(clueCards);
        gameOdd.drawClueCard();
        gameOdd.updatePlayerPosition(1);
        assertEquals(0,gameOdd.getClueCards().size());
        assertEquals(1,gameOdd.getCardDrawn());
    }
    @Test
    public void canMoveTest(){
        gameOdd.decidePlayerWhoMovesFirst();
        //gameOdd.createNewGame();
        gameOdd.setStepsTaken(2);
        Gameplay.setNumDice(3);
        gameOdd.canMove();
        assertFalse(gameOdd.findPlayerByCharacterName(gameOdd.getCurrentPlayer()).getIsAbleToMove());
    }

    @Test
    public void quitGameTest(){
        //gameOdd.createNewGame();
        gameOdd.quitGame(Player1);
        assertEquals(5, Player2.getPlayerOwnedCards().size());
        assertEquals(5, Player3.getPlayerOwnedCards().size());
        assertEquals(4, Player4.getPlayerOwnedCards().size());
        assertEquals(4, Player5.getPlayerOwnedCards().size());
    }

    @Test
    public void questionTest(){
        Player1.getPlayerOwnedCards().clear();Player2.getPlayerOwnedCards().clear();Player3.getPlayerOwnedCards().clear();Player4.getPlayerOwnedCards().clear();Player5.getPlayerOwnedCards().clear();
        Player2.getPlayerOwnedCards().add(1);Player2.getPlayerOwnedCards().add(4);Player2.getPlayerOwnedCards().add(5);Player2.getPlayerOwnedCards().add(9);
        Player3.getPlayerOwnedCards().add(10);Player3.getPlayerOwnedCards().add(11);Player3.getPlayerOwnedCards().add(3);Player3.getPlayerOwnedCards().add(2);
        Player4.getPlayerOwnedCards().add(13);Player4.getPlayerOwnedCards().add(20);Player4.getPlayerOwnedCards().add(19);Player4.getPlayerOwnedCards().add(6);
        Player5.getPlayerOwnedCards().add(7);Player5.getPlayerOwnedCards().add(8);Player5.getPlayerOwnedCards().add(15);Player5.getPlayerOwnedCards().add(14);

        gameOdd.decidePlayerWhoMovesFirst();
        AllTheCards allCluedoCards = new AllTheCards();
        //gameOdd.askPlayerAQuestion(Player1,allCluedoCards.getGameCards().get(3),allCluedoCards.getGameCards().get(10),allCluedoCards.getGameCards().get(17));
        int result = Player1.getCardsKnownThroughQuestions().get(0);
        assertEquals(10,result);
    }
}
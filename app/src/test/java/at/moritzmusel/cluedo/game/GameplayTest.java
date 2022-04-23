package at.moritzmusel.cluedo.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
    Gameplay game;
    Gameplay game2;
    ArrayList<Player> players;
    ArrayList<Player> players2;

    @Before
    public void setUp(){
        Player1 = new Player(1, MISS_SCARLET, new ArrayList<>(Arrays.asList(1, 2, 3,4,5,25)));
        Player2 = new Player(2, THE_REVEREND_GREEN,new ArrayList<>(Arrays.asList(6, 7, 8,9)));
        Player3 = new Player(3, PROFESSOR_PLUM,new ArrayList<>(Arrays.asList(11, 12, 13,14,26)));
        Player4 = new Player(4, MRS_PEACOCK,new ArrayList<>(Arrays.asList(15, 16, 17,18,19)));
        Player5 = new Player(5, MRS_WHITE,new ArrayList<>(Arrays.asList(20, 21, 22,23,24)));
        players = new ArrayList<>(Arrays.asList(Player1,Player2,Player3,Player4,Player5));
        players2 = new ArrayList<>(Arrays.asList(Player3,Player4,Player5));
        game = new Gameplay(players);
        game2 = new Gameplay(players2);
        Gameplay.setNumDice(3);
    }

    @Test
    public void endTurn() {
        game.decidePlayerWhoMovesFirst();
        Character currentPlayer = game.endTurn();
        assertEquals(MRS_WHITE,currentPlayer);
    }

    @Test
    public void endTurnOfLastPlayer() {
        game.setCurrentPlayer(PROFESSOR_PLUM);
        Character currentPlayer = game.endTurn();
        assertEquals(MISS_SCARLET,currentPlayer);
    }

    @Test
    public void endTurnPlayerNotMoving() {
        game.decidePlayerWhoMovesFirst();
        endTurn();
        Assert.assertFalse(Player1.getIsAbleToMove());
    }

    @Test
    public void movePlayerOverTheStartRight() {
        game.decidePlayerWhoMovesFirst();
        game.findPlayerByCharacterName(game.getCurrentPlayer()).setPositionOnBoard(9);
        game.movePlayer((byte) 1);
        assertEquals(3,game.findPlayerByCharacterName(game.getCurrentPlayer()).getPositionOnBoard());
    }

    @Test
    public void movePlayerOverTheStartLeft() {
        game.decidePlayerWhoMovesFirst();
        game.findPlayerByCharacterName(game.getCurrentPlayer()).setPositionOnBoard(2);
        game.movePlayer((byte) 0);
        assertEquals(8,game.findPlayerByCharacterName(game.getCurrentPlayer()).getPositionOnBoard());
    }

    @Test
    public void move2PlayersRightWithSameDiceResult() {
        int player1Pos, player2Pos;
        game.decidePlayerWhoMovesFirst();
        game.movePlayer((byte) 1);
        player1Pos = game.findPlayerByCharacterName(game.getCurrentPlayer()).getPositionOnBoard();
        game.endTurn();
        game.movePlayer((byte) 1);
        player2Pos = game.findPlayerByCharacterName(game.getCurrentPlayer()).getPositionOnBoard();
        assertEquals(player1Pos,player2Pos);
    }

    @Test
    public void move2PlayersLeftWithSameDiceResult() {
        int player1Pos, player2Pos;
        game.decidePlayerWhoMovesFirst();
        game.movePlayer((byte) 0);
        player1Pos = game.findPlayerByCharacterName(game.getCurrentPlayer()).getPositionOnBoard();
        game.endTurn();
        game.movePlayer((byte) 0);
        player2Pos = game.findPlayerByCharacterName(game.getCurrentPlayer()).getPositionOnBoard();
        assertEquals(player1Pos,player2Pos);
    }

    @Test
    public void useSecretPassageKitchenToWorkingRoom() {
        game.decidePlayerWhoMovesFirst();
        Player player = game.findPlayerByCharacterName(game.getCurrentPlayer());
        player.setPositionOnBoard(7);
        game.useSecretPassage();
        assertEquals(2, player.getPositionOnBoard());
    }

    @Test
    public void useSecretPassageSalonToWinterGarden() {
        game.decidePlayerWhoMovesFirst();
        Player player = game.findPlayerByCharacterName(game.getCurrentPlayer());
        player.setPositionOnBoard(9);
        game.useSecretPassage();
        assertEquals(5, player.getPositionOnBoard());
    }

    @Test
    public void currentPlayerIsAllowedToUseSecretPassage(){
        game.decidePlayerWhoMovesFirst();
        Player player = game.findPlayerByCharacterName(game.getCurrentPlayer());
        player.setPositionOnBoard(9);
        assertTrue(game.isAllowedToUseSecretPassage());
    }

    @Test
    public void currentPlayerIsNotAllowedToUseSecretPassage(){
        game.decidePlayerWhoMovesFirst();
        assertFalse(game.isAllowedToUseSecretPassage());
    }

    @Test
    public void decidePlayerWhoMovesFirst() {
        game.decidePlayerWhoMovesFirst();
        assertEquals(MISS_SCARLET,game.getCurrentPlayer());
    }

    @Test
    public void decidePlayerWhoMovesWhenScarlettIsNotPlayed() {
        game2 = new Gameplay(players2);
        game2.decidePlayerWhoMovesFirst();
        assertEquals(MRS_WHITE,game2.getCurrentPlayer());
    }

    @Test
    public void findPlayerByCharacterName() {
        assertEquals(Player3, game.findPlayerByCharacterName(PROFESSOR_PLUM));
    }

    @Test
    public void findCharacterWhoIsNotInGame(){
        Assert.assertNull(game2.findPlayerByCharacterName(COLONEL_MUSTARD));
    }
}
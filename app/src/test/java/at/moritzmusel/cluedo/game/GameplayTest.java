package at.moritzmusel.cluedo.game;

import static org.junit.Assert.assertEquals;

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

        Player1 = new Player(1,Miss_Scarlet, new ArrayList<>(Arrays.asList(1, 2, 3,4,5,25)));
        Player2 = new Player(2,The_Reverend_Green,new ArrayList<>(Arrays.asList(6, 7, 8,9)));
        Player3 = new Player(3,Professor_Plum,new ArrayList<>(Arrays.asList(11, 12, 13,14,26)));
        Player4 = new Player(4,Mrs_Peacock,new ArrayList<>(Arrays.asList(15, 16, 17,18,19)));
        Player5 = new Player(5,Mrs_White,new ArrayList<>(Arrays.asList(20, 21, 22,23,24)));
        players = new ArrayList<>(Arrays.asList(Player1,Player2,Player3,Player4,Player5));
        players2 = new ArrayList<>(Arrays.asList(Player3,Player4,Player5));
        game = new Gameplay(players);
        game2 = new Gameplay(players2);
        Gameplay.setNumDice(3);
    }

    @Test
    public void endTurn() {
        game.DecidePlayerWhoMovesFirst();
        Character currentPlayer = game.endTurn();
        assertEquals(Mrs_White,currentPlayer);
    }

    @Test
    public void endTurnOfLastPlayer() {
        game.setCurrentPlayer(Professor_Plum);
        Character currentPlayer = game.endTurn();
        assertEquals(currentPlayer, Miss_Scarlet);
    }

    @Test
    public void endTurnPlayerNotMoving() {
        game.DecidePlayerWhoMovesFirst();
        endTurn();
        Assert.assertFalse(Player1.getIsAbleToMove());
    }

    @Test
    public void movePlayerOverTheStartRight() {
        game.DecidePlayerWhoMovesFirst();
        game.findPlayerByCharacterName(game.getCurrentPlayer()).setPositionOnBoard(9);
        game.movePlayer((byte) 1);
        assertEquals(game.findPlayerByCharacterName(game.getCurrentPlayer()).getPositionOnBoard(),3);
    }

    @Test
    public void movePlayerOverTheStartLeft() {
        game.DecidePlayerWhoMovesFirst();
        game.findPlayerByCharacterName(game.getCurrentPlayer()).setPositionOnBoard(2);
        game.movePlayer((byte) 0);
        assertEquals(game.findPlayerByCharacterName(game.getCurrentPlayer()).getPositionOnBoard(),8);
    }

    @Test
    public void move2PlayersRightWithSameDiceResult() {
        int player1Pos, player2Pos;
        game.DecidePlayerWhoMovesFirst();
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
        game.DecidePlayerWhoMovesFirst();
        game.movePlayer((byte) 0);
        player1Pos = game.findPlayerByCharacterName(game.getCurrentPlayer()).getPositionOnBoard();
        game.endTurn();
        game.movePlayer((byte) 0);
        player2Pos = game.findPlayerByCharacterName(game.getCurrentPlayer()).getPositionOnBoard();
        assertEquals(player1Pos,player2Pos);
    }


    @Test
    public void decidePlayerWhoMovesFirst() {
        game.DecidePlayerWhoMovesFirst();
        assertEquals(game.getCurrentPlayer(), Miss_Scarlet);
    }

    @Test
    public void decidePlayerWhoMovesWhenScarlettIsNotPlayed() {
        game2 = new Gameplay(players2);
        game2.DecidePlayerWhoMovesFirst();
        assertEquals(game2.getCurrentPlayer(), Mrs_White);
    }

    @Test
    public void findPlayerByCharacterName() {
        assertEquals(game.findPlayerByCharacterName(Professor_Plum), Player3);
    }

    @Test
    public void findCharacterWhoIsNotInGame(){
        Assert.assertNull(game2.findPlayerByCharacterName(Colonel_Mustard));
    }
}
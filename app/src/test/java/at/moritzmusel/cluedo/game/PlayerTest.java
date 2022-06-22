package at.moritzmusel.cluedo.game;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import at.moritzmusel.cluedo.entities.Player;

public class PlayerTest {
    Player player;
    ArrayList<Integer> playerCards;
    ArrayList<Integer> playerKnownCards;
    @Before
    public void setUp() {
        playerCards = new ArrayList<>();
        playerCards.add(1);playerCards.add(6);playerCards.add(10);

        playerKnownCards = new ArrayList<>();
        playerKnownCards.add(4);playerKnownCards.add(2);


        player = new Player("1");
        player.setPositionOnBoard(1);
        player.setPlayerOwnedCards(playerCards);
        player.setCardsKnownThroughQuestions(playerKnownCards);
    }

    @Test
    public void testGetCardAsString(){
        Assert.assertEquals( "1 6 10",player.getOwnedCardsAsString());
    }

    @Test
    public void testGetKnownCardAsString(){
       Assert.assertEquals( "4 2",player.getKnownCardsAsString());
    }
}

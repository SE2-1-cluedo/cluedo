package at.moritzmusel.cluedo.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static at.moritzmusel.cluedo.entities.Character.DR_ORCHID;
import static at.moritzmusel.cluedo.entities.Character.MISS_SCARLETT;
import static at.moritzmusel.cluedo.entities.Character.MRS_PEACOCK;
import static at.moritzmusel.cluedo.entities.Character.PROFESSOR_PLUM;
import static at.moritzmusel.cluedo.entities.Character.REVEREND_GREEN;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import at.moritzmusel.cluedo.entities.Player;

@RunWith(Parameterized.class)
public class GameplayParameterizedTests {
/*
    Gameplay game;

    //param 1: pos Player; param 2: goto Field
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {1, 7}, {7, 1}, {3, 7}, {7, 3}
        });
    }

    private final int secretInput;

    private final int secretExpected;

    public GameplayParameterizedTests(int secretInput, int secretExpected) {
        this.secretInput = secretInput;
        this.secretExpected = secretExpected;
    }
    //7-1; 1-7; 3-7; 7-3

    @Before
    public void setUp() {
        game = Gameplay.getInstance();
        Gameplay.setNumDice(3);
    }

    @Test
    public void useSecretPassageLoungeToBilliardRoom() {
        game.decidePlayerWhoMovesFirst();
        Player player = game.findPlayerByCharacterName(game.getCurrentPlayer());
        player.setPositionOnBoard(secretInput);
        game.useSecretPassage(secretExpected);
        assertEquals(secretExpected, player.getPositionOnBoard());
    }

    @Test
    public void currentPlayerIsAllowedToUseSecretPassage() {
        game.decidePlayerWhoMovesFirst();
        Player player = game.findPlayerByCharacterName(game.getCurrentPlayer());
        player.setPositionOnBoard(secretInput);
        assertTrue(game.isAllowedToUseSecretPassage());
    }*/
}

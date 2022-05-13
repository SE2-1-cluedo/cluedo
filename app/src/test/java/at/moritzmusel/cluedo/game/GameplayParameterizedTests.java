package at.moritzmusel.cluedo.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static at.moritzmusel.cluedo.entities.Character.MADAME_WHITE;
import static at.moritzmusel.cluedo.entities.Character.MISS_SCARLET;
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

    Player Player1;
    Player Player2;
    Player Player3;
    Player Player4;
    Player Player5;
    Gameplay game;
    Gameplay game2;
    ArrayList<Player> playersEven;
    ArrayList<Player> playersOdd;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {2, 7}, {7, 2}, {5, 9}, {9, 5}
        });
    }

    private final int secretInput;

    private final int secretExpected;

    public GameplayParameterizedTests(int secretInput, int secretExpected) {
        this.secretInput = secretInput;
        this.secretExpected = secretExpected;
    }


    @Before
    public void setUp() {
        Player1 = new Player(1, MISS_SCARLET);
        Player2 = new Player(2, REVEREND_GREEN);
        Player3 = new Player(3, PROFESSOR_PLUM);
        Player4 = new Player(4, MRS_PEACOCK);
        Player5 = new Player(5, MADAME_WHITE);
        playersEven = new ArrayList<>(Arrays.asList(Player1, Player2, Player3, Player4, Player5));
        playersOdd = new ArrayList<>(Arrays.asList(Player2,Player3, Player4, Player5));
        game = new Gameplay(playersEven);
        game2 = new Gameplay(playersOdd);
        Gameplay.setNumDice(3);
    }

    @Test
    public void useSecretPassageKitchenToWorkingRoom() {
        game.decidePlayerWhoMovesFirst();
        Player player = game.findPlayerByCharacterName(game.getCurrentPlayer());
        player.setPositionOnBoard(secretInput);
        game.useSecretPassage();
        assertEquals(secretExpected, player.getPositionOnBoard());
    }

    @Test
    public void currentPlayerIsAllowedToUseSecretPassage() {
        game.decidePlayerWhoMovesFirst();
        Player player = game.findPlayerByCharacterName(game.getCurrentPlayer());
        player.setPositionOnBoard(secretInput);
        assertTrue(game.isAllowedToUseSecretPassage());
    }
}

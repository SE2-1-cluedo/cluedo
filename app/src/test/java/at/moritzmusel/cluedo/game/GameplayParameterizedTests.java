package at.moritzmusel.cluedo.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static at.moritzmusel.cluedo.entities.Character.MISS_SCARLET;
import static at.moritzmusel.cluedo.entities.Character.MRS_PEACOCK;
import static at.moritzmusel.cluedo.entities.Character.MRS_WHITE;
import static at.moritzmusel.cluedo.entities.Character.PROFESSOR_PLUM;
import static at.moritzmusel.cluedo.entities.Character.THE_REVEREND_GREEN;

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
    ArrayList<Player> players;
    ArrayList<Player> players2;

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
        Player1 = new Player(1, MISS_SCARLET, new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 25)));
        Player2 = new Player(2, THE_REVEREND_GREEN, new ArrayList<>(Arrays.asList(6, 7, 8, 9)));
        Player3 = new Player(3, PROFESSOR_PLUM, new ArrayList<>(Arrays.asList(11, 12, 13, 14, 26)));
        Player4 = new Player(4, MRS_PEACOCK, new ArrayList<>(Arrays.asList(15, 16, 17, 18, 19)));
        Player5 = new Player(5, MRS_WHITE, new ArrayList<>(Arrays.asList(20, 21, 22, 23, 24)));
        players = new ArrayList<>(Arrays.asList(Player1, Player2, Player3, Player4, Player5));
        players2 = new ArrayList<>(Arrays.asList(Player3, Player4, Player5));
        game = new Gameplay(players);
        game2 = new Gameplay(players2);
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

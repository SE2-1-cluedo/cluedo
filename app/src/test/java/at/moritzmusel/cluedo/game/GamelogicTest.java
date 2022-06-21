package at.moritzmusel.cluedo.game;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static at.moritzmusel.cluedo.entities.Character.COLONEL_MUSTARD;
import static at.moritzmusel.cluedo.entities.Character.DR_ORCHID;
import static at.moritzmusel.cluedo.entities.Character.MISS_SCARLETT;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.robolectric.util.ReflectionHelpers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import at.moritzmusel.cluedo.communication.GameplayCommunicator;
import at.moritzmusel.cluedo.communication.NetworkCommunicator;
import at.moritzmusel.cluedo.entities.Player;
import at.moritzmusel.cluedo.network.pojo.GameState;


@RunWith(MockitoJUnitRunner.class)
public class GamelogicTest {
    String[] turnOrder = {"1","2","3"};

    Player player1 = new Player("1"),player2= new Player("2"),player3= new Player("3");
    ArrayList<Integer> cards1 = new ArrayList<>(),cards2 = new ArrayList<>(),cards3 = new ArrayList<>();
    ArrayList<Player> playersGameOdd = new ArrayList<>(3);

    @Mock
    private GameState gameState;
    @Mock
    private GameplayCommunicator gameCommunicator;
    @Mock
    private NetworkCommunicator netCommunicator;


    private Gameplay gameOdd;

    private GameState originalGameState;
    private GameplayCommunicator originalGameCommunicator;
    private NetworkCommunicator originalNetCommuicator;

    @Before
    public void setUp() throws Exception {
        cards1.add(3);cards1.add(7);cards1.add(17);
        cards2.add(5);cards2.add(9);cards2.add(19);
        cards3.add(10);cards3.add(4);cards3.add(15);

        player1.setPlayerCharacterName(MISS_SCARLETT);player1.setPositionOnBoard(1);player1.setPlayerOwnedCards(cards1);
        player2.setPlayerCharacterName(DR_ORCHID);player2.setPositionOnBoard(4);player2.setPlayerOwnedCards(cards2);
        player3.setPlayerCharacterName(COLONEL_MUSTARD);player3.setPositionOnBoard(7);player3.setPlayerOwnedCards(cards3);
        playersGameOdd.add(player1);playersGameOdd.add(player2);playersGameOdd.add(player3);

        originalGameState = GameState.getInstance();
        originalGameCommunicator = GameplayCommunicator.getInstance();
        originalNetCommuicator = NetworkCommunicator.getInstance();

        when(gameState.getPlayerTurn()).thenReturn("1");
        when(gameState.getPlayerState()).thenReturn(playersGameOdd);
        when(gameState.getTurnOrder()).thenReturn(turnOrder);
        when(gameState.getWeaponPositions()).thenReturn(new int[]{1,5,6,8,4,3});


        doNothing().when(gameState).setAskQuestion(any(),anyBoolean());
        doNothing().when(gameState).setPlayerState(anyList(),anyBoolean());
        doNothing().when(gameState).setPlayerTurn(anyString(),anyBoolean());
        doNothing().when(gameState).setTurnOrder(any());
        doNothing().when(gameCommunicator).setTurnChange(anyBoolean());
        doNothing().when(gameCommunicator).notifyList();

        // Now set the instance with your mockSingleton using reflection
        ReflectionHelpers.setStaticField(GameState.class, "OBJ", gameState);
        ReflectionHelpers.setStaticField(GameplayCommunicator.class, "OBJ", gameCommunicator);
        ReflectionHelpers.setStaticField(NetworkCommunicator.class, "OBJ", netCommunicator);

        gameOdd = Gameplay.getInstance();
    }

    /**
     * Remove the mocked instance from the class. It is important to clean up the class, because other tests will be confused with the mocked instance.
     * @throws Exception if the instance could not be accessible
     */
    @After
    public void resetSingleton() throws Exception {
        ReflectionHelpers.setStaticField(GameState.class, "OBJ", null);
        ReflectionHelpers.setStaticField(GameplayCommunicator.class, "OBJ", null);
        ReflectionHelpers.setStaticField(NetworkCommunicator.class, "OBJ", null);
        gameOdd = null;
    }


    @Test
    public void endTurnTest(){
        gameOdd.endTurn();
        Assert.assertEquals(DR_ORCHID,gameOdd.getCurrentPlayer());
    }
}
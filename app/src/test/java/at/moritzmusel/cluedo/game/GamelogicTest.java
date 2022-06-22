package at.moritzmusel.cluedo.game;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doNothing;
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
import org.mockito.junit.MockitoJUnitRunner;
import org.robolectric.util.ReflectionHelpers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    private GameplayCommunicator originalGameCommunicator;
    private NetworkCommunicator originalNetCommuicator;

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        cards1.add(3);cards1.add(7);cards1.add(17);
        cards2.add(5);cards2.add(9);cards2.add(19);
        cards3.add(10);cards3.add(4);cards3.add(15);

        player1.setPlayerCharacterName(MISS_SCARLETT);player1.setPositionOnBoard(1);player1.setPlayerOwnedCards(cards1);
        player2.setPlayerCharacterName(DR_ORCHID);player2.setPositionOnBoard(4);player2.setPlayerOwnedCards(cards2);
        player3.setPlayerCharacterName(COLONEL_MUSTARD);player3.setPositionOnBoard(7);player3.setPlayerOwnedCards(cards3);
        playersGameOdd.add(player1);playersGameOdd.add(player2);playersGameOdd.add(player3);
        Field instanceGameState;

        instanceGameState = GameState.class.getDeclaredField("OBJ");
        Objects.requireNonNull(instanceGameState).setAccessible(true);
        instanceGameState.set(null,null);

        originalGameCommunicator = GameplayCommunicator.getInstance();
        originalNetCommuicator = NetworkCommunicator.getInstance();

        when(gameState.getPlayerTurn()).thenReturn("1");
        when(gameState.getPlayerState()).thenReturn(playersGameOdd);
        when(gameState.getTurnOrder()).thenReturn(turnOrder);
        when(gameState.getWeaponPositions()).thenReturn(new int[]{1,5,6,8,4,3});
        when(gameState.getKiller()).thenReturn(new int[]{2,11,20});

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
        Field instance = Gameplay.class.getDeclaredField("OBJ");
        instance.setAccessible(true);
        instance.set(null, null);
    }


    @Test
    public void testEndTurn(){
        gameOdd.endTurn();
        Assert.assertEquals(DR_ORCHID,gameOdd.getCurrentPlayer());
    }

    @Test
    public void testGetPlayerForSuspectedCardsPlayerHasCard() {
        Assert.assertEquals(MISS_SCARLETT.name(),gameOdd.getPlayerForSuspectedCards(new int[]{3,1,5}));
    }

    @Test
    public void testGetPlayerForSuspectedCardsPlayerDoesNotHaveCard() {
        Assert.assertEquals("nobody",gameOdd.getPlayerForSuspectedCards(new int[]{1,2,11}));
    }

    @Test
    public void testUpdatePlayerPosition() {
        gameOdd.updatePlayerPosition(5);
        Assert.assertEquals(5,gameOdd.findPlayerByCharacterName(gameOdd.getCurrentPlayer()).getPositionOnBoard());
    }

    @Test
    public void testCanMove(){
        Gameplay.rollDiceForPlayer(1);
        gameOdd.setStepsTaken(1);
        gameOdd.canMove();
        Assert.assertFalse(gameOdd.findPlayerByCharacterName(gameOdd.getCurrentPlayer()).getIsAbleToMove());
    }

    @Test
    public void testAskPlayerAQuestion() {
        gameOdd.askPlayerAQuestion(new int[]{1,8,19});
        verify(gameState,times(1)).setAskQuestion(any(),anyBoolean());
    }

    @Test
    public void testNotifyDatabase() {
        gameOdd.notifyDatabase(new int[]{});
        Assert.assertArrayEquals(new int[]{},gameOdd.getWeaponsPos());
        verify(gameState,times(1)).setWeaponPositions(any(),anyBoolean());
    }

    @Test
    public void testAccusationTrue() {
        Assert.assertTrue(gameOdd.accusation(new int[]{2,11,20}));
    }

    @Test
    public void testAccusationFalse() {
        Assert.assertFalse(gameOdd.accusation(new int[]{2,10,20}));
    }

    @Test
    public void testIsAllowedToUseSecretPassage() {
        gameOdd.findPlayerByCharacterName(gameOdd.getCurrentPlayer()).setPositionOnBoard(1);
        Assert.assertTrue(gameOdd.isAllowedToUseSecretPassage());
    }

    @Test
    public void testIsNotAllowedToUseSecretPassage() {
        gameOdd.findPlayerByCharacterName(gameOdd.getCurrentPlayer()).setPositionOnBoard(2);
        Assert.assertFalse(gameOdd.isAllowedToUseSecretPassage());
    }


    @Test
    public void testGetPlayerToWhichCardBelongs() {
        Assert.assertEquals("MISS_SCARLETT",gameOdd.getPlayerToWhichCardBelongs(3));
    }

    @Test
    public void testGetPlayerToWhichCardNotBelongs() {
        Assert.assertEquals("nobody",gameOdd.getPlayerToWhichCardBelongs(1));
    }


    @Test
    public void testGetCharacterByPlayerID() {
        Assert.assertEquals(MISS_SCARLETT,gameOdd.getCharacterByPlayerID("1"));
    }

    @Test
    public void testCheckWhatChangedInPlayer() {
        List<Player> newPlayer = new ArrayList<>();
        newPlayer.add(new Player("1"));
        gameOdd.checkWhatChangedInPlayer(newPlayer);
        Assert.assertEquals(newPlayer,gameOdd.getPlayers());
        verify(gameCommunicator,times(1)).setMoved(anyBoolean());
        verify(gameCommunicator,times(1)).notifyList();
    }

    @Test
    public void testCheckWeaponChanged() {
        int[] pos = new int[]{1,7,8};
        gameOdd.checkWeaponChanged(pos);
        Assert.assertEquals(pos,gameOdd.getWeaponsPos());
        verify(gameCommunicator,times(1)).setMoved(anyBoolean());
        verify(gameCommunicator,times(1)).notifyList();

    }

    @Test
    public void testCheckTurnChanged() {
        gameOdd.checkTurnChanged("DR_ORCHID");
        verify(gameCommunicator,times(1)).setTurnChange(anyBoolean());
        verify(gameCommunicator,times(1)).notifyList();
    }

}
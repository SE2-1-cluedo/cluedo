package at.moritzmusel.cluedo.game;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.mockito.Mockito.*;

import static org.mockito.internal.util.MockUtil.createMock;
import static org.powermock.api.support.SuppressCode.suppressConstructor;
import static org.powermock.core.MockRepository.*;
import org.easymock.*;

import static at.moritzmusel.cluedo.entities.Character.COLONEL_MUSTARD;
import static at.moritzmusel.cluedo.entities.Character.DR_ORCHID;
import static at.moritzmusel.cluedo.entities.Character.MISS_SCARLETT;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

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

    Gameplay gameOdd;
    GameState gameState;
    Player player1 = new Player("1"),player2= new Player("2"),player3= new Player("3");
    ArrayList<Integer> cards1 = new ArrayList<>(),cards2 = new ArrayList<>(),cards3 = new ArrayList<>();
    ArrayList<Player> playersGameOdd = new ArrayList<>(3);

    @Before
    public void setUp() throws Exception {
        cards1.add(3);cards1.add(7);cards1.add(17);
        cards2.add(5);cards2.add(9);cards2.add(19);
        cards3.add(10);cards3.add(4);cards3.add(15);

        player1.setPlayerCharacterName(MISS_SCARLETT);player1.setPositionOnBoard(1);player1.setPlayerOwnedCards(cards1);
        player2.setPlayerCharacterName(DR_ORCHID);player2.setPositionOnBoard(4);player2.setPlayerOwnedCards(cards2);
        player3.setPlayerCharacterName(COLONEL_MUSTARD);player3.setPositionOnBoard(7);player3.setPlayerOwnedCards(cards3);
        playersGameOdd.add(player1);playersGameOdd.add(player2);playersGameOdd.add(player3);

    }

    @Test
    public void endTurnTest(){
        /*************mocking singleton******************/
        //Tell powermock to not to invoke constructor
        //import import static org.powermock.api.easymock.PowerMock.suppressConstructor;
        suppressConstructor(GameState.class);
        suppressConstructor(Gameplay.class);
        //mock static
        mockStatic(GameState.class);
        mockStatic(Gameplay.class);
        //create mock for Singleton
        GameState mockGameState = createMock(GameState.class);
        Gameplay mockGameplay = createMock(Gameplay.class);
        //set expectation for getInstance()
        expect(mockGameState.getInstance()).andReturn(mockGameState).anyTimes();
        expect(mockGameplay.getInstance()).andReturn(mockGameplay).anyTimes();
        //set expectation for getHostName()
        expect(mockGameplay.get);
        replay(Singleton.class);
        replay(mockSingleton);
        /*******************************/
        //create mock for data access
        DaoService mockService = createMock(DaoService.class);
        try {
            expect(
                    mockService.getHostLocationFromDB((String) EasyMock
                            .anyObject())).andReturn(new ArrayList<string>());
            ClassUsingSingleton classUsingSingleton = new ClassUsingSingleton(
                    mockService);
            replay(mockService);
            classUsingSingleton.getHostLocationFromDB();
        } catch (Exception e) {
            Assert.fail("Unexpected Exception");
            e.printStackTrace();
        }
    }
}
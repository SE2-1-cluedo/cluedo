package at.moritzmusel.cluedo;

import static org.junit.Assert.assertEquals;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import at.moritzmusel.cluedo.network.Network;
@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NetworkingTest {
    Network network;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Before
    public void init() {
        //FirebaseApp.initializeApp(this);
        network = new Network();
    }

    @Test
    public void a_lobby_creation(){
        //network.createLobby(user);
        //assertEquals(1, 1);
    }

    @Test
    public void b_add_player_cards(){
        //network.givePlayerCards("user", new int[]{1, 2, 3}, "gameid");
        //assertEquals(/*Network call to values here*/1, new int[]{1, 2, 3});
    }
    @Test
    public void c_add_player_elimination_cards(){
        //network.addPlayerEliminationCard(user, 2, "gameid");
        //assertEquals(/*Network call to values here*/1, 2);
    }
}

package at.moritzmusel.cluedo;

import static org.junit.Assert.assertEquals;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Test;

import at.moritzmusel.cluedo.network.Network;

public class Networking {
    Network network;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user;

    @Before
    public void init() {
        network = new Network();
        user = mAuth.getCurrentUser();
    }

    @Test
    public void lobby_creation(){
        network.createLobby(user);
        assertEquals(1, 1);
    }

    @Test
    public void add_player_cards(){
        network.givePlayerCards(user, new int[]{1, 2, 3}, "gameid");
        assertEquals(/*Network call to values here*/1, new int[]{1, 2, 3});
    }
    @Test
    public void add_player_elimination_cards(){
        network.addPlayerEliminationCard(user, 2, "gameid");
        assertEquals(/*Network call to values here*/1, 2);
    }
}

package at.moritzmusel.cluedo;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import at.moritzmusel.cluedo.network.Network;
@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NetworkingTest {
    @Mock
    FirebaseDatabase fMock;
    @Mock
    DatabaseReference drefMock;
    @Mock
    FirebaseAuth authMock;
    @Mock
    FirebaseUser userMock;
    @Mock
    Task<AuthResult> taskresultMock;

    Network n;

    @Before
    public void init(){
        when(authMock.signInAnonymously()).thenReturn(taskresultMock);
        fMock = FirebaseDatabase.getInstance();
    }

    @Test
    public void a_signInAnonymously(){
        Network.signInAnonymously(authMock);
        verify(authMock).signInAnonymously();
    }

    @Test
    public void b_join_lobby() {
        //network.givePlayerCards("user", new int[]{1, 2, 3}, "gameid");
        assertEquals(/*Network call to values here*/1, 1);
    }

    @Test
    public void c_leave_lobby() {
        //Network.updatePlayerEliminationCard(user, 2, "gameid");
        assertEquals(/*Network call to values here*/1, 2);
    }
}

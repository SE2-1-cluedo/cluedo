package at.moritzmusel.cluedo;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void EvidenceCardsTest(){
        EvidenceCards ec = new EvidenceCards();
        for(int i=0;i<ec.getCards().size();i++){
            System.out.println(ec.getCards().get(i).toString());
        }
        System.out.println(ec.getCard().getDesignation());
        for(int i=0;i<ec.getCards().size();i++){
            System.out.println(ec.getCards().get(i).toString());
        }
        System.out.println(ec.getCard().getDesignation());
    }
}
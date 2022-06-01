package at.moritzmusel.cluedo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public class EvidenceCards {
    private LinkedList cards;

    public EvidenceCards() {
        shuffle();
        this.cards = getCards();
    }

    public LinkedList getCards(){
        AllTheCards cards = new AllTheCards();
        return cards.getGameCards();
    }

    public void shuffle(){
        Collections.shuffle(cards);
    }
}

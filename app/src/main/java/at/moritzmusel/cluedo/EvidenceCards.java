package at.moritzmusel.cluedo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public class EvidenceCards {
    private LinkedList cards;

    public EvidenceCards() {
        this.cards = getGameCards();
        shuffle(getCards());
    }

    private LinkedList getGameCards(){
        AllTheCards cards = new AllTheCards();
        return cards.getGameCards();
    }

    public LinkedList getCards(){
        return cards;
    }

    public void setCards(LinkedList cards){
        this.cards = cards;
    }

    public void shuffle(LinkedList cards){
        Collections.shuffle(cards);
    }

    public Card getCard(){
        Card evidence_card = (Card) getCards().getFirst();
        setCards(moveCardToEnd(getCards()));
        return evidence_card;
    }

    public static LinkedList<Card> moveCardToEnd(LinkedList<Card> data) {
        /*int i = 0;
        int j = array.size()-1;

        while(i < j){
            while( i < j && array.get(j) == toMove)
                j--;
            if(array.get(i) == toMove)
                swap(i,j, array);
            i++;
        }
        return array;
        for(int j=0;j<=data.size()+1;j++){
            swap(data,j,j-1);
        }
        return data;
        */
        Card new_last_card = data.get(0);
        data.addLast(new_last_card);
        data.remove(0);
        return data;
    }

    public String getCardName(){
        return getCard().getDesignation();
    }

    public int getCardId(){
        return getCard().getId();
    }



}

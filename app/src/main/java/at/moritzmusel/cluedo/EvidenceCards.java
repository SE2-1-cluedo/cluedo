package at.moritzmusel.cluedo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public class EvidenceCards {
    private LinkedList<Card> cards;

    /**
     * When created calls getGameCards and shuffles the list randomly.
     */
    public EvidenceCards() {
        this.cards = getGameCards();
        shuffle(getCards());
    }

    /**
     * Copies the hardcoded game cards and reuses them.
     */
    private LinkedList<Card> getGameCards(){
        AllTheCards cards = new AllTheCards();
        return cards.getGameCards();
    }

    /**
     * Getter and Setter
     */
    public LinkedList<Card> getCards(){
        return cards;
    }

    public void setCards(LinkedList<Card> cards){
        this.cards = cards;
    }

    public void shuffle(LinkedList<Card> cards){
        Collections.shuffle(cards);
    }

    /**
     * Gives a card back and moves it to the bottom of the list.
     */
    public Card getDrawnCard(){
        Card evidence_card = getCards().getFirst();
        setCards(moveCardToEnd(getCards()));
        return evidence_card;
    }

    public Card getCard(){
        return getCards().getFirst();
    }

    /**
     * Moves the first object to the end.
     * @param data List with cards
     * @return Newly rearranged list of cards.
     */
    public static LinkedList<Card> moveCardToEnd(LinkedList<Card> data) {
        Card new_last_card = data.get(0);
        data.addLast(new_last_card);
        data.remove(0);
        return data;
    }

    public String getCardName(){
        return getCard().getDesignation();
    }

    /**
     * Gives player back who has the card.
     */
    public String getPlayer(){
        String player = "Nobody";
        //Hier if mit Netzwerk einfügen um zu überprüfen, wer die Karte in der Hand hält.
        return player;
    }

    /**
     * Get the CardID
     * @return id
     */
    public int getCardId(){
        return getCard().getId();
    }



}
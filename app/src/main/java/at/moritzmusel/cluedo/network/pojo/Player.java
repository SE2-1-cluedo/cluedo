package at.moritzmusel.cluedo.network.pojo;

import java.util.List;

public class Player {
    private List<Card> cards;
    private List<Card> eliminationCards;
    private int cRoom = -1;

    public Player(List<Card> cards, List<Card> eliminationCards) {
        this.cards = cards;
        this.eliminationCards = eliminationCards;
    }
    public int getcRoom() {
        return cRoom;
    }

    public void setcRoom(int cRoom) {
        this.cRoom = cRoom;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public void addCardToCards(Card card){
        cards.add(card);
    }

    public List<Card> getEliminationCards() {
        return eliminationCards;
    }


    public void setEliminationCards(List<Card> eliminationCards) {
        this.eliminationCards = eliminationCards;
    }

    public void addEliminationCardToEliminationCards(Card card){
        eliminationCards.add(card);
    }
    public String getCardsAsString(){
        StringBuilder res = new StringBuilder();
        for (Card c : cards)res.append(c.getCardID()).append(" ");
        return res.length()<2?"":res.substring(0, res.length()-1);
    }
    public String getEliminatedCardsAsString(){
        StringBuilder res = new StringBuilder();
        for (Card c : eliminationCards)res.append(c.getCardID()).append(" ");
        return res.length()<2?"":res.substring(0, res.length()-1);
    }

    @Override
    public String toString() {
        return "Player{" +
                "cards=" + cards +
                ", eliminationCards=" + eliminationCards +
                '}';
    }
}

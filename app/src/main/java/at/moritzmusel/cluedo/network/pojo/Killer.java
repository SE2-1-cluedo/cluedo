package at.moritzmusel.cluedo.network.pojo;

import java.util.List;

public class Killer {
    private List<Card> cards;
    public Killer(List<Card> cards){
        this.cards = cards;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    @Override
    public String toString() {
        return "Killer{" +
                "cards=" + cards +
                '}';
    }
}

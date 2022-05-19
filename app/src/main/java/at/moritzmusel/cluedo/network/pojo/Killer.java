package at.moritzmusel.cluedo.network.pojo;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

public class Killer {
    private boolean validCards;
    private List<Card> cards;

    public Killer(List<Card> cards) throws IllegalArgumentException {
        if (cards.size() != 3)
            throw new IllegalArgumentException("Please make sure that the Killer Object only consists of 3 elements in the Card-List (person, weapon, room)!");
        else
            this.cards = cards;
        validCards = true;
    }

    public List<Card> getCards() {
        return cards;
    }

    public String getCardsAsString() {
        return validCards ? (cards.get(0) + ", " + cards.get(1) + ", " + cards.get(2)) : null;
    }

    @NonNull
    @Override
    public String toString() {
        return validCards ? ("Killer{person=" + cards.get(0) + ", weapon=" + cards.get(1) + ", room=" + cards.get(2) + "}") : "";
    }
}

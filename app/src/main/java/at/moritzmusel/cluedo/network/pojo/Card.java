package at.moritzmusel.cluedo.network.pojo;

import androidx.annotation.NonNull;

public class Card {
    //rooms: 13-21
    //characters: 1-6
    //weapons: 7-12

    protected int cardID;
    private String description;

    public Card(int cardID) {
        this.cardID = cardID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCardID() {
        return cardID;
    }

    public void setCardID(int cardID) {
        this.cardID = cardID;
    }

    @NonNull
    @Override
    public String toString() {
        return "Card{" +
                "cardID=" + cardID +
                ", description='" + description + '\'' +
                '}';
    }
}

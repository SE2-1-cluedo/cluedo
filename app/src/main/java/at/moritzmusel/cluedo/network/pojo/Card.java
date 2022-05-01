package at.moritzmusel.cluedo.network.pojo;

public class Card {
    private int cardID;
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

    @Override
    public String toString() {
        return "Card{" +
                "cardID=" + cardID +
                ", description='" + description + '\'' +
                '}';
    }
}

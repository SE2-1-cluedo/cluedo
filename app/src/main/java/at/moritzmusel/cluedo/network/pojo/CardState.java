package at.moritzmusel.cluedo.network.pojo;

public class CardState extends Card {
    //rooms: 13-21
    //characters: 1-6
    //weapons: 7-12
    private int position;

    public CardState(int cardID) {
        super(cardID);
    }

    public void setPosition(int position) {
        if (this.cardID >= 1 && this.cardID <= 12) this.position = position;
    }
}

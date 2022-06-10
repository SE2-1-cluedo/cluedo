package at.moritzmusel.cluedo.network.pojo;

public class CardState extends Card {
    //characters: 1-6
    //weapons: 7-12
    //rooms: 13-21
    private int position;

    public CardState(int cardID) {
        super(cardID);
    }

    public void setPosition(int position) {
        if (this.cardID >= 1 && this.cardID <= 9) this.position = position;
    }
    public int getPosition() {
        return position;
    }
}

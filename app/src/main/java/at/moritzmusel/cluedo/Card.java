package at.moritzmusel.cluedo;

public class Card{

    private int id;
    private String designation;
    private CardType type;

    private Card() {

    }

    /**
     * Card with id, designation and type
     * @param id identifies every single card
     * @param designation the name of the card
     * @param type of the card
     */
    public Card(int id, String designation, CardType type) {
        this.id = id;
        this.designation = designation;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getDesignation() {
        return designation;
    }

    public CardType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", designation='" + designation + '\'' +
                ", type=" + type +
                '}';
    }
}

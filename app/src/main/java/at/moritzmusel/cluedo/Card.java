package at.moritzmusel.cluedo;

public class Card{

    private int id;
    private String designation;
    private CardType type;

    private Card() {

    }

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

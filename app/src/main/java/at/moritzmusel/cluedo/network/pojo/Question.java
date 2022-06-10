package at.moritzmusel.cluedo.network.pojo;

//This class is used to ask a question to a user
//Further: Will be used in gamestate
public class Question {
    private String askPerson;
    private int[] cards;

    public Question(String askPerson, int[] cards) {
        if (cards != null && cards.length !=3)
            throw new IllegalArgumentException("Make sure the question cards array is of size 3!");
        else {
            this.askPerson = askPerson;
            this.cards = cards;
        }
    }

    public String getAskPerson() {
        return askPerson;
    }

    public void setAskPerson(String askPerson) {
        this.askPerson = askPerson;
    }

    public int[] getCards() {
        return cards;
    }

    public void setCards(int[] cards) {
        if (cards.length != 3)
            throw new IllegalArgumentException("Make sure the question cards array is of size 3!");
        else this.cards = cards;
    }
}

package at.moritzmusel.cluedo;

import java.util.LinkedList;

import at.moritzmusel.cluedo.Card;

public class AllTheCards {

    private LinkedList<Card> gamecards = new LinkedList<>();

    public Card missScarlett = new Card(0, "Miss Scarlett", CardType.PERSON);
    public Card profPlum = new Card(1, "Professor Plum", CardType.PERSON);
    public Card reverendGreen = new Card(2, "Reverend Green", CardType.PERSON);
    public Card mrsPeacock = new Card(3, "Mrs Peacock", CardType.PERSON);
    public Card colonelMustard = new Card(4, "Colonel Mustard", CardType.PERSON);
    public Card drOrchid = new Card(5, "Dr. Orchid", CardType.PERSON);


}

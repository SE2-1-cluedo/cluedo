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

    public Card dagger = new Card(6, "Dagger", CardType.WEAPON);
    public Card candlestick = new Card(7, "Candlestick", CardType.WEAPON);
    public Card pistol = new Card(8, "Pistol", CardType.WEAPON);
    public Card rope = new Card(9, "Rope", CardType.WEAPON);
    public  Card pipe = new Card(10, "Pipe", CardType.WEAPON);
    public Card wrench = new Card(11, "Wrench", CardType.WEAPON);


}

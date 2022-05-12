package at.moritzmusel.cluedo;

import java.util.LinkedList;

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
    public Card pipe = new Card(10, "Pipe", CardType.WEAPON);
    public Card wrench = new Card(11, "Wrench", CardType.WEAPON);

    public Card hall = new Card(12, "Hall", CardType.ROOM);
    public Card lounge = new Card(13, "Lounge", CardType.ROOM);
    public Card dining_room = new Card(14, "Dining Room", CardType.ROOM);
    public Card kitchen = new Card(15, "Kitchen", CardType.ROOM);
    public Card ballroom = new Card(16, "Ballroom", CardType.ROOM);
    public Card conservatory = new Card(17, "Conservatory", CardType.ROOM);
    public Card billiard_room = new Card(18, "Billiard Room", CardType.ROOM);
    public Card library = new Card(19, "Library", CardType.ROOM);
    public Card study = new Card(20, "Study", CardType.ROOM);

    public AllTheCards(){
        //GameFigures
        gamecards.add(missScarlett);
        gamecards.add(profPlum);
        gamecards.add(reverendGreen);
        gamecards.add(mrsPeacock);
        gamecards.add(colonelMustard);
        gamecards.add(drOrchid);

        //Weapons
        gamecards.add(dagger);
        gamecards.add(candlestick);
        gamecards.add(pistol);
        gamecards.add(rope);
        gamecards.add(pipe);
        gamecards.add(wrench);

        //Rooms
        gamecards.add(hall);
        gamecards.add(lounge);
        gamecards.add(dining_room);
        gamecards.add(kitchen);
        gamecards.add(ballroom);
        gamecards.add(conservatory);
        gamecards.add(billiard_room);
        gamecards.add(library);
        gamecards.add(study);
    }
    public LinkedList<Card> getGameCards(){
        return gamecards;
    }



}

package at.moritzmusel.cluedo;

import java.util.LinkedList;

public class AllTheCards {

    private final LinkedList<Card> gameCards = new LinkedList<>();

    public Card missScarlett = new Card(0, "Miss Scarlett", CardType.PERSON);
    public Card profPlum = new Card(1, "Professor Plum", CardType.PERSON);
    public Card reverendGreen = new Card(2, "Reverend Green", CardType.PERSON);
    public Card mrsPeacock = new Card(3, "Mrs Peacock", CardType.PERSON);
    public Card colonelMustard = new Card(4, "Colonel Mustard", CardType.PERSON);
    public Card drOrchid = new Card(5, "Dr Orchid", CardType.PERSON);

    public Card dagger = new Card(6, "Dagger", CardType.WEAPON);
    public Card candlestick = new Card(7, "Candlestick", CardType.WEAPON);
    public Card revolver = new Card(8, "Revolver", CardType.WEAPON);
    public Card rope = new Card(9, "Rope", CardType.WEAPON);
    public Card pipe = new Card(10, "Pipe", CardType.WEAPON);
    public Card wrench = new Card(11, "Wrench", CardType.WEAPON);

    public Card hall = new Card(12, "Lounge", CardType.ROOM);
    public Card lounge = new Card(13, "Conservatory", CardType.ROOM);
    public Card dining_room = new Card(14, "Ball Room", CardType.ROOM);
    public Card kitchen = new Card(15, "Dining Room", CardType.ROOM);
    public Card ballroom = new Card(16, "Kitchen", CardType.ROOM);
    public Card conservatory = new Card(17, "Library", CardType.ROOM);
    public Card billiard_room = new Card(18, "Billiard Room", CardType.ROOM);
    public Card library = new Card(19, "Study", CardType.ROOM);
    public Card study = new Card(20, "Hall", CardType.ROOM);

    /**
     * adds all the cards for the game in a list
     */
    public AllTheCards(){
        //GameFigures
        gameCards.add(missScarlett);
        gameCards.add(profPlum);
        gameCards.add(reverendGreen);
        gameCards.add(mrsPeacock);
        gameCards.add(colonelMustard);
        gameCards.add(drOrchid);

        //Weapons
        gameCards.add(dagger);
        gameCards.add(candlestick);
        gameCards.add(revolver);
        gameCards.add(rope);
        gameCards.add(pipe);
        gameCards.add(wrench);

        //Rooms
        gameCards.add(hall);
        gameCards.add(lounge);
        gameCards.add(dining_room);
        gameCards.add(kitchen);
        gameCards.add(ballroom);
        gameCards.add(conservatory);
        gameCards.add(billiard_room);
        gameCards.add(library);
        gameCards.add(study);
    }

    /**
     * Getter for the cards
     * @return list with cards
     */
    public LinkedList<Card> getGameCards(){
        return gameCards;
    }

    public int findIdWithName(String name){
        for(Card c: gameCards)
            if(c.getDesignation().equals(name))
                return c.getId();
            return -1;
    }

    public Card findCardWithId(int id){
        for(Card c: gameCards)
            if(c.getId() == id)
                return c;
            return null;
    }

}

package at.moritzmusel.cluedo.network.pojo;

import androidx.annotation.NonNull;

import java.util.List;

public class Player {
    private String playerID;
    private List<Card> cards;
    private List<Card> eliminationCards;
    //rooms: 13-21
    private int cRoom = -1;
    //characters: 1-6
    private int character = -1;
    //weapons: 7-12

    public Player(List<Card> cards, List<Card> eliminationCards) {
        this.cards = cards;
        this.eliminationCards = eliminationCards;
    }

    public int getcRoom() {
        return cRoom;
    }

    public void setcRoom(int cRoom) {
        this.cRoom = cRoom;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public void addCardToCards(Card card) {
        cards.add(card);
    }

    public List<Card> getEliminationCards() {
        return eliminationCards;
    }

    public int getCharacter() {
        return character;
    }

    public void setCharacter(int character) {
        this.character = character;
    }

    public void setEliminationCards(List<Card> eliminationCards) {
        this.eliminationCards = eliminationCards;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public void addEliminationCardToEliminationCards(Card card) {
        eliminationCards.add(card);
    }

    public String getCardsAsString() {
        StringBuilder res = new StringBuilder();
        for (Card c : cards) res.append(c.getCardID()).append(" ");
        return res.length() < 2 ? "" : res.substring(0, res.length() - 1);
    }

    public String getEliminatedCardsAsString() {
        StringBuilder res = new StringBuilder();
        for (Card c : eliminationCards) res.append(c.getCardID()).append(" ");
        return res.length() < 2 ? "" : res.substring(0, res.length() - 1);
    }

    @NonNull
    @Override
    public String toString() {
        return "Player{" +
                "cards=" + cards +
                ", eliminationCards=" + eliminationCards +
                '}';
    }
}

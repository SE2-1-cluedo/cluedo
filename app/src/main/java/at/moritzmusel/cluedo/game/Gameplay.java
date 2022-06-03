package at.moritzmusel.cluedo.game;

import static at.moritzmusel.cluedo.entities.Character.DR_ORCHID;
import static at.moritzmusel.cluedo.entities.Character.MISS_SCARLETT;
import static at.moritzmusel.cluedo.entities.Character.MRS_PEACOCK;
import static at.moritzmusel.cluedo.entities.Character.PROFESSOR_PLUM;
import static at.moritzmusel.cluedo.entities.Character.REVEREND_GREEN;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import at.moritzmusel.cluedo.Card;
import at.moritzmusel.cluedo.entities.Character;
import at.moritzmusel.cluedo.entities.Player;
import at.moritzmusel.cluedo.network.pojo.Killer;

public class Gameplay {
    private static int numDice;
    private static int stepsTaken = 0;
    private Character currentPlayer;
    private List<Player> players = new ArrayList<>();
    private ArrayList<Integer> clueCards = new ArrayList<>();
    private final SecureRandom rand = new SecureRandom();
    private int cardDrawn;
    private Killer killer;

    private static final Gameplay OBJ = new Gameplay();

    private Gameplay() {
        players.add(new Player(1,MISS_SCARLETT));
        players.add(new Player(2, REVEREND_GREEN));
        players.add(new Player(3, PROFESSOR_PLUM));
        players.add(new Player(4, MRS_PEACOCK));
        players.add(new Player(5, DR_ORCHID));

        //players = Network.getPlayers();
    }

    public static Gameplay getInstance(){
        return OBJ;
    }

    /**
     * Called after the Player ends his/her turn
     */
    public Character endTurn() {
        decideCharacterWhoMovesNext();
        return currentPlayer;
    }

    /**
     * Calculates the position of the Player after the dice was thrown depending on if
     * he moved right or left
     * dice was thrown and
     *
     * @param position new position of player
     */

    //Todo: Fragen wie ich die position übergeben bekomm
    public void updatePlayerPosition(int position) {
        Player player = findPlayerByCharacterName(currentPlayer);
        player.setPositionOnBoard(position);
    }

    /**
     * Is called in move-Methode Board Activity and checks if Player can still move
     */
    public void canMove(){
        stepsTaken++;
        if(stepsTaken == numDice)
            findPlayerByCharacterName(currentPlayer).setIsAbleToMove(false);
    }

    /**
     * method to use the secret passage if the player is on the field 9 , 5, 7 or 2
     * cant throw dice after the use of the passage
     */
    public void useSecretPassage(int newPosition) {
        Player player = findPlayerByCharacterName(currentPlayer);
        int position = player.getPositionOnBoard();
        player.setIsAbleToMove(true);
        if (player.getIsAbleToMove()) {
            switch (newPosition) {
                case 7:
                    player.setPositionOnBoard(7); //move from salon to winter garden
                    break;
                case 1:
                    player.setPositionOnBoard(1); //move from winter garden to salon
                    break;
                case 3:
                    player.setPositionOnBoard(3); //move from winter garden to salon
                    break;
            }
        }
    }

    /**
     * Called when a player left the game without finishing it
     * and sending the cards of said player to the other active players
     * @param player player who quit game
     */
    //Noch mit frontend und backend über das schicken und verteilen reden
    public void quitGame(Player player){
        List<Integer> cards = new ArrayList<>();
        for (int i = 0; i <players.size();i++){
            if(players.get(i).equals(player)){
                cards = players.get(i).getPlayerOwnedCards();
            }
        }
        players.remove(player);
        distributeCardsEquallyToPlayers(cards);
        //send all cards to other players
    }

    /**
     * Method to check if one of the players in the correct order has a card
     * which is equal to one of the cards send. If yes the person who asked the question
     * get the card added to their inventory. And update to the other players
     * @param player Player how asked the question
     * @param weapon the used weapon
     * @param person the person who did it
     * @param room the room
     */
    public void askPlayerAQuestion(Player player, Card person,Card weapon, Card room){
        boolean cardSend = false;
        int counter = 0;
        int i = 0;

        for(; i < players.size();i++){
            if(players.get(i).equals(player)){
                break;
            }
        }
        while (counter < players.size()) {
            if(i >= players.size()){
                i = 0;
            }

            Player current = checkWhoIsNextPlaying(players.get(i));
            System.out.println("Player: "+ current.getPlayerCharacterName());
            if (current.getPlayerOwnedCards().contains(weapon.getId())) {
                player.addCardsKnownThroughQuestions(weapon.getId());
                cardSend = true;
                break;
            } else if (current.getPlayerOwnedCards().contains(person.getId())) {
                player.addCardsKnownThroughQuestions(person.getId());
                cardSend = true;
                break;
            } else if (current.getPlayerOwnedCards().contains(room.getId())) {
                player.addCardsKnownThroughQuestions(room.getId());
                cardSend = true;
                break;
            }
            i++;
            counter++;
        }
        if(!cardSend){
            //Todo: sende nichts und hinweis an players
            System.out.println("Nichts gefunden");
        }
        //send updated cards to other players and which player showed the card to whom
    }

    /**
     * A method to check which player plays next
     * @param player which player
     * @return player whos turn is next
     */
    private Player checkWhoIsNextPlaying(Player player){
        Character character = player.getPlayerCharacterName().getNextCharacter();
        while(true){
            character = character.getNextCharacter();
            Player currentPlayer = findPlayerByCharacterName(character);
            if (currentPlayer != null) {
                return currentPlayer;
            }
        }
    }
    /**
     * Checks if the current Player is allowed to use the Secret Passage
     * @return true if allowed / false if isnt allowed
     */
    public boolean isAllowedToUseSecretPassage() {
        Player player = findPlayerByCharacterName(currentPlayer);
        int position = player.getPositionOnBoard();
        return position == 1 || position == 7 || position == 3;
    }

    /**
     * Takes the result after the Player throw the dice and safes it in a variable
     *
     * @param numberRolled Takes the result after the dice throw
     */
    public static void rollDiceForPlayer(int numberRolled) {
        numDice = numberRolled;
    }

    /**
     * Decides which Player/Character is able to move first
     */
    public void decidePlayerWhoMovesFirst() {
        currentPlayer = Character.MISS_SCARLETT;
        while (true) {
            Player firstPlayer = findPlayerByCharacterName(currentPlayer);
            if (firstPlayer == null) {
                currentPlayer = currentPlayer.getNextCharacter();
            } else {
                currentPlayer = firstPlayer.getPlayerCharacterName();
                findPlayerByCharacterName(currentPlayer).setIsAbleToMove(true);
                break;
            }
        }

    }

    /**
     * which character moves next
     */
    private void decideCharacterWhoMovesNext() {
        while (true) {
            assert currentPlayer != null;
            currentPlayer = currentPlayer.getNextCharacter();
            Player player = findPlayerByCharacterName(currentPlayer);
            if (player != null) {
                currentPlayer = player.getPlayerCharacterName();
                findPlayerByCharacterName(currentPlayer).setIsAbleToMove(true);
                break;
            }
        }
    }


    public void distributeCluedoCards(){
        //if player is host
        generateCluedoCards();
        //send die generierten Cluedo Cards zu den Spielern

        //else nicht host
        //update player Cluedo Cards
    }

    /**
     * Draw a Random Card from the Clue Cards staple
     * and delete it from the staple
     */
    public void drawClueCard(){
        cardDrawn = getRandomIntInRange(22,51);
        if(clueCards.size() == 1){
            cardDrawn = clueCards.get(0);
            //no Cards left
        }else{
            while(true){
                if(clueCards.contains(cardDrawn)){
                    break;
                }else if(cardDrawn < 51){
                    cardDrawn++;
                }else{
                    cardDrawn = 0;
                }
            }
        }
        //send cardDrawn to UI and to other Players
        clueCards.remove((Integer) cardDrawn);
        //send rest of Cluedo Cards to Players
    }

    public void generateClueCards(){
        //if host send to other players
        clueCards = generateRandomCards(22,51);
    }

    /**
     * randomized the Cluedo Cards and safes them in the players card list
     */
    void generateCluedoCards(){
        ArrayList<Integer> cards = generateRandomCards(1,21);
        List<Integer> playerCards = new ArrayList<>();
        List<Integer> killerCards = new ArrayList<>();
        killerCards.add(killer.getCards().get(0).getCardID());
        killerCards.add(killer.getCards().get(1).getCardID());
        killerCards.add(killer.getCards().get(2).getCardID());

        for(int i = 0; i < cards.size(); i++){
            if(!killerCards.contains(cards.get(i))){
                playerCards.add(cards.get(i));
            }
        }

        int j = 0;
        for(int i = 0; i < playerCards.size();i++){
            if(players.size() == j){
                j = 0;
            }
            players.get(j).setPlayerCard(playerCards.get(i));
            j++;
        }
    }

    /**
     * Fill a List with numbers from min to max and then randomize it through Collection.shuffle
     * which randomly permutes elements in a given list.
     * @param min
     * smallest Card in deck
     * @param max
     * biggest Card in deck
     * @return
     * a sorted integer List with the numbers min to max
     */
    private ArrayList<Integer> generateRandomCards(int min, int max){
        ArrayList<Integer> cards = new ArrayList<>();
        for(int i = min;i <= max;i++){
            cards.add(i);
        }
        Collections.shuffle(cards);
        return cards;
    }

    /**
     * Method to generate a Card from Typ Pojo
     * @param min minimum value incl.
     * @param max max. value incl.
     * @return
     * Card typ Pojo Card
     */
    public at.moritzmusel.cluedo.network.pojo.Card generateRandomKillerCard(int min,int max){
        return new at.moritzmusel.cluedo.network.pojo.Card(rand.nextInt(max + 1 - min) + min);
    }

    /**
     * @param character Find the Character belonging to a player if not return null
     * @return Player who playing as the character
     */
    public Player findPlayerByCharacterName(Character character) {
        int countCharacters = 1;
        while (true) {
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getPlayerCharacterName() == character) {
                    return players.get(i);
                } else if (countCharacters > 6) {
                    return null;
                }
            }
            countCharacters++;
        }
    }


    //characters: 1-6
    //weapons: 7-12
    //rooms: 13-21

    /**
     * Create New Game with connection to Database
     */
    public void createNewGame(){
        List<at.moritzmusel.cluedo.network.pojo.Card> killerCards = new ArrayList<>();
        killerCards.add(generateRandomKillerCard(1,6));
        killerCards.add(generateRandomKillerCard(7,12));
        killerCards.add(generateRandomKillerCard(13,21));
        killer = new Killer(killerCards);
        distributeCluedoCards();
        generatePlayers();
        //Network.startGame(Network.getCurrentGameID(),generatePlayers() ,killer);
    }

    /**
     * Method to generate a List of players who are playing
     * @return List of players
     */
    private List<at.moritzmusel.cluedo.network.pojo.Player> generatePlayers(){
        List<at.moritzmusel.cluedo.network.pojo.Player> playerArray = new ArrayList<>();
        for(int i = 0; i < players.size();i++){
            at.moritzmusel.cluedo.network.pojo.Player player =
                    new at.moritzmusel.cluedo.network.pojo.Player(convertIntegerToCards(players.get(i).getPlayerOwnedCards()));
            playerArray.add(player);
        }
        return playerArray;
    }


    /**
     * Convert a Integer List to a List of the Type Card
     * @param integers integers to convert
     * @return card List
     */
    private List<at.moritzmusel.cluedo.network.pojo.Card> convertIntegerToCards(List<Integer> integers){
        List<at.moritzmusel.cluedo.network.pojo.Card> cards = new ArrayList<>();
        for(int i = 0; i < integers.size(); i++){
            at.moritzmusel.cluedo.network.pojo.Card card = new at.moritzmusel.cluedo.network.pojo.Card(integers.get(i));
            cards.add(card);
        }
        return cards;
    }

    /**
     * @param position  the current position of the player on board
     * @param direction if the player wants to move right(1) or left(0)
     * @param diceNum   the number thrown by the player
     * @return final position that the position is a valid field on the board
     */
    private int calculatePosition(int position, byte direction, int diceNum) {
        int finalPosition;
        if (direction == 1) {
            finalPosition = position + diceNum <= 9 ? diceNum + position : (diceNum + position) - 9;
        } else {
            finalPosition = position - diceNum > 0 ? position - diceNum : (position - diceNum) + 9;
        }
        return finalPosition;
    }

    private int getRandomIntInRange(int min,int max) {
        int range = max - min + 1;
        return min + rand.nextInt(range);
    }

    private void distributeCardsEquallyToPlayers(List<Integer> cards){
        int i = 0;
        int j = 0;
        while(i < cards.size()){
            if(j >= players.size()){
                j = 0;
            }
            players.get(j).getPlayerOwnedCards().add(cards.get(i));
            i++;
            j++;
        }
    }

    public static void setNumDice(int numDice) {
        Gameplay.numDice = numDice;
    }

    public void setStepsTaken(int steps){
        stepsTaken = steps;
    }

    public Character getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Character currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public List<Integer> getClueCards() {
        return clueCards;
    }

    public void setClueCards(List<Integer> clueCards) {
        this.clueCards = (ArrayList<Integer>) clueCards;
    }

    public List<Player> getPlayers() {
        return players;
    }
    public int getCardDrawn() {
        return cardDrawn;
    }
}

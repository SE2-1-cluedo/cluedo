package at.moritzmusel.cluedo.game;

import static at.moritzmusel.cluedo.entities.Character.DR_ORCHID;
import static at.moritzmusel.cluedo.entities.Character.MISS_SCARLETT;
import static at.moritzmusel.cluedo.entities.Character.MRS_PEACOCK;
import static at.moritzmusel.cluedo.entities.Character.PROFESSOR_PLUM;
import static at.moritzmusel.cluedo.entities.Character.REVEREND_GREEN;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import at.moritzmusel.cluedo.Card;
import at.moritzmusel.cluedo.entities.Character;
import at.moritzmusel.cluedo.entities.Player;
import at.moritzmusel.cluedo.network.pojo.GameState;
import at.moritzmusel.cluedo.network.pojo.Question;


//characters: 1-6
//weapons: 7-12
//rooms: 13-21

public class Gameplay {
    private static int numDice;
    private static int stepsTaken = 0;
    private Character currentPlayer;
    private List<Player> players = new ArrayList<>();
    private ArrayList<Integer> clueCards = new ArrayList<>();
    private final SecureRandom rand = new SecureRandom();
    private int cardDrawn;
    private String[] turnOrderGame;

    private static final Gameplay OBJ = new Gameplay();

    private Gameplay() {
        /*Player p1 = new Player("1");
        p1.setPlayerCharacterName(MISS_SCARLETT);
        Player p2 = new Player("2");
        p2.setPlayerCharacterName(DR_ORCHID);
        Player p3 = new Player("3");
        p3.setPlayerCharacterName(PROFESSOR_PLUM);
        Player p4 = new Player("4");
        p4.setPlayerCharacterName(REVEREND_GREEN);
        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);
        turnOrderGame = GameState.getInstance().getTurnOrder();
        setPlayersGame();*/
    }

    public void startGame(){
        /*turnOrderGame = GameState.getInstance().getTurnOrder();
        players = GameState.getInstance().getPlayerState();
        decidePlayerWhoMovesFirst();*/

        Player p1 = new Player("1");
        p1.setPlayerCharacterName(MISS_SCARLETT);
        Player p2 = new Player("2");
        p2.setPlayerCharacterName(DR_ORCHID);
        Player p3 = new Player("3");
        p3.setPlayerCharacterName(PROFESSOR_PLUM);
        Player p4 = new Player("4");
        p4.setPlayerCharacterName(REVEREND_GREEN);
        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);
    }
    public static Gameplay getInstance(){
        return OBJ;
    }

    /**
     * Called after the Player ends his/her turn
     */
    public Character endTurn() {
        currentPlayer = getCharacterByPlayerID(getPlayerIDOfNextPlayer());
        GameState.getInstance().setPlayerTurn(getPlayerIDOfNextPlayer(), true);
        updateGameState();
        return currentPlayer;
    }

    /**
     * Calculates the position of the Player after the dice was thrown depending on if
     * he moved right or left
     * dice was thrown and
     *
     * @param position new position of player
     */

    public void updatePlayerPosition(int position) {
        Player player = findPlayerByCharacterName(currentPlayer);
        player.setPositionOnBoard(position);
        GameState.getInstance().setPlayerState(players,true);
    }

    /**
     * Is called in move-Methode Board Activity and checks if Player can still move
     */
    public void canMove(){
        stepsTaken++;
        if(stepsTaken == numDice) {
            findPlayerByCharacterName(currentPlayer).setAbleToMove(false);
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
        GameState.getInstance().setPlayerState(players,true);
        //send all cards to other players
    }

    /**
     * Method to check if one of the players in the correct order has a card
     * which is equal to one of the cards send. If yes the person who asked the question
     * get the card added to their inventory. And update to the other players
     * @param cardsForQuestion the 3 cards person, weapon and room. The player is known
     */
    public void askPlayerAQuestion(int[] cardsForQuestion){
        String playerCharacterName = getCurrentPlayer().name();
        Question question = new Question(playerCharacterName,cardsForQuestion);
        GameState.getInstance().setAskQuestion(question,true);
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
     * Decides which Player/Character is able to move first
     */
    public void decidePlayerWhoMovesFirst() {
        String playerID = GameState.getInstance().getTurnOrder()[0];
        currentPlayer = getCharacterByPlayerID(playerID);

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

    public Character getCharacterByPlayerID(String playerID){
        for(int i = 0; i < players.size(); i++){
            if(players.get(i).getPlayerId().equals(playerID)){
                return players.get(i).getPlayerCharacterName();
            }
        }
        return currentPlayer;
    }

    private void updateGameState(){
        //GameState.getInstance().getPlayerState().replace->{}
    }


    /**
     * A method to find the next Player according to the turn Order
     * @return playerID whos turn is next
     */
    private String getPlayerIDOfNextPlayer(){
        String playerID = "";
        for (int i = 0; i < turnOrderGame.length;i++) {
            if (turnOrderGame[i].equals(GameState.getInstance().getPlayerTurn())) {
                if(i+1 == turnOrderGame.length){
                    i = 0;
                }else {
                    i+=1;
                }
                playerID = turnOrderGame[i];
                break;
            }
        }
        return playerID;
    }

    /**
     * Takes the result after the Player throw the dice and safes it in a variable
     *
     * @param numberRolled Takes the result after the dice throw
     */
    public static void rollDiceForPlayer(int numberRolled) {
        numDice = numberRolled;
    }


    // ---------------------------------------Getter and Setter ------------------------------------ //
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

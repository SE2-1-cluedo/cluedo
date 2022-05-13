package at.moritzmusel.cluedo.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import at.moritzmusel.cluedo.entities.Character;
import at.moritzmusel.cluedo.entities.Player;

public class Gameplay {
    private static int numDice;
    private int stepsTaken = 0;
    private Character currentPlayer;
    private List<Player> players;
    private ArrayList<Integer> clueCards = new ArrayList<>();
    private final Random rand = new Random();
    private int cardDrawn;

    /**
     * @param players all the Players in the Session
     */
    public Gameplay(List<Player> players) {
        this.players = players;
    }

    public Gameplay(){}
    /**
     * Called after the Player ends his/her turn
     */
    public Character endTurn() {
        Player player = findPlayerByCharacterName(currentPlayer);
        player.setIsAbleToMove(false);
        decideCharacterWhoMovesNext();
        return currentPlayer;
    }

    /**
     * Calculates the position of the Player after the dice was thrown depending on if
     * he moved right or left
     * dice was thrown and
     *
     * @param direction 1 (move right)     /    0 (move left)
     */
    public void movePlayer(byte direction) {
        Player player = findPlayerByCharacterName(currentPlayer);
        player.setIsAbleToMove(true);
        if (player.getIsAbleToMove()) {
            if(numDice == 4){
                drawClueCard();
                //wait for answers
            }
            int newPosition = calculatePosition(player.getPositionOnBoard(), direction, numDice);
            player.setPositionOnBoard(newPosition);
            //movePlayerUi(player)
            //dont allow dice throw again
        }
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
    public void useSecretPassage() {
        Player player = findPlayerByCharacterName(currentPlayer);
        int position = player.getPositionOnBoard();
        player.setIsAbleToMove(true);
        if (player.getIsAbleToMove()) {
            switch (position) {
                case 9:
                    player.setPositionOnBoard(5); //move from salon to winter garden
                    break;
                case 5:
                    player.setPositionOnBoard(9); //move from winter garden to salon
                    break;
                case 2:
                    player.setPositionOnBoard(7);
                    break; //move from working room to kitchen
                case 7:
                    player.setPositionOnBoard(2); //move from kitchen to working room
                    break;
            }
            //move player UI
            //disable dice throw
        }
    }

    /**
     * Checks if the current Player is allowed to use the Secret Passage
     * @return true if allowed / false if isnt allowed
     */
    public boolean isAllowedToUseSecretPassage() {
        Player player = findPlayerByCharacterName(currentPlayer);
        int position = player.getPositionOnBoard();
        return position == 2 || position == 7 || position == 9 || position == 5;
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
        currentPlayer = Character.MISS_SCARLET;
        while (true) {
            Player firstPlayer = findPlayerByCharacterName(currentPlayer);
            if (firstPlayer == null) {
                currentPlayer = currentPlayer.getNextCharacter();
            } else {
                currentPlayer = firstPlayer.getPlayerCharacterName();
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
    private void drawClueCard(){
        cardDrawn = getRandomIntInRange(21,50);
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
        clueCards = generateRandomCards(21,49);
    }

    /**
     * randomized the Cluedo Cards and safes them in the players card list
     */
    void generateCluedoCards(){
        ArrayList<Integer> playerCards = generateRandomCards(0,20);
        //playerCard.contains(getKillerCardFromNetwork)
        //delete those cards
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

    public static void setNumDice(int numDice) {
        Gameplay.numDice = numDice;
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

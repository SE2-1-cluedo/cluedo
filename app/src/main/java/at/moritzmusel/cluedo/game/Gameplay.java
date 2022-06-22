package at.moritzmusel.cluedo.game;

import static at.moritzmusel.cluedo.entities.Character.DR_ORCHID;
import static at.moritzmusel.cluedo.entities.Character.MISS_SCARLETT;
import static at.moritzmusel.cluedo.entities.Character.PROFESSOR_PLUM;
import static at.moritzmusel.cluedo.entities.Character.REVEREND_GREEN;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import at.moritzmusel.cluedo.Card;
import at.moritzmusel.cluedo.communication.GameplayCommunicator;
import at.moritzmusel.cluedo.communication.NetworkCommunicator;
import at.moritzmusel.cluedo.entities.Character;
import at.moritzmusel.cluedo.entities.Player;
import at.moritzmusel.cluedo.network.Network;
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
    private GameState gameState;
    private GameplayCommunicator gameCommunicator;
    private NetworkCommunicator netCommunicator;
    //Positions in Array -> {dagger - candlestick - revolver - rope - pipe - wrench}
    private int[] weaponsPos;

    private static Gameplay OBJ;

    private Gameplay() {
        startGame();
    }


    public void startGame(){
        gameState = GameState.getInstance();
        turnOrderGame = gameState.getTurnOrder();
        players = gameState.getPlayerState();
        decidePlayerWhoMovesFirst();
        weaponsPos = gameState.getWeaponPositions();

        gameCommunicator = GameplayCommunicator.getInstance();
        netCommunicator = NetworkCommunicator.getInstance();

        netCommunicator.register(()->{
            if(netCommunicator.isPlayerChanged()){
                checkWhatChangedInPlayer(gameState.getPlayerState());
            }
            if(netCommunicator.isQuestionChanged()){
                gameCommunicator.setSuspicion(true);
                gameCommunicator.notifyList();
            }
            if(netCommunicator.isTurnChanged()){
                checkTurnChanged(gameState.getPlayerTurn());
            }
            if(netCommunicator.isWeaponsChanged()){
                checkWeaponChanged(gameState.getWeaponPositions());
            }
            if(netCommunicator.isHasWon()){
                gameCommunicator.setWinner(true);
                gameCommunicator.notifyList();
            }
            if(netCommunicator.isHasLost()){
                gameCommunicator.setLoser(true);
                gameCommunicator.notifyList();
            }
            if (netCommunicator.isMagnify()){
                gameCommunicator.setMagnifying(true);
                gameCommunicator.notifyList();
            }
        });

       /* Player p1 = new Player("1");
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
        players.add(p4);*/
    }
    public static Gameplay getInstance(){
        if(OBJ == null){
            OBJ = new Gameplay();
        }return OBJ;
    }

    /**
     * Called after the Player ends his/her turn
     */
    public Character endTurn() {
        String playerID = getPlayerIDOfNextPlayerInTurnOrder();
        currentPlayer = getCharacterByPlayerID(playerID);
        gameState.setPlayerTurn(getPlayerIDOfNextPlayerInTurnOrder(), true);
        gameCommunicator.setTurnChange(true);
        gameCommunicator.notifyList();
        return currentPlayer;
    }

    public String getPlayerForSuspectedCards(int[] cards){
        for(Player p : players){
            for (int j = 0; j < 3; j++){
                if(p.getPlayerOwnedCards().contains(cards[j]))
                    return p.getPlayerCharacterName().name();
            }
        }
        return "nobody";
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
        gameState.setPlayerState(players,true);
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


   /*
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
        gameState.setPlayerState(players,true);
        //send all cards to other players
    }*/

    /**
     * Method to ask other Players a Question
     * @param cardsForQuestion the 3 cards person, weapon and room. The player is known
     */
    public void askPlayerAQuestion(int[] cardsForQuestion){
        String playerCharacterName = getCurrentPlayer().name();
        Question question = new Question(playerCharacterName,cardsForQuestion);
        gameState.setAskQuestion(question,true);
    }

    public void notifyDatabase(int[] array){
        weaponsPos = array;
        gameState.setWeaponPositions(array,true);
    }

    public boolean accusation(int[] cards) {
        int[] killerCards = gameState.getKiller();
        return Arrays.equals(cards,killerCards);
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
        String playerID = turnOrderGame[0];
        currentPlayer = getCharacterByPlayerID(playerID);
    }


    /**
     * Draw a Random Card from the Clue Cards staple
     * and delete it from the staple
     */
    public String getPlayerToWhichCardBelongs(int cardDrawn){
        gameCommunicator.setMagnifying(true);
        for(Player p : players) {
            if(p.getPlayerOwnedCards().contains(cardDrawn)){
                gameCommunicator.notifyList();
                return p.getPlayerCharacterName().name();
            }
        }
        gameCommunicator.notifyList();
        return "nobody";
    }

    /*    private ArrayList<Integer> generateRandomCards(int min, int max){
        ArrayList<Integer> cards = new ArrayList<>();
        for(int i = min;i <= max;i++){
            cards.add(i);
        }
        Collections.shuffle(cards);
        return cards;
    }*/


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
                }
            }
        }
    }


/*    private void distributeCardsEquallyToPlayers(List<Integer> cards){
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
    }*/

    public Character getCharacterByPlayerID(String playerID){
        int i = 0;
        for(; i < players.size(); i++){
            if(players.get(i).getPlayerId().equals(playerID)){
                break;
            }
        }
        return players.get(i).getPlayerCharacterName();
    }

    protected void checkWhatChangedInPlayer(List<Player> newPlayers){
        for(int i = 0; i < players.size(); i++){
            if(!(newPlayers.get(i).getPositionOnBoard() == players.get(i).getPositionOnBoard())){
                players = newPlayers;
                gameCommunicator.setMoved(true);
                gameCommunicator.notifyList();
            }
        }
    }

  /*  protected void questionChanged(Question newQuestion) {
        //if(!newQuestion.equals())
    }*/

    protected void checkWeaponChanged(int[] newWeapon) {
        if(!(Arrays.equals(newWeapon, weaponsPos))){
            weaponsPos = newWeapon;
            gameCommunicator.setMoved(true);
            gameCommunicator.notifyList();
        }
    }

    protected void checkTurnChanged(String newTurn){
        if(!currentPlayer.name().equals(newTurn)){
            gameCommunicator.setTurnChange(true);
            gameCommunicator.notifyList();
        }
    }

    /**
     * Check if the currentPlayer is the Player who owns the device
     * @return true if it is the currentPlayer
     */
    public boolean checkIfPlayerIsOwn(){
        return Network.getCurrentUser().getUid().equals(findPlayerByCharacterName(currentPlayer).getPlayerId());
    }

    public List<Integer> getCardsOfPlayerOwn(){
        List<Integer> cards = null;
        for(int i = 0; i<players.size();i++){
            if(players.get(i).getPlayerId().equals(Network.getCurrentUser().getUid())){
                cards = players.get(i).getPlayerOwnedCards();
            }
        }
        return cards;
    }
    /**
     * A method to find the next Player according to the turn Order
     * @return playerID whos turn is next
     */
    private String getPlayerIDOfNextPlayerInTurnOrder(){
        String playerID = "";
        for (int i = 0; i < turnOrderGame.length;i++) {
            if (turnOrderGame[i].equals(gameState.getPlayerTurn())) {
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

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setTurnOrderGame(String[] turnOrderGame) {
        this.turnOrderGame = turnOrderGame;
    }

    public int[] getWeaponsPos() {
        return weaponsPos;
    }
}

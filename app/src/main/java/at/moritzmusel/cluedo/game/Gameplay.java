package at.moritzmusel.cluedo.game;

import org.apache.commons.lang3.ArrayUtils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private List<Player> players;
    private ArrayList<Integer> clueCards = new ArrayList<>();
    private final SecureRandom rand = new SecureRandom();
    private int cardDrawn;
    private String[] turnOrderGame;
    private final GameState gameState;
    private final GameplayCommunicator gameCommunicator;
    private final NetworkCommunicator netCommunicator;
    //Positions in Array -> {dagger - candlestick - revolver - rope - pipe - wrench}
    private int[] weaponsPos;

    private static Gameplay OBJ;

    private Gameplay() {
        gameState = GameState.getInstance();
        turnOrderGame = gameState.getTurnOrder();
        players = gameState.getPlayerState();
        weaponsPos = gameState.getWeaponPositions();
        decidePlayerWhoMovesFirst();
        gameCommunicator = GameplayCommunicator.getInstance();
        netCommunicator = NetworkCommunicator.getInstance();
        netCommunicator.setTurnChanged(false);
        netCommunicator.setQuestionChanged(false);
        netCommunicator.setPositionChanged(false);
        netCommunicator.setMagnify(false);
        netCommunicator.setWeaponsChanged(false);
        netCommunicator.setPlayerChanged(false);
        netCommunicator.register(()->{
            if(netCommunicator.isTurnChanged()) {
                if (!gameState.getPlayerTurn().equals(findPlayerByCharacterName(currentPlayer).getPlayerId())) {
                    currentPlayer = findPlayerById(gameState.getPlayerTurn()).getPlayerCharacterName();
                    findPlayerByCharacterName(currentPlayer).setAbleToMove(true);
                    gameCommunicator.setTurnChange(true);
                    gameCommunicator.notifyList();
                    netCommunicator.setTurnChanged(false);
                }
            }
            if(netCommunicator.isTurnOrderChanged()){
                turnOrderGame = gameState.getTurnOrder();
                netCommunicator.setTurnOrderChanged(false);
            }
        });
        currentPlayer = findPlayerById(gameState.getPlayerTurn()).getPlayerCharacterName();
        findPlayerByCharacterName(currentPlayer).setAbleToMove(true);
    }

    public static Gameplay getInstance(){
        if(OBJ == null){
            OBJ = new Gameplay();
        }return OBJ;
    }

    public int[] weaponDifference(int[] newPos){
        for(int i = 0; i < weaponsPos.length; i++){
            if(weaponsPos[i]!=newPos[i]) {
                weaponsPos = newPos;
                return new int[]{newPos[i],i};
            }
        }
        return null;
    }

    public String[] playerDifference(List<Player> newPlayers){
        for(int i = 0; i < players.size(); i++){
            if(players.get(i).getPositionOnBoard() != newPlayers.get(i).getPositionOnBoard()) {
                players = newPlayers;
                return new String[]{String.valueOf(newPlayers.get(i).getPositionOnBoard()),newPlayers.get(i).getPlayerId()};
            }
        }
        return null;
    }

    /**
     * Called after the Player ends his/her turn
     */
    public Character endTurn() {
        String playerID = getPlayerIDOfNextPlayerInTurnOrder();
        System.out.println(playerID);
        System.out.println(Arrays.toString(turnOrderGame));
        currentPlayer = getCharacterByPlayerID(playerID);
        gameState.setPlayerTurn(playerID, true);
        return currentPlayer;
    }

    public String[] getPlayerForSuspectedCards(int[] cards){
        for(Player p : players){
            for (int card : cards) {
                if (!p.getPlayerId().equals(findPlayerByCharacterName(currentPlayer).getPlayerId())) {
                    if (p.getPlayerOwnedCards().contains(card))
                        return new String[]{p.getPlayerCharacterName().name(), String.valueOf(card)};
                }
            }
        }
        return new String[]{"Nobody"};
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
        currentPlayer = findPlayerById(gameState.getPlayerTurn()).getPlayerCharacterName();
        findPlayerByCharacterName(currentPlayer).setAbleToMove(true);
    }


    /**
     * Draw a Random Card from the Clue Cards staple
     * and delete it from the staple
     */
    public String getPlayerToWhichCardBelongs(int cardDrawn){
        for(Player p : players)
            if(p.getPlayerOwnedCards().contains(cardDrawn))
                return p.getPlayerCharacterName().name();

        return "?nobody? :^)";
    }

    /**
     * @param character Find the Character belonging to a player if not return null
     * @return Player who playing as the character
     */
    public Player findPlayerByCharacterName(Character character) {
        while (true) {
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getPlayerCharacterName() == character) {
                    return players.get(i);
                }
            }
        }
    }


    public Player findPlayerById(String id){
        for(Player p: players)
            if(p.getPlayerId().equals(id))
                return p;
            return null;
    }

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
                gameCommunicator.setPlayerMoved(true);
                gameCommunicator.notifyList();
            }
        }
    }

    protected void checkWeaponChanged(int[] newWeapon) {
        if(!(Arrays.equals(newWeapon, weaponsPos))){
            weaponsPos = newWeapon;
            gameCommunicator.setPlayerMoved(true);
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
        int pos = ArrayUtils.indexOf(turnOrderGame,findPlayerByCharacterName(currentPlayer).getPlayerId());
        if(pos == turnOrderGame.length-1)
            return turnOrderGame[0];
        return turnOrderGame[pos+1];
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

    public int[] getWeaponsPos() {
        return weaponsPos;
    }

    public void setWeaponsPos(int[] weaponsPos) {
        this.weaponsPos = weaponsPos;
        notifyDatabase(weaponsPos);
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setTurnOrderGame(String[] turnOrderGame) {
        this.turnOrderGame = turnOrderGame;
    }
}

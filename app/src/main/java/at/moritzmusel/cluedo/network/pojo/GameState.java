package at.moritzmusel.cluedo.network.pojo;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import at.moritzmusel.cluedo.AllTheCards;
import at.moritzmusel.cluedo.Card;
import at.moritzmusel.cluedo.communication.Communicator;
import at.moritzmusel.cluedo.communication.NetworkCommunicator;
import at.moritzmusel.cluedo.entities.Player;
import at.moritzmusel.cluedo.network.Network;
import at.moritzmusel.cluedo.network.data.QuestionCards;

public class GameState {
    private List<Player> playerState;
    private List<Integer> cardState;
    private List<Integer> cardsEliminated;
    private List<Card> questionCardStack;
    private List<Integer> eliminatedCards = new ArrayList<>();
    private Question askQuestion;
    private String framed, framer;
    private String winner, loser;
    private int[] killer;
    private String playerTurn;
    private String[] turnOrder, magnify;
    //Positions in Array -> {dagger - candlestick - revolver - rope - pipe - wrench}
    private int[] weaponPositions = new int[]{5,1,9,3,6,8};
    DatabaseReference dbRef;
    private final NetworkCommunicator communicator;


    private static GameState OBJ;

    private GameState(){
        //initQuestionCardsStack(Network.getCtx());
        dbRef = Network.getCurrentGame();
        communicator = NetworkCommunicator.getInstance();
    }

    public static GameState getInstance() {
        if(OBJ == null){
            OBJ = new GameState();
            return OBJ;
        }
        return OBJ;
    }

    public void reset(){
        OBJ = null;
    }

    public List<Integer> getCardState() {
        return cardState;
    }

    public void setCardState(List<Integer> cardState, boolean database) {
        this.cardState = cardState;
        if(!database) {
            communicator.notifyList();
        }
        else if (cardState == null)
            dbRef.child("players").child(Network.getCurrentUser().getUid()).child("cards").setValue("");

        else {
            StringBuilder sB = new StringBuilder();
            for (int c : cardState)
                sB.append(c).append("");

            dbRef.child("players").child(Network.getCurrentUser().getUid()).child("cards").setValue(sB.toString().trim());
        }
    }

    public List<Player> getPlayerState() {
        return playerState;
    }

    public List<Integer> getEliminatedCards() {
        return eliminatedCards;
    }

    public void setEliminatedCards(List<Integer> eliminatedCards, boolean database) {
        this.eliminatedCards = eliminatedCards;
        if(!database){
            communicator.setEliminatedChanged(true);
            communicator.notifyList();
            System.out.println("Got eliminated Cards");
        } else if (eliminatedCards == null)
            dbRef.child("cards-eliminated").setValue("");
        else {
            StringBuilder sB = new StringBuilder();
            for(int i: eliminatedCards)
                sB.append(i).append(" ");
            dbRef.child("cards-eliminated").setValue(sB.toString().trim());
        }
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner, boolean database){
        this.winner = winner;
        if(!database){
            if(!communicator.isHasWon()){
                communicator.setHasWon(true);
                communicator.notifyList();
                System.out.println("has won");
            }
        } else if(winner == null)
            dbRef.child("result").child("winner").setValue("");
        else
            dbRef.child("result").child("winner").setValue(winner);

    }

    public String getFramed() {
        return framed;
    }

    public void setFramed(String framed, boolean database){
        this.framed = framed;
        if(!database){
            if(!communicator.isFramed()){
                communicator.setFramed(true);
                communicator.notifyList();
                System.out.println("is framed");
            }
        } else if(framed == null){
            dbRef.child("result").child("framed").setValue("");
        }
        else
            dbRef.child("result").child("framed").setValue(framed);

    }

    public String getFramer() {
        return framer;
    }

    public void setFramer(String framer, boolean database){
        this.framer = framer;
        if(!database){
            if(!communicator.isFramer()){
                communicator.setFramer(true);
                communicator.notifyList();
                System.out.println("is framer");
            }
        } else if(framed == null){
            dbRef.child("result").child("framer").setValue("");
        }
        else
            dbRef.child("result").child("framer").setValue(framer);

    }

    public String getLoser() {
        return loser;
    }

    public void setLoser(String loser, boolean database) {
        this.loser = loser;
        if(!database){
            if(!communicator.isHasLost()) {
                communicator.setHasLost(true);
                communicator.notifyList();
                System.out.println("lost");
            }
        } else if(loser == null)
            dbRef.child("result").child("loser").setValue("");
        else
            dbRef.child("result").child("loser").setValue(loser);
    }

    public String[] getMagnify() {
        return magnify;
    }

    public void setMagnify(String[] magnify, boolean database) {
        this.magnify = magnify;
        if(!database) {
            if(!communicator.isMagnify()){
                communicator.setMagnify(true);
                communicator.notifyList();
                System.out.println("magnified");
            }
        } else if(magnify == null)
            dbRef.child("turn-flag").child("magnify").setValue("");
        else {
            StringBuilder sB = new StringBuilder();
            for(String s: magnify)
                sB.append(s).append(" ");

            dbRef.child("turn-flag").child("magnify").setValue(sB.toString().trim());
        }
    }

    public void setPlayerState(List<Player> playerState, boolean database) {
        this.playerState = playerState;
        if(!database) {
            if(!communicator.isPlayerChanged()){
                communicator.setPlayerChanged(true);
                communicator.notifyList();
            }
            if(!communicator.isPositionChanged()){
                communicator.setPositionChanged(true);
                communicator.notifyList();
            }
        }
        else if(playerState == null){
            List<String> players = new ArrayList<>();
            dbRef.child("players").get().addOnCompleteListener(task -> {
                if (!task.isSuccessful())
                    Log.e("firebase", "Error getting data", task.getException());
                else {
                    String[] splitter = String.valueOf(task.getResult().getValue()).split("[{ =]");
                    for(String s : splitter)
                        if (s.length() == 28)
                            players.add(s);

                    for(String player : players){
                        Map<String,Object> map = new HashMap<>();
                        map.put("cards","");
                        map.put("character","");
                        map.put("position","");
                        dbRef.child("players").child(player).updateChildren(map);
                    }
                }
            });
        } else {
            for (Player p : playerState){
                dbRef.child("players").child(p.getPlayerId()).child("cards").setValue(p.getOwnedCardsAsString());
                dbRef.child("players").child(p.getPlayerId()).child("position").setValue(Integer.toString(p.getPositionOnBoard()));
            }
        }
    }

    public void assignCards(){
        ArrayList<Integer> cards = generateRandomCards();
        List<Integer> killerCards = Arrays.stream(getKiller()).boxed().collect(Collectors.toList());

        int j = 0;
        for(int i = 0; i < cards.size();i++){
            if(!killerCards.contains(cards.get(i))) {
                if (getPlayerState().size() == j)
                    j = 0;

                getPlayerState().get(j).addOwnedCard(cards.get(i));
                j++;
            }
        }
        setPlayerState(getPlayerState(),true);
    }

    private ArrayList<Integer> generateRandomCards(){
        AllTheCards allCards = new AllTheCards();
        ArrayList<Integer> cards = (ArrayList<Integer>) IntStream.range(0, allCards.getGameCards().size()).boxed().collect(Collectors.toList());
        Collections.shuffle(cards);
        return cards;
    }

    public Question getAskQuestion() {
        return askQuestion;
    }

    public void setAskQuestion(Question askQuestion, boolean database) {
        this.askQuestion = askQuestion;
        if(!database) {
            if(!communicator.isQuestionChanged()){
                communicator.setQuestionChanged(true);
                communicator.notifyList();
                System.out.println("question changed");
            }
        }
        else if(askQuestion == null)
            dbRef.child("turn-flag").child("question").setValue("");
        else {
            StringBuilder sB = new StringBuilder();
            sB.append(askQuestion.getAskPerson()).append(" ");
            for(int i : askQuestion.getCards())
                sB.append(i).append(" ");
            dbRef.child("turn-flag").child("question").setValue(sB.toString().trim());
        }
    }

    public void setKiller(int[] killer){
        this.killer = killer;
    }

    public int[] getKiller() {
        return killer;
    }

    public String getKillerAsString() {
        StringBuilder sb = new StringBuilder();
        for(int i: getKiller())
            sb.append(i).append(" ");

        return sb.toString().trim();
    }

    public String getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(String playerTurn, boolean database) {
        if(playerTurn != null && !playerTurn.equals(this.playerTurn)){
            this.playerTurn = playerTurn;
            if(!database) {
                if(!communicator.isPlayerChanged()){
                    communicator.setTurnChanged(true);
                    communicator.notifyList();
                    System.out.println("turn changed");
                }
            }
            else dbRef.child("turn-flag").child("player-turn").setValue(playerTurn);
        }
    }

    private void initQuestionCardsStack(Context ctx){
        QuestionCards qc = new QuestionCards(ctx);
        this.questionCardStack = qc.getQuestionCards();
    }

    public int[] getWeaponPositions() {
        return weaponPositions;
    }

    public void setWeaponPositions(int[] weaponPositions, boolean database) {
        this.weaponPositions = weaponPositions;
        if(!database) {
                communicator.setWeaponsChanged(true);
                communicator.notifyList();
        }
        else if(weaponPositions == null)
            dbRef.child("weapon-positions").setValue("");
        else {
            StringBuilder sB = new StringBuilder();
            for(int i: weaponPositions)
                sB.append(i).append(" ");

            dbRef.child("weapon-positions").setValue(sB.toString().trim());
        }
    }

    public void setTurnOrder(String[] turnOrder, boolean database){
        this.turnOrder = turnOrder;
        if(!database){
            communicator.setTurnOrderChanged(true);
            communicator.notifyList();
        } else {
            StringBuilder sB = new StringBuilder();
            for(String s: turnOrder)
                sB.append(s).append(" ");

            dbRef.child("turn-order").setValue(sB.toString().trim());
        }
    }

    public String[] getTurnOrder(){
        return turnOrder;
    }

    /**
     * Deletes a player id from the turnorder, so he can not do anything after a wrong accusation
     * @param uid Id to delete
     */
    public void removeFromTurnOrder(String uid) {
        String [] turnOrder = ArrayUtils.removeElement(this.getTurnOrder(), uid);
        this.setTurnOrder(turnOrder,true);
    }
}

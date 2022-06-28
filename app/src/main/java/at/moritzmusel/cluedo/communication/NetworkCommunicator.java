package at.moritzmusel.cluedo.communication;

import java.util.ArrayList;

import at.moritzmusel.cluedo.network.pojo.GameState;

public class NetworkCommunicator implements Communicator {
    private final ArrayList<ChangeListener> listenerArray;
    private boolean turnChanged, turnOrderChanged, playerChanged, eliminatedChanged, positionChanged, questionChanged, weaponsChanged, hasWon, framed, framer, hasLost, magnify, characterChanged, startGame;

    private static NetworkCommunicator OBJ;

    private NetworkCommunicator(){
        listenerArray = new ArrayList<>();
    }

    public static NetworkCommunicator getInstance(){
        if(OBJ == null){
            OBJ = new NetworkCommunicator();
            return OBJ;
        }
        return OBJ;
    }

    public void reset(){
        OBJ = null;
    }

    @Override
    public void register(ChangeListener listener) {
        listenerArray.add(listener);
    }

    @Override
    public void notifyList(){
        for(ChangeListener listener : listenerArray)
            listener.onChange();
    }

    public boolean isEliminatedChanged() {
        return eliminatedChanged;
    }

    public void setEliminatedChanged(boolean eliminatedChanged) {
        this.eliminatedChanged = eliminatedChanged;
    }

    public boolean isTurnChanged() {
        return turnChanged;
    }

    public void setTurnChanged(boolean turnChanged) {
        this.turnChanged = turnChanged;
    }

    public boolean isPlayerChanged() {
        return playerChanged;
    }

    public void setPlayerChanged(boolean playerChanged) {
        this.playerChanged = playerChanged;
    }

    public boolean isQuestionChanged() {
        return questionChanged;
    }

    public void setQuestionChanged(boolean questionChanged) {
        this.questionChanged = questionChanged;
    }

    public boolean isWeaponsChanged() {
        return weaponsChanged;
    }

    public void setWeaponsChanged(boolean weaponsChanged) {
        this.weaponsChanged = weaponsChanged;
    }

    public boolean isHasWon() {
        return hasWon;
    }

    public boolean isFramed() {
        return framed;
    }
    public void setFramed(boolean framed) {
        this.framed = framed;
    }

    public boolean isFramer() {
        return framer;
    }
    public void setFramer(boolean framer) {
        this.framer = framer;
    }

    public boolean isHasLost() {
        return hasLost;
    }

    public void setHasLost(boolean hasLost) {
        this.hasLost = hasLost;
    }

    public void setHasWon(boolean hasWon) {
        this.hasWon = hasWon;
    }

    public boolean isMagnify() {
        return magnify;
    }

    public void setMagnify(boolean magnify) {
        this.magnify = magnify;
    }

    public boolean isCharacterChanged() {
        return characterChanged;
    }

    public void setCharacterChanged(boolean characterChanged) {
        this.characterChanged = characterChanged;
    }

    public boolean isStartGame() {
        return startGame;
    }

    public void setStartGame(boolean startGame) {
        this.startGame = startGame;
    }

    public boolean isPositionChanged() {
        return positionChanged;
    }

    public void setPositionChanged(boolean positionChanged) {
        this.positionChanged = positionChanged;
    }

    public boolean isTurnOrderChanged() {
        return turnOrderChanged;
    }

    public void setTurnOrderChanged(boolean turnOrderChanged) {
        this.turnOrderChanged = turnOrderChanged;
    }
}
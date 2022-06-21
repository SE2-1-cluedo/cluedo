package at.moritzmusel.cluedo.communication;

import java.util.ArrayList;

public class NetworkCommunicator implements Communicator {
    private final ArrayList<ChangeListener> listenerArray;
    private boolean turnChanged, playerChanged, questionChanged, weaponsChanged, hasWon, hasLost, magnify, characterChanged, startGame;

    private static final NetworkCommunicator OBJ = new NetworkCommunicator();

    private NetworkCommunicator(){
        listenerArray = new ArrayList<>();
    }

    public static NetworkCommunicator getInstance(){
        return OBJ;
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
}
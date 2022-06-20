package at.moritzmusel.cluedo.communication;

import java.util.ArrayList;

public class GameplayCommunicator implements Communicator{
    private boolean moved, suspicion, winner, loser, turnChange, magnifying;
    private final ArrayList<ChangeListener> listenerArray;

    private static final GameplayCommunicator OBJ = new GameplayCommunicator();

    private GameplayCommunicator(){
        listenerArray = new ArrayList<>();
    }

    public static GameplayCommunicator getInstance(){
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

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public boolean isSuspicion() {
        return suspicion;
    }

    public void setSuspicion(boolean suspicion) {
        this.suspicion = suspicion;
    }

    public boolean isWinner() {
        return winner;
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
    }

    public boolean isLoser() {
        return loser;
    }

    public void setLoser(boolean loser) {
        this.loser = loser;
    }

    public boolean isTurnChange() {
        return turnChange;
    }

    public void setTurnChange(boolean turnChange) {
        this.turnChange = turnChange;
    }

    public boolean isMagnifying() {
        return magnifying;
    }

    public void setMagnifying(boolean magnifying) {
        this.magnifying = magnifying;
    }
}

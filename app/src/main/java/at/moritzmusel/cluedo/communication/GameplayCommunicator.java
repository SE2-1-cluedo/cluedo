package at.moritzmusel.cluedo.communication;

import java.util.ArrayList;

public class GameplayCommunicator implements Communicator{
    private boolean moved, suspicion, turnChange;
    private final ArrayList<ChangeListener> listenerArray;

    private static GameplayCommunicator OBJ = new GameplayCommunicator();

    private GameplayCommunicator(){
        listenerArray = new ArrayList<>();
    }

    public static GameplayCommunicator getInstance(){
        if(OBJ == null){
            OBJ = new GameplayCommunicator();
            return OBJ;
        }
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

    public boolean isTurnChange() {
        return turnChange;
    }

    public void setTurnChange(boolean turnChange) {
        this.turnChange = turnChange;
    }
}
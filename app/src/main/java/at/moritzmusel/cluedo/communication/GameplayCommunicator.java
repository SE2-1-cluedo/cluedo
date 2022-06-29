package at.moritzmusel.cluedo.communication;

import java.util.ArrayList;

public class GameplayCommunicator implements Communicator{
    private boolean playerMoved, weaponMoved, suspicion, turnChange;
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

    public boolean isPlayerMoved() {
        return playerMoved;
    }

    public void setPlayerMoved(boolean playerMoved) {
        this.playerMoved = playerMoved;
    }

    public boolean isWeaponMoved() {
        return weaponMoved;
    }

    public void setWeaponMoved(boolean weaponMoved) {
        this.weaponMoved = weaponMoved;
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
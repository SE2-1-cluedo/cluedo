package at.moritzmusel.cluedo.communication;

import java.util.ArrayList;

public class GameplayCommunicator implements Communicator{

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
}

package at.moritzmusel.cluedo.communication;

import java.util.ArrayList;

public class NetworkCommunicator implements Communicator {
    private final ArrayList<ChangeListener> listenerArray;

    private static final Communicator OBJ = new NetworkCommunicator();

    private NetworkCommunicator(){
        listenerArray = new ArrayList<>();
    }

    public static Communicator getInstance(){
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

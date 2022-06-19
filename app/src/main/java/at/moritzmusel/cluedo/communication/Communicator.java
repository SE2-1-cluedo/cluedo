package at.moritzmusel.cluedo.communication;

public interface Communicator {

    void register(ChangeListener listener);

    void notifyList();

    interface ChangeListener{
        void onChange();
    }
}

package at.moritzmusel.cluedo.game;

import java.util.ArrayList;

public class Communicator {
    private final ArrayList<ChangeListener> listenerArray;
    private String weapon,character;
    private boolean hasSuspected, hasAccused;

    private static final Communicator OBJ = new Communicator();

    private Communicator(){
        listenerArray = new ArrayList<>();
    }

    public static Communicator getInstance(){
        return OBJ;
    }

    public void register(ChangeListener listener) {
        listenerArray.add(listener);
    }

    public void unregister(ChangeListener listener) {
        listenerArray.remove(listener);
    }

    private void notifyList(){
        for(ChangeListener listener : listenerArray)
            listener.onChange();
    }

    public void setWeapon(String weapon) {
        this.weapon = weapon;
        notifyList();
    }

    public void setCharacter(String character) {
        this.character = character;
        notifyList();
    }

    public void setHasSuspected(boolean hasSuspected) {
        this.hasSuspected = hasSuspected;
        notifyList();
    }

    public boolean getHasAccused() {
        return hasAccused;
    }

    public void setHasAccused(boolean hasAccused) {
        this.hasAccused = hasAccused;
        notifyList();
    }

    public boolean getHasSuspected() {
        return hasSuspected;
    }

    public String getWeapon() {
        return weapon;
    }

    public String getCharacter() {
        return character;
    }

    public interface ChangeListener{
        void onChange();
    }
}
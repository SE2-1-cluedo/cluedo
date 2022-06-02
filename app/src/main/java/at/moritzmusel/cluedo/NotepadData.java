package at.moritzmusel.cluedo;

import java.io.Serializable;

public class NotepadData implements Serializable {
    private static boolean state;
    private static boolean [] checkboxes;

    public static boolean[] getCheckboxes() {
        return checkboxes;
    }

    public static void setCheckboxes(boolean[] checkboxes, int number) {
        NotepadData.checkboxes[number] = true;
    }

    public static boolean getState() {
        return state;
    }

    public static void setState(boolean state) {
        NotepadData.state = state;
    }
}

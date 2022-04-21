package at.moritzmusel.cluedo.entities;

public enum Character {
    Miss_Scarlet, Colonel_Mustard, Mrs_White, The_Reverend_Green, Mrs_Peacock, Professor_Plum;

    /**
     * This method returns the next character in turn. When current character ends turn,
     * It's useful for the game to know who the next acting character is.
     *
     * @return --- the next character in turn.
     */
    public Character nextPlayer() {
        switch (this) {
            case Colonel_Mustard:
                return Mrs_White;
            case Miss_Scarlet:
                return Colonel_Mustard;
            case Mrs_Peacock:
                return Professor_Plum;
            case Mrs_White:
                return The_Reverend_Green;
            case Professor_Plum:
                return Miss_Scarlet;
            case The_Reverend_Green:
                return Mrs_Peacock;
            default:
                return null;
        }
    }

}

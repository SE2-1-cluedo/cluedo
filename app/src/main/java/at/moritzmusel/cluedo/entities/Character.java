package at.moritzmusel.cluedo.entities;

public enum Character {
    MISS_SCARLET, COLONEL_MUSTARD, DR_ORCHID, THE_REVEREND_GREEN, MRS_PEACOCK, PROFESSOR_PLUM;

    /**
     * This method returns the next character in turn. When current character ends turn,
     * It's useful for the game to know who the next acting character is.
     *
     * @return --- the next character in turn.
     */
    public Character getNextCharacter() {
        switch (this) {
            case COLONEL_MUSTARD:
                return DR_ORCHID;
            case MISS_SCARLET:
                return COLONEL_MUSTARD;
            case MRS_PEACOCK:
                return PROFESSOR_PLUM;
            case DR_ORCHID:
                return THE_REVEREND_GREEN;
            case PROFESSOR_PLUM:
                return MISS_SCARLET;
            case THE_REVEREND_GREEN:
                return MRS_PEACOCK;
            default:
                return null;
        }
    }
}

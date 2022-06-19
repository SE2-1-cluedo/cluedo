package at.moritzmusel.cluedo.entities;

public enum Character {
    MISS_SCARLETT, COLONEL_MUSTARD, DR_ORCHID, REVEREND_GREEN, MRS_PEACOCK, PROFESSOR_PLUM;

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
            case MISS_SCARLETT:
                return COLONEL_MUSTARD;
            case MRS_PEACOCK:
                return PROFESSOR_PLUM;
            case DR_ORCHID:
                return REVEREND_GREEN;
            case PROFESSOR_PLUM:
                return MISS_SCARLETT;
            case REVEREND_GREEN:
                return MRS_PEACOCK;
            default:
                return null;
        }
    }

    public static Character getFirstCharacter() {
        return MISS_SCARLETT;
    }
}

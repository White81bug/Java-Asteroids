public enum Difficulty {
    EASY,
    NORMAL,
    HARD;

    public Difficulty next() {
        return values()[(this.ordinal() + 1) % values().length];
    }

    public Difficulty prev() {
        return values()[(this.ordinal() + values().length - 1) % values().length];
    }

    public float getSpawnCooldown() {
        switch (this) {
            case EASY:
                return 3.0f; //time to spawn an asteroid in seconds
            case NORMAL:
                return 2.0f;
            case HARD:
                return 1.0f;
        }
        return 2.0f; // fallback
    }

    public int getScoreValue() {
        switch (this) {
            case EASY:
                return 5;
            case NORMAL:
                return 10;
            case HARD:
                return 15;
        }
        return 10;
    }

}

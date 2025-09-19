package hhvrfn;

/**
 * Represents the type of a Task.
 */
public enum TaskType {
    TODO("T"),
    DEADLINE("D"),
    EVENT("E");

    private final String symbol;

    TaskType(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Returns the short symbol of this task type (e.g., "T", "D", "E").
     *
     * @return The symbol string of this type.
     */
    @Override
    public String toString() {
        return symbol;
    }
}

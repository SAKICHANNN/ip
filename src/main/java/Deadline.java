/**
 * Represents a task that should be done by a specific time.
 */
public class Deadline extends Task {
    protected String by;

    /**
     * Constructs a deadline task with the given description and due time.
     *
     * @param description The description of the task.
     * @param by The due time string.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the string representation of this deadline task.
     *
     * @return The string representation including the due time.
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}

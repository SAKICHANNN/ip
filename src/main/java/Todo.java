/**
 * Represents a to-do task without any date or time attached.
 */
public class Todo extends Task {

    /**
     * Constructs a to-do task with the given description.
     *
     * @param description The description of the task.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the string representation of this to-do task.
     *
     * @return The string representation in the form "[T]" + super string.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}

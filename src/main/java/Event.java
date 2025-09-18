/**
 * Represents a task that has a start time and an end time.
 */
public class Event extends Task {
    protected String from;
    protected String to;

    /**
     * Constructs an event task with the given description, start time, and end time.
     *
     * @param description The description of the event.
     * @param from The start time string.
     * @param to The end time string.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the string representation of this event task.
     *
     * @return The string representation including the start and end time.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}

package hhvrfn;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task that should be done by a specific time.
 */
public class Deadline extends Task {
    protected LocalDate by;

    /**
     * Constructs a deadline task with the given description and due time.
     *
     * @param description The description of the task.
     * @param by The due time string.
     */
    public Deadline(String description, LocalDate by) {
        super(description, TaskType.DEADLINE);
        this.by = by;
    }

    /**
     * Reschedules this deadline to a new date.
     *
     * @param newDate New due date (non-null).
     */
    public void reschedule(LocalDate newDate) {
        assert newDate != null : "Deadline.reschedule: newDate must be non-null";
        this.by = newDate;
    }

    /**
     * Returns the string representation of this deadline task.
     *
     * @return The string representation including the due time.
     */
    @Override
    public String toString() {
        // Display as "Oct 15 2019"
        String pretty = by.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
        return "[" + type + "][" + getStatusIcon() + "] " + description + " (by: " + pretty + ")";
    }
}

package hhvrfn;

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
        super(description, TaskType.TODO);
    }
}

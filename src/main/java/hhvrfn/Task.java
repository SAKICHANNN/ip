package hhvrfn;

/**
 * Represents a task with a description and a completion status.
 */
public class Task {
    protected String description;
    protected boolean isDone;
    protected TaskType type;

    /**
     * Constructs a task with the given description and type.
     * By default, the task is not done.
     *
     * @param description The description of the task.
     * @param type        The task type.
     */
    public Task(String description, TaskType type) {
        this.description = description;
        this.isDone = false;
        this.type = type;
    }

    /**
     * Returns the status icon of the task.
     *
     * @return "X" if done, " " if not done.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Marks this task as done.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void markAsNotDone() {
        isDone = false;
    }
    
    /**
     * Returns type of this task.
     */
    public TaskType getType() {
        return type;
    }

    /**
     * Returns the string representation of the task.
     * Format: "[{type}][{status}] {description}" where type is "T"/"D"/"E"
     *
     * @return The string representation of the task.
     */
    @Override
    public String toString() {
        return "[" + type + "][" + getStatusIcon() + "] " + description;
    }
}

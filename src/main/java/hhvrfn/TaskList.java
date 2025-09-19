package hhvrfn;

import java.util.ArrayList;
import java.util.List;

/**
 * Wraps the list of tasks and provides basic operations.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Constructs an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Constructs a task list from an existing list (e.g., loaded from storage).
     *
     * @param initial Tasks to populate with.
     */
    public TaskList(ArrayList<Task> initial) {
        this.tasks = new ArrayList<>(initial);
    }

    /**
     * Returns the number of tasks.
     *
     * @return number of tasks
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns whether the list is empty.
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Gets a task at zero-based index (no bounds checking).
     *
     * @param indexZeroBased index of the task to retrieve
     * @return the task at the given index
     */
    public Task get(int indexZeroBased) {
        return tasks.get(indexZeroBased);
    }

    /**
     * Adds a task (no saving here).
     *
     * @param task the task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes and returns a task at zero-based index.
     *
     * @param indexZeroBased index of the task to remove
     * @return the removed task
     */
    public Task remove(int indexZeroBased) {
        return tasks.remove(indexZeroBased);
    }

    /**
     * Returns the underlying list (for storage).
     *
     * @return the internal ArrayList of tasks
     */
    public ArrayList<Task> asList() {
        return tasks;
    }

    /**
     * Returns tasks whose string representation contains the given keyword
     * (case-insensitive). Matching is done against the task's display text,
     * which includes the description.
     *
     * @param keyword keyword to search for (non-empty)
     * @return a new list containing matching tasks in original order
     */
    public List<Task> findByKeyword(String keyword) {
        String needle = keyword.toLowerCase();
        List<Task> result = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            Task t = tasks.get(i);
            if (t.toString().toLowerCase().contains(needle)) {
                result.add(t);
            }
        }
        return result;
    }
}

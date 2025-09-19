package hhvrfn;

import java.util.List;
import java.util.Scanner;

/**
 * Handles all user interactions (printing lines/messages and reading input).
 * Each UI block prints with a divider line before and after to match expected output.
 */
public class Ui {
    private static final String LINE = "_".repeat(60) + System.lineSeparator();
    private static final String GREETING = "Hello! I'm hhvrfn" + System.lineSeparator()
            + "What can I do for you?";
    private static final String FAREWELL = "Bye. Hope to see you again soon!";

    /**
     * Prints the divider line to standard output.
     */
    public void showLine() {
        System.out.print(LINE);
    }

    /**
     * Shows the greeting message with divider lines.
     */
    public void showGreeting() {
        showLine();
        System.out.println(GREETING);
        showLine();
    }

    /**
     * Shows the farewell message with divider lines.
     */
    public void showFarewell() {
        showLine();
        System.out.println(FAREWELL);
        showLine();
    }

    /**
     * Reads a command line from the given scanner.
     *
     * @param scanner Scanner to read from.
     * @return The raw input line (may be empty).
     */
    public String readCommand(Scanner scanner) {
        return scanner.nextLine();
    }

    /**
     * Shows full list of tasks.
     *
     * @param tasks TaskList to print.
     */
    public void showList(TaskList tasks) {
        showLine();
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + ". " + tasks.get(i));
        }
        showLine();
    }

    /**
     * Shows the "added" feedback.
     *
     * @param task The task added.
     * @param total Current size of the list.
     */
    public void showAdded(Task task, int total) {
        showLine();
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + total + " tasks in the list.");
        showLine();
    }

    /**
     * Shows the "marked done" feedback.
     */
    public void showMarked(Task task) {
        showLine();
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + task);
        showLine();
    }

    /**
     * Shows the "marked not done" feedback.
     */
    public void showUnmarked(Task task) {
        showLine();
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + task);
        showLine();
    }

    /**
     * Shows the "deleted" feedback.
     */
    public void showDeleted(Task removed, int remaining) {
        showLine();
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + removed);
        System.out.println(" Now you have " + remaining + " tasks in the list.");
        showLine();
    }

    /**
     * Shows an error message in a standard block.
     */
    public void showError(String message) {
        showLine();
        System.out.println(" " + message);
        showLine();
    }

    /**
     * Shows a loading/storage error message in a standard block.
     */
    public void showLoadingError(String message) {
        showError(message);
    }

    /**
     * Shows matching tasks for a find operation.
     *
     * @param matches tasks that matched the user's keyword.
     */
    public void showFindResults(List<Task> matches) {
        showLine();
        System.out.println(" Here are the matching tasks in your list:");
        for (int i = 0; i < matches.size(); i++) {
            System.out.println(" " + (i + 1) + ". " + matches.get(i));
        }
        showLine();
    }

    /**
     * Shows the "rescheduled/snoozed" feedback.
     *
     * @param task The task that has been rescheduled.
     */
    public void showSnoozed(Task task) {
        showLine();
        System.out.println(" OK, I've rescheduled this task:");
        System.out.println("   " + task);
        showLine();
    }
}

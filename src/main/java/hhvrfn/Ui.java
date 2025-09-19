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
     * Shows an error message in a standard block with improved formatting.
     */
    public void showError(String message) {
        showLine();
        System.out.println(" ❌ Error: " + message);

        // Add helpful hints for common errors
        if (message.toLowerCase().contains("unknown command")) {
            System.out.println();
            System.out.println(" 💡 Tip: Type 'list' to see your tasks, 'todo <description>' to add a task,");
            System.out.println("         or 'help' for more commands.");
        } else if (message.toLowerCase().contains("empty description") || message.toLowerCase().contains("non-empty")) {
            System.out.println();
            System.out.println(" 💡 Tip: Make sure to provide a description for your task.");
        } else if (message.toLowerCase().contains("invalid date") || message.toLowerCase().contains("date")) {
            System.out.println();
            System.out.println(" 💡 Tip: Use the format yyyy-MM-dd for dates (e.g., 2024-12-25).");
        } else if (message.toLowerCase().contains("index")) {
            System.out.println();
            System.out.println(" 💡 Tip: Use 'list' to see task numbers, then use those numbers in your command.");
        }

        showLine();
    }

    /**
     * Shows a loading/storage error message in a standard block with special formatting.
     */
    public void showLoadingError(String message) {
        showLine();
        System.out.println(" ⚠️ Data Loading Issue: " + message);

        if (message.toLowerCase().contains("permission denied")) {
            System.out.println();
            System.out.println(" 💡 How to fix: Check file permissions or try running from a different location.");
        } else if (message.toLowerCase().contains("not found")) {
            System.out.println();
            System.out.println(" 💡 Note: Starting with an empty task list. Your tasks will be saved automatically.");
        } else if (message.toLowerCase().contains("disk space")) {
            System.out.println();
            System.out.println(" 💡 How to fix: Free up some disk space and try again.");
        }

        showLine();
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

    /**
     * Shows help information with all available commands.
     */
    public void showHelp() {
        showLine();
        System.out.println(" 📚 Available Commands:");
        System.out.println();
        System.out.println(" 📋 Task Management:");
        System.out.println("   list                          - Show all tasks");
        System.out.println("   todo <description>            - Add a new todo task");
        System.out.println("   deadline <desc> /by yyyy-MM-dd - Add a deadline task");
        System.out.println("   event <desc> /from <time> /to <time> - Add an event task");
        System.out.println();
        System.out.println(" ✅ Task Operations:");
        System.out.println("   mark <number>                 - Mark task as done");
        System.out.println("   unmark <number>               - Mark task as not done");
        System.out.println("   delete <number>               - Delete a task");
        System.out.println("   find <keyword>                - Search for tasks");
        System.out.println("   snooze <number> /to yyyy-MM-dd - Reschedule a deadline");
        System.out.println();
        System.out.println(" 🚪 Other:");
        System.out.println("   help                          - Show this help message");
        System.out.println("   bye                           - Exit the program");
        System.out.println();
        System.out.println(" 💡 Examples:");
        System.out.println("   todo Read a book");
        System.out.println("   deadline Submit assignment /by 2024-12-25");
        System.out.println("   event Team meeting /from 2pm /to 4pm");
        System.out.println("   mark 1");
        System.out.println("   find book");
        showLine();
    }
}

import java.util.Scanner;

/**
 * A simple console-based echo chatbot.
 * Prints a greeting, echoes user input verbatim, and exits when the user types "bye".
 */
public class Hhvrfn {
    private static final String LINE = "_".repeat(60) + System.lineSeparator();
    private static final String GREETING = "Hello! I'm hhvrfn" + System.lineSeparator()
            + "What can I do for you?";
    private static final String FAREWELL = "Bye. Hope to see you again soon!";
    private static final int MAX_TASKS = 100;

    // In-memory storage
    private static final String[] tasks = new String[MAX_TASKS];
    private static int taskCount = 0;

    /**
    * Prints a horizontal separator line of underscores.
    */
    private static void printLine() {
        System.out.println(LINE);
    }

    /**
    * Adds a task (up to MAX_TASKS) and prints the feedback block.
    *
    * @param description the task description entered by the user
    */
    private static void addTask(String description) {
        if (taskCount < MAX_TASKS) {
            tasks[taskCount++] = description;
            printLine();
            System.out.println(" added: " + description);
            printLine();
        } else {
            printLine();
            System.out.println(" storage full: cannot add more than " + MAX_TASKS + " tasks");
            printLine();
        }
    }

    /**
    * Lists all tasks stored in memory (1-based numbering), wrapped with separator lines.
    * Prints nothing but separators if the list is empty.
    */
    private static void listTasks() {
        printLine();
        for (int i = 0; i < taskCount; i++) {
            System.out.println(" " + (i + 1) + ". " + tasks[i]);
        }
        printLine();
    }

    /**
    * Starts the chatbot application: prints greeting, stores user input,
    * lists all items and exits when the user types "bye".
    *
    * @param args command-line arguments (unused)
    */
    public static void main(String[] args) {
        printLine();
        System.out.println(GREETING);
        printLine();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                String input = scanner.nextLine();

                if (input.equals("bye")) {
                    printLine();
                    System.out.println(FAREWELL);
                    printLine();
                    break;
                } else if (input.equals("list")) {
                    listTasks();
                } else {
                    addTask(input);
                }
            }
        }
    }
}

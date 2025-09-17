import java.util.Scanner;

/**
 * A simple console-based chatbot with task management features.
 * Supports adding tasks, listing tasks, and marking/unmarking tasks as done.
 */
public class Hhvrfn {
    private static final String LINE = "_".repeat(60) + System.lineSeparator();
    private static final String GREETING = "Hello! I'm hhvrfn" + System.lineSeparator()
            + "What can I do for you?";
    private static final String FAREWELL = "Bye. Hope to see you again soon!";
    private static final int MAX_TASKS = 100;

    // Class-based storage
    private static final Task[] tasks = new Task[MAX_TASKS];
    private static int taskCount = 0;

    private static void printLine() {
        System.out.println(LINE);
    }

    private static void addTask(String description) {
        if (taskCount < MAX_TASKS) {
            tasks[taskCount++] = new Task(description);
            printLine();
            System.out.println(" added: " + description);
            printLine();
        } else {
            printLine();
            System.out.println(" storage full: cannot add more than " + MAX_TASKS + " tasks");
            printLine();
        }
    }

    private static void listTasks() {
        printLine();
        for (int i = 0; i < taskCount; i++) {
            System.out.println(" " + (i + 1) + ". " + tasks[i]);
        }
        printLine();
    }

    private static void markTask(int index) {
        if (index >= 1 && index <= taskCount) {
            Task t = tasks[index - 1];
            t.markAsDone();
            printLine();
            System.out.println(" Nice! I've marked this task as done:");
            System.out.println("   " + t);
            printLine();
        }
    }

    private static void unmarkTask(int index) {
        if (index >= 1 && index <= taskCount) {
            Task t = tasks[index - 1];
            t.markAsNotDone();
            printLine();
            System.out.println(" OK, I've marked this task as not done yet:");
            System.out.println("   " + t);
            printLine();
        }
    }

    /**
    * Starts the chatbot application: prints greeting, reacts with user,
    * and exits when the user types "bye".
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
                } else if (input.startsWith("mark ")) {
                    int index = Integer.parseInt(input.split(" ")[1]);
                    markTask(index);
                } else if (input.startsWith("unmark ")) {
                    int index = Integer.parseInt(input.split(" ")[1]);
                    unmarkTask(index);
                } else {
                    addTask(input);
                }
            }
        }
    }
}

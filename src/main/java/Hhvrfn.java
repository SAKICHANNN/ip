import java.util.Scanner;

/**
 * A simple console-based chatbot with task management features.
 * Supports adding tasks, listing tasks, marking/unmarking tasks,
 * and creating todo/deadline/event tasks.
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
    // Legacy method for adding tasks
    private static void addTaskLegacy(String description) {
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
        System.out.println(" Here are the tasks in your list:");
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

    private static void addTodo(String description) {
        if (taskCount < MAX_TASKS) {
            Todo t = new Todo(description);
            tasks[taskCount++] = t;
            printLine();
            System.out.println(" Got it. I've added this task:");
            System.out.println("   " + t);
            System.out.println(" Now you have " + taskCount + " tasks in the list.");
            printLine();
        } else {
            printLine();
            System.out.println(" storage full: cannot add more than " + MAX_TASKS + " tasks");
            printLine();
        }
    }

    private static void addDeadline(String description, String by) {
        if (taskCount < MAX_TASKS) {
            Deadline t = new Deadline(description, by);
            tasks[taskCount++] = t;
            printLine();
            System.out.println(" Got it. I've added this task:");
            System.out.println("   " + t);
            System.out.println(" Now you have " + taskCount + " tasks in the list.");
            printLine();
        } else {
            printLine();
            System.out.println(" storage full: cannot add more than " + MAX_TASKS + " tasks");
            printLine();
        }
    }

    private static void addEvent(String description, String from, String to) {
        if (taskCount < MAX_TASKS) {
            Event t = new Event(description, from, to);
            tasks[taskCount++] = t;
            printLine();
            System.out.println(" Got it. I've added this task:");
            System.out.println("   " + t);
            System.out.println(" Now you have " + taskCount + " tasks in the list.");
            printLine();
        } else {
            printLine();
            System.out.println(" storage full: cannot add more than " + MAX_TASKS + " tasks");
            printLine();
        }
    }

    private static void handleInput(String input) throws HhvrfnException {
        if (input == null || input.trim().isEmpty()) {
            return; // ignore empty lines
        }

        if (input.equals("list")) {
            listTasks();
            return;
        } else if (input.startsWith("mark ")) {
            int index = Integer.parseInt(input.split(" ")[1]);
            markTask(index);
            return;
        } else if (input.startsWith("unmark ")) {
            int index = Integer.parseInt(input.split(" ")[1]);
            unmarkTask(index);
            return;
        } else if (input.equals("todo")) {
            // Error case 1: todo without description
            throw new HhvrfnException("Todo needs a non-empty description.");
        } else if (input.startsWith("todo ")) {
            String desc = input.substring(5).trim();
            if (desc.isEmpty()) {
                throw new HhvrfnException("Todo needs a non-empty description.");
            }
            addTodo(desc);
            return;
        } else if (input.startsWith("deadline ")) {
            String rest = input.substring(9).trim();
            int byPos = rest.indexOf("/by ");
            if (byPos >= 0) {
                String desc = rest.substring(0, byPos).trim();
                String by = rest.substring(byPos + 4).trim();
                addDeadline(desc, by);
                return;
            }
            // fallback to legacy add for malformed deadline
        } else if (input.startsWith("event ")) {
            String rest = input.substring(6).trim();
            int fromPos = rest.indexOf("/from ");
            int toPos = rest.indexOf("/to ");
            if (fromPos >= 0 && toPos >= 0 && toPos > fromPos) {
                String desc = rest.substring(0, fromPos).trim();
                String from = rest.substring(fromPos + 6, toPos).trim();
                String to = rest.substring(toPos + 4).trim();
                addEvent(desc, from, to);
                return;
            }
            // fallback to legacy add for malformed event
        }

        // Error case 2: unknown single-word command (e.g., "blah")
        if (!input.contains(" ")) {
            throw new HhvrfnException(
                    "Unknown command. Try: list, todo, deadline, event, mark, unmark, bye.");
        }

        // Legacy: treat multi-word free text as task
        addTaskLegacy(input);
    }

    /**
     * Starts the chatbot application.
     * Greets the user, processes commands, and exits on "bye".
     *
     * @param args Command-line arguments (unused).
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
                }

                try {
                    handleInput(input);
                } catch (HhvrfnException e) {
                    printLine();
                    System.out.println(" " + e.getMessage());
                    printLine();
                }
            }
        }
    }
}

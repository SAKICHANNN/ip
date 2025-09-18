import java.util.ArrayList;
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

    // ArrayList-based storage
    private static final ArrayList<Task> tasks = new ArrayList<>();

    private static void printLine() {
        System.out.println(LINE);
    }
    // Legacy method for adding tasks
    private static void addTaskLegacy(String description) {
        tasks.add(new Task(description, TaskType.TODO));
        printLine();
        System.out.println(" added: " + description);
        printLine();
    }

    private static void listTasks() {
        printLine();
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + ". " + tasks.get(i));
        }
        printLine();
    }

    private static void markTask(int index) throws HhvrfnException {
        if (index < 1 || index > tasks.size()) {
            throw new HhvrfnException("Invalid index for mark. Use 1.." + tasks.size());
        }
        Task t = tasks.get(index - 1);
        t.markAsDone();
        printLine();
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + t);
        printLine();
    }

    private static void unmarkTask(int index) throws HhvrfnException {
        if (index < 1 || index > tasks.size()) {
            throw new HhvrfnException("Invalid index for unmark. Use 1.." + tasks.size());
        }
        Task t = tasks.get(index - 1);
        t.markAsNotDone();
        printLine();
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + t);
        printLine();
    }

    private static void addTodo(String description) {
        Todo t = new Todo(description);
        tasks.add(t);
        printLine();
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + t);
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
        printLine();
    }

    private static void addDeadline(String description, String by) {
        Deadline t = new Deadline(description, by);
        tasks.add(t);
        printLine();
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + t);
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
        printLine();
    }

    private static void addEvent(String description, String from, String to) {
        Event t = new Event(description, from, to);
        tasks.add(t);
        printLine();
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + t);
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
        printLine();
    }

    private static void deleteTask(int index) throws HhvrfnException {
        if (index < 1 || index > tasks.size()) {
            throw new HhvrfnException("Invalid index for delete. Use 1.." + tasks.size());
        }
        Task removed = tasks.remove(index - 1);
        printLine();
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + removed);
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
        printLine();
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
        } else if (input.startsWith("delete ")) {
            int index = Integer.parseInt(input.split(" ")[1]);
            deleteTask(index);
            return;
        }

        if (!input.contains(" ")) {
            throw new HhvrfnException(
                    "Unknown command. Try: list, todo, deadline, event, mark, unmark, delete, bye.");
        }

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

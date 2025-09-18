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
    // Disk-based storage
    private static final Storage storage = new Storage("./data/hhvrfn.txt");

    private static void printLine() {
        System.out.println(LINE);
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
        if (tasks.isEmpty()) {
            throw new HhvrfnException("Your list is empty.");
        }
        if (index < 1 || index > tasks.size()) {
            throw new HhvrfnException("Invalid index for mark. Use 1.." + tasks.size());
        }
        Task t = tasks.get(index - 1);
        t.markAsDone();
        printLine();
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + t);
        printLine();
        storage.save(tasks);
    }

    private static void unmarkTask(int index) throws HhvrfnException {
        if (tasks.isEmpty()) {
            throw new HhvrfnException("Your list is empty.");
        }
        if (index < 1 || index > tasks.size()) {
            throw new HhvrfnException("Invalid index for unmark. Use 1.." + tasks.size());
        }
        Task t = tasks.get(index - 1);
        t.markAsNotDone();
        printLine();
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + t);
        printLine();
        storage.save(tasks);
    }

    private static void addTodo(String description) throws HhvrfnException {
        Todo t = new Todo(description);
        tasks.add(t);
        printLine();
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + t);
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
        printLine();
        storage.save(tasks);
    }

    private static void addDeadline(String description, String by) throws HhvrfnException {
        Deadline t = new Deadline(description, by);
        tasks.add(t);
        printLine();
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + t);
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
        printLine();
        storage.save(tasks);
    }

    private static void addEvent(String description, String from, String to) throws HhvrfnException {
        Event t = new Event(description, from, to);
        tasks.add(t);
        printLine();
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + t);
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
        printLine();
        storage.save(tasks);
    }
    private static void deleteTask(int index) throws HhvrfnException {
        if (tasks.isEmpty()) {
            throw new HhvrfnException("Your list is empty.");
        }
        if (index < 1 || index > tasks.size()) {
            throw new HhvrfnException("Invalid index for delete. Use 1.." + tasks.size());
        }
        Task removed = tasks.remove(index - 1);
        printLine();
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + removed);
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
        printLine();
        storage.save(tasks);
    }

    private static void handleInput(String input) throws HhvrfnException {
        if (input == null || input.trim().isEmpty()) {
            return; // ignore empty lines
        }

        if (input.equals("list")) {
            listTasks();
            return;
        } else if (input.startsWith("mark ")) {
            int index = parseIndex(input);
            markTask(index);
            return;
        } else if (input.startsWith("unmark ")) {
            int index = parseIndex(input);
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
            if (byPos < 0) {
                throw new HhvrfnException("Usage: deadline DESCRIPTION /by BY");
            }
            String desc = rest.substring(0, byPos).trim();
            String by = rest.substring(byPos + 4).trim();
            if (desc.isEmpty() || by.isEmpty()) {
                throw new HhvrfnException("Deadline description and /by must be non-empty.");
            }
            addDeadline(desc, by);
            return;
        } else if (input.startsWith("event ")) {
            String rest = input.substring(6).trim();
            int fromPos = rest.indexOf("/from ");
            int toPos = rest.indexOf("/to ");
            if (fromPos < 0 || toPos < 0 || toPos <= fromPos) {
                throw new HhvrfnException("Usage: event DESCRIPTION /from FROM /to TO");
            }
            String desc = rest.substring(0, fromPos).trim();
            String from = rest.substring(fromPos + 6, toPos).trim();
            String to = rest.substring(toPos + 4).trim();
            if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
                throw new HhvrfnException("Event description, /from, and /to must be non-empty.");
            }
            addEvent(desc, from, to);
            return;
        } else if (input.startsWith("delete ")) {
            int index = parseIndex(input);
            deleteTask(index);
            return;
        }

        if (!input.contains(" ")) {
            throw new HhvrfnException(
                    "Unknown command. Try: list, todo, deadline, event, mark, unmark, delete, bye.");
        }

    }

    private static int parseIndex(String input) throws HhvrfnException {
        String[] parts = input.trim().split("\\s+");
        if (parts.length != 2) {
            throw new HhvrfnException("Usage: mark|unmark|delete <positive integer>");
        }
        try {
            int idx = Integer.parseInt(parts[1]);
            if (idx <= 0) {
                throw new HhvrfnException("Index must be a positive integer.");
            }
            return idx;
        } catch (NumberFormatException nfe) {
            throw new HhvrfnException("Index must be a positive integer.");
        }
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
            // Load tasks once at startup
            try {
                ArrayList<Task> loaded = storage.load();
                tasks.clear();
                tasks.addAll(loaded);
            } catch (HhvrfnException e) {
                printLine();
                System.out.println(" " + e.getMessage());
                printLine();
            }
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

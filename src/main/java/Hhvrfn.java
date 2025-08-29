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

    /**
     * Starts the application: prints greeting, echoes input, and exits on "bye".
     *
     * @param args command-line arguments (unused)
     */
    private static void printLine() {
        System.out.println(LINE);
    }

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
                } else {
                    printLine();
                    System.out.println(" " + input);
                    printLine();
                }
            }
        }
    }
}

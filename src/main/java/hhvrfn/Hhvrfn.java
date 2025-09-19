package hhvrfn;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main entry point that wires Ui, Storage, Parser, and TaskList.
 * Behavior and output format remain consistent with earlier levels.
 */
public class Hhvrfn {
    private final Ui ui;
    private final Storage storage;
    private TaskList tasks;

    /**
     * Constructs the app with given storage file path.
     *
     * @param filePath Relative path to data file, e.g., "./data/hhvrfn.txt".
     */
    public Hhvrfn(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        try {
            ArrayList<Task> loaded = storage.load();
            this.tasks = new TaskList(loaded);
        } catch (HhvrfnException e) {
            ui.showLoadingError(e.getMessage());
            this.tasks = new TaskList();
        }
    }

    /**
     * Runs the main interaction loop until the user enters "bye".
     * Continuously reads user input, processes commands, and handles errors.
     */
    public void run() {
        ui.showGreeting();
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                String input = ui.readCommand(scanner);
                if ("bye".equals(input)) {
                    ui.showFarewell();
                    break;
                }
                try {
                    Parser.process(input, tasks, ui, storage);
                } catch (HhvrfnException e) {
                    ui.showError(e.getMessage());
                }
            }
        }
    }

    /**
     * Program entry.
     *
     * @param args CLI args (unused).
     */
    public static void main(String[] args) {
        new Hhvrfn("./data/hhvrfn.txt").run();
    }
}

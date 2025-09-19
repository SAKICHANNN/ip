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
        Logger.info("Initializing CLI application with data file: " + filePath);
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        try {
            ArrayList<Task> loaded = storage.load();
            this.tasks = new TaskList(loaded);
        } catch (HhvrfnException e) {
            Logger.error("Failed to load data during CLI initialization", new Exception(e.getMessage()));
            ui.showLoadingError(e.getMessage());
            this.tasks = new TaskList();
        }
    }

    /**
     * Runs the main interaction loop until the user enters "bye".
     * Continuously reads user input, processes commands, and handles errors.
     */
    public void run() {
        Logger.info("Starting CLI application loop");
        ui.showGreeting();
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                String input = ui.readCommand(scanner);
                if ("bye".equals(input)) {
                    Logger.info("User initiated application exit");
                    ui.showFarewell();
                    break;
                }
                try {
                    Parser.process(input, tasks, ui, storage);
                } catch (HhvrfnException e) {
                    Logger.error("Command processing error: " + e.getMessage());
                    ui.showError(e.getMessage());
                }
            }
        }
        Logger.info("CLI application ended");
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

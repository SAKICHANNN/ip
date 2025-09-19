package hhvrfn;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * A minimal JavaFX GUI for Duke/Hhvrfn.
 * This GUI provides a text area for output, a text field for input,
 * and a Send button to process user commands.
 */
public class Main extends Application {

    private final TaskList taskList;
    private final Storage storage = new Storage("./data/hhvrfn.txt");
    private String loadingErrorMessage = null;

    /**
     * Constructs the Main application with error handling for data loading.
     */
    public Main() {
        Logger.info("Initializing GUI application");
        // Initialize TaskList with error handling for data loading
        TaskList tempTaskList;
        try {
            ArrayList<Task> loadedTasks = storage.load();
            tempTaskList = new TaskList(loadedTasks);
        } catch (HhvrfnException e) {
            // If loading fails, start with empty task list and remember the error
            Logger.error("Failed to load data during GUI initialization", new Exception(e.getMessage()));
            tempTaskList = new TaskList();
            loadingErrorMessage = e.getMessage();
        }
        this.taskList = tempTaskList;
    }

    @Override
    public void start(Stage stage) {
        TextArea dialog = new TextArea();
        dialog.setEditable(false);

        TextField input = new TextField();
        Button send = new Button("Send");

        Ui ui = new UiCapture(dialog);

        Runnable doSend = () -> {
            String text = input.getText().trim();
            if (text.isEmpty()) {
                return;
            }
            input.clear();

            // Echo user input
            dialog.appendText("> " + text + System.lineSeparator());

            // --- handle "bye" here (same logic as CLI loop) ---
            if ("bye".equals(text)) {
                Logger.info("User initiated application exit");
                ui.showFarewell();
                Platform.exit();
                return;
            }

            // Other commands go to Parser
            try {
                Parser.process(text, taskList, ui, storage);
            } catch (HhvrfnException e) {
                ui.showError(e.getMessage());
            }
        };

        send.setOnAction(e -> doSend.run());
        input.setOnAction(e -> doSend.run());

        VBox root = new VBox(8);
        root.getChildren().addAll(new ScrollPane(dialog), new HBox(8, input, send));

        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Hhvrfn");
        stage.show();

        ui.showGreeting();

        // Show loading error if there was one
        if (loadingErrorMessage != null) {
            ui.showLoadingError(loadingErrorMessage);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

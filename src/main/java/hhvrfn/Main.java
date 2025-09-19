package hhvrfn;

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

    private final TaskList taskList = new TaskList();
    private final Storage storage = new Storage("./data/hhvrfn.txt");

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
    }

    public static void main(String[] args) {
        launch(args);
    }
}

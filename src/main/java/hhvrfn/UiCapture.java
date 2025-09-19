package hhvrfn;

import javafx.scene.control.TextArea;

/**
 * A UI adapter that writes program output into a {@code TextArea}.
 */
public class UiCapture extends Ui {
    private final TextArea out;

    public UiCapture(TextArea out) {
        this.out = out;
    }

    @Override
    public void showLine() {
        out.appendText("____________________________________________________________\n");
    }

    @Override
    public void showGreeting() {
        out.appendText("Hello! I'm hhvrfn\nWhat can I do for you?\n");
    }

    @Override
    public void showFarewell() {
        out.appendText("Bye. Hope to see you again soon!\n");
    }

    @Override
    public void showList(TaskList tasks) {
        out.appendText("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            out.appendText((i + 1) + ". " + tasks.get(i) + "\n");
        }
    }

    @Override
    public void showAdded(Task task, int total) {
        out.appendText("Got it. I've added this task: " + task + "\n");
        out.appendText("Now you have " + total + " tasks in the list.\n");
    }

    @Override
    public void showMarked(Task task) {
        out.appendText("Nice! I've marked this task as done: " + task + "\n");
    }

    @Override
    public void showUnmarked(Task task) {
        out.appendText("OK, I've marked this task as not done yet: " + task + "\n");
    }

    @Override
    public void showDeleted(Task removed, int remaining) {
        out.appendText("Noted. I've removed this task: " + removed + "\n");
        out.appendText("Now you have " + remaining + " tasks in the list.\n");
    }

    @Override
    public void showError(String message) {
        out.appendText("[Error] " + message + "\n");
    }
}

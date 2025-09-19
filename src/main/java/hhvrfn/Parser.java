package hhvrfn;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Parses and executes a single user command against the given model/components.
 * This parser dispatches to small handlers and preserves existing behavior.
 */
public final class Parser {

    /* ================== Centralized messages & usages ================== */

    private static final String MSG_UNKNOWN =
            "Unknown command. Try: list, todo, deadline, event, mark, unmark, delete, find, snooze, bye.";
    private static final String MSG_EMPTY_LIST = "Your list is empty.";
    private static final String ERR_TODO_EMPTY = "Todo needs a non-empty description.";
    private static final String ERR_FIND_EMPTY = "Find needs a non-empty keyword.";
    private static final String ERR_DEADLINE_EMPTY = "Deadline description and /by must be non-empty.";
    private static final String ERR_EVENT_EMPTY = "Event description, /from, and /to must be non-empty.";
    private static final String ERR_DATE_INVALID = "Invalid date. Use yyyy-MM-dd, e.g., 2019-10-15.";

    private static final String USAGE_MUTATE_INDEX = "Usage: mark|unmark|delete <positive integer>";
    private static final String USAGE_DEADLINE = "Usage: deadline DESCRIPTION /by yyyy-MM-dd";
    private static final String USAGE_EVENT = "Usage: event DESCRIPTION /from FROM /to TO";
    private static final String USAGE_SNOOZE = "Usage: snooze INDEX /to yyyy-MM-dd";

    /** Utility class; no instantiation. */
    private Parser() { }

    /**
     * Processes one input line and executes the corresponding action.
     * On success, UI is updated and mutations are persisted via {@code storage}.
     *
     * @param input   raw user input
     * @param tasks   task list model
     * @param ui      UI facade to render results
     * @param storage storage used to persist mutations
     * @throws HhvrfnException if user input is invalid or a recoverable I/O error occurs
     */
    public static void process(String input, TaskList tasks, Ui ui, Storage storage) throws HhvrfnException {
        assert tasks != null && ui != null && storage != null
                : "Parser.process(): collaborators must be non-null";

        if (input == null || input.trim().isEmpty()) {
            // Ignore empty lines.
            return;
        }

        if (input.equals("list")) {
            handleList(tasks, ui);
            return;
        }
        if (input.startsWith("mark ")) {
            handleMark(input, tasks, ui, storage);
            return;
        }
        if (input.startsWith("unmark ")) {
            handleUnmark(input, tasks, ui, storage);
            return;
        }
        if (input.equals("todo") || input.startsWith("todo ")) {
            handleTodo(input, tasks, ui, storage);
            return;
        }
        if (input.startsWith("deadline ")) {
            handleDeadline(input, tasks, ui, storage);
            return;
        }
        if (input.startsWith("event ")) {
            handleEvent(input, tasks, ui, storage);
            return;
        }
        if (input.startsWith("delete ")) {
            handleDelete(input, tasks, ui, storage);
            return;
        }
        if (input.equals("find") || input.startsWith("find ")) {
            handleFind(input, tasks, ui);
            return;
        }
        if (input.equals("snooze") || input.startsWith("snooze ")) {
            handleSnooze(input, tasks, ui, storage);
            return;
        }

        throw new HhvrfnException(MSG_UNKNOWN);
    }

    /* ============================= Handlers ============================ */

    // Shows the full list; no persistence.
    private static void handleList(TaskList tasks, Ui ui) {
        ui.showList(tasks);
    }

    // Marks a task as done and persists.
    private static void handleMark(String input, TaskList tasks, Ui ui, Storage storage) throws HhvrfnException {
        final int index = parseIndex(input);
        ensureNotEmpty(tasks, MSG_EMPTY_LIST);
        ensureInRange(index, tasks.size(), "Invalid index for mark. Use 1.." + tasks.size());
        final Task t = tasks.get(index - 1);
        t.markAsDone();
        ui.showMarked(t);
        storage.save(tasks.asList());
    }

    // Marks a task as not done and persists.
    private static void handleUnmark(String input, TaskList tasks, Ui ui, Storage storage) throws HhvrfnException {
        final int index = parseIndex(input);
        ensureNotEmpty(tasks, MSG_EMPTY_LIST);
        ensureInRange(index, tasks.size(), "Invalid index for unmark. Use 1.." + tasks.size());
        final Task t = tasks.get(index - 1);
        t.markAsNotDone();
        ui.showUnmarked(t);
        storage.save(tasks.asList());
    }

    // Adds a TODO and persists. Handles both "todo" and "todo xxx".
    private static void handleTodo(String input, TaskList tasks, Ui ui, Storage storage) throws HhvrfnException {
        final String desc = input.equals("todo") ? "" : input.substring(5).trim();
        if (desc.isEmpty()) {
            throw new HhvrfnException(ERR_TODO_EMPTY);
        }
        final Task t = new Todo(desc);
        tasks.add(t);
        ui.showAdded(t, tasks.size());
        storage.save(tasks.asList());
    }

    // Adds a Deadline and persists.
    private static void handleDeadline(String input, TaskList tasks, Ui ui, Storage storage) throws HhvrfnException {
        final String rest = input.substring(9).trim();
        final int byPos = rest.indexOf("/by ");
        if (byPos < 0) {
            throw new HhvrfnException(USAGE_DEADLINE);
        }
        final String desc = rest.substring(0, byPos).trim();
        final String by = rest.substring(byPos + 4).trim();
        if (desc.isEmpty() || by.isEmpty()) {
            throw new HhvrfnException(ERR_DEADLINE_EMPTY);
        }
        try {
            final LocalDate date = LocalDate.parse(by); // yyyy-MM-dd.
            final Task t = new Deadline(desc, date);
            tasks.add(t);
            ui.showAdded(t, tasks.size());
            storage.save(tasks.asList());
        } catch (DateTimeParseException dtpe) {
            throw new HhvrfnException(ERR_DATE_INVALID);
        }
    }

    // Adds an Event and persists.
    private static void handleEvent(String input, TaskList tasks, Ui ui, Storage storage) throws HhvrfnException {
        final String rest = input.substring(6).trim();
        final int fromPos = rest.indexOf("/from ");
        final int toPos = rest.indexOf("/to ");
        if (fromPos < 0 || toPos < 0 || toPos <= fromPos) {
            throw new HhvrfnException(USAGE_EVENT);
        }
        final String desc = rest.substring(0, fromPos).trim();
        final String from = rest.substring(fromPos + 6, toPos).trim();
        final String to = rest.substring(toPos + 4).trim();
        if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new HhvrfnException(ERR_EVENT_EMPTY);
        }
        final Task t = new Event(desc, from, to); // Event still uses String.
        tasks.add(t);
        ui.showAdded(t, tasks.size());
        storage.save(tasks.asList());
    }

    // Deletes a task and persists.
    private static void handleDelete(String input, TaskList tasks, Ui ui, Storage storage) throws HhvrfnException {
        final int index = parseIndex(input);
        ensureNotEmpty(tasks, MSG_EMPTY_LIST);
        ensureInRange(index, tasks.size(), "Invalid index for delete. Use 1.." + tasks.size());
        final Task removed = tasks.remove(index - 1);
        ui.showDeleted(removed, tasks.size());
        storage.save(tasks.asList());
    }

    // Finds tasks by keyword; no persistence. Handles both "find" and "find xxx".
    private static void handleFind(String input, TaskList tasks, Ui ui) throws HhvrfnException {
        if (input.equals("find")) {
            throw new HhvrfnException(ERR_FIND_EMPTY);
        }
        final String keyword = input.substring(5).trim();
        if (keyword.isEmpty()) {
            throw new HhvrfnException(ERR_FIND_EMPTY);
        }
        final List<Task> matches = tasks.findByKeyword(keyword);
        ui.showFindResults(matches);
    }

    // Snoozes (reschedules) a Deadline and persists.
    private static void handleSnooze(String input, TaskList tasks, Ui ui, Storage storage) throws HhvrfnException {
        // Format: snooze INDEX /to yyyy-MM-dd
        final String rest = input.length() == 6 ? "" : input.substring(7).trim(); // after "snooze"
        if (rest.isEmpty()) {
            throw new HhvrfnException(USAGE_SNOOZE);
        }

        // split first token as index, remaining as "/to ..."
        final String[] parts = rest.split("\\s+", 2);
        if (parts.length < 2) {
            throw new HhvrfnException(USAGE_SNOOZE);
        }

        final int index = parseIndex("mark " + parts[0]);
        ensureNotEmpty(tasks, MSG_EMPTY_LIST);
        ensureInRange(index, tasks.size(), "Invalid index for snooze. Use 1.." + tasks.size());

        final String afterIndex = parts[1].trim();
        final int toPos = afterIndex.indexOf("/to ");
        if (toPos < 0) {
            throw new HhvrfnException(USAGE_SNOOZE);
        }
        final String dateStr = afterIndex.substring(toPos + 4).trim();
        if (dateStr.isEmpty()) {
            throw new HhvrfnException(USAGE_SNOOZE);
        }

        final Task task = tasks.get(index - 1);
        if (!(task instanceof Deadline)) {
            throw new HhvrfnException("Only deadlines can be snoozed/rescheduled.");
        }

        try {
            final LocalDate newDate = LocalDate.parse(dateStr); // yyyy-MM-dd
            final Deadline d = (Deadline) task;
            d.reschedule(newDate);
            ui.showSnoozed(d);
            storage.save(tasks.asList());
        } catch (DateTimeParseException dtpe) {
            throw new HhvrfnException(ERR_DATE_INVALID);
        }
    }


    /* ============================== Helpers ============================= */

    // Parses a one-argument index command (e.g., "mark 2").
    private static int parseIndex(String input) throws HhvrfnException {
        final String[] parts = input.trim().split("\\s+");
        if (parts.length != 2) {
            throw new HhvrfnException(USAGE_MUTATE_INDEX);
        }
        try {
            final int idx = Integer.parseInt(parts[1]);
            if (idx <= 0) {
                throw new HhvrfnException("Index must be a positive integer.");
            }
            return idx;
        } catch (NumberFormatException nfe) {
            throw new HhvrfnException("Index must be a positive integer.");
        }
    }

    // Throws if the task list is empty.
    private static void ensureNotEmpty(TaskList tasks, String message) throws HhvrfnException {
        if (tasks.isEmpty()) {
            throw new HhvrfnException(message);
        }
    }

    // Throws if a one-based index is outside [1, size].
    private static void ensureInRange(int oneBasedIndex, int size, String message) throws HhvrfnException {
        if (oneBasedIndex < 1 || oneBasedIndex > size) {
            throw new HhvrfnException(message);
        }
    }
}

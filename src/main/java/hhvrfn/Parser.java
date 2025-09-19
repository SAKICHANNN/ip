package hhvrfn;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Parses and executes a single user command against the given model/components.
 * This is a minimal Parser (no Command classes yet).
 */
public final class Parser {
    private Parser() {}

    /**
     * Processes one input line. On success, prints via Ui and saves via Storage if mutated.
     *
     * @param input   Raw user input.
     * @param tasks   Task list.
     * @param ui      UI facade.
     * @param storage Storage to persist changes.
     * @throws HhvrfnException For user errors or I/O errors reported in a user-friendly way.
     */
    public static void process(String input, TaskList tasks, Ui ui, Storage storage) throws HhvrfnException {
        if (input == null || input.trim().isEmpty()) {
            return; // ignore empty lines
        }

        if (input.equals("list")) {
            ui.showList(tasks);
            return;
        } else if (input.startsWith("mark ")) {
            int index = parseIndex(input);
            ensureNotEmpty(tasks, "Your list is empty.");
            ensureInRange(index, tasks.size(), "Invalid index for mark. Use 1.." + tasks.size());
            Task t = tasks.get(index - 1);
            t.markAsDone();
            ui.showMarked(t);
            storage.save(tasks.asList());
            return;
        } else if (input.startsWith("unmark ")) {
            int index = parseIndex(input);
            ensureNotEmpty(tasks, "Your list is empty.");
            ensureInRange(index, tasks.size(), "Invalid index for unmark. Use 1.." + tasks.size());
            Task t = tasks.get(index - 1);
            t.markAsNotDone();
            ui.showUnmarked(t);
            storage.save(tasks.asList());
            return;
        } else if (input.equals("todo")) {
            throw new HhvrfnException("Todo needs a non-empty description.");
        } else if (input.startsWith("todo ")) {
            String desc = input.substring(5).trim();
            if (desc.isEmpty()) {
                throw new HhvrfnException("Todo needs a non-empty description.");
            }
            Task t = new Todo(desc);
            tasks.add(t);
            ui.showAdded(t, tasks.size());
            storage.save(tasks.asList());
            return;
        } else if (input.startsWith("deadline ")) {
            String rest = input.substring(9).trim();
            int byPos = rest.indexOf("/by ");
            if (byPos < 0) {
                throw new HhvrfnException("Usage: deadline DESCRIPTION /by yyyy-MM-dd");
            }
            String desc = rest.substring(0, byPos).trim();
            String by = rest.substring(byPos + 4).trim();
            if (desc.isEmpty() || by.isEmpty()) {
                throw new HhvrfnException("Deadline description and /by must be non-empty.");
            }
            try {
                LocalDate date = LocalDate.parse(by); // L8: yyyy-MM-dd
                Task t = new Deadline(desc, date);
                tasks.add(t);
                ui.showAdded(t, tasks.size());
                storage.save(tasks.asList());
                return;
            } catch (DateTimeParseException dtpe) {
                throw new HhvrfnException("Invalid date. Use yyyy-MM-dd, e.g., 2019-10-15.");
            }
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
            Task t = new Event(desc, from, to); // L8: Event still uses String
            tasks.add(t);
            ui.showAdded(t, tasks.size());
            storage.save(tasks.asList());
            return;
        } else if (input.startsWith("delete ")) {
            int index = parseIndex(input);
            ensureNotEmpty(tasks, "Your list is empty.");
            ensureInRange(index, tasks.size(), "Invalid index for delete. Use 1.." + tasks.size());
            Task removed = tasks.remove(index - 1);
            ui.showDeleted(removed, tasks.size());
            storage.save(tasks.asList());
            return;
        } else if (input.equals("find")) {
            throw new HhvrfnException("Find needs a non-empty keyword.");
        } else if (input.startsWith("find ")) {
            String keyword = input.substring(5).trim();
            if (keyword.isEmpty()) {
                throw new HhvrfnException("Find needs a non-empty keyword.");
            }
            List<Task> matches = tasks.findByKeyword(keyword);
            ui.showFindResults(matches);
            return;
        }

        throw new HhvrfnException(
                "Unknown command. Try: list, todo, deadline, event, mark, unmark, delete, bye.");
    }

    // --- helpers ---

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

    private static void ensureNotEmpty(TaskList tasks, String message) throws HhvrfnException {
        if (tasks.isEmpty()) {
            throw new HhvrfnException(message);
        }
    }

    private static void ensureInRange(int oneBasedIndex, int size, String message) throws HhvrfnException {
        if (oneBasedIndex < 1 || oneBasedIndex > size) {
            throw new HhvrfnException(message);
        }
    }
}

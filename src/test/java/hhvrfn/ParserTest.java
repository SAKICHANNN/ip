package hhvrfn;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class ParserTest {

    static class StubUi extends Ui {
        List<String> lines = new ArrayList<>();
        @Override public void showAdded(Task t, int total) { lines.add("added:" + t.toString()); }
        @Override public void showMarked(Task t) { lines.add("marked:" + t.toString()); }
        @Override public void showError(String msg) { lines.add("error:" + msg); }
        @Override public void showList(TaskList tasks) { lines.add("list:" + tasks.size()); }
        @Override public void showDeleted(Task removed, int remaining) { lines.add("deleted:" + remaining); }
        @Override public void showUnmarked(Task t) { lines.add("unmarked:" + t.toString()); }
    }

    static class StubStorage extends Storage {
        int saves = 0;
        public StubStorage() { super("N/A"); }
        @Override public void save(ArrayList<Task> tasks) { saves++; }
        @Override public ArrayList<Task> load() { return new ArrayList<>(); }
    }

    @Test
    void todo_then_mark_flows_ok() throws Exception {
        TaskList tl = new TaskList();
        StubUi ui = new StubUi();
        StubStorage st = new StubStorage();

        Parser.process("todo read book", tl, ui, st);
        Parser.process("mark 1", tl, ui, st);

        assertEquals(1, tl.size());
        assertTrue(tl.get(0).toString().contains("[X]"));
        assertTrue(ui.lines.get(0).startsWith("added:"));
        assertTrue(ui.lines.get(1).startsWith("marked:"));
        assertEquals(2, st.saves);
    }

    @Test
    void delete_outOfRange_throwsFriendlyError() {
        TaskList tl = new TaskList();
        StubUi ui = new StubUi();
        StubStorage st = new StubStorage();

        HhvrfnException ex = assertThrows(HhvrfnException.class,
                () -> Parser.process("delete 1", tl, ui, st));
        assertTrue(ex.getMessage().toLowerCase().contains("empty"));
    }
}

package hhvrfn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class ParserTest {

    static class StubUi extends Ui {
        private final List<String> lines = new ArrayList<>();

        public List<String> getLines() {
            return lines;
        }

        @Override
        public void showAdded(Task t, int total) {
            lines.add("added:" + t.toString());
        }

        @Override
        public void showMarked(Task t) {
            lines.add("marked:" + t.toString());
        }

        @Override
        public void showError(String msg) {
            lines.add("error:" + msg);
        }

        @Override
        public void showList(TaskList tasks) {
            lines.add("list:" + tasks.size());
        }

        @Override
        public void showDeleted(Task removed, int remaining) {
            lines.add("deleted:" + remaining);
        }

        @Override
        public void showUnmarked(Task t) {
            lines.add("unmarked:" + t.toString());
        }
    }

    static class StubStorage extends Storage {
        private int saves = 0;

        StubStorage() {
            super("N/A");
        }

        public int getSaves() {
            return saves;
        }

        @Override
        public void save(ArrayList<Task> tasks) {
            saves++;
        }

        @Override
        public ArrayList<Task> load() {
            return new ArrayList<>();
        }
    }

    @Test
    void todoThenMark_ok() throws Exception {
        TaskList tl = new TaskList();
        StubUi ui = new StubUi();
        StubStorage st = new StubStorage();

        Parser.process("todo read book", tl, ui, st);
        Parser.process("mark 1", tl, ui, st);

        assertEquals(1, tl.size());
        assertTrue(tl.get(0).toString().contains("[X]"));
        assertTrue(ui.getLines().get(0).startsWith("added:"));
        assertTrue(ui.getLines().get(1).startsWith("marked:"));
        assertEquals(2, st.getSaves());
    }

    @Test
    void delete_outOfRange_throwsFriendlyError() {
        TaskList tl = new TaskList();
        StubUi ui = new StubUi();
        StubStorage st = new StubStorage();

        HhvrfnException ex = assertThrows(HhvrfnException.class, () -> Parser.process("delete 1", tl, ui, st));
        assertTrue(ex.getMessage().toLowerCase().contains("empty"));
    }
}

package hhvrfn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TaskListTest {

    @Test
    void addAndGetTask_success() {
        TaskList list = new TaskList();
        Todo todo = new Todo("read book");
        list.add(todo);

        assertEquals(1, list.size());
        assertEquals(todo, list.get(0));
    }

    @Test
    void removeTask_success() {
        TaskList list = new TaskList();
        Todo todo = new Todo("read book");
        list.add(todo);
        list.remove(0);

        assertTrue(list.isEmpty());
    }
}

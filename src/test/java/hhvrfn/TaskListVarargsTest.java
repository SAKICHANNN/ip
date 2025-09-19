package hhvrfn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

/**
 * Tests for TaskList varargs overload.
 */
public class TaskListVarargsTest {

    @Test
    void addMultipleTasks_success() {
        TaskList list = new TaskList();
        Todo t1 = new Todo("task one");
        Todo t2 = new Todo("task two");

        // Use the varargs overload
        list.add(t1, t2);

        assertEquals(2, list.size());
        assertEquals(t1, list.get(0));
        assertEquals(t2, list.get(1));
    }

    @Test
    void addNullOrEmptyVarargs_noop() {
        TaskList list = new TaskList(new ArrayList<>());

        list.add((Task[]) null); // should be a no-op
        assertTrue(list.isEmpty());

        list.add(); // empty varargs; also a no-op
        assertTrue(list.isEmpty());
    }
}

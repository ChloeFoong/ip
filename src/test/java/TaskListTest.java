import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/** Tests adding and removing tasks from TaskList. */
class TaskListTest {
    /** TaskList should store tasks in insertion order. */
    @Test
    void tasksCanBeAddedAndRemoved() {
        TaskList list = new TaskList();
        list.add(new Todo("first"));
        list.add(new Deadline("second", "Sunday"));

        assertEquals(2, list.size());
        assertEquals("first", list.get(0).getDescription());
        assertEquals("second", list.remove(1).getDescription());
        assertEquals(1, list.size());
    }
}

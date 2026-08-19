import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** Tests the core behavior of Task objects. */
class TaskTest {
    /** A newly created task should be incomplete. */
    @Test
    void newTaskIsIncomplete() {
        Task task = new Todo("read book");

        assertEquals(" ", task.getStatusIcon());
        assertEquals("[T][ ] read book", task.toString());
    }

    /** Marking and unmarking should update the task status. */
    @Test
    void taskCanBeMarkedAndUnmarked() {
        Task task = new Todo("read book");

        task.markAsDone();
        assertTrue(task.toString().contains("[X]"));

        task.markAsUndone();
        assertTrue(task.toString().contains("[ ]"));
    }
}

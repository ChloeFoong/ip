package probe.task;

import java.util.ArrayList;
import java.util.List;
import probe.ProbeException;

/**
 * Owns the tasks and provides operations for changing the task list.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        tasks = new ArrayList<>(); 
    }

    /**
     * Creates a task list containing a copy of the supplied tasks.
     *
     * @param tasks Tasks to copy into the list.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks); 
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task Task to add.
     */
    public void add(Task task) {
        tasks.add(task); 
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return Number of tasks.
     */
    public int size() {
        return tasks.size(); 
    }

    /**
     * Returns a copy of the tasks in their current order.
     *
     * @return Copy of the tasks.
     */
    public List<Task> asList() {
        return new ArrayList<>(tasks); 
    }

    /**
     * Returns the task at a one-based task number.
     *
     * @param number One-based task number.
     * @return Task at the specified number.
     * @throws ProbeException If the task number is invalid.
     */
    public Task get(int number) throws ProbeException {
        check(number); return tasks.get(number - 1); 
    }

    /**
     * Removes and returns the task at a one-based task number.
     *
     * @param number One-based task number.
     * @return Removed task.
     * @throws ProbeException If the task number is invalid.
     */
    public Task delete(int number) throws ProbeException {
        check(number); return tasks.remove(number - 1); 
    }
    /**
     * Validates that a one-based task number refers to an existing task.
     *
     * @param number One-based task number to validate.
     * @throws ProbeException If the task number is invalid.
     */
    private void check(int number) throws ProbeException {
        if (number < 1 || number > tasks.size()) throw new ProbeException("That task number does not exist.");
    }
}

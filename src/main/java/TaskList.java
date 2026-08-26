import java.util.ArrayList;
import java.util.List;

/** Owns the tasks and provides operations for changing the task list. */
public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList() { 
        tasks = new ArrayList<>(); 
    }

    public TaskList(List<Task> tasks) { 
        this.tasks = new ArrayList<>(tasks); 
    }

    public void add(Task task) { 
        tasks.add(task); 
    }

    public int size() { 
        return tasks.size(); 
    }

    public List<Task> asList() { 
        return new ArrayList<>(tasks); 
    }

    public Task get(int number) throws ProbeException { 
        check(number); return tasks.get(number - 1); 
    }

    public Task delete(int number) throws ProbeException { 
        check(number); return tasks.remove(number - 1); 
    }
    private void check(int number) throws ProbeException {
        if (number < 1 || number > tasks.size()) throw new ProbeException("That task number does not exist.");
    }
}

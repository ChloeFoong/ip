package probe;

import probe.parser.Parser;
import probe.storage.Storage;
import probe.task.Task;
import probe.task.TaskList;
import probe.ui.Ui;

/**
 * Coordinates the user interface, parser, task list, and storage.
 */
public class Probe {
    private static final String FILE_PATH = "probe.txt";
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;
    private final Parser parser;

    /**
     * Creates an application using the specified task-storage file.
     *
     * @param filePath Path of the task-storage file.
     */
    public Probe(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        parser = new Parser();
        tasks = new TaskList(storage.load());
    }

    /**
     * Runs the command loop until the user enters {@code bye}.
     */
    public void run() {
        ui.showWelcome();
        while (true) {
            String command = ui.readCommand();
            if (command.equals("bye")) {
                ui.showGoodbye();
                break;
            }
            try {
                if (command.equals("list")) {
                    ui.showList(tasks.asList());
                } else if (command.startsWith("todo") || command.startsWith("deadline")
                        || command.startsWith("event")) {
                    Task task = parser.parseTask(command);
                    tasks.add(task);
                    storage.save(tasks.asList());
                    ui.showAdded(task, tasks.size());
                } else if (command.startsWith("delete ")) {
                    Task removed = tasks.delete(parser.parseNumber(command, "Please provide a task number to delete."));
                    storage.save(tasks.asList());
                    ui.showRemoved(removed, tasks.size());
                } else if (command.startsWith("mark ") || command.startsWith("unmark ")) {
                    boolean mark = command.startsWith("mark ");
                    Task task = tasks.get(parser.parseNumber(command, "Please provide a task number."));
                    if (mark) {
                        task.markAsDone();
                    } else {
                        task.markAsUndone();
                    }
                    storage.save(tasks.asList()); ui.showUpdated(task, mark);
                } else if (command.equals("find") || command.startsWith("find ")) {
                    String keyword = command.length() > 4 ? command.substring(4).trim() : "";
                    if (keyword.isBlank()) {
                        throw new ProbeException("Enter some keyword to search.");
                    }
                    TaskList list = tasks.search(keyword);
                    ui.showMatch(list.asList());
                } else {
                    ui.showMessage("Invalid task type. Use todo, deadline, or event.");
                }
            } catch (ProbeException e) {
                ui.showMessage(e.getMessage());
            }
        }
        ui.close();
    }

    /**
     * Starts the application using the default storage file.
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        new Probe(FILE_PATH).run(); 
    }
}

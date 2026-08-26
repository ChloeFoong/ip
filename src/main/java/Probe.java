import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Probe {
    private static final String FILE_PATH = "probe.txt";
    private static final String BANNER = "____________________________________________________________\n"
            + " ____  ____   ___  ____  _____\n"
            + "|  _ \\|  _ \\ / _ \\| __ )| ____|\n"
            + "| |_) | |_) | | | |  _ \\|  _|\n"
            + "|  __/|  _ <| |_| | |_) | |___\n"
            + "|_|   |_| \\_\\\\___/|____/|_____|\n";

    /** Reads and responds to commands until the user enters bye. */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Storage storage = new Storage(FILE_PATH);
        List<Task> taskList = storage.load();
        ArrayList<Task> tasks = new ArrayList<>(taskList);

        System.out.println(BANNER);
        System.out.println("Hello! I'm Probe.");
        System.out.println("What can I do for you?");
        System.out.println("____________________________________________________________");

        while (true) {
            String command = scanner.nextLine().trim();

            if (command.isEmpty()) {
                System.out.println("The command cannot be empty.");
            } else if (command.equals("bye")) {
                System.out.println("    ____________________________________________________________");
                System.out.println("    " + "Bye. Hope to see you again soon!");
                System.out.println("    ____________________________________________________________");
                break;
            } else if (command.equals("list")) {
                System.out.println("    ____________________________________________________________");
                System.out.println("     Here are the tasks in your list:");
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println("     " + (i + 1) + ". " + tasks.get(i));
                }
                System.out.println("    ____________________________________________________________");
            } else if (command.equals("todo") || command.startsWith("todo ")
                    || command.equals("deadline") || command.startsWith("deadline ")
                    || command.equals("event") || command.startsWith("event ")) {
                try {
                    Task task = createTask(command);
                    tasks.add(task);
                    storage.save(tasks);
                    printAddedMessage(task, tasks.size());
                } catch (ProbeException e) {
                    System.out.println(e.getMessage());
                }
            } else if (command.startsWith("delete ")) {
                try {
                    deleteTask(tasks, command, storage);
                } catch (ProbeException e) {
                    System.out.println(e.getMessage());
                }
            } else if (command.startsWith("unmark ")) {
                updateTask(tasks, tasks.size(), command, false, storage);
            } else if (command.startsWith("mark ")) {
                updateTask(tasks, tasks.size(), command, true, storage);
            } else {
                System.out.println("Invalid task type. Use todo, deadline, or event.");
            }
        }

        scanner.close();
    }

    /** Creates the appropriate task subtype or throws for invalid task input. */
    private static Task createTask(String command) throws ProbeException {
        if (command.startsWith("todo")) {
            String description = command.substring(4).trim();
            if (description.isBlank()) {
                throw new ProbeException("A todo description cannot be empty.");
            }
            return new Todo(description);
        }

        if (command.startsWith("deadline")) {
            String content = command.substring(8).trim();
            String[] parts = content.split(" /by ", 2);
            if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) {
                throw new ProbeException(
                        "A deadline must include a description and /by a date or time.");
            }
            return new Deadline(parts[0], parts[1]);
        }

        String content = command.substring(5).trim();
        String[] parts = content.split(" /from ", 2);
        if (parts.length != 2 || parts[0].isBlank()) {
            throw new ProbeException(
                    "An event must include a description and /from a starting date or time.");
        }

        String[] times = parts[1].split(" /to ", 2);
        if (times.length != 2 || times[0].isBlank() || times[1].isBlank()) {
            throw new ProbeException(
                    "An event must include /to and an ending date or time.");
        }
        return new Event(parts[0], times[0], times[1]);
    }

    /** Displays confirmation after adding a task. */
    private static void printAddedMessage(Task task, int taskCount) {
        System.out.println("    ____________________________________________________________");
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + taskCount + " tasks in the list.");
        System.out.println("    ____________________________________________________________");
    }

    /** Marks or unmarks a task after validating the requested task number. */
    private static void updateTask(ArrayList<Task> tasks, int taskCount,
            String command, boolean markAsDone, Storage storage) {
        String[] parts = command.split(" ");

        if (parts.length != 2) {
            System.out.println("Please provide a task number.");
            return;
        }

        try {
            int taskNumber = Integer.parseInt(parts[1]);

            if (taskNumber < 1 || taskNumber > taskCount) {
                System.out.println("That task number does not exist.");
                return;
            }

            Task task = tasks.get(taskNumber - 1);

            System.out.println("    ____________________________________________________________");
            if (markAsDone) {
                task.markAsDone();
                System.out.println("    Nice! I've marked this task as done:");
            } else {
                task.markAsUndone();
                System.out.println("    OK, I've marked this task as not done yet:");
            }

            storage.save(tasks);

            System.out.println("       [" + task.getStatusIcon() + "] "
                    + task.getDescription());
            System.out.println("    ____________________________________________________________");
        } catch (NumberFormatException e) {
            System.out.println("The task number must be a number.");
        }
    }

    /** Removes a task after validating its list number. */
    private static void deleteTask(ArrayList<Task> tasks, String command, Storage storage)
            throws ProbeException {
        String[] parts = command.split(" ");

        if (parts.length != 2) {
            throw new ProbeException("Please provide a task number to delete.");
        }

        try {
            int taskNumber = Integer.parseInt(parts[1]);

            if (taskNumber < 1 || taskNumber > tasks.size()) {
                throw new ProbeException("That task number does not exist.");
            }

            Task removedTask = tasks.remove(taskNumber - 1);
            
            storage.save(tasks);

            System.out.println("    ____________________________________________________________");
            System.out.println("     Noted. I've removed this task:");
            System.out.println("       " + removedTask);
            System.out.println("     Now you have " + tasks.size() + " tasks in the list.");
            System.out.println("    ____________________________________________________________");
        } catch (NumberFormatException e) {
            throw new ProbeException("The task number must be a number.");
        }
    }
}
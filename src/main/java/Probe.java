import java.util.Scanner;

/** Runs the Probe chatbot. */
public class Probe {
    private static final String BANNER = "____________________________________________________________\n"
            + " ____  ____   ___  ____  _____\n"
            + "|  _ \\|  _ \\ / _ \\| __ )| ____|\n"
            + "| |_) | |_) | | | |  _ \\|  _|\n"
            + "|  __/|  _ <| |_| | |_) | |___\n"
            + "|_|   |_| \\_\\\\___/|____/|_____|\n";

    /** Reads and responds to commands until the user enters bye. */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[100];
        int taskCount = 0;

        System.out.println(BANNER);
        System.out.println("Hello! I'm Probe.");
        System.out.println("What can I do for you?");
        System.out.println("____________________________________________________________");

        while (true) {
            String command = scanner.nextLine();

            if (command.equals("bye")) {
                System.out.println("    ____________________________________________________________");
                System.out.println("    " + "Bye. Hope to see you again soon!");
                System.out.println("    ____________________________________________________________");
                break;
            } else if (command.equals("list")) {
                System.out.println("    ____________________________________________________________");
                System.out.println("     Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println("     " + (i + 1) + ". " + tasks[i]);
                }
                System.out.println("    ____________________________________________________________");
            } else if (command.startsWith("todo ")) {
                String description = command.substring(5);
                addTask(tasks, taskCount, new Todo(description));
                taskCount++;
                printAddedMessage(tasks[taskCount - 1], taskCount);
            } else if (command.startsWith("deadline ")) {
                String content = command.substring(9);
                String[] parts = content.split(" /by ", 2);

                if (parts.length == 2) {
                    addTask(tasks, taskCount, new Deadline(parts[0], parts[1]));
                    taskCount++;
                    printAddedMessage(tasks[taskCount - 1], taskCount);
                } else {
                    System.out.println("A deadline must include /by and a date or time.");
                }
            } else if (command.startsWith("event ")) {
                String content = command.substring(6);
                String[] parts = content.split(" /from ", 2);

                if (parts.length == 2) {
                    String[] times = parts[1].split(" /to ", 2);

                    if (times.length == 2) {
                        addTask(tasks, taskCount,
                                new Event(parts[0], times[0], times[1]));
                        taskCount++;
                        printAddedMessage(tasks[taskCount - 1], taskCount);
                    } else {
                        System.out.println("An event must include /to and an ending date or time.");
                    }
                } else {
                    System.out.println("An event must include /from and a starting date or time.");
                }
            } else if (command.startsWith("unmark ")) {
                updateTask(tasks, taskCount, command, false);
            } else if (command.startsWith("mark ")) {
                updateTask(tasks, taskCount, command, true);
            } else {
                System.out.println("    ____________________________________________________________");
                Task t = new Task(command);
                tasks[taskCount] = t;
                taskCount++;
                System.out.println("    added: " + command);
                System.out.println("    ____________________________________________________________");
            }
        }

        scanner.close();
    }

    /** Stores a task at the next available position in the task array. */
    private static void addTask(Task[] tasks, int taskCount, Task task) {
        tasks[taskCount] = task;
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
    private static void updateTask(Task[] tasks, int taskCount,
                                   String command, boolean markAsDone) {
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

            Task task = tasks[taskNumber - 1];

            System.out.println("    ____________________________________________________________");
            if (markAsDone) {
                task.markAsDone();
                System.out.println("    Nice! I've marked this task as done:");
            } else {
                task.markAsUndone();
                System.out.println("    OK, I've marked this task as not done yet:");
            }

            System.out.println("       [" + task.getStatusIcon() + "] "
                    + task.getDescription());
            System.out.println("    ____________________________________________________________");
        } catch (NumberFormatException e) {
            System.out.println("The task number must be a number.");
        }
    }
}

package probe.ui;

import java.util.List;
import java.util.Scanner;
import probe.task.Task;

public class Ui {
    private static final String SEPARATOR = "    ____________________________________________________________";
    private static final String BANNER = "____________________________________________________________\n"
            + " ____  ____   ___  ____  _____\n"
            + "|  _ \\|  _ \\ / _ \\| __ )| ____|\n"
            + "| |_) | |_) | | | |  _ \\|  _|\n"
            + "|  __/|  _ <| |_| | |_) | |___\n"
            + "|_|   |_| \\_\\\\___/|____/|_____|\n";
    private final Scanner scanner = new Scanner(System.in);
    public String readCommand() { return scanner.nextLine().trim(); }
    public void showWelcome() {
        System.out.println(BANNER);
        System.out.println("Hello! I'm Probe.");
        System.out.println("What can I do for you?");
        System.out.println("____________________________________________________________");
    }
    public void showGoodbye() {
        System.out.println(SEPARATOR);
        System.out.println("    Bye. Hope to see you again soon!");
        System.out.println(SEPARATOR);
    }
    public void showMessage(String message) { System.out.println(message); }
    public void showList(List<Task> tasks) {
        System.out.println(SEPARATOR);
        System.out.println("     Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) System.out.println("     " + (i + 1) + ". " + tasks.get(i));
        System.out.println(SEPARATOR);
    }
    public void showAdded(Task task, int count) {
        System.out.println(SEPARATOR);
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + count + " tasks in the list.");
        System.out.println(SEPARATOR);
    }
    public void showRemoved(Task task, int count) {
        System.out.println(SEPARATOR);
        System.out.println("     Noted. I've removed this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + count + " tasks in the list.");
        System.out.println(SEPARATOR);
    }
    public void showUpdated(Task task, boolean marked) {
        System.out.println(SEPARATOR);
        System.out.println(marked ? "    Nice! I've marked this task as done:"
                : "    OK, I've marked this task as not done yet:");
        System.out.println("       [" + task.getStatusIcon() + "] " + task.getDescription());
        System.out.println(SEPARATOR);
    }
    public void close() { scanner.close(); }
}

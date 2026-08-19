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
            }

            System.out.println("    ____________________________________________________________");
            System.out.println("    " + command);
            System.out.println("    ____________________________________________________________");
        }

        scanner.close();
    }
}

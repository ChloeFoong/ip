import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/** Provides a JavaFX graphical interface for the Probe chatbot. */
public class ProbeGui extends Application {
    private final TaskList tasks = new TaskList();
    private final TaskParser parser = new TaskParser();
    private final TextArea conversation = new TextArea();
    private final TextField commandInput = new TextField();

    /** Starts the Probe graphical user interface. */
    @Override
    public void start(Stage stage) {
        Label title = new Label("Probe");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        conversation.setEditable(false);
        conversation.setWrapText(true);
        conversation.setText("Hello! I'm Probe.\nWhat can I do for you?\n");

        commandInput.setPromptText("Enter a command, e.g. todo read book");
        commandInput.setOnAction(event -> processCommand());

        Button sendButton = new Button("Send");
        sendButton.setOnAction(event -> processCommand());

        HBox inputRow = new HBox(8, commandInput, sendButton);
        inputRow.setPadding(new Insets(10, 0, 0, 0));
        HBox.setHgrow(commandInput, javafx.scene.layout.Priority.ALWAYS);

        BorderPane layout = new BorderPane();
        layout.setTop(title);
        layout.setCenter(conversation);
        layout.setBottom(inputRow);
        layout.setPadding(new Insets(15));

        stage.setTitle("Probe");
        stage.setScene(new Scene(layout, 650, 450));
        stage.show();
    }

    /** Processes the command currently entered in the input field. */
    private void processCommand() {
        String command = commandInput.getText().trim();
        if (command.isEmpty()) {
            append("The command cannot be empty.");
            return;
        }

        append("> " + command);
        commandInput.clear();

        if (command.equals("bye")) {
            append("Bye. Hope to see you again soon!");
            return;
        }

        try {
            if (command.equals("list")) {
                if (tasks.isEmpty()) {
                    append("Your task list is empty.");
                } else {
                    for (int i = 0; i < tasks.size(); i++) {
                        append((i + 1) + ". " + tasks.get(i));
                    }
                }
            } else if (command.startsWith("delete ")) {
                int index = parseTaskNumber(command, "delete");
                Task removed = tasks.remove(index);
                append("Noted. I've removed this task: " + removed);
                append("Now you have " + tasks.size() + " tasks in the list.");
            } else if (command.startsWith("mark ") || command.startsWith("unmark ")) {
                boolean mark = command.startsWith("mark ");
                int index = parseTaskNumber(command, mark ? "mark" : "unmark");
                Task task = tasks.get(index);
                if (mark) {
                    task.markAsDone();
                    append("Nice! I've marked this task as done: " + task);
                } else {
                    task.markAsUndone();
                    append("OK, I've marked this task as not done yet: " + task);
                }
            } else if (command.startsWith("todo") || command.startsWith("deadline")
                    || command.startsWith("event")) {
                Task task = parser.parse(command);
                tasks.add(task);
                append("Got it. I've added this task: " + task);
                append("Now you have " + tasks.size() + " tasks in the list.");
            } else {
                throw new ProbeException("Invalid command. Use todo, deadline, event, list, mark, unmark, or delete.");
            }
        } catch (ProbeException | NumberFormatException | IndexOutOfBoundsException e) {
            append(e.getMessage() == null ? "That task number does not exist." : e.getMessage());
        }
    }

    /** Converts a one-based task number in a command to a zero-based list index. */
    private int parseTaskNumber(String command, String operation) throws ProbeException {
        String[] parts = command.split(" ");
        if (parts.length != 2) {
            throw new ProbeException("Please provide a task number for " + operation + ".");
        }
        int taskNumber;
        try {
            taskNumber = Integer.parseInt(parts[1]);
        } catch (NumberFormatException exception) {
            throw new ProbeException("The task number must be a number.");
        }
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new ProbeException("That task number does not exist.");
        }
        return taskNumber - 1;
    }

    /** Adds a line to the conversation area. */
    private void append(String message) {
        conversation.appendText(message + "\n");
    }

    /** Launches the JavaFX application. */
    public static void main(String[] args) {
        launch(args);
    }
}

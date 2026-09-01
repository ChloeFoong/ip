package probe.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import probe.ProbeException;
import probe.parser.Parser;
import probe.storage.Storage;
import probe.task.Task;
import probe.task.TaskList;

/**
 * Provides a chatbot-style JavaFX user interface for the Probe task manager.
 */
public class Main extends Application {
    private static final String FILE_PATH = "probe.txt";
    private final Parser parser = new Parser();
    private final Storage storage = new Storage(FILE_PATH);
    private final TaskList tasks = new TaskList(storage.load());
    private final VBox dialogContainer = new VBox(8);
    private final ScrollPane scrollPane = new ScrollPane(dialogContainer);
    private TextField userInput;

    /**
     * Displays the JavaFX chatbot window.
     *
     * @param stage Primary JavaFX window.
     */
    @Override
    public void start(Stage stage) {
        userInput = new TextField();
        userInput.setPromptText("Enter a command, e.g. todo read a book");
        Button sendButton = new Button("Send");
        sendButton.setOnAction(event -> handleInput());
        userInput.setOnAction(event -> handleInput());

        AnchorPane mainLayout = new AnchorPane(scrollPane, userInput, sendButton);
        configureLayout(mainLayout, sendButton);
        addProbeMessage("Hello! I'm Probe. How can I help you?");
        stage.setTitle("Probe");
        stage.setMinWidth(400);
        stage.setMinHeight(600);
        stage.setScene(new Scene(mainLayout, 500, 650));
        stage.show();
    }

    /**
     * Handles a command submitted through the chat input field.
     */
    private void handleInput() {
        String command = userInput.getText().trim();
        if (command.isBlank()) {
            return;
        }
        addUserMessage(command);
        userInput.clear();
        if (command.equals("bye")) {
            addProbeMessage("Bye. Hope to see you again soon!");
            Platform.exit();
            return;
        }
        try {
            addProbeMessage(executeCommand(command));
        } catch (ProbeException exception) {
            addProbeMessage(exception.getMessage());
        }
    }

    /**
     * Returns the response produced by executing a user command.
     *
     * @param command User command to execute.
     * @return Response to display in the chat.
     * @throws ProbeException If the command is invalid.
     */
    private String executeCommand(String command) throws ProbeException {
        if (command.equals("list")) {
            return formatTasks(tasks.asList());
        } else if (command.startsWith("todo") || command.startsWith("deadline")
                || command.startsWith("event")) {
            Task task = parser.parseTask(command);
            tasks.add(task);
            storage.save(tasks.asList());
            return "Added: " + task;
        } else if (command.startsWith("delete ")) {
            int number = parser.parseNumber(command, "Please provide a task number to delete.");
            Task removed = tasks.delete(number);
            storage.save(tasks.asList());
            return "Removed: " + removed;
        } else if (command.startsWith("mark ") || command.startsWith("unmark ")) {
            boolean marked = command.startsWith("mark ");
            int number = parser.parseNumber(command, "Please provide a task number.");
            Task task = tasks.get(number);
            if (marked) {
                task.markAsDone();
            } else {
                task.markAsUndone();
            }
            storage.save(tasks.asList());
            return marked ? "Marked as done: " + task : "Marked as not done: " + task;
        } else if (command.startsWith("find ")) {
            String keyword = command.substring(4).trim();
            if (keyword.isBlank()) {
                throw new ProbeException("Enter some keyword to search.");
            }
            return formatTasks(tasks.search(keyword).asList());
        }
        throw new ProbeException("I do not understand that command.");
    }

    /**
     * Configures the tutorial-style chat layout and its anchors.
     */
    private void configureLayout(AnchorPane mainLayout, Button sendButton) {
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        dialogContainer.setPadding(new Insets(10));
        AnchorPane.setTopAnchor(scrollPane, 0.0);
        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 55.0);
        AnchorPane.setLeftAnchor(userInput, 8.0);
        AnchorPane.setRightAnchor(userInput, 70.0);
        AnchorPane.setBottomAnchor(userInput, 8.0);
        AnchorPane.setRightAnchor(sendButton, 8.0);
        AnchorPane.setBottomAnchor(sendButton, 8.0);
    }

    /**
     * Adds a user message to the conversation.
     */
    private void addUserMessage(String message) {
        dialogContainer.getChildren().add(new DialogBox("You", message, true));
        scrollPane.setVvalue(1.0);
    }

    /**
     * Adds a Probe response to the conversation.
     */
    private void addProbeMessage(String message) {
        dialogContainer.getChildren().add(new DialogBox("Probe", message, false));
        scrollPane.setVvalue(1.0);
    }

    /**
     * Returns a readable response containing the supplied tasks.
     */
    private String formatTasks(java.util.List<Task> taskItems) {
        if (taskItems.isEmpty()) {
            return "There are no matching tasks.";
        }
        StringBuilder response = new StringBuilder();
        for (int i = 0; i < taskItems.size(); i++) {
            response.append(i + 1).append(". ").append(taskItems.get(i));
            if (i < taskItems.size() - 1) {
                response.append("\n");
            }
        }
        return response.toString();
    }
}

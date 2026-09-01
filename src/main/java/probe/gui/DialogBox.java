package probe.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Represents one message in the JavaFX chatbot conversation.
 */
public class DialogBox extends HBox {
    /**
     * Creates a message bubble for the specified speaker.
     *
     * @param speaker Name of the message sender.
     * @param message Message text.
     * @param fromUser Whether the message was sent by the user.
     */
    public DialogBox(String speaker, String message, boolean fromUser) {
        Label speakerLabel = new Label(speaker);
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(360);
        VBox content = new VBox(2, speakerLabel, messageLabel);
        content.setPadding(new Insets(8));
        content.setStyle(fromUser
                ? "-fx-background-color: #d9ecff; -fx-background-radius: 8;"
                : "-fx-background-color: #eeeeee; -fx-background-radius: 8;");
        setAlignment(fromUser ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        getChildren().add(content);
    }
}

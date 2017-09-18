package de.treichels.hott.vdfeditor.ui;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class MessageDialog extends Alert {
    public static void show(final AlertType alertType, final String header, final String message, final Object... args) {
        Platform.runLater(() -> new MessageDialog(alertType, header, message, args).showAndWait());
    }

    public MessageDialog(final AlertType alertType, final String header, final String message, final Object... args) {
        super(alertType);

        ((Stage) getDialogPane().getScene().getWindow()).getIcons().add(Controller.ICON);
        setHeaderText(header);

        setContentText(String.format(message, args));
    }
}

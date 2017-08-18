package de.treichels.hott.vdfeditor.ui;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class ErrorDialog extends Alert {
    public static void show(final String header, final String message, final Object... args) {
        Platform.runLater(() -> new ErrorDialog(header, message, args).showAndWait());
    }

    public ErrorDialog(final String header, final String message, final Object... args) {
        super(AlertType.ERROR);

        ((Stage) getDialogPane().getScene().getWindow()).getIcons().add(Controller.ICON);
        setHeaderText(header);

        setContentText(String.format(message, args));
    }
}

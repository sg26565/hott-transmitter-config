package de.treichels.hott.vdfeditor.ui;

import java.io.PrintWriter;
import java.io.StringWriter;

import gde.util.Util;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class ExceptionDialog extends Alert {
    private static boolean SHOWING = false;

    synchronized static void show(final Throwable throwable) {
        // show only one instance of the dialog at a time
        if (!SHOWING) {
            SHOWING = true;
            Platform.runLater(() -> {
                new ExceptionDialog(throwable).showAndWait();
                SHOWING = false;
            });
        }
    }

    private ExceptionDialog(final Throwable throwable) {
        super(AlertType.ERROR);

        if (Util.DEBUG) throwable.printStackTrace();

        ((Stage) getDialogPane().getScene().getWindow()).getIcons().add(Controller.ICON);
        setHeaderText(null);

        String message = throwable.getLocalizedMessage();
        if (message == null) message = throwable.getMessage();
        if (message == null) message = throwable.getClass().getSimpleName();
        setContentText(message);

        // Create expandable Exception.
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        final String exceptionText = sw.toString();

        final Label label = new Label("The exception stacktrace was:");

        final TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        final GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        getDialogPane().setExpandableContent(expContent);
    }
}

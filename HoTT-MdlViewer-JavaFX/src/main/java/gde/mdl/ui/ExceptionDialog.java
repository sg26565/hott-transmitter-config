package gde.mdl.ui;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import gde.mdl.messages.Messages;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class ExceptionDialog extends Alert {
    private static final Logger LOG = Logger.getLogger(ExceptionDialog.class.getName());

    public static void show(final Throwable throwable) {
        Platform.runLater(() -> new ExceptionDialog(throwable).showAndWait());
    }

    public ExceptionDialog(final Throwable throwable) {
        super(AlertType.ERROR);

        LOG.log(Level.SEVERE, throwable.getMessage(), throwable);

        setTitle(Messages.getString("Error"));
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

package de.treichels.hott.vdfeditor.ui;

import java.io.IOException;
import java.util.ResourceBundle;

import de.treichels.hott.HoTTSerialPort;
import gde.model.HoTTException;
import gde.model.serial.JSSCSerialPort;
import gde.model.voice.VoiceFile;
import javafx.beans.property.ObjectProperty;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

class DialogController {
    private static final ResourceBundle RES = ResourceBundle.getBundle(DialogController.class.getName());
    private final MessageDialog dialog;
    private final Stage stage;
    private final ObjectProperty<HoTTSerialPort> serialPort;
    private TransmitterTask task = null;
    private Thread thread = null;

    @FXML
    private ComboBox<String> portCombo;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label message;

    public DialogController(final ObjectProperty<HoTTSerialPort> serialPort) {
        this.serialPort = serialPort;

        try {
            dialog = new MessageDialog(AlertType.CONFIRMATION, RES.getString("rf_off_header"), RES.getString("rf_off_message"));

            final FXMLLoader loader = new FXMLLoader(getClass().getResource("Dialog.fxml"), RES);
            loader.setController(this);
            final Parent root = loader.load();
            final Scene scene = new Scene(root);

            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UTILITY);
            stage.setOnCloseRequest(this::onAbort);
            stage.setScene(scene);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized void closeDialog(final WorkerStateEvent ev) {
        final Throwable t = ev == null ? null : ev.getSource().getException();
        if (t != null) ExceptionDialog.show(t);
        if (thread != null) thread.interrupt();
        thread = null;
        // serialPort.set(null);
        stage.hide();
    }

    public VoiceFile getResult() {
        return task.getValue();
    }

    @FXML
    private void onAbort(final Event ev) {
        ev.consume(); // prevent immediate window close in case of WindowEvent
        task.cancel();
    }

    @FXML
    private synchronized void onPortChanged() throws HoTTException {
        final String portName = portCombo.getValue();

        if (portName != null) {
            final JSSCSerialPort implementation = new JSSCSerialPort(portName);
            final HoTTSerialPort port = new HoTTSerialPort(implementation);

            if (serialPort.get() != null && serialPort.get().isOpen()) serialPort.get().close();
            serialPort.set(port);

            if (thread != null || dialog.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK)
                closeDialog(null);
            else {
                task.setPort(serialPort.get());
                thread = new Thread(task);
                thread.start();
            }
        }
    }

    public void openDialog(final TransmitterTask task) {
        this.task = task;

        serialPort.set(null);
        portCombo.setValue(null);
        portCombo.getItems().clear();
        portCombo.getItems().addAll(JSSCSerialPort.getAvailablePorts());
        portCombo.disableProperty().bind(task.runningProperty());

        progressBar.progressProperty().bind(task.progressProperty());
        message.textProperty().bind(task.messageProperty());
        stage.titleProperty().bind(task.titleProperty());

        task.setOnSucceeded(this::closeDialog);
        task.setOnCancelled(this::closeDialog);
        task.setOnFailed(this::closeDialog);

        stage.showAndWait();
    }
}

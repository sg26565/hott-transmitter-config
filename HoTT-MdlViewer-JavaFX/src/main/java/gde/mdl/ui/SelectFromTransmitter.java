package gde.mdl.ui;

import java.io.IOException;
import java.util.List;

import de.treichels.hott.HoTTSerialPort;
import gde.mdl.messages.Messages;
import gde.model.serial.SerialPortDefaultImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public abstract class SelectFromTransmitter extends Dialog<Model> {
	private static final List<String> portNames = SerialPortDefaultImpl.getAvailablePorts();

	@FXML
	public ComboBox<String> comboBox;

	protected ButtonType loadButtonType;
	protected BorderPane borderPane;

	protected SelectFromTransmitter() {
		// set buttons
		loadButtonType = new ButtonType(Messages.getString("Load"), ButtonData.OK_DONE);
		getDialogPane().getButtonTypes().addAll(loadButtonType, ButtonType.CANCEL);

		// load ui
		final FXMLLoader loader = new FXMLLoader(getClass().getResource("SelectFromTransmitterDialog.fxml"), Messages.getResourceBundle());
		loader.setController(this);
		try {
			borderPane = loader.load();
			getDialogPane().setContent(borderPane);
		} catch (final IOException e) {
			Controller.showExceptionDialog(e);
		}

		// set com ports
		for (final String portName : portNames) {
			comboBox.getItems().add(portName);
		}

		// event handler
		comboBox.setOnAction(evt -> storePrefs());

		loadPrefs();
		initOwner(Controller.STAGE);
		setResultConverter(b -> result(b));
	}

	protected void handleDoubleClick(final MouseEvent evt) {
		if (evt.getButton() == MouseButton.PRIMARY && evt.getClickCount() == 2 && result(loadButtonType) != null) {
			((Button) getDialogPane().lookupButton(loadButtonType)).fire();
		}
	}

	private void loadPrefs() {
		final String portName = Controller.PREFS.get("portName", comboBox.getValue());
		if (portName != null && portName.length() > 0 && portNames.contains(portName)) {
			comboBox.setValue(portName);
		}
	}

	protected abstract Model result(final ButtonType b);

	private void storePrefs() {
		final String portName = comboBox.getValue();
		if (portName != null && portName.length() > 0 && portNames.contains(portName)) {
			Controller.PREFS.put("portName", portName);
		}
	}

	protected <T> T withPort(final PortFunction<T> consumer) {
		T result = null;

		final String portName = comboBox.getValue();
		if (portName != null && portName.length() > 0) {
			try (HoTTSerialPort port = new HoTTSerialPort(new SerialPortDefaultImpl(portName))) {
				port.open();
				result = consumer.apply(port);
			} catch (final Exception e) {
				Controller.showExceptionDialog(e);
			}
		}

		return result;
	}
}
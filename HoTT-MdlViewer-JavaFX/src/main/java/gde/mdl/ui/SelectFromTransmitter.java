package gde.mdl.ui;

import java.io.IOException;
import java.util.List;

import gde.mdl.messages.Messages;
import gde.model.serial.SerialPortDefaultImpl;
import javafx.concurrent.Task;
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

/**
 * Abstract base class for modal dialogs that read some data from the
 * transmitter.
 *
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
public abstract class SelectFromTransmitter extends Dialog<Task<Model>> {
	/** List of serial port names */
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
			ExceptionDialog.show(e);
		}

		// set com ports
		comboBox.getItems().addAll(portNames);

		// remember last com port
		comboBox.setOnAction(evt -> storePrefs());

		// load last com port
		loadPrefs();

		initOwner(Controller.STAGE);
		setResultConverter(b -> getResult(b));
	}

	/**
	 * Compute the result for the currently selected item. May retrun
	 * <code>null</code> if no suitable item was selected or the cancel button
	 * was pressed.
	 *
	 * @param buttonType
	 *            The button type of the button that was pressed to close the
	 *            dialog.
	 * @return A Model instance for the selection or <code>null</code>.
	 */
	protected abstract Task<Model> getResult(final ButtonType buttonType);

	/**
	 * Handle double click on an item. If a suitable item was selected close
	 * dialog as if the load button was pressed.
	 *
	 * @param evt
	 */
	protected void handleDoubleClick(final MouseEvent evt) {
		if (evt.getButton() == MouseButton.PRIMARY && evt.getClickCount() == 2 && hasResult()) {
			// do as if the user clicked the load button
			((Button) getDialogPane().lookupButton(loadButtonType)).fire();
		}
	}

	/**
	 * Check whether a suitable item was selected. Subclasses need to define
	 * what actually is "suitable".
	 *
	 * @return
	 */
	protected abstract boolean hasResult();

	/**
	 * Load last used com port from preferences and update combobox.
	 */
	private void loadPrefs() {
		final String portName = Controller.PREFS.get("portName", comboBox.getValue());
		if (portName != null && portName.length() > 0 && portNames.contains(portName)) {
			comboBox.setValue(portName);
		}
	}

	/**
	 * Store current com port in preferences.
	 */
	private void storePrefs() {
		final String portName = comboBox.getValue();
		if (portName != null && portName.length() > 0 && portNames.contains(portName)) {
			Controller.PREFS.put("portName", portName);
		}
	}
}
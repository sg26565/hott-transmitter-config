package gde.mdl.ui.background;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.scene.Cursor;
import javafx.scene.Node;

/**
 * A background service that communicates with the transmitter.
 *
 * @author olive
 *
 * @param <T>
 *            The result parameter
 */
public abstract class TransmitterService<T> extends Service<T> {
	private final StringProperty portNameProperty;
	private final Node view;

	protected TransmitterService(final Node view) {
		this.view = view;
		portNameProperty = new SimpleStringProperty(this, "portName");

		// (re-)start this service on change of com port
		portNameProperty().addListener((p, o, n) -> restart());
	}

	@Override
	protected void cancelled() {
		view.setCursor(Cursor.DEFAULT);
		view.setDisable(false);
		super.cancelled();
	}

	@Override
	protected void failed() {
		view.setCursor(Cursor.DEFAULT);
		view.setDisable(false);
		super.failed();
	}

	public String getPortName() {
		return portNameProperty.get();
	}

	public StringProperty portNameProperty() {
		return portNameProperty;
	}

	public void setPortName(final String portName) {
		portNameProperty.set(portName);
	}

	@Override
	public void start() {
		view.setDisable(true);
		view.setCursor(Cursor.WAIT);
		super.start();
	}

	@Override
	protected void succeeded() {
		view.setCursor(Cursor.DEFAULT);
		view.setDisable(false);
		super.succeeded();
	}
}

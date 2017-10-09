package gde.mdl.ui.background;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;

/**
 * A background service that communicates with the transmitter.
 *
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 *
 * @param <T>
 *            The result parameter
 */
public abstract class TransmitterService<T> extends UIService<T> {
    private final StringProperty portNameProperty;

    protected TransmitterService(final Node view) {
        super(view);
        portNameProperty = new SimpleStringProperty(this, "portName");

        // (re-)start this service on change of com port
        portNameProperty().addListener((p, o, n) -> restart());
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
}

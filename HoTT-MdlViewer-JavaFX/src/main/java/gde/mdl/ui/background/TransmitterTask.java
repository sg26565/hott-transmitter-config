package gde.mdl.ui.background;

import javafx.scene.Node;

public abstract class TransmitterTask<T> extends UITask<T> {
	protected final String portName;

	public TransmitterTask(final Node view, final String portName) {
		super(view);
		this.portName = portName;
	}
}

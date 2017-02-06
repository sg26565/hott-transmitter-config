package gde.mdl.ui.background;

import gde.mdl.ui.ExceptionDialog;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.scene.Cursor;
import javafx.scene.Node;

public abstract class UITask<T> extends Task<T> {
	protected final Node view;

	public UITask(final Node view) {
		this.view = view;
	}

	@Override
	protected void failed() {
		ExceptionDialog.show(getException());
		super.failed();
	}

	public void start() {
		view.getScene().cursorProperty().bind(Bindings.when(runningProperty()).then(Cursor.WAIT).otherwise(Cursor.DEFAULT));
		runningProperty().addListener((p, o, n) -> view.setDisable(n));

		final Thread thread = new Thread(this);
		thread.setDaemon(true);
		thread.start();
	}
}

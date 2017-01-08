package gde.mdl.ui.background;

import gde.mdl.ui.Controller;
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
		Controller.showExceptionDialog(getException());
		view.setDisable(false);
		view.setCursor(Cursor.DEFAULT);
	}

	public void start() {
		view.setCursor(Cursor.WAIT);
		view.setDisable(true);
		final Thread thread = new Thread(this);
		thread.setDaemon(true);
		thread.start();
	}

	@Override
	protected void succeeded() {
		view.setDisable(false);
		view.setCursor(Cursor.DEFAULT);
	}
}

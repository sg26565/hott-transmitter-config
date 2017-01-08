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
	protected void cancelled() {
		disableUI(false);
		super.cancelled();
	}

	private void disableUI(final boolean disable) {
		view.setDisable(disable);
		view.setCursor(disable ? Cursor.WAIT : Cursor.DEFAULT);
	}

	@Override
	protected void failed() {
		Controller.showExceptionDialog(getException());
		disableUI(false);
		super.failed();
	}

	public void start() {
		disableUI(true);
		final Thread thread = new Thread(this);
		thread.setDaemon(true);
		thread.start();
	}

	@Override
	protected void succeeded() {
		disableUI(false);
		super.succeeded();
	}
}

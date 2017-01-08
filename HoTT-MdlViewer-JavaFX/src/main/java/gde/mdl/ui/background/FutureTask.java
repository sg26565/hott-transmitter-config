package gde.mdl.ui.background;

import java.util.concurrent.Future;

import gde.mdl.ui.Controller;
import gde.mdl.ui.Model;
import javafx.concurrent.Task;

public class FutureTask extends Task<Model> {
	private final Future<Model> future;

	public FutureTask(final Future<Model> future) {
		this.future = future;
	}

	@Override
	protected Model call() throws Exception {
		return future.get();
	}

	@Override
	protected void failed() {
		Controller.showExceptionDialog(getException());
	}

	public void start() {
		final Thread thread = new Thread(this);
		thread.setDaemon(true);
		thread.start();
	}
}

package gde.mdl.ui.background;

import gde.mdl.ui.ExceptionDialog;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Service;
import javafx.scene.Cursor;
import javafx.scene.Node;

public abstract class UIService<T> extends Service<T> {
	protected final Node view;

	public UIService(final Node view) {
		this.view = view;
	}

	@Override
	protected void failed() {
		ExceptionDialog.show(getException());
		super.failed();
	}

	@Override
	public void start() {
		view.getScene().cursorProperty().bind(Bindings.when(runningProperty()).then(Cursor.WAIT).otherwise(Cursor.DEFAULT));
		runningProperty().addListener((p, o, n) -> view.setDisable(n));
		super.start();
	}
}

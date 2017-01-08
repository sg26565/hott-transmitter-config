package gde.mdl.ui.background;

import java.util.concurrent.Future;

import gde.mdl.ui.Model;
import javafx.scene.web.WebView;

public class RefreshFutureTask extends UITask<String> {
	protected final Future<Model> model;

	public RefreshFutureTask(final WebView view, final Future<Model> model) {
		super(view);
		this.model = model;
	}

	@Override
	protected String call() throws Exception {
		final Model m = model.get();
		return m.getHtml();
	}

	@Override
	protected void succeeded() {
		((WebView) view).getEngine().load(getValue());
		super.succeeded();
	}
}

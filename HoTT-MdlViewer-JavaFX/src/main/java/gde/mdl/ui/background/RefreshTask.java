package gde.mdl.ui.background;

import gde.mdl.ui.Model;
import javafx.scene.web.WebView;

public class RefreshTask extends UITask<String> {
	protected final Model model;

	public RefreshTask(final WebView view, final Model model) {
		super(view);
		this.model = model;
	}

	@Override
	protected String call() throws Exception {
		return model.getHtml();
	}

	@Override
	protected void succeeded() {
		((WebView) view).getEngine().load(getValue());
		super.succeeded();
	}
}

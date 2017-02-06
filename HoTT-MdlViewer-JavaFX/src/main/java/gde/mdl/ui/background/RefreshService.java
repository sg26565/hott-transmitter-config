package gde.mdl.ui.background;

import gde.mdl.ui.Model;
import javafx.concurrent.Task;
import javafx.scene.web.WebView;

public class RefreshService extends UIService<String> {
	private Model model;

	public RefreshService(final WebView view) {
		super(view);
	}

	@Override
	protected Task<String> createTask() {
		return new Task<String>() {
			@Override
			protected String call() throws Exception {
				return model.getHtml();
			}
		};
	}

	public Model getModel() {
		return model;
	}

	public void setModel(final Model model) {
		this.model = model;
	}

	public void start(final Model model) {
		setModel(model);
		super.restart();
	}

	@Override
	protected void succeeded() {
		((WebView) view).getEngine().loadContent(getValue());
		super.succeeded();
	}
}

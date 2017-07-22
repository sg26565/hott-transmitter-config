package gde.mdl.ui.background;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import gde.mdl.ui.Model;
import javafx.concurrent.Task;
import javafx.scene.web.WebView;

public class RefreshService extends UIService<File> {
    private Model model;
    private final File tempFile;

    public RefreshService(final WebView view) throws IOException {
        super(view);
        tempFile = File.createTempFile(getClass().getName(), ".html");
        tempFile.deleteOnExit();
    }

    @Override
    protected Task<File> createTask() {
        return new Task<File>() {
            @Override
            protected File call() throws Exception {
                try (FileWriter w = new FileWriter(tempFile)) {
                    w.write(model.getHtml());
                }

                return tempFile;
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
        ((WebView) view).getEngine().load("file:/" + tempFile.getAbsolutePath());

        super.succeeded();
    }
}

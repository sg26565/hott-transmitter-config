package gde.mdl.ui;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FilenameUtils;

import com.itextpdf.text.DocumentException;

import gde.mdl.messages.Messages;
import gde.mdl.ui.background.RefreshService;
import gde.report.ReportException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Controller extends Application {
	private static final String LAST_LOAD_DIR = "lastLoadDir"; //$NON-NLS-1$
	private static final String LAST_SAVE_DIR = "lastSaveDir"; //$NON-NLS-1$
	private static final Logger LOG = Logger.getLogger(Controller.class.getName());
	static final Preferences PREFS = Preferences.userNodeForPackage(Controller.class);
	static Stage STAGE;

	public static void showExceptionDialog(final Throwable throwable) {
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(() -> showExceptionDialog(throwable));
		} else {
			LOG.log(Level.SEVERE, throwable.getMessage(), throwable);
			final Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle(Messages.getString("Error"));
			alert.setHeaderText(null);

			String message = throwable.getLocalizedMessage();
			if (message == null) {
				message = throwable.getMessage();
			}
			if (message == null) {
				message = throwable.getClass().getSimpleName();
			}

			alert.setContentText(message);

			// Create expandable Exception.
			final StringWriter sw = new StringWriter();
			final PrintWriter pw = new PrintWriter(sw);
			throwable.printStackTrace(pw);
			final String exceptionText = sw.toString();

			final Label label = new Label("The exception stacktrace was:");

			final TextArea textArea = new TextArea(exceptionText);
			textArea.setEditable(false);
			textArea.setWrapText(true);

			textArea.setMaxWidth(Double.MAX_VALUE);
			textArea.setMaxHeight(Double.MAX_VALUE);
			GridPane.setVgrow(textArea, Priority.ALWAYS);
			GridPane.setHgrow(textArea, Priority.ALWAYS);

			final GridPane expContent = new GridPane();
			expContent.setMaxWidth(Double.MAX_VALUE);
			expContent.add(label, 0, 0);
			expContent.add(textArea, 0, 1);

			// Set expandable Exception into the dialog pane.
			alert.getDialogPane().setExpandableContent(expContent);

			alert.showAndWait();
		}
	}

	@FXML
	private BorderPane borderPane;

	@FXML
	private StackPane stackPane;

	@FXML
	private ContextMenu contextMenu;

	@FXML
	private Menu fileMenu;

	@FXML
	private Region overlay;

	@FXML
	private MenuItem refresh1;

	@FXML
	private MenuItem refresh2;

	@FXML
	private MenuItem save1;

	@FXML
	private MenuItem save2;

	@FXML
	private WebView webview;
	private Scene scene;
	private RefreshService refreshService;

	private final ObjectProperty<Model> modelProperty = new SimpleObjectProperty<>(null);

	private void disableUI(final ReadOnlyBooleanProperty readOnlyBooleanProperty) {
		scene.cursorProperty().bind(Bindings.when(readOnlyBooleanProperty).then(Cursor.WAIT).otherwise(Cursor.DEFAULT));
		overlay.visibleProperty().bind(readOnlyBooleanProperty);
		webview.visibleProperty().bind(readOnlyBooleanProperty.not());
		readOnlyBooleanProperty.addListener((p, o, n) -> borderPane.setDisable(n));
	}

	@FXML
	public void initialize() {
		save1.disableProperty().bind(modelProperty.isNull());
		save2.disableProperty().bind(modelProperty.isNull());
		refresh1.disableProperty().bind(modelProperty.isNull());
		refresh2.disableProperty().bind(modelProperty.isNull());

		refreshService = new RefreshService(webview);

		final LoadingIndicator indicator = new LoadingIndicator();
		indicator.visibleProperty().bind(overlay.visibleProperty());

		stackPane.getChildren().add(indicator);

		// FIXME: FXMLLoader does not set contextMenuEnabled correctly.
		// Therefore, we have to disable it manually.
		webview.setContextMenuEnabled(false);
	}

	@FXML
	public void onExit() {
		Platform.exit();
	}

	@FXML
	public void onLoadFromFile() {
		final FileChooser chooser = new FileChooser();
		chooser.setTitle(Messages.getString("LoadFromFile"));
		final File dir = new File(PREFS.get(LAST_LOAD_DIR, System.getProperty(Launcher.MDL_DIR)));
		if (dir.exists() && dir.isDirectory()) {
			chooser.setInitialDirectory(dir);
		}
		chooser.getExtensionFilters().add(new ExtensionFilter(Messages.getString("MdlFileDescription"), "*.mdl"));

		final File file = chooser.showOpenDialog(STAGE);
		if (file != null) {
			PREFS.put(LAST_LOAD_DIR, file.getParentFile().getAbsolutePath());
			try {
				modelProperty.set(Model.load(file));
			} catch (final IOException e) {
				showExceptionDialog(e);
			}
			onRefresh();
		}
	}

	@FXML
	public void onLoadFromMemory() {
		final SelectFromTransmitter dialog = new SelectFromMemory();
		final Optional<Task<Model>> result = dialog.showAndWait();
		if (result.isPresent()) {
			final Task<Model> task = result.get();
			disableUI(task.runningProperty());
			task.setOnSucceeded(e -> {
				modelProperty.set(task.getValue());
				onRefresh();
			});
		}
	}

	@FXML
	public void onLoadFromSdCard() {
		final SelectFromSdCard dialog = new SelectFromSdCard();
		final Optional<Task<Model>> result = dialog.showAndWait();
		if (result.isPresent()) {
			final Task<Model> task = result.get();
			disableUI(task.runningProperty());
			task.setOnSucceeded(e -> {
				modelProperty.set(task.getValue());
				onRefresh();
			});
		}
	}

	@FXML
	public void onMouseClicked(final MouseEvent e) {
		if (e.getButton() == MouseButton.SECONDARY) {
			contextMenu.show(webview, e.getScreenX(), e.getScreenY());
		} else {
			contextMenu.hide();
		}
	}

	@FXML
	public void onRefresh() {
		if (modelProperty.isNotNull().get()) {
			refreshService.start(modelProperty.get());
			disableUI(refreshService.runningProperty());
		}
	}

	@FXML
	public void onSave() {
		final FileChooser chooser = new FileChooser();
		chooser.setTitle(Messages.getString("Save"));
		final File dir = new File(PREFS.get(LAST_SAVE_DIR, System.getProperty(Launcher.MDL_DIR)));
		if (dir.exists() && dir.isDirectory()) {
			chooser.setInitialDirectory(dir);
		}
		chooser.setInitialFileName(modelProperty.get().getFileName());
		chooser.getExtensionFilters().add(new ExtensionFilter(Messages.getString("SimpleGUI.HTML"), "*.html"));
		chooser.getExtensionFilters().add(new ExtensionFilter(Messages.getString("SimpleGUI.PDF"), "*.pdf"));
		chooser.getExtensionFilters().add(new ExtensionFilter(Messages.getString("SimpleGUI.XML"), "*.xml"));
		chooser.getExtensionFilters().add(new ExtensionFilter(Messages.getString("MdlFileDescription"), "*.mdl"));

		final File fileToSave = chooser.showSaveDialog(STAGE);
		if (fileToSave != null) {
			PREFS.put(LAST_SAVE_DIR, fileToSave.getParentFile().getAbsolutePath());

			final String extension = FilenameUtils.getExtension(fileToSave.getName()).toLowerCase();
			try {
				switch (extension) {
				case "html":
					modelProperty.get().saveHtml(fileToSave);
					break;

				case "pdf":
					modelProperty.get().savePdf(fileToSave);
					break;

				case "xml":
					modelProperty.get().saveXml(fileToSave);
					break;

				case "mdl":
					modelProperty.get().saveMdl(fileToSave);
					break;
				}
			} catch (ReportException | IOException | DocumentException | JAXBException e) {
				showExceptionDialog(e);
			}
		}
	}

	@Override
	public void start(final Stage primaryStage) throws Exception {
		final String title = Messages.getString("SimpleGUI.Title", System.getProperty(Launcher.PROGRAM_VERSION));
		final FXMLLoader loader = new FXMLLoader(getClass().getResource("MdlViewer.fxml"), Messages.getResourceBundle());
		loader.setController(this);
		scene = new Scene(loader.load());
		final Image icon = new Image(getClass().getResource("/icon.png").toString());
		STAGE = primaryStage;

		primaryStage.getIcons().add(icon);
		primaryStage.setTitle(title);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}

package gde.mdl.ui;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FilenameUtils;

import com.itextpdf.text.DocumentException;

import gde.mdl.messages.Messages;
import gde.mdl.ui.background.FutureTask;
import gde.report.ReportException;
import javafx.application.Application;
import javafx.application.Platform;
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
		LOG.log(Level.SEVERE, throwable.getMessage(), throwable);
		final Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(Messages.getString("Error"));
		alert.setHeaderText(null);
		alert.setContentText(throwable.getLocalizedMessage());

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

	@FXML
	private BorderPane borderPane;

	@FXML
	private ContextMenu contextMenu;

	@FXML
	private Menu fileMenu;

	private Model model;

	@FXML
	private Region overlay;

	@FXML
	private MenuItem save1;

	@FXML
	private MenuItem save2;

	@FXML
	private WebView webview;

	private void disableUI(final boolean disable) {
		final Cursor cursor = disable ? Cursor.WAIT : Cursor.DEFAULT;
		borderPane.setDisable(disable);
		borderPane.setCursor(cursor);
		overlay.setVisible(disable);
		overlay.setCursor(cursor);
	}

	@FXML
	public void initialize() {
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
				model = Model.load(file);
				save1.setDisable(false);
				save2.setDisable(false);
			} catch (final IOException e) {
				showExceptionDialog(e);
			}
			onRefresh();
		}
	}

	@FXML
	public void onLoadFromMemory() {
		final SelectFromTransmitter dialog = new SelectFromMemory();
		final Optional<Future<Model>> result = dialog.showAndWait();
		if (result.isPresent()) {
			disableUI(true);
			final FutureTask task = new FutureTask(result.get());
			task.setOnSucceeded(e -> onRefresh(task.getValue()));
			task.start();
		}
	}

	@FXML
	public void onLoadFromSdCard() {
		final SelectFromSdCard dialog = new SelectFromSdCard();
		final Optional<Future<Model>> result = dialog.showAndWait();
		if (result.isPresent()) {
			disableUI(true);
			final FutureTask task = new FutureTask(result.get());
			task.setOnSucceeded(e -> onRefresh(task.getValue()));
			task.start();
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
		if (model != null) {
			disableUI(true);
			Platform.runLater(() -> {
				try {
					webview.getEngine().loadContent(model.getHtml());
					disableUI(false);
				} catch (final IOException e) {
					showExceptionDialog(e);
				}
			});
		}
	}

	private void onRefresh(final Model model) {
		this.model = model;
		onRefresh();
	}

	@FXML
	public void onSave() {
		final FileChooser chooser = new FileChooser();
		chooser.setTitle(Messages.getString("Save"));
		final File dir = new File(PREFS.get(LAST_SAVE_DIR, System.getProperty(Launcher.MDL_DIR)));
		if (dir.exists() && dir.isDirectory()) {
			chooser.setInitialDirectory(dir);
		}
		chooser.setInitialFileName(model.getFileName());
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
					model.saveHtml(fileToSave);
					break;

				case "pdf":
					model.savePdf(fileToSave);
					break;

				case "xml":
					model.saveXml(fileToSave);
					break;

				case "mdl":
					model.saveMdl(fileToSave);
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
		final Scene scene = new Scene(FXMLLoader.load(getClass().getResource("MdlViewer.fxml"), Messages.getResourceBundle()));
		final Image icon = new Image(getClass().getResource("/icon.png").toString());
		STAGE = primaryStage;

		primaryStage.getIcons().add(icon);
		primaryStage.setTitle(title);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}

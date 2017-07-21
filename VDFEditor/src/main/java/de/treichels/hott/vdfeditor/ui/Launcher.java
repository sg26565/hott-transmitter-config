package de.treichels.hott.vdfeditor.ui;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ResourceBundle;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

import gde.mdl.messages.Messages;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Launcher extends Application {
    public static final String PROGRAM_VERSION = "program.version"; //$NON-NLS-1$

    static String getTitle() {
        return String.format("VDF Editor - %s", getVersion());
    }

    static String getVersion() {
        try {
            // get the location of this class
            final File source = new File(Launcher.class.getProtectionDomain().getCodeSource().getLocation().toURI());

            // read program version from manifest
            if (source.getName().endsWith(".jar") || source.getName().endsWith(".exe")) try (JarFile jarFile = new JarFile(source)) {
                final Attributes attributes = jarFile.getManifest().getMainAttributes();
                return Messages.getString("Launcher.Version", attributes.getValue("Implementation-Version"), attributes.getValue("Implementation-Build"));
            }
        } catch (final IOException | URISyntaxException e) {}

        return Messages.getString("Launcher.Unknown");
    }

    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        final Image icon = new Image(getClass().getResource("icon.png").toString());
        final ResourceBundle resourceBundle = ResourceBundle.getBundle(Controller.class.getName());
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("MainUI.fxml"), resourceBundle);
        final Parent root = loader.load();
        final Controller controller = loader.getController();
        primaryStage.setOnCloseRequest(ev -> {
            if (!controller.askSave()) ev.consume();
        });
        final Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle(getTitle());
        primaryStage.show();
    }
}

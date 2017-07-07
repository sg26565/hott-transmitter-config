package de.treichels.hott.vdfeditor.ui;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import gde.mdl.messages.Messages;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Launcher extends Application {
    public static final String PROGRAM_VERSION = "program.version"; //$NON-NLS-1$

    private static String getVersion() throws IOException, URISyntaxException {
        // get the location of this class
        final URL url = Launcher.class.getProtectionDomain().getCodeSource().getLocation();
        final File source = new File(url.toURI());
        final String version;

        if (source.getName().endsWith(".jar") || source.getName().endsWith(".exe")) { //$NON-NLS-1$
            // read program version from manifest
            JarFile jarFile = null;
            try {
                jarFile = new JarFile(source);
                final Manifest manifest = jarFile.getManifest();
                final Attributes attributes = manifest.getMainAttributes();
                version = Messages.getString("Launcher.Version", attributes.getValue("Implementation-Version"), //$NON-NLS-1$ //$NON-NLS-2$
                        attributes.getValue("Implementation-Build"));
                return version;
            } finally {
                if (jarFile != null) jarFile.close();
            }
        } else
            version = Messages.getString("Launcher.Unknown");

        System.setProperty(Launcher.PROGRAM_VERSION, version);
        return version;
    }

    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        final Image icon = new Image(getClass().getResource("/icon.png").toString());
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("MainUI.fxml"));
        final Parent root = loader.load();
        final Controller controller = loader.getController();
        primaryStage.setOnCloseRequest(ev -> {
            if (!controller.askSave()) ev.consume();
        });
        final Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle(String.format("VDF Editor - %s", getVersion()));
        primaryStage.show();
    }
}

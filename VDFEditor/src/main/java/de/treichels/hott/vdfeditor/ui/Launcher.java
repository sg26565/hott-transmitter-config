package de.treichels.hott.vdfeditor.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Launcher extends Application {
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
        primaryStage.setTitle("VDF Editor");
        primaryStage.show();
    }
}

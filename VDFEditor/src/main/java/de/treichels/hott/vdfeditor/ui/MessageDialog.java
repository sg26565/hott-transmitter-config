package de.treichels.hott.vdfeditor.ui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.EventTarget;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.scene.control.Alert;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class MessageDialog extends Alert {
    public static void show(final AlertType alertType, final String header, final boolean html, final String message, final Object... args) {
        Platform.runLater(() -> new MessageDialog(alertType, header, html, message, args).showAndWait());
    }

    public static void show(final AlertType alertType, final String header, final String message, final Object... args) {
        Platform.runLater(() -> new MessageDialog(alertType, header, false, message, args).showAndWait());
    }

    public MessageDialog(final AlertType alertType, final String header, final boolean html, final String message, final Object... args) {
        super(alertType);

        ((Stage) getDialogPane().getScene().getWindow()).getIcons().add(Controller.ICON);
        setHeaderText(header);

        final String text = String.format(message, args);

        if (html) {
            final WebView webView = new WebView();
            final WebEngine engine = webView.getEngine();
            engine.loadContent(text);
            webView.setPrefSize(400, 400);
            getDialogPane().setContent(webView);
            engine.getLoadWorker().stateProperty().addListener((ChangeListener<State>) (observable, oldState, newState) -> {
                if (newState == Worker.State.SUCCEEDED) {
                    // loop though all <a> tags
                    final NodeList links = engine.getDocument().getElementsByTagName("a");
                    for (int i = 0; i < links.getLength(); i++) {
                        final Element link = (Element) links.item(i);
                        final String url = link.getAttribute("href");

                        // disable normal onclick event handler as it will load the page into the dialog
                        link.setAttribute("onclick", "return false;");

                        // instead, load the url using the system default web browser
                        ((EventTarget) link).addEventListener("click", ev -> browse(url), false);
                    }
                }
            });
        } else
            setContentText(text);
    }

    public MessageDialog(final AlertType alertType, final String header, final String message, final Object... args) {
        this(alertType, header, false, message, args);
    }

    private void browse(final String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            ExceptionDialog.show(e);
        }
    }
}

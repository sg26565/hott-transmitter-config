package gde.mdl.ui;

import java.util.List;

import gde.mdl.messages.Messages;
import gde.mdl.ui.background.LoadFromSdCardTask;
import gde.model.serial.FileInfo;
import gde.model.serial.FileType;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TreeFileInfo extends TreeItem<String> {
    public static final Image FILE = LoadFile("/File.gif");
    public static final Image OPEN_FOLDER = LoadFile("/Folder_open.gif");
    public static final Image CLOSED_FOLDER = LoadFile("/Folder_closed.gif");
    public static final Image ROOT_FOLDER = LoadFile("/Root.gif");

    private static Image LoadFile(final String fileName) {
        return new Image(SelectFromSdCard.class.getResource(fileName).toString());
    }

    private final FileInfo fileInfo;
    private final StringProperty portNameProperty;
    private final Node view;

    public TreeFileInfo(final Node view, final FileInfo info) {
        // retrieve value form fileInfo
        this(view, info.getName(), info);
    }

    public TreeFileInfo(final Node view, final String value) {
        // root node without fileInfo
        this(view, value, null);

    }

    public TreeFileInfo(final Node view, final String value, final FileInfo fileInfo) {
        super(value);
        this.view = view;
        this.fileInfo = fileInfo;
        portNameProperty = new SimpleStringProperty(this, "portName");

        // update treeitem's children on expand
        expandedProperty().addListener(e -> update());

        // update treeitem's children when portName changes
        portNameProperty.addListener((p, o, n) -> update());

        setExpanded(false);

        if (fileInfo == null) {
            setGraphic(new ImageView(ROOT_FOLDER));
            getChildren().add(new TreeItem<>(Messages.getString("SelectFromTransmitter.loading")));
        } else if (fileInfo.getType() == FileType.Dir) {
            setGraphic(new ImageView(CLOSED_FOLDER));
            getChildren().add(new TreeItem<>(Messages.getString("SelectFromTransmitter.loading")));
        } else
            setGraphic(new ImageView(FILE));
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    /**
     * Get port name from portName property or parent tree item.
     *
     * @return
     */
    public String getPortName() {
        if (getParent() != null)
            return ((TreeFileInfo) getParent()).getPortName();
        else
            return portNameProperty.get();
    }

    public StringProperty portNameProperty() {
        return portNameProperty;
    }

    public void setPortName(final String portName) {
        portNameProperty.set(portName);
    }

    /**
     * Update children of this tree item in a background task.
     */
    @SuppressWarnings("unchecked")
    void update() {
        if (isExpanded()) {
            LoadFromSdCardTask task = null;

            if (fileInfo == null)
                // root node
                task = new LoadFromSdCardTask(view, getPortName(), "/");
            else if (fileInfo.getType() == FileType.Dir) // dir node
                task = new LoadFromSdCardTask(view, getPortName(), fileInfo.getPath());

            if (task != null) {
                task.setOnSucceeded(e -> {
                    if (fileInfo != null) setGraphic(new ImageView(OPEN_FOLDER));
                    getChildren().clear();
                    getChildren().addAll((List<TreeItem<String>>) e.getSource().getValue());
                });

                task.start();
            }
        } else {
            if (fileInfo != null) setGraphic(new ImageView(CLOSED_FOLDER));

            // add loading pseudo child
            getChildren().clear();
            getChildren().add(new TreeItem<>(Messages.getString("SelectFromTransmitter.loading")));
        }
    }
}

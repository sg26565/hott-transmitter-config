package gde.mdl.ui.background;

import java.util.ArrayList;
import java.util.List;

import gde.mdl.ui.PortUtils;
import gde.mdl.ui.TreeFileInfo;
import gde.model.serial.FileInfo;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;

public class LoadFromSdCardTask extends TransmitterTask<List<TreeItem<String>>> {
    private final String path;

    public LoadFromSdCardTask(final Node view, final String portName, final String path) {
        super(view, portName);
        this.path = path;
    }

    @Override
    protected List<TreeItem<String>> call() throws Exception {
        final List<TreeItem<String>> result = new ArrayList<>();
        final String[] names = PortUtils.withPort(portName, p -> p.listDir(path));

        if (names != null) for (final String name : names) {
            final FileInfo info = PortUtils.withPort(portName, p -> p.getFileInfo(name));
            result.add(new TreeFileInfo(view, info));
        }
        return result;
    }
}
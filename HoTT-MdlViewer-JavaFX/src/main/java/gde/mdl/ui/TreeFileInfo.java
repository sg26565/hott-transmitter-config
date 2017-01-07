package gde.mdl.ui;

import gde.model.serial.FileInfo;
import javafx.scene.control.TreeItem;

public class TreeFileInfo extends TreeItem<String> {
	private final FileInfo info;

	public TreeFileInfo(final FileInfo info) {
		this.info = info;
		setValue(info.getName());
	}

	public FileInfo getInfo() {
		return info;
	}
}

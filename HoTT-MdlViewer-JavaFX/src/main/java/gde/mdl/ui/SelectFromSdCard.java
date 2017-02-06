/**
 *  HoTT Transmitter Config
 *  Copyright (C) 2013  Oliver Treichel
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package gde.mdl.ui;

import gde.mdl.messages.Messages;
import gde.mdl.ui.background.GetFileTask;
import gde.model.serial.FileInfo;
import gde.model.serial.FileType;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 * @author oli@treichels.de
 */
public class SelectFromSdCard extends SelectFromTransmitter {
	final TreeView<String> treeView = new TreeView<>();

	public SelectFromSdCard() {
		setTitle(Messages.getString("LoadFromSdCard"));

		final TreeFileInfo rootNode = new TreeFileInfo(treeView, Messages.getString("SelectFromSdCard.RootNodeLabel"));

		// handle changes of serial port
		rootNode.portNameProperty().bind(comboBox.valueProperty());

		treeView.setRoot(rootNode);
		treeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		treeView.setOnMouseClicked(e -> handleDoubleClick(e));

		borderPane.setCenter(treeView);

		// expand root node and trigger load
		Platform.runLater(() -> rootNode.setExpanded(true));
	}

	@Override
	protected Task<Model> getResult(final ButtonType b) {
		if (b.getButtonData() == ButtonData.OK_DONE && hasResult()) {
			final GetFileTask task = new GetFileTask(treeView, comboBox.getValue());
			task.start();
			return task;
		}

		// not yet ready or cancel button
		return null;
	}

	@Override
	protected boolean hasResult() {
		final TreeItem<String> item = treeView.getSelectionModel().getSelectedItem();
		if (item != null && item instanceof TreeFileInfo) {
			final FileInfo fileInfo = ((TreeFileInfo) item).getFileInfo();
			if (fileInfo != null) {
				return fileInfo.getType() == FileType.File && fileInfo.getName().endsWith(".mdl") && fileInfo.getSize() <= 0x3000 //$NON-NLS-1$
						&& fileInfo.getSize() >= 0x2000;
			}
		}

		return false;
	}
}
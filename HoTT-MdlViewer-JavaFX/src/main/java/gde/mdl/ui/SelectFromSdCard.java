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

import java.io.ByteArrayOutputStream;

import gde.mdl.messages.Messages;
import gde.model.HoTTException;
import gde.model.enums.ModelType;
import gde.model.serial.FileInfo;
import gde.model.serial.FileType;
import gde.model.serial.ModelInfo;
import javafx.event.EventType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeItem.TreeModificationEvent;
import javafx.scene.control.TreeView;

/**
 * @author oli@treichels.de
 */
public class SelectFromSdCard extends SelectFromTransmitter {
	private final static EventType<TreeModificationEvent<String>> EVENT_TYPE = TreeItem.branchExpandedEvent();

	private final TreeItem<String> rootNode = new TreeItem<>(Messages.getString("SelectFromSdCard.RootNodeLabel"));
	private final TreeView<String> treeView = new TreeView<>(rootNode);

	public SelectFromSdCard() {
		setTitle(Messages.getString("LoadFromSdCard"));

		rootNode.setExpanded(true);
		treeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		treeView.setOnMouseClicked(e -> handleDoubleClick(e));
		treeView.addEventHandler(EVENT_TYPE, event -> loadTree(event.getSource()));
		borderPane.setCenter(treeView);
		comboBox.valueProperty().addListener((p, o, n) -> loadTree(rootNode));
		rootNode.expandedProperty().addListener((p, o, n) -> loadTree(rootNode));
		loadTree(rootNode);
	}

	private void loadTree(final TreeItem<String> treeItem) {
		treeView.setDisable(true);
		final String[] names;

		if (treeItem == rootNode) {
			names = withPort(p -> p.listDir("/"));
		} else if (treeItem instanceof TreeFileInfo) {
			if (treeItem.isExpanded()) {
				final FileInfo info = ((TreeFileInfo) treeItem).getInfo();
				names = withPort(p -> p.listDir(info.getPath()));
		} else {
				names = null;
			}
		} else {
			names = null;
		}

		if (names != null) {
		treeItem.getChildren().clear();
		for (final String name : names) {
			final FileInfo info = withPort(p -> p.getFileInfo(name));
				final TreeFileInfo node = new TreeFileInfo(info);
				if (info.getType() == FileType.Dir) {
					node.setExpanded(false);
					node.getChildren().add(new TreeItem<>("loading ..."));
					node.expandedProperty().addListener((p, o, n) -> loadTree(node));
				}
				treeItem.getChildren().add(node);
			}
		}

		treeView.setDisable(false);
	}

	@Override
	protected Model result(final ButtonType b) {
		Model result = null;

		if (b.getButtonData() == ButtonData.OK_DONE) {
			final TreeItem<String> item = treeView.getSelectionModel().getSelectedItem();
			if (item != null && item instanceof TreeFileInfo) {
				final FileInfo fileInfo = ((TreeFileInfo) item).getInfo();

				if (fileInfo != null && fileInfo.getType() == FileType.File && fileInfo.getName().endsWith(".mdl") && fileInfo.getSize() <= 0x3000 //$NON-NLS-1$
						&& fileInfo.getSize() >= 0x2000) {

					final byte[] data = withPort(p -> {
						try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
							p.readFile(fileInfo.getPath(), os);
							return os.toByteArray();
						}
					});

					final String fileName = fileInfo.getName();
					// check model type
					ModelType type;
					final char typeChar = fileName.charAt(0);
					switch (typeChar) {
					case 'a':
						type = ModelType.Winged;
						break;

					case 'h':
						type = ModelType.Helicopter;
						break;

					default:
						Controller.showExceptionDialog(new HoTTException("InvalidModelType", typeChar)); //$NON-NLS-1$
						return null;
					}

					final String name = fileName.substring(1, fileName.length() - 4);
					final ModelInfo info = new ModelInfo(0, name, type, null, null);
					result = new Model(info, data);
				}
			}
		}

		return result;
	}
}
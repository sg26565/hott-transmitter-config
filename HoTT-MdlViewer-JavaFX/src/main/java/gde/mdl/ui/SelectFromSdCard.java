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
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Cursor;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 * @author oli@treichels.de
 */
public class SelectFromSdCard extends SelectFromTransmitter {
	private final TreeView<String> treeView = new TreeView<>();

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
			final Task<Model> task = new Task<Model>() {
				@Override
				protected Model call() throws Exception {
					final TreeFileInfo item = (TreeFileInfo) treeView.getSelectionModel().getSelectedItem();
					final FileInfo fileInfo = item.getFileInfo();

					final byte[] data = PortUtils.withPort(comboBox.getValue(), p -> {
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
					return new Model(info, data);
				}
			};

			treeView.setDisable(true);
			treeView.setCursor(Cursor.WAIT);

			final Thread thread = new Thread(task);
			thread.setDaemon(true);
			thread.start();

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

			return fileInfo.getType() == FileType.File && fileInfo.getName().endsWith(".mdl") && fileInfo.getSize() <= 0x3000 //$NON-NLS-1$
					&& fileInfo.getSize() >= 0x2000;
		}

		return false;
	}
}
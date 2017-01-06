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
import gde.model.enums.ModelType;
import gde.model.serial.ModelInfo;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

/**
 * @author oli@treichels.de
 */
public class SelectFromMemory extends SelectFromTransmitter {
	private ModelInfo infos[];
	private final ListView<String> listView = new ListView<>();

	public SelectFromMemory() {
		setTitle(Messages.getString("LoadFromMemory"));

		listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		listView.setOnMouseClicked(e -> handleDoubleClick(e));
		borderPane.setCenter(listView);
		comboBox.valueProperty().addListener((p, o, n) -> handleChange());
	}

	private void handleChange() {
		listView.setDisable(true);
		listView.getItems().clear();

		infos = withPort(p -> p.getAllModelInfos());

		for (final ModelInfo info : infos) {
			final String item = String.format("%02d: %c%s.mdl", info.getModelNumber(), info.getModelType() == ModelType.Helicopter ? 'h' : 'a', //$NON-NLS-1$
					info.getModelName());
			listView.getItems().add(item);
		}

		listView.setDisable(false);
	}

	@Override
	protected Model result(final ButtonType b) {
		Model result = null;

		if (b.getButtonData() == ButtonData.OK_DONE) {
			final int index = listView.getSelectionModel().getSelectedIndex();
			if (index != -1) {
				final ModelInfo info = infos[index];
				result = withPort(p -> new Model(info, p.getModelData(info)));
			}
		}

		return result;
	}
}

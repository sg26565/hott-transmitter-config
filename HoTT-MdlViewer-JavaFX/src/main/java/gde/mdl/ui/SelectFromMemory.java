/**
 * HoTT Transmitter Config Copyright (C) 2013 Oliver Treichel
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package gde.mdl.ui;

import gde.mdl.messages.Messages;
import gde.mdl.ui.background.GetModelDataTask;
import gde.mdl.ui.background.LoadFromMemoryService;
import gde.model.serial.ModelInfo;
import javafx.concurrent.Task;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

/**
 * @author oli@treichels.de
 */
public class SelectFromMemory extends SelectFromTransmitter {
    private final ListView<String> listView = new ListView<>();
    private final LoadFromMemoryService service;

    public SelectFromMemory() {
        setTitle(Messages.getString("LoadFromMemory"));

        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.setOnMouseClicked(e -> handleDoubleClick(e));

        borderPane.setCenter(listView);

        service = new LoadFromMemoryService(listView);
        service.portNameProperty().bind(comboBox.valueProperty());
    }

    @Override
    protected Task<Model> getResult(final ButtonType b) {
        if (b.getButtonData() == ButtonData.OK_DONE && hasResult()) {
            final int index = listView.getSelectionModel().getSelectedIndex();
            final ModelInfo info = service.getValue().get(index);
            final GetModelDataTask task = new GetModelDataTask(listView, comboBox.getValue(), info);

            task.start();
            return task;
        }

        return null;
    }

    @Override
    protected boolean hasResult() {
        return listView.getSelectionModel().getSelectedIndex() != -1;
    }
}

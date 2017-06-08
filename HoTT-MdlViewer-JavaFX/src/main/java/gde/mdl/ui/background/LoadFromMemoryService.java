package gde.mdl.ui.background;

import java.util.Arrays;
import java.util.List;

import gde.mdl.messages.Messages;
import gde.mdl.ui.PortUtils;
import gde.model.serial.ModelInfo;
import javafx.concurrent.Task;
import javafx.scene.control.ListView;

/**
 * Background service that loads a list of {@linkplain ModelInfo} from the transmitter and updates a {@link ListView} with it.
 *
 * @author olive
 *
 */
public class LoadFromMemoryService extends TransmitterService<List<ModelInfo>> {
    private final ListView<String> listView;

    public LoadFromMemoryService(final ListView<String> listView) {
        super(listView);
        this.listView = listView;
    }

    @Override
    protected Task<List<ModelInfo>> createTask() {
        return new Task<List<ModelInfo>>() {
            @Override
            protected List<ModelInfo> call() throws Exception {
                final ModelInfo[] infos = PortUtils.withPort(getPortName(), p -> p.getAllModelInfos());
                return infos == null ? null : Arrays.asList(infos);
            }
        };
    }

    @Override
    public void start() {
        listView.getItems().clear();
        listView.getItems().add(Messages.getString("SelectFromTransmitter.loading"));

        super.start();
    }

    @Override
    protected void succeeded() {
        listView.getItems().clear();
        if (getValue() != null) for (final ModelInfo info : getValue())
            if (info.getModelName().length() > 0) {
                final String item = String.format("%02d: %c%s.mdl", info.getModelNumber(), info.getModelType().getChar(), //$NON-NLS-1$
                        info.getModelName());
                listView.getItems().add(item);
            }

        super.succeeded();
    }
}
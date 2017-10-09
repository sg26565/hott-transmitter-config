package gde.mdl.ui.background;

import gde.mdl.ui.Model;
import gde.mdl.ui.PortUtils;
import gde.model.serial.ModelInfo;
import javafx.scene.Node;

public class GetModelDataTask extends TransmitterTask<Model> {
    private final ModelInfo modelInfo;

    public GetModelDataTask(final Node view, final String portName, final ModelInfo modelInfo) {
        super(view, portName);
        this.modelInfo = modelInfo;
    }

    @Override
    protected Model call() throws Exception {
        return PortUtils.withPort(portName, p -> new Model(modelInfo, p.getModelData(modelInfo)));
    }
}

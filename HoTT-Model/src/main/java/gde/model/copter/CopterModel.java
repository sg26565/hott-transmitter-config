package gde.model.copter;

import gde.model.enums.ModelType;
import gde.model.helicopter.HelicopterModel;

public class CopterModel extends HelicopterModel {
    private static final long serialVersionUID = 1L;

    public CopterModel() {
        super(ModelType.Copter);
    }
}

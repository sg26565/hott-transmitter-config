package de.treichels.hott.model.copter;

import de.treichels.hott.model.enums.ModelType;
import de.treichels.hott.model.helicopter.HelicopterModel;

public class CopterModel extends HelicopterModel {
    private static final long serialVersionUID = 1L;

    public CopterModel() {
        super(ModelType.Copter);
    }
}

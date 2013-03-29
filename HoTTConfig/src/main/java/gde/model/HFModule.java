package gde.model;

import gde.model.enums.HFModuleType;

/**
 * @author oli@treichels.de
 */
public class HFModule {
	private HFModuleType type;

	public HFModuleType getType() {
		return type;
	}

	public void setType(final HFModuleType type) {
		this.type = type;
	}
}

package gde.model.container;

import gde.model.Control;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author oli@treichels.de
 */
public class Controls extends ArrayList<Control> {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "control")
	public List<Control> getControls() {
		return this;
	}

	public void setControls(final List<Control> controls) {
		clear();
		addAll(controls);
	}
}
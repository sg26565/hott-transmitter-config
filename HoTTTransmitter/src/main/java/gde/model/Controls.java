package gde.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author oli@treichels.de
 */
public class Controls implements Iterable<Control> {
	private List<Control> controls = new ArrayList<Control>();

	@XmlElement(name = "control")
	public List<Control> getControls() {
		return controls;
	}

	public void setControls(final List<Control> controls) {
		this.controls = controls;
	}

	public void addControl(final Control control) {
		getControls().add(control);
	}

	public Control getControl(final int number) {
		return getControls().get(number);
	}

	public int size() {
		return getControls().size();
	}

	@Override
	public Iterator<Control> iterator() {
		return getControls().iterator();
	}
}
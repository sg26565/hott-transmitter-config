package gde.model.container;

import gde.model.CurvePoint;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author oli@treichels.de
 */
public class CurvePoints extends ArrayList<CurvePoint> {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "point")
	public List<CurvePoint> getCurvePoints() {
		return this;
	}

	public void setCurvePoints(final List<CurvePoint> curvePoints) {
		clear();
		addAll(curvePoints);
	}
}

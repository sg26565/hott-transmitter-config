package gde.model.container;

import gde.model.CurvePoint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author oli@treichels.de
 */
public class CurvePoints implements Iterable<CurvePoint> {
	private List<CurvePoint> curvePoints = new ArrayList<CurvePoint>();

	@XmlElement(name = "point")
	public List<CurvePoint> getCurvePoints() {
		return curvePoints;
	}

	public void setCurvePoints(final List<CurvePoint> curvePoints) {
		this.curvePoints = curvePoints;
	}

	public void addCurvePoint(final CurvePoint curvePoint) {
		getCurvePoints().add(curvePoint);
	}

	public CurvePoint getCurvePoint(final int number) {
		return getCurvePoints().get(number);
	}

	public int size() {
		return getCurvePoints().size();
	}

	@Override
	public Iterator<CurvePoint> iterator() {
		return getCurvePoints().iterator();
	}
}

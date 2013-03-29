package gde.model.container;

import gde.model.Channel;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author oli@treichels.de
 */
public class Channels extends ArrayList<Channel> {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "channel")
	public List<Channel> getChannels() {
		return this;
	}

	public void setChannels(final List<Channel> channels) {
		clear();
		addAll(channels);
	}
}

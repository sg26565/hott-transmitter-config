package gde.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author oli@treichels.de
 */
public class Channels implements Iterable<Channel> {
	private List<Channel> channels = new ArrayList<Channel>();

	@XmlElement(name = "channel")
	public List<Channel> getChannels() {
		return channels;
	}

	public void setChannels(final List<Channel> channels) {
		this.channels = channels;
	}

	public void addChannel(final Channel channel) {
		getChannels().add(channel);
	}

	public Channel getChannel(final int number) {
		return getChannels().get(number);
	}

	public int size() {
		return getChannels().size();
	}

	@Override
	public Iterator<Channel> iterator() {
		return getChannels().iterator();
	}
}

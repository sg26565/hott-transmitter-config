/**
 * HoTT Transmitter Config Copyright (C) 2013 Oliver Treichel
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package de.treichels.hott.model;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 *
 */
public class ChannelMapping extends AbstractBase {
    private static final long serialVersionUID = 1L;

    private int inputChannel;
    private int outputChannel;

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final ChannelMapping other = (ChannelMapping) obj;
        if (inputChannel != other.inputChannel) return false;
        return outputChannel == other.outputChannel;
    }

    @XmlAttribute
    public int getInputChannel() {
        return inputChannel;
    }

    @XmlAttribute
    public int getOutputChannel() {
        return outputChannel;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + inputChannel;
        result = prime * result + outputChannel;
        return result;
    }

    public void setInputChannel(final int inputChannel) {
        this.inputChannel = inputChannel;
    }

    public void setOutputChannel(final int outputChannel) {
        this.outputChannel = outputChannel;
    }
}

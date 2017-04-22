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
package gde.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

/**
 * Abstract base class with support for bound properties.
 *
 * @author oli@treichels.de
 */
public abstract class AbstractBase implements Serializable {
    private static final long serialVersionUID = 1L;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
        support.addPropertyChangeListener(propertyName, listener);
    }

    public void fireIndexedPropertyChange(final String propertyName, final int index, final boolean oldValue, final boolean newValue) {
        support.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
    }

    public void fireIndexedPropertyChange(final String propertyName, final int index, final int oldValue, final int newValue) {
        support.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
    }

    public void fireIndexedPropertyChange(final String propertyName, final int index, final Object oldValue, final Object newValue) {
        support.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
    }

    public void firePropertyChange(final PropertyChangeEvent event) {
        support.firePropertyChange(event);
    }

    public void firePropertyChange(final String propertyName, final boolean oldValue, final boolean newValue) {
        support.firePropertyChange(propertyName, oldValue, newValue);
    }

    public void firePropertyChange(final String propertyName, final int oldValue, final int newValue) {
        support.firePropertyChange(propertyName, oldValue, newValue);
    }

    public void firePropertyChange(final String propertyName, final Object oldValue, final Object newValue) {
        support.firePropertyChange(propertyName, oldValue, newValue);
    }

    public PropertyChangeListener[] getPropertyChangeListeners() {
        return support.getPropertyChangeListeners();
    }

    public PropertyChangeListener[] getPropertyChangeListeners(final String propertyName) {
        return support.getPropertyChangeListeners(propertyName);
    }

    public boolean hasListeners(final String propertyName) {
        return support.hasListeners(propertyName);
    }

    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
        support.removePropertyChangeListener(propertyName, listener);
    }
}

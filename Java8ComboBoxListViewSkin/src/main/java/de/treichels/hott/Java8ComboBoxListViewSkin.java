package de.treichels.hott;

import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.control.ComboBox;

/**
 * A skin for a combo box with check boxes where the combo box should not hide on every mouse click. Therefore the
 * protected {@link #isHideOnClickEnabled()} method needs to be overridden.
 * <p>
 * The parent class does only exist in the Java 8 runtime. Therefore this class needs to be compiled with Java 8 and
 * code can not make a static reference to this class as it would cause an {@link NoClassDefFoundError} on newer Java
 * versions. Instead, code needs to load this class via reflection when it detects a Java 8 runtime.
 */
public class Java8ComboBoxListViewSkin<T> extends ComboBoxListViewSkin<T> {
    public Java8ComboBoxListViewSkin(ComboBox<T> comboBox) {
        super(comboBox);
    }

    /**
     * Do not hide the combo box on mouse click
     *
     * @return Always {@code false}
     */
    @Override
    protected boolean isHideOnClickEnabled() {
        return false;
    }
}

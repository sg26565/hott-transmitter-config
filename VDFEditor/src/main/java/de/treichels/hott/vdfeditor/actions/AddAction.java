package de.treichels.hott.vdfeditor.actions;

import java.util.List;

public class AddAction<T> extends InsertAction<T> {
    public AddAction(final T item) {
        super(-1, item);
    }

    @Override
    public List<T> apply(final List<T> list) {
        setIndex(list.size());
        return super.apply(list);
    }
}

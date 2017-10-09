package de.treichels.hott.vdfeditor.actions;

import java.util.List;

public class ReplaceAction<T> extends UndoableAction<T> {
    public ReplaceAction(final int index, final T item) {
        super(index, null, item);
    }

    @Override
    public List<T> apply(final List<T> list) {
        setOldItem(list.get(getIndex()));
        list.set(getIndex(), getNewItem());
        return list;
    }

    @Override
    public List<T> undo(final List<T> list) {
        list.set(getIndex(), getOldItem());
        return list;
    }
}

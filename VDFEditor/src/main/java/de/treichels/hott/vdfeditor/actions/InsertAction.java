package de.treichels.hott.vdfeditor.actions;

import java.util.List;

public class InsertAction<T> extends UndoableAction<T> {
    public InsertAction(final int index, final T item) {
        super(index, item);
    }

    @Override
    public List<T> apply(final List<T> list) {
        list.add(getIndex(), getItem());
        return list;
    }

    /**
     * @see de.treichels.hott.vdfeditor.actions.UndoableAction#undo(java.util.List)
     */
    @Override
    public List<T> undo(final List<T> list) {
        list.remove(getIndex());
        return list;
    }
}

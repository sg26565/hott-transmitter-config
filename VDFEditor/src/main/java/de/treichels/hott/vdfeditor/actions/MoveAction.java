package de.treichels.hott.vdfeditor.actions;

import java.util.List;

public class MoveAction<T> extends UndoableAction<T> {
    public MoveAction(final int fromIndex, final int toIndex) {
        super(fromIndex, toIndex);
    }

    @Override
    public List<T> apply(final List<T> list) {
        final T item = list.remove(getOldIndex());
        list.add(getNewIndex(), item);
        return list;

    }

    /**
     * @see de.treichels.hott.vdfeditor.actions.UndoableAction#undo(java.util.List)
     */
    @Override
    public List<T> undo(final List<T> list) {
        final T item = list.remove(getNewIndex());
        list.add(getOldIndex(), item);
        return list;
    }
}

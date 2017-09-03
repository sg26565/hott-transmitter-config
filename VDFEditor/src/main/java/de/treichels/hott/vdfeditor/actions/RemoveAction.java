package de.treichels.hott.vdfeditor.actions;

import java.util.List;

public class RemoveAction<T> extends UndoableAction<T> {
    public RemoveAction(final int index) {
        super(index);
    }

    @Override
    public List<T> apply(final List<T> list) {
        setItem(list.get(getIndex()));
        list.remove(getIndex());
        return list;
    }

    @Override
    public List<T> undo(final List<T> list) {
        list.add(getIndex(), getItem());
        return list;
    }
}

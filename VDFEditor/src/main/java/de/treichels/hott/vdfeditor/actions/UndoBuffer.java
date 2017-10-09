package de.treichels.hott.vdfeditor.actions;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.binding.BooleanBinding;

public class UndoBuffer<T> {
    private final class CanRedoBinding extends BooleanBinding {
        @Override
        protected boolean computeValue() {
            return index < actions.size();
        }
    }

    private final class CanUndoBinding extends BooleanBinding {
        @Override
        protected boolean computeValue() {
            return index > 0;
        }
    }

    private final List<UndoableAction<T>> actions = new ArrayList<>();
    private List<T> items;
    private int index = 0;
    private final BooleanBinding canUndoBinding = new CanUndoBinding();
    private final CanRedoBinding canRedoBinding = new CanRedoBinding();

    public UndoBuffer() {
        this(null);
    }

    public UndoBuffer(final List<T> items) {
        this.items = items;
    }

    public BooleanBinding canRedo() {
        return canRedoBinding;
    }

    public BooleanBinding canUndo() {
        return canUndoBinding;
    }

    public void clear() {
        actions.clear();
        index = 0;
        invalidate();
    }

    List<UndoableAction<T>> getActions() {
        return actions;
    }

    UndoableAction<T> getCurrent() {
        return actions.get(index);
    }

    public int getIndex() {
        return index;
    }

    public List<T> getItems() {
        return items;
    }

    private void invalidate() {
        canRedoBinding.invalidate();
        canUndoBinding.invalidate();
    }

    public void pop() {
        if (getIndex() != actions.size()) throw new IllegalStateException();

        undo();
        actions.remove(index);
        invalidate();
    }

    public void push(final UndoableAction<T> action) {
        while (index < actions.size())
            actions.remove(index);

        actions.add(action);
        index = actions.size();
        action.apply(items);
        invalidate();
    }

    public void redo() {
        final UndoableAction<T> action = getCurrent();
        index++;
        action.apply(items);
        invalidate();
    }

    public void setItems(final List<T> items) {
        this.items = items;
        clear();
    }

    public void undo() {
        index--;
        getCurrent().undo(items);
        invalidate();
    }
}

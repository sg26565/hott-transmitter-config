package de.treichels.hott.vdfeditor.actions;

import java.util.List;

public abstract class UndoableAction<T> {
    private int oldIndex;
    private int newIndex;
    private T oldItem;
    private T newItem;

    protected UndoableAction(final int index) {
        this(index, -1, null, null);
    }

    protected UndoableAction(final int oldIndex, final int newIndex) {
        this(oldIndex, newIndex, null, null);
    }

    protected UndoableAction(final int oldIndex, final int newIndex, final T oldItem, final T newItem) {
        this.oldIndex = oldIndex;
        this.newIndex = newIndex;
        this.oldItem = oldItem;
        this.newItem = newItem;
    }

    protected UndoableAction(final int index, final T item) {
        this(index, -1, item, null);
    }

    protected UndoableAction(final int index, final T oldItem, final T newItem) {
        this(index, -1, oldItem, newItem);
    }

    public abstract List<T> apply(List<T> list);

    public int getIndex() {
        return getOldIndex();
    }

    public T getItem() {
        return getOldItem();
    }

    public int getNewIndex() {
        return newIndex;
    }

    public T getNewItem() {
        return newItem;
    }

    public int getOldIndex() {
        return oldIndex;
    }

    public T getOldItem() {
        return oldItem;
    }

    protected void setIndex(final int index) {
        setOldIndex(index);
    }

    protected void setItem(final T item) {
        setOldItem(item);
    }

    protected void setNewIndex(final int newIndex) {
        this.newIndex = newIndex;
    }

    protected void setNewItem(final T newItem) {
        this.newItem = newItem;
    }

    protected void setOldIndex(final int oldIndex) {
        this.oldIndex = oldIndex;
    }

    protected void setOldItem(final T oldItem) {
        this.oldItem = oldItem;
    }

    public abstract List<T> undo(List<T> list);
}

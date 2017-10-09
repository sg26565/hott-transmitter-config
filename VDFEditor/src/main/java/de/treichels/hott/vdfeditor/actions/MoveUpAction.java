package de.treichels.hott.vdfeditor.actions;

public class MoveUpAction<T> extends MoveAction<T> {
    public MoveUpAction(final int index) {
        super(index, index + 1);
    }
}

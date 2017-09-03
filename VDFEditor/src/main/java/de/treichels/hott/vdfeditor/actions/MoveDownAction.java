package de.treichels.hott.vdfeditor.actions;

public class MoveDownAction<T> extends MoveAction<T> {
    public MoveDownAction(final int index) {
        super(index, index - 1);
    }
}

package de.treichels.hott.vdfeditor.actions;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class UndoBufferTest {
    private List<String> items;
    private UndoBuffer<String> buffer;

    @Before
    public void setup() {
        items = new ArrayList<>();
        buffer = new UndoBuffer<>(items);
    }

    @Test
    public void testClear() {
        testPushAdd();
        buffer.clear();
        assertEquals("[a, b, c]", items.toString());
        assertEquals(0, buffer.getIndex());
        assertEquals(0, buffer.getActions().size());
    }

    @Test
    public void testEmpty() {
        assertEquals("[]", items.toString());
        assertEquals(0, buffer.getIndex());
        assertEquals(0, buffer.getActions().size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testEmptyRedo() {
        buffer.redo();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testEmptyRedo2() {
        testPushAdd();
        buffer.redo();
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testEmptyUndo() {
        buffer.undo();
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testEmptyUndo2() {
        testUndoAdd();
        buffer.undo();
    }

    @Test
    public void testPop() {
        testPushAdd();

        buffer.pop();
        assertEquals("[a, b]", items.toString());
        assertEquals(2, buffer.getIndex());
        assertEquals(2, buffer.getActions().size());

        buffer.pop();
        assertEquals("[a]", items.toString());
        assertEquals(1, buffer.getIndex());
        assertEquals(1, buffer.getActions().size());

        buffer.pop();
        assertEquals("[]", items.toString());
        assertEquals(0, buffer.getIndex());
        assertEquals(0, buffer.getActions().size());
    }

    @Test
    public void testPushAdd() {
        buffer.push(new AddAction<>("a"));
        assertEquals("[a]", items.toString());
        assertEquals(1, buffer.getIndex());
        assertEquals(1, buffer.getActions().size());

        buffer.push(new AddAction<>("b"));
        assertEquals("[a, b]", items.toString());
        assertEquals(2, buffer.getIndex());
        assertEquals(2, buffer.getActions().size());

        buffer.push(new AddAction<>("c"));
        assertEquals("[a, b, c]", items.toString());
        assertEquals(3, buffer.getIndex());
        assertEquals(3, buffer.getActions().size());
    }

    @Test
    public void testPushAfterUndo() {
        testPushAdd();

        buffer.undo();
        assertEquals("[a, b]", items.toString());
        assertEquals(2, buffer.getIndex());
        assertEquals(3, buffer.getActions().size());

        buffer.push(new AddAction<>("d"));
        assertEquals("[a, b, d]", items.toString());
        assertEquals(3, buffer.getIndex());
        assertEquals(3, buffer.getActions().size());

        buffer.undo();
        buffer.undo();
        assertEquals("[a]", items.toString());
        assertEquals(1, buffer.getIndex());
        assertEquals(3, buffer.getActions().size());

        buffer.push(new AddAction<>("e"));
        assertEquals("[a, e]", items.toString());
        assertEquals(2, buffer.getIndex());
        assertEquals(2, buffer.getActions().size());
    }

    @Test
    public void testPushInsert() {
        testPushAdd();

        buffer.push(new InsertAction<>(1, "x"));
        assertEquals("[a, x, b, c]", items.toString());
        assertEquals(4, buffer.getIndex());
        assertEquals(4, buffer.getActions().size());
    }

    @Test
    public void testPushMove() {
        testPushAdd();

        buffer.push(new MoveAction<>(1, 2));
        assertEquals("[a, c, b]", items.toString());
        assertEquals(4, buffer.getIndex());
        assertEquals(4, buffer.getActions().size());
    }

    @Test
    public void testPushMoveDown() {
        testPushAdd();

        buffer.push(new MoveDownAction<>(2));
        assertEquals("[a, c, b]", items.toString());
        assertEquals(4, buffer.getIndex());
        assertEquals(4, buffer.getActions().size());
    }

    @Test
    public void testPushMoveUp() {
        testPushAdd();

        buffer.push(new MoveUpAction<>(1));
        assertEquals("[a, c, b]", items.toString());
        assertEquals(4, buffer.getIndex());
        assertEquals(4, buffer.getActions().size());
    }

    @Test
    public void testPushRemove() {
        testPushAdd();

        buffer.push(new RemoveAction<>(1));
        assertEquals("[a, c]", items.toString());
        assertEquals(4, buffer.getIndex());
        assertEquals(4, buffer.getActions().size());
    }

    @Test
    public void testPushReplace() {
        testPushAdd();

        buffer.push(new ReplaceAction<>(1, "x"));
        assertEquals("[a, x, c]", items.toString());
        assertEquals(4, buffer.getIndex());
        assertEquals(4, buffer.getActions().size());
    }

    @Test
    public void testRedoAdd() {
        testUndoAdd();

        buffer.redo();
        assertEquals("[a]", items.toString());
        assertEquals(1, buffer.getIndex());
        assertEquals(3, buffer.getActions().size());

        buffer.redo();
        assertEquals("[a, b]", items.toString());
        assertEquals(2, buffer.getIndex());
        assertEquals(3, buffer.getActions().size());

        buffer.redo();
        assertEquals("[a, b, c]", items.toString());
        assertEquals(3, buffer.getIndex());
        assertEquals(3, buffer.getActions().size());
    }

    @Test
    public void testRedoMove() {
        testUndoMove();
        buffer.redo();
        assertEquals("[a, c, b]", items.toString());
        assertEquals(4, buffer.getIndex());
        assertEquals(4, buffer.getActions().size());
    }

    @Test
    public void testRedoMoveDown() {
        testUndoMove();
        buffer.redo();
        assertEquals("[a, c, b]", items.toString());
        assertEquals(4, buffer.getIndex());
        assertEquals(4, buffer.getActions().size());
    }

    @Test
    public void testRedoMoveUp() {
        testUndoMove();
        buffer.redo();
        assertEquals("[a, c, b]", items.toString());
        assertEquals(4, buffer.getIndex());
        assertEquals(4, buffer.getActions().size());
    }

    @Test
    public void testRedoRemove() {
        testUndoRemove();
        buffer.redo();
        assertEquals("[a, c]", items.toString());
        assertEquals(4, buffer.getIndex());
        assertEquals(4, buffer.getActions().size());
    }

    @Test
    public void testRedoReplace() {
        testUndoReplace();
        buffer.redo();
        assertEquals("[a, x, c]", items.toString());
        assertEquals(4, buffer.getIndex());
        assertEquals(4, buffer.getActions().size());
    }

    @Test
    public void testUndoAdd() {
        testPushAdd();

        buffer.undo();
        assertEquals("[a, b]", items.toString());
        assertEquals(2, buffer.getIndex());
        assertEquals(3, buffer.getActions().size());

        buffer.undo();
        assertEquals("[a]", items.toString());
        assertEquals(1, buffer.getIndex());
        assertEquals(3, buffer.getActions().size());

        buffer.undo();
        assertEquals("[]", items.toString());
        assertEquals(0, buffer.getIndex());
        assertEquals(3, buffer.getActions().size());
    }

    @Test
    public void testUndoMove() {
        testPushMove();

        buffer.undo();
        assertEquals("[a, b, c]", items.toString());
        assertEquals(3, buffer.getIndex());
        assertEquals(4, buffer.getActions().size());
    }

    @Test
    public void testUndoMoveDown() {
        testPushMoveDown();

        buffer.undo();
        assertEquals("[a, b, c]", items.toString());
        assertEquals(3, buffer.getIndex());
        assertEquals(4, buffer.getActions().size());
    }

    @Test
    public void testUndoMoveUp() {
        testPushMoveUp();

        buffer.undo();
        assertEquals("[a, b, c]", items.toString());
        assertEquals(3, buffer.getIndex());
        assertEquals(4, buffer.getActions().size());
    }

    @Test
    public void testUndoRemove() {
        testPushRemove();

        buffer.undo();
        assertEquals("[a, b, c]", items.toString());
        assertEquals(3, buffer.getIndex());
        assertEquals(4, buffer.getActions().size());
    }

    @Test
    public void testUndoReplace() {
        testPushReplace();

        buffer.undo();
        assertEquals("[a, b, c]", items.toString());
        assertEquals(3, buffer.getIndex());
        assertEquals(4, buffer.getActions().size());
    }
}

package de.treichels.hott.vdfeditor.actions;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.treichels.hott.vdfeditor.actions.MoveAction;
import de.treichels.hott.vdfeditor.actions.UndoableAction;

public class MoveActionTest {
    private List<String> list;
    private UndoableAction<String> action;

    @Before
    public void setup() {
        list = new ArrayList<>(Arrays.asList("a", "b", "c", "d"));
    }

    @Test
    public void testMoveAction1() {
        action = new MoveAction<>(2, 0);
        assertEquals("[a, b, c, d]", list.toString());
        action.apply(list);
        assertEquals("[c, a, b, d]", list.toString());
        action.undo(list);
        assertEquals("[a, b, c, d]", list.toString());
    }

    @Test
    public void testMoveAction2() {
        action = new MoveAction<>(3, 2);
        assertEquals("[a, b, c, d]", list.toString());
        action.apply(list);
        assertEquals("[a, b, d, c]", list.toString());
        action.undo(list);
        assertEquals("[a, b, c, d]", list.toString());
    }

    @Test
    public void testMoveAction3() {
        action = new MoveAction<>(2, 3);
        assertEquals("[a, b, c, d]", list.toString());
        action.apply(list);
        assertEquals("[a, b, d, c]", list.toString());
        action.undo(list);
        assertEquals("[a, b, c, d]", list.toString());
    }

    @Test
    public void testMoveAction4() {
        action = new MoveAction<>(0, 2);
        assertEquals("[a, b, c, d]", list.toString());
        action.apply(list);
        assertEquals("[b, c, a, d]", list.toString());
        action.undo(list);
        assertEquals("[a, b, c, d]", list.toString());
    }
}

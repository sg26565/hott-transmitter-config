package de.treichels.hott.vdfeditor.actions;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.treichels.hott.vdfeditor.actions.MoveUpAction;
import de.treichels.hott.vdfeditor.actions.UndoableAction;

public class MoveUpActionTest {
    private List<String> list;
    private UndoableAction<String> action;

    @Before
    public void setup() {
        list = new ArrayList<>(Arrays.asList("a", "b", "c", "d"));
    }

    @Test
    public void testMoveUpAction1() {
        action = new MoveUpAction<>(0);
        assertEquals("[a, b, c, d]", list.toString());
        action.apply(list);
        assertEquals("[b, a, c, d]", list.toString());
        action.undo(list);
        assertEquals("[a, b, c, d]", list.toString());
    }

    @Test
    public void testMoveUpAction2() {
        action = new MoveUpAction<>(2);
        assertEquals("[a, b, c, d]", list.toString());
        action.apply(list);
        assertEquals("[a, b, d, c]", list.toString());
        action.undo(list);
        assertEquals("[a, b, c, d]", list.toString());
    }
}

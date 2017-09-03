package de.treichels.hott.vdfeditor.actions;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.treichels.hott.vdfeditor.actions.MoveDownAction;
import de.treichels.hott.vdfeditor.actions.UndoableAction;

public class MoveDownActionTest {
    private List<String> list;
    private UndoableAction<String> action;

    @Before
    public void setup() {
        list = new ArrayList<>(Arrays.asList("a", "b", "c", "d"));
    }

    @Test
    public void testMoveDownAction1() {
        action = new MoveDownAction<>(1);
        assertEquals("[a, b, c, d]", list.toString());
        action.apply(list);
        assertEquals("[b, a, c, d]", list.toString());
        action.undo(list);
        assertEquals("[a, b, c, d]", list.toString());
    }

    @Test
    public void testMoveDownAction2() {
        action = new MoveDownAction<>(3);
        assertEquals("[a, b, c, d]", list.toString());
        action.apply(list);
        assertEquals("[a, b, d, c]", list.toString());
        action.undo(list);
        assertEquals("[a, b, c, d]", list.toString());
    }
}

package de.treichels.hott.vdfeditor.actions;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.treichels.hott.vdfeditor.actions.RemoveAction;
import de.treichels.hott.vdfeditor.actions.UndoableAction;

public class RemoveActionTest {
    private List<String> list;
    private UndoableAction<String> action;

    @Before
    public void setup() {
        list = new ArrayList<>(Arrays.asList("a", "b", "c", "d"));
    }

    @Test
    public void testremoveAction1() {
        action = new RemoveAction<>(2);
        assertEquals("[a, b, c, d]", list.toString());
        action.apply(list);
        assertEquals("[a, b, d]", list.toString());
        action.undo(list);
        assertEquals("[a, b, c, d]", list.toString());
    }

    @Test
    public void testremoveAction2() {
        action = new RemoveAction<>(0);
        assertEquals("[a, b, c, d]", list.toString());
        action.apply(list);
        assertEquals("[b, c, d]", list.toString());
        action.undo(list);
        assertEquals("[a, b, c, d]", list.toString());
    }

    @Test
    public void testremoveAction3() {
        action = new RemoveAction<>(3);
        assertEquals("[a, b, c, d]", list.toString());
        action.apply(list);
        assertEquals("[a, b, c]", list.toString());
        action.undo(list);
        assertEquals("[a, b, c, d]", list.toString());
    }
}

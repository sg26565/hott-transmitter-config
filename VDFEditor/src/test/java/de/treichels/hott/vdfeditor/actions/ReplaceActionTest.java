package de.treichels.hott.vdfeditor.actions;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.treichels.hott.vdfeditor.actions.ReplaceAction;
import de.treichels.hott.vdfeditor.actions.UndoableAction;

public class ReplaceActionTest {
    private List<String> list;
    private UndoableAction<String> action;

    @Before
    public void setup() {
        list = new ArrayList<>(Arrays.asList("a", "b", "c", "d"));
        action = new ReplaceAction<>(2, "x");
    }

    @Test
    public void testReplaceAction() {
        assertEquals("[a, b, c, d]", list.toString());
        action.apply(list);
        assertEquals("[a, b, x, d]", list.toString());
        action.undo(list);
        assertEquals("[a, b, c, d]", list.toString());
    }
}

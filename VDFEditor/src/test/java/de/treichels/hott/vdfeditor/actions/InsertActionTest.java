package de.treichels.hott.vdfeditor.actions;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.treichels.hott.vdfeditor.actions.InsertAction;
import de.treichels.hott.vdfeditor.actions.UndoableAction;

public class InsertActionTest {
    private List<String> list;
    private UndoableAction<String> action;

    @Before
    public void setup() {
        list = new ArrayList<>(Arrays.asList("a", "b", "c", "d"));
    }

    @Test
    public void testInsertAction1() {
        action = new InsertAction<>(2, "x");
        assertEquals("[a, b, c, d]", list.toString());
        action.apply(list);
        assertEquals("[a, b, x, c, d]", list.toString());
        action.undo(list);
        assertEquals("[a, b, c, d]", list.toString());
    }

    @Test
    public void testInsertAction2() {
        action = new InsertAction<>(0, "x");
        assertEquals("[a, b, c, d]", list.toString());
        action.apply(list);
        assertEquals("[x, a, b, c, d]", list.toString());
        action.undo(list);
        assertEquals("[a, b, c, d]", list.toString());
    }

    @Test
    public void testInsertAction3() {
        action = new InsertAction<>(4, "x");
        assertEquals("[a, b, c, d]", list.toString());
        action.apply(list);
        assertEquals("[a, b, c, d, x]", list.toString());
        action.undo(list);
        assertEquals("[a, b, c, d]", list.toString());
    }
}

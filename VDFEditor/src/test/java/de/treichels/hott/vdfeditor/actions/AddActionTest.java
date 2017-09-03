package de.treichels.hott.vdfeditor.actions;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.treichels.hott.vdfeditor.actions.AddAction;
import de.treichels.hott.vdfeditor.actions.UndoableAction;

public class AddActionTest {
    private List<String> list;
    private UndoableAction<String> action;

    @Before
    public void setup() {
        list = new ArrayList<>(Arrays.asList("a", "b", "c", "d"));
    }

    @Test
    public void testAddAction() {
        action = new AddAction<>("x");
        assertEquals("[a, b, c, d]", list.toString());
        action.apply(list);
        assertEquals("[a, b, c, d, x]", list.toString());
        action.undo(list);
        assertEquals("[a, b, c, d]", list.toString());

    }
}

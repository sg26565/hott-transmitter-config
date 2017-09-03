package de.treichels.hott.vdfeditor.actions;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import gde.model.voice.VoiceData;

public class RenameActionTest {
    private List<VoiceData> list;
    private UndoableAction<VoiceData> action;

    @Before
    public void setup() {
        list = new ArrayList<>();
        list.add(new VoiceData("before", null));
    }

    @Test
    public void testRenameAction() {
        action = new RenameAction(0, "after");
        assertEquals(1, list.size());
        assertEquals("before", list.get(0).getName());

        action.apply(list);
        assertEquals(1, list.size());
        assertEquals("after", list.get(0).getName());

        action.undo(list);
        assertEquals(1, list.size());
        assertEquals("before", list.get(0).getName());
    }
}

package de.treichels.hott.vdfeditor.actions;

import java.util.List;

import gde.model.voice.VoiceData;

public class RenameAction extends UndoableAction<VoiceData> {
    private String oldName;
    private final String newName;

    public RenameAction(final int index, final String newName) {
        super(index);
        this.newName = newName;
    }

    @Override
    public List<VoiceData> apply(final List<VoiceData> list) {
        final VoiceData item = list.get(getIndex());
        oldName = item.getName();
        item.setName(newName);
        list.set(getIndex(), item);
        return list;
    }

    @Override
    public List<VoiceData> undo(final List<VoiceData> list) {
        final VoiceData item = list.get(getIndex());
        item.setName(oldName);
        list.set(getIndex(), item);
        return list;
    }
}

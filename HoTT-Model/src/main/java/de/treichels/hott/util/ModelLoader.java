package de.treichels.hott.util;

import de.treichels.hott.model.BaseModel;

import java.io.IOException;

public interface ModelLoader {
    BaseModel getModel() throws IOException;

    byte[] getModelData() throws IOException;

    void onCancel();

    void onOpen();

    void onReload();
}
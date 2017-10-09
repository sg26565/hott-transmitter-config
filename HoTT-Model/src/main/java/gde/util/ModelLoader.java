package gde.util;

import java.io.IOException;

import gde.model.BaseModel;

public interface ModelLoader {
    public BaseModel getModel() throws IOException;

    public byte[] getModelData() throws IOException;

    public void onCancel();

    public void onOpen();

    public void onReload();
}
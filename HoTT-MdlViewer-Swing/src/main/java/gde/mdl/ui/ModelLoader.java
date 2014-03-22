package gde.mdl.ui;

import gde.model.BaseModel;

import java.io.IOException;

public interface ModelLoader {
  public abstract BaseModel getModel() throws IOException;

  public void onOpen();

  public void onCancel();

  public void onReload();
}
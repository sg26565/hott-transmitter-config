package gde.mdl.ui;

import java.io.IOException;

import javax.swing.JPanel;

import gde.model.BaseModel;

public class ModelLoader extends JPanel {
	private static final long serialVersionUID = 1L;

	public BaseModel getModel() throws IOException {
		return null;
	}

	public byte[] getModelData() throws IOException {
		return null;
	}

	public void onCancel() {
	};

	public void onOpen() {
	};

	public void onReload() {
	};
}
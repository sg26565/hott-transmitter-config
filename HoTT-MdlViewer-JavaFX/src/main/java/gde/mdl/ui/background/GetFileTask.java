package gde.mdl.ui.background;

import java.io.ByteArrayOutputStream;

import gde.mdl.ui.ExceptionDialog;
import gde.mdl.ui.Model;
import gde.mdl.ui.PortUtils;
import gde.mdl.ui.TreeFileInfo;
import gde.model.HoTTException;
import gde.model.enums.ModelType;
import gde.model.serial.FileInfo;
import gde.model.serial.ModelInfo;
import javafx.scene.control.TreeView;

public final class GetFileTask extends UITask<Model> {
	private final TreeView<String> treeView;
	private final String portName;

	public GetFileTask(final TreeView<String> treeView, final String portName) {
		super(treeView);
		this.treeView = treeView;
		this.portName = portName;
	}

	@Override
	protected Model call() throws Exception {
		final TreeFileInfo item = (TreeFileInfo) treeView.getSelectionModel().getSelectedItem();
		final FileInfo fileInfo = item.getFileInfo();

		final byte[] data = PortUtils.withPort(portName, p -> {
			try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
				p.readFile(fileInfo.getPath(), os);
				return os.toByteArray();
			}
		});

		final String fileName = fileInfo.getName();
		// check model type
		ModelType type;
		final char typeChar = fileName.charAt(0);
		switch (typeChar) {
		case 'a':
			type = ModelType.Winged;
			break;

		case 'h':
			type = ModelType.Helicopter;
			break;

		default:
			ExceptionDialog.show(new HoTTException("InvalidModelType", typeChar)); //$NON-NLS-1$
			return null;
		}

		final String name = fileName.substring(1, fileName.length() - 4);
		final ModelInfo info = new ModelInfo(0, name, type, null, null);
		return new Model(info, data);
	}
}
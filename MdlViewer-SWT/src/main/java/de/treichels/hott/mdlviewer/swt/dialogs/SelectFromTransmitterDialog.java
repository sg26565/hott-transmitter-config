package de.treichels.hott.mdlviewer.swt.dialogs;

import de.treichels.hott.decoder.HoTTSerialPort;
import de.treichels.hott.messages.Messages;
import de.treichels.hott.model.BaseModel;
import de.treichels.hott.serial.SerialPort;
import de.treichels.hott.util.Util;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;

import java.util.List;
import java.util.prefs.Preferences;

public abstract class SelectFromTransmitterDialog extends Dialog {
    private final class CancelListener implements Listener {
        @Override
        public void handleEvent(final Event arg0) {
            onCancel();
            dialog.close();
        }
    }

    private final class OpenListener implements Listener {
        @Override
        public void handleEvent(final Event arg0) {
            onOpen();
            dialog.close();
        }
    }

    private final class PortListener implements ModifyListener {
        @Override
        public void modifyText(final ModifyEvent arg0) {
            portSelected(combo.getText());
        }
    }

    private static final Preferences PREFS = Preferences.userNodeForPackage(SelectFromTransmitterDialog.class);
    private final List<String> portNames = SerialPort.Companion.getAvailablePorts();
    HoTTSerialPort port = null;
    BaseModel model = null;
    private Combo combo;
    Shell dialog;

    SelectFromTransmitterDialog(final Shell parent) {
        super(parent, SWT.NONE);
    }

    public BaseModel getModel() {
        return model;
    }

    protected abstract void getSelectionComponent(final Composite dialog);

    protected abstract void onCancel();

    protected abstract void onOpen();

    protected abstract void onReload();

    public Object open() {
        final Shell shell = getParent();
        dialog = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE);

        // title
        dialog.setText(Messages.getString("Open"));
        dialog.setSize(300, 400);

        // layout
        final GridLayout dialoglayout = new GridLayout();
        dialoglayout.numColumns = 1;
        dialog.setLayout(dialoglayout);

        // serial port selection
        final Composite portSelection = new Composite(dialog, SWT.NONE);
        portSelection.setLayoutData(new GridData(GridData.CENTER, GridData.BEGINNING, true, false));
        final RowLayout portLayout = new RowLayout();
        portLayout.center = true;
        portLayout.wrap = false;
        portSelection.setLayout(portLayout);
        final Label label = new Label(portSelection, SWT.CENTER);
        label.setText(Messages.getString("SerialPort"));
        combo = new Combo(portSelection, SWT.READ_ONLY);

        // main selection component
        getSelectionComponent(dialog);

        if (!portNames.isEmpty()) {
            for (final String s : portNames)
                combo.add(s);

            String prefPort = PREFS.get("portName", portNames.get(0));

            if (!portNames.contains(prefPort)) prefPort = portNames.get(0);

            combo.setText(prefPort);
            portSelected(prefPort);
        }

        combo.addModifyListener(new PortListener());

        // buttons
        final Composite buttonsComposite = new Composite(dialog, SWT.NONE);
        buttonsComposite.setLayoutData(new GridData(GridData.END, GridData.END, false, false));
        buttonsComposite.setLayout(new RowLayout());
        final Button openButton = new Button(buttonsComposite, SWT.PUSH);
        openButton.setText(Messages.getString("Open"));
        final Button cancelButton = new Button(buttonsComposite, SWT.PUSH);
        cancelButton.setText(Messages.getString("Cancel"));
        buttonsComposite.pack();

        openButton.addListener(SWT.Selection, new OpenListener());
        cancelButton.addListener(SWT.Selection, new CancelListener());

        dialog.open();

        final Display display = shell.getDisplay();

        while (!dialog.isDisposed())
            if (!display.readAndDispatch()) display.sleep();

        return model;
    }

    private void portSelected(final String portName) {
        if (portName != null && portName.length() > 0 && portNames.contains(portName)) {
            PREFS.put("portName", portName);
            port = new HoTTSerialPort(SerialPort.Companion.getPort(portName));
            onReload();
        }
    }

    void showError(final Throwable t) {
        final MessageBox mb = new MessageBox(getParent().getShell(), SWT.NONE);
        mb.setText(t.getClass().getSimpleName());
        mb.setMessage(t.getMessage() == null ? t.getClass().getSimpleName() : t.getMessage());
        mb.open();
        if (Util.INSTANCE.getDEBUG()) t.printStackTrace();
    }
}

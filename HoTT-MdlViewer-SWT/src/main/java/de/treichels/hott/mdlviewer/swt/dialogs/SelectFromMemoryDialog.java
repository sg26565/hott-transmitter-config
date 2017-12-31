package de.treichels.hott.mdlviewer.swt.dialogs;

import de.treichels.hott.decoder.HoTTDecoder;
import de.treichels.hott.model.enums.ModelType;
import de.treichels.hott.model.serial.ModelInfo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.*;

import java.util.ArrayList;

public class SelectFromMemoryDialog extends SelectFromTransmitterDialog {
    // handle double click
    private final class DefaultListener implements Listener {
        @Override
        public void handleEvent(final Event arg0) {
            onOpen();
            dialog.close();
        }
    }

    // load model data from transmitter memory
    private final class OpenRunner implements Runnable {
        @Override
        public void run() {
            try {
                final int index = list.getSelectionIndex();
                final int modelNumber = infos.get(index).getModelNumber();

                try {
                    port.open();
                    model = HoTTDecoder.INSTANCE.decodeMemory(port, modelNumber);
                } finally {
                    port.close();
                }
            } catch (final Throwable t) {
                showError(t);
            }
        }
    }

    // (re-)load model list from transmitter memory
    private final class ReloadRunner implements Runnable {
        @Override
        public void run() {
            try {
                final java.util.List<ModelInfo> i;

                try {
                    port.open();
                    i = port.getAllModelInfos();
                } finally {
                    port.close();
                }

                list.removeAll();
                for (final ModelInfo info : i)
                    if (info.getModelType() != ModelType.Unknown) {
                        infos.add(info);
                        list.add(String.format("%02d: %c%s.mdl", info.getModelNumber(), info.getModelType().getChar(),
                                info.getModelName()));
                    }
            } catch (final Throwable t) {
                showError(t);
            }
        }
    }

    private List list;
    private final java.util.List<ModelInfo> infos = new ArrayList<>();

    public SelectFromMemoryDialog(final Shell parent) {
        super(parent);
    }

    @Override
    protected void getSelectionComponent(final Composite dialog) {
        list = new List(dialog, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL);
        list.addListener(SWT.DefaultSelection, new DefaultListener());

        final GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.FILL;
        list.setLayoutData(gridData);

    }

    @Override
    protected void onCancel() {
        model = null;
    }

    @Override
    protected void onOpen() {
        BusyIndicator.showWhile(getParent().getDisplay(), new OpenRunner());
    }

    @Override
    protected void onReload() {
        BusyIndicator.showWhile(getParent().getDisplay(), new ReloadRunner());
    }
}

package gde.mdl.ui.dialogs;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

import de.treichels.hott.HoTTDecoder;
import gde.model.enums.ModelType;
import gde.model.serial.ModelInfo;

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
                    model = HoTTDecoder.decodeMemory(port, modelNumber);
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
                final ModelInfo[] i;

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
                        list.add(String.format("%02d: %c%s.mdl", info.getModelNumber(), info.getModelType() == ModelType.Helicopter ? 'h' : 'a', //$NON-NLS-1$
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

    public SelectFromMemoryDialog(final Shell partent, final int style) {
        super(partent, style);
    }

    @Override
    protected Widget getSelectionComponent(final Composite dialog) {
        list = new List(dialog, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL);
        list.addListener(SWT.DefaultSelection, new DefaultListener());

        final GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.FILL;
        list.setLayoutData(gridData);

        return list;
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

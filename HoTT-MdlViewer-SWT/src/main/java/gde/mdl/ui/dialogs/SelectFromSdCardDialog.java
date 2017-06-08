package gde.mdl.ui.dialogs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;

import de.treichels.hott.HoTTDecoder;
import gde.model.enums.ModelType;
import gde.model.serial.FileInfo;
import gde.model.serial.FileType;

public class SelectFromSdCardDialog extends SelectFromTransmitterDialog {
    // handle double click
    private final class DefaultListener implements Listener {
        @Override
        public void handleEvent(final Event event) {
            final TreeItem treeItem = (TreeItem) event.item;
            final FileInfo fileInfo = (FileInfo) treeItem.getData();

            if (fileInfo != null && fileInfo.getType() == FileType.File && fileInfo.getName().endsWith(".mdl") && fileInfo.getSize() <= 0x3000 //$NON-NLS-1$
                    && fileInfo.getSize() >= 0x2000) {
                onOpen();
                dialog.close();
            }
        }
    }

    private final class ExpandListener implements Listener {
        @Override
        public void handleEvent(final Event event) {
            final TreeItem parentTreeItem = (TreeItem) event.item;
            final FileInfo parentFileInfo = (FileInfo) parentTreeItem.getData();

            parentTreeItem.removeAll();

            BusyIndicator.showWhile(getParent().getDisplay(), () -> {
                try {
                    try {
                        port.open();

                        final String[] names = port.listDir(parentFileInfo.getPath());
                        for (final String name : names) {
                            final FileInfo fileInfo = port.getFileInfo(name);
                            final TreeItem treeItem = new TreeItem(parentTreeItem, SWT.NONE);
                            treeItem.setData(fileInfo);
                            treeItem.setText(fileInfo.getName());

                            if (fileInfo.getType() == FileType.Dir) // add dummy child item
                                new TreeItem(treeItem, SWT.NONE);

                        }
                    } finally {
                        port.close();
                    }
                } catch (final Throwable t) {
                    showError(t);
                }
            });
        }
    }

    // load model data from transmitter sd card
    private final class OpenRunner implements Runnable {
        @Override
        public void run() {
            try {
                final TreeItem[] selection = tree.getSelection();
                if (selection != null && selection.length > 0) {
                    final TreeItem selectedItem = selection[0];
                    final FileInfo fileInfo = (FileInfo) selectedItem.getData();

                    if (fileInfo != null && fileInfo.getType() == FileType.File && fileInfo.getName().endsWith(".mdl") && fileInfo.getSize() <= 0x3000 //$NON-NLS-1$
                            && fileInfo.getSize() >= 0x2000) {
                        final String fileName = fileInfo.getName();

                        // check model type
                        final ModelType type = ModelType.forChar(fileName.charAt(0));
                        final String name = fileName.substring(1, fileName.length() - 4);
                        final ByteArrayOutputStream os = new ByteArrayOutputStream();

                        try {
                            port.open();
                            port.readFile(fileInfo.getPath(), os);
                        } finally {
                            port.close();
                        }

                        final ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
                        model = HoTTDecoder.decodeStream(type, name, is);
                    }
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
                port.open();
                final String[] names = port.listDir("/");

                tree.removeAll();

                for (final String name : names) {
                    final FileInfo fileInfo = port.getFileInfo(name);
                    final TreeItem treeItem = new TreeItem(tree, SWT.NONE);
                    treeItem.setData(fileInfo);
                    treeItem.setText(fileInfo.getName());

                    if (fileInfo.getType() == FileType.Dir) // add dummy child item
                        new TreeItem(treeItem, SWT.NONE);
                }
                port.close();
            } catch (final Throwable t) {
                showError(t);
            }
        }
    }

    private Tree tree;

    public SelectFromSdCardDialog(final Shell parent) {
        super(parent);
    }

    public SelectFromSdCardDialog(final Shell partent, final int style) {
        super(partent, style);
    }

    @Override
    protected Widget getSelectionComponent(final Composite dialog) {
        tree = new Tree(dialog, SWT.SINGLE | SWT.BORDER | SWT.VIRTUAL);
        tree.addListener(SWT.DefaultSelection, new DefaultListener());
        tree.addListener(SWT.Expand, new ExpandListener());

        final GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.FILL;
        tree.setLayoutData(gridData);

        return tree;
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

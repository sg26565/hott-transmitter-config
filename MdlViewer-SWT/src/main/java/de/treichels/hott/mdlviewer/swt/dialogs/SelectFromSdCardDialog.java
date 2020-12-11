package de.treichels.hott.mdlviewer.swt.dialogs;

import de.treichels.hott.model.enums.ModelType;
import de.treichels.hott.serial.FileInfo;
import de.treichels.hott.serial.FileType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static de.treichels.hott.decoder.HoTTDecoderKt.decodeStream;

public class SelectFromSdCardDialog extends SelectFromTransmitterDialog {
    // handle double click
    private final class DefaultListener implements Listener {
        @Override
        public void handleEvent(final Event event) {
            final TreeItem treeItem = (TreeItem) event.item;
            final FileInfo fileInfo = (FileInfo) treeItem.getData();

            if (fileInfo != null && fileInfo.getType() == FileType.File && fileInfo.getName().endsWith(".mdl") && fileInfo.getSize() <= 0x3000
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
                    final String[] names = port.listDir(parentFileInfo.getPath());
                    for (final String name : names) {
                        final FileInfo fileInfo = port.getFileInfo(name);
                        final TreeItem treeItem = new TreeItem(parentTreeItem, SWT.NONE);
                        treeItem.setData(fileInfo);
                        treeItem.setText(fileInfo.getName());

                        if (fileInfo.getType() == FileType.Dir) // add dummy child item
                            new TreeItem(treeItem, SWT.NONE);

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

                    if (fileInfo != null && fileInfo.getType() == FileType.File && fileInfo.getName().endsWith(".mdl") && fileInfo.getSize() <= 0x3000
                            && fileInfo.getSize() >= 0x2000) {
                        final String fileName = fileInfo.getName();

                        // check model type
                        final ModelType type = ModelType.forChar(fileName.charAt(0));
                        final String name = fileName.substring(1, fileName.length() - 4);
                        final ByteArrayOutputStream os = new ByteArrayOutputStream();

                        port.readFile(fileInfo.getPath(), os);

                        final ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
                        model = decodeStream(type, name, is);
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
            } catch (final Throwable t) {
                showError(t);
            }
        }
    }

    private Tree tree;

    public SelectFromSdCardDialog(final Shell parent) {
        super(parent);
    }

    @Override
    protected void getSelectionComponent(final Composite dialog) {
        tree = new Tree(dialog, SWT.SINGLE | SWT.BORDER | SWT.VIRTUAL);
        tree.addListener(SWT.DefaultSelection, new DefaultListener());
        tree.addListener(SWT.Expand, new ExpandListener());

        final GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.FILL;
        tree.setLayoutData(gridData);

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

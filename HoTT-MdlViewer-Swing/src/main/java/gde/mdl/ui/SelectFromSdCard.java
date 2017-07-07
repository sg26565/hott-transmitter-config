/**
 * HoTT Transmitter Config Copyright (C) 2013 Oliver Treichel
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package gde.mdl.ui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import de.treichels.hott.HoTTDecoder;
import gde.mdl.messages.Messages;
import gde.model.BaseModel;
import gde.model.enums.ModelType;
import gde.model.serial.FileInfo;
import gde.model.serial.FileType;

/**
 * @author oli@treichels.de
 */
public class SelectFromSdCard extends SelectFromTransmitter {
    private final class ExpandNodeListener implements TreeWillExpandListener {
        @Override
        public void treeWillCollapse(final TreeExpansionEvent ev) throws ExpandVetoException {}

        @Override
        public void treeWillExpand(final TreeExpansionEvent ev) throws ExpandVetoException {
            expandNode((DefaultMutableTreeNode) ev.getPath().getLastPathComponent());
        }
    }

    private final class ExpandWorker extends SwingWorker<Void, FileInfoTreeNode> {
        private final DefaultMutableTreeNode node;

        private ExpandWorker(final DefaultMutableTreeNode node) {
            this.node = node;
        }

        @Override
        protected Void doInBackground() throws Exception {
            layerUI.start();
            tree.setEnabled(false);
            final String[] names;

            try {
                port.open();

                if (node == rootNode)
                    names = port.listDir("/");
                else if (node instanceof FileInfoTreeNode)
                    names = port.listDir(((FileInfoTreeNode) node).getFileInfo().getPath());
                else
                    names = new String[] {};

                for (final String name : names) {
                    final FileInfo info = port.getFileInfo(name);
                    node.add(new FileInfoTreeNode(info));
                    model.nodesWereInserted(node, new int[] { node.getChildCount() - 1 });
                }
            } finally {
                port.close();
            }

            return null;
        }

        @Override
        protected void done() {
            layerUI.stop();
            tree.setEnabled(true);
        }
    }

    private class FileInfoTreeNode extends DefaultMutableTreeNode {
        private static final long serialVersionUID = 1L;

        public FileInfoTreeNode(final FileInfo info) {
            super(info);
        }

        private FileInfo getFileInfo() {
            return (FileInfo) getUserObject();
        }

        @Override
        public boolean isLeaf() {
            return getFileInfo().getType() == FileType.File;
        }

        @Override
        public String toString() {
            return getFileInfo().getName();
        }
    }

    private static final long serialVersionUID = 1L;

    private final DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(Messages.getString("SelectFromSdCard.RootNodeLabel")); //$NON-NLS-1$
    private final DefaultTreeModel model = new DefaultTreeModel(rootNode);
    private final JTree tree = new JTree(model);
    private FileInfo fileInfo = null;

    public SelectFromSdCard() {
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);
        tree.addTreeWillExpandListener(new ExpandNodeListener());

        initUI();

        onReload();
    }

    private void expandNode(final DefaultMutableTreeNode node) {
        if (node.getChildCount() == 0) new ExpandWorker(node).execute();
    }

    @Override
    public BaseModel getModel() throws IOException {
        BaseModel result = null;

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
            result = HoTTDecoder.decodeStream(type, name, is);
        }

        return result;
    }

    @Override
    public byte[] getModelData() throws IOException {
        if (fileInfo == null || fileInfo.getType() != FileType.File || !fileInfo.getName().endsWith(".mdl") || fileInfo.getSize() > 0x3000 //$NON-NLS-1$
                || fileInfo.getSize() < 0x2000)
            return null;

        final ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            port.open();
            port.readFile(fileInfo.getPath(), os);
        } finally {
            port.close();
        }

        return os.toByteArray();
    }

    @Override
    protected JComponent getSelectionComponent() {
        return tree;
    }

    @Override
    public void onCancel() {
        fileInfo = null;
    }

    @Override
    public void onOpen() {
        fileInfo = null;
        final TreePath path = tree.getSelectionPath();

        if (path != null) {
            final Object node = path.getLastPathComponent();

            if (node != null && node instanceof FileInfoTreeNode) fileInfo = ((FileInfoTreeNode) node).getFileInfo();
        }
    }

    @Override
    public void onReload() {
        fileInfo = null;
        rootNode.removeAllChildren();
        model.reload(rootNode);
        tree.expandPath(new TreePath(rootNode.getPath()));

        if (port != null) expandNode(rootNode);
    }
}
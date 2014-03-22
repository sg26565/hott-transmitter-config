package gde.mdl.ui;

import gde.messages.Messages;
import gde.model.BaseModel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class OpenDialog extends JDialog {
  private final class NullModelLoader implements ModelLoader {
    @Override
    public BaseModel getModel() throws IOException {
      return null;
    }

    @Override
    public void onCancel() {}

    @Override
    public void onOpen() {}

    @Override
    public void onReload() {}
  }

  private final class OnCancelListener implements ActionListener {
    @Override
    public void actionPerformed(final ActionEvent arg0) {
      getLoader().onCancel();
      setVisible(false);
    }
  }

  private final class OnOpenListener implements ActionListener {
    @Override
    public void actionPerformed(final ActionEvent ev) {
      final ModelLoader loader = getLoader();

      loader.onOpen();
      setVisible(false);
    }
  }

  private final class TabChangeListener implements ChangeListener {
    @Override
    public void stateChanged(final ChangeEvent ev) {
      Component component = tabbedPane.getSelectedComponent();

      if (component == null) {
        switch (tabbedPane.getSelectedIndex()) {
        case 1:
          component = new SelectFromMemory();
          tabbedPane.setComponentAt(1, component);
          break;

        case 2:
          component = new SelectFromSdCard();
          tabbedPane.setComponentAt(2, component);
          break;
        }
      }
    }
  }

  private static final long serialVersionUID = 1L;

  private final JButton     openButton       = new JButton(Messages.getString("Open"));

  private final JButton     cancelButton     = new JButton(Messages.getString("Cancel"));
  private final JTabbedPane tabbedPane       = new JTabbedPane();

  public OpenDialog(final JFrame owner) {
    super(owner, Messages.getString("Load"), true); //$NON-NLS-1$

    tabbedPane.addTab(Messages.getString("LoadFromFile"), new SelectFromFile()); //$NON-NLS-1$
    tabbedPane.addTab(Messages.getString("LoadFromMemory"), null); //$NON-NLS-1$
    tabbedPane.addTab(Messages.getString("LoadFromSdCard"), null); //$NON-NLS-1$
    tabbedPane.addChangeListener(new TabChangeListener());

    cancelButton.addActionListener(new OnCancelListener());
    openButton.addActionListener(new OnOpenListener());

    final JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
    buttonPanel.add(openButton);
    buttonPanel.add(cancelButton);

    final Container container = getContentPane();
    container.setLayout(new BorderLayout());
    container.add(buttonPanel, BorderLayout.SOUTH);
    container.add(tabbedPane, BorderLayout.CENTER);

    getRootPane().setDefaultButton(openButton);

    pack();

    setLocationRelativeTo(owner);
  }

  private ModelLoader getLoader() {
    final Component component = tabbedPane.getSelectedComponent();
    if (component != null && component instanceof ModelLoader) {
      return (ModelLoader) component;
    }

    return new NullModelLoader();
  }

  public BaseModel getModel() throws IOException {
    return getLoader().getModel();
  }
}

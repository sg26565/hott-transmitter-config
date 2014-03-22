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

public class OpenDialog extends JDialog {
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

  private static final long serialVersionUID = 1L;

  private final JButton     openButton       = new JButton(Messages.getString("Open"));
  private final JButton     cancelButton     = new JButton(Messages.getString("Cancel"));
  private final JTabbedPane tabbedPane       = new JTabbedPane();

  public OpenDialog(final JFrame owner) {
    super(owner, Messages.getString("Load"), true); //$NON-NLS-1$

    tabbedPane.addTab(Messages.getString("LoadFromFile"), new SelectFromFile()); //$NON-NLS-1$
    tabbedPane.addTab(Messages.getString("LoadFromMemory"), new SelectFromMemory()); //$NON-NLS-1$
    tabbedPane.addTab(Messages.getString("LoadFromSdCard"), new SelectFromSdCard()); //$NON-NLS-1$

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

    return new ModelLoader();
  }

  public BaseModel getModel() throws IOException {
    return getLoader().getModel();
  }
}

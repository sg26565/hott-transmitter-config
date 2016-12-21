package gde.mdl.ui.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class BaseConfiguration extends ScrolledComposite {

	private CLabel infoLabel;
	private CLabel receiverLabel;
	private CLabel modulTypeLabel;
	private CLabel autoTrimLabel;
	private CLabel clockResetLabel;
	private CCombo powerWarningCombo;
	private CLabel txLabel1, txLabel2, txLabel3, txLabel4;
	private CCombo autoResetClockCombo;
	private CCombo autoTrimCombo;
	private CCombo txModuleCombo;
	private CCombo controlModeCombo;
	private Text infoText;
	private Composite baseComposite;
	private Text modelNameText;
	private CLabel powerWarningLabel;
	private CLabel dscOutputLabel;
	private CLabel stickModeLabel;
	private CLabel modellNameLabel;
	private CLabel headerLabel;

	public BaseConfiguration(final Composite tabFolder, final CTabItem tabItem) {
		super(tabFolder, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		tabItem.setControl(this);
		open(""); //$NON-NLS-1$
		baseComposite.layout();
	}

	public BaseConfiguration(final Composite tabFolder, final String name) {
		super(tabFolder, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		open(name);
		baseComposite.layout();
	}

	private void open(final String header) {
		baseComposite = new Composite(this, SWT.NONE);
		setContent(baseComposite);
		final GridLayout composite1Layout1 = new GridLayout();
		composite1Layout1.numColumns = 6;
		baseComposite.setBounds(0, 0, 500, 300);
		baseComposite.setLayout(composite1Layout1);
		if (header != null && header.length() > 0) {
			headerLabel = new CLabel(baseComposite, SWT.NONE);
			headerLabel.setText(header);
			final GridData gridData = new GridData();
			gridData.horizontalSpan = 6;
			gridData.widthHint = 175;
			gridData.heightHint = 22;
			headerLabel.setLayoutData(gridData);
			// TODO headerLabel.setFont(SWTResourceManager.getFont("Microsoft
			// Sans Serif", 10, 1, false, false));
		}
		{
			modellNameLabel = new CLabel(baseComposite, SWT.NONE);
			modellNameLabel.setText("Modellname"); //$NON-NLS-1$
			final GridData gridData = new GridData();
			gridData.widthHint = 175;
			gridData.heightHint = 22;
			gridData.horizontalSpan = 2;
			modellNameLabel.setLayoutData(gridData);

		}
		{
			modelNameText = new Text(baseComposite, SWT.BORDER);
			final GridData modelNameTextLData = new GridData();
			modelNameTextLData.horizontalSpan = 4;
			modelNameTextLData.widthHint = 100;
			modelNameTextLData.heightHint = 16;
			modelNameText.setLayoutData(modelNameTextLData);
			modelNameText.setText("Wingo"); //$NON-NLS-1$
		}
		{
			infoLabel = new CLabel(baseComposite, SWT.NONE);
			final GridData infoLabelLData = new GridData();
			infoLabelLData.widthHint = 175;
			infoLabelLData.heightHint = 22;
			infoLabelLData.horizontalSpan = 2;
			infoLabel.setLayoutData(infoLabelLData);
			infoLabel.setText("Information"); //$NON-NLS-1$
		}
		{
			infoText = new Text(baseComposite, SWT.BORDER);
			final GridData infoTextLData = new GridData();
			infoTextLData.horizontalSpan = 4;
			infoTextLData.widthHint = 100;
			infoTextLData.heightHint = 16;
			infoText.setLayoutData(infoTextLData);
			infoText.setText("Modell Zusatzinformation"); //$NON-NLS-1$
		}
		{
			stickModeLabel = new CLabel(baseComposite, SWT.NONE);
			final GridData stickModeLabelLData = new GridData();
			stickModeLabelLData.widthHint = 175;
			stickModeLabelLData.heightHint = 22;
			stickModeLabelLData.horizontalSpan = 2;
			stickModeLabel.setLayoutData(stickModeLabelLData);
			stickModeLabel.setText("Steueranordnung"); //$NON-NLS-1$
		}
		{
			controlModeCombo = new CCombo(baseComposite, SWT.BORDER);
			final GridData controlModeComboLData = new GridData();
			controlModeComboLData.widthHint = 40;
			controlModeComboLData.heightHint = 17;
			controlModeComboLData.horizontalSpan = 4;
			controlModeCombo.setLayoutData(controlModeComboLData);
			controlModeCombo.setText("1"); //$NON-NLS-1$
		}
		{
			modulTypeLabel = new CLabel(baseComposite, SWT.NONE);
			final GridData modulTypeLabelLData = new GridData();
			modulTypeLabelLData.widthHint = 105;
			modulTypeLabelLData.heightHint = 22;
			modulTypeLabel.setLayoutData(modulTypeLabelLData);
			modulTypeLabel.setText("Modul"); //$NON-NLS-1$
		}
		{
			txModuleCombo = new CCombo(baseComposite, SWT.BORDER);
			final GridData txModuleComboLData = new GridData();
			txModuleComboLData.widthHint = 60;
			txModuleComboLData.heightHint = 17;
			txModuleCombo.setLayoutData(txModuleComboLData);
			txModuleCombo.setText("HoTT"); //$NON-NLS-1$
		}
		{
			txLabel1 = new CLabel(baseComposite, SWT.CENTER | SWT.EMBEDDED | SWT.BORDER);
			final GridData txLabel1LData = new GridData();
			txLabel1LData.widthHint = 29;
			txLabel1LData.heightHint = 22;
			txLabel1.setLayoutData(txLabel1LData);
			txLabel1.setText("geb."); //$NON-NLS-1$
		}
		{
			txLabel2 = new CLabel(baseComposite, SWT.CENTER | SWT.EMBEDDED | SWT.BORDER);
			final GridData txLabel2LData = new GridData();
			txLabel2LData.widthHint = 29;
			txLabel2LData.heightHint = 22;
			txLabel2.setLayoutData(txLabel2LData);
			txLabel2.setText("----"); //$NON-NLS-1$
		}
		{
			txLabel3 = new CLabel(baseComposite, SWT.CENTER | SWT.EMBEDDED | SWT.BORDER);
			final GridData txLabel3LData1 = new GridData();
			txLabel3LData1.widthHint = 29;
			txLabel3LData1.heightHint = 22;
			txLabel3.setLayoutData(txLabel3LData1);
			txLabel3.setText("----"); //$NON-NLS-1$
		}
		{
			txLabel4 = new CLabel(baseComposite, SWT.CENTER | SWT.EMBEDDED | SWT.BORDER);
			final GridData txLabel4LData1 = new GridData();
			txLabel4LData1.widthHint = 29;
			txLabel4LData1.heightHint = 22;
			txLabel4.setLayoutData(txLabel4LData1);
			txLabel4.setText("----"); //$NON-NLS-1$
		}
		{
			receiverLabel = new CLabel(baseComposite, SWT.NONE);
			final GridData receiverLabelLData = new GridData();
			receiverLabelLData.widthHint = 175;
			receiverLabelLData.heightHint = 22;
			receiverLabelLData.horizontalSpan = 2;
			receiverLabel.setLayoutData(receiverLabelLData);
			receiverLabel.setText("Empfänger Ausgang"); //$NON-NLS-1$
		}
		{
			txLabel1 = new CLabel(baseComposite, SWT.CENTER | SWT.EMBEDDED | SWT.BORDER);
			final GridData txLabel1LData = new GridData();
			txLabel1LData.widthHint = 29;
			txLabel1LData.heightHint = 22;
			txLabel1.setLayoutData(txLabel1LData);
			txLabel1.setText("E08"); //$NON-NLS-1$
		}
		{
			txLabel2 = new CLabel(baseComposite, SWT.CENTER | SWT.EMBEDDED | SWT.BORDER);
			final GridData txLabel2LData = new GridData();
			txLabel2LData.widthHint = 29;
			txLabel2LData.heightHint = 22;
			txLabel2.setLayoutData(txLabel2LData);
			txLabel2.setText("n/v"); //$NON-NLS-1$
		}
		{
			txLabel3 = new CLabel(baseComposite, SWT.CENTER | SWT.EMBEDDED | SWT.BORDER);
			final GridData txLabel3LData1 = new GridData();
			txLabel3LData1.widthHint = 29;
			txLabel3LData1.heightHint = 22;
			txLabel3.setLayoutData(txLabel3LData1);
			txLabel3.setText("n/v"); //$NON-NLS-1$
		}
		{
			txLabel4 = new CLabel(baseComposite, SWT.CENTER | SWT.EMBEDDED | SWT.BORDER);
			final GridData txLabel4LData1 = new GridData();
			txLabel4LData1.widthHint = 29;
			txLabel4LData1.heightHint = 22;
			txLabel4.setLayoutData(txLabel4LData1);
			txLabel4.setText("n/v"); //$NON-NLS-1$
		}
		{
			dscOutputLabel = new CLabel(baseComposite, SWT.NONE);
			final GridData dscOutputLabelLData = new GridData();
			dscOutputLabelLData.widthHint = 175;
			dscOutputLabelLData.heightHint = 22;
			dscOutputLabelLData.horizontalSpan = 2;
			dscOutputLabel.setLayoutData(dscOutputLabelLData);
			dscOutputLabel.setText("DSC-Ausgang"); //$NON-NLS-1$
		}
		{
			controlModeCombo = new CCombo(baseComposite, SWT.CENTER | SWT.BORDER);
			final GridData controlModeTextLData = new GridData();
			controlModeTextLData.widthHint = 60;
			controlModeTextLData.heightHint = 17;
			controlModeTextLData.horizontalSpan = 4;
			controlModeCombo.setLayoutData(controlModeTextLData);
			controlModeCombo.setText("PPM10"); //$NON-NLS-1$
		}
		{
			powerWarningLabel = new CLabel(baseComposite, SWT.NONE);
			final GridData powerWarningLabelLData = new GridData();
			powerWarningLabelLData.widthHint = 175;
			powerWarningLabelLData.heightHint = 22;
			powerWarningLabelLData.horizontalSpan = 2;
			powerWarningLabel.setLayoutData(powerWarningLabelLData);
			powerWarningLabel.setText("Einschaltwarnung"); //$NON-NLS-1$
		}
		{
			powerWarningCombo = new CCombo(baseComposite, SWT.BORDER);
			final GridData powerWarningComboLData = new GridData();
			powerWarningComboLData.horizontalSpan = 4;
			powerWarningComboLData.widthHint = 60;
			powerWarningComboLData.heightHint = 17;
			powerWarningCombo.setLayoutData(powerWarningComboLData);
			powerWarningCombo.setText("G1"); //$NON-NLS-1$
		}
		{
			autoTrimLabel = new CLabel(baseComposite, SWT.NONE);
			final GridData autoTrimLabelLData = new GridData();
			autoTrimLabelLData.widthHint = 175;
			autoTrimLabelLData.heightHint = 22;
			autoTrimLabelLData.horizontalSpan = 2;
			autoTrimLabel.setLayoutData(autoTrimLabelLData);
			autoTrimLabel.setText("Auto-Trimm"); //$NON-NLS-1$
		}
		{
			autoTrimCombo = new CCombo(baseComposite, SWT.BORDER);
			final GridData autoTrimComboLData = new GridData();
			autoTrimComboLData.horizontalSpan = 4;
			autoTrimComboLData.widthHint = 60;
			autoTrimComboLData.heightHint = 17;
			autoTrimCombo.setLayoutData(autoTrimComboLData);
			autoTrimCombo.setText("ON"); //$NON-NLS-1$
		}
		{
			clockResetLabel = new CLabel(baseComposite, SWT.NONE);
			final GridData clockResetLabelLData = new GridData();
			clockResetLabelLData.widthHint = 175;
			clockResetLabelLData.heightHint = 22;
			clockResetLabelLData.horizontalSpan = 2;
			clockResetLabel.setLayoutData(clockResetLabelLData);
			clockResetLabel.setText("Auto rücksetzen Uhr"); //$NON-NLS-1$
		}
		{
			autoResetClockCombo = new CCombo(baseComposite, SWT.BORDER);
			final GridData autoResetClockComboLData = new GridData();
			autoResetClockComboLData.horizontalSpan = 4;
			autoResetClockComboLData.widthHint = 60;
			autoResetClockComboLData.heightHint = 17;
			autoResetClockCombo.setLayoutData(autoResetClockComboLData);
			autoResetClockCombo.setText("ON"); //$NON-NLS-1$
		}
	}

}

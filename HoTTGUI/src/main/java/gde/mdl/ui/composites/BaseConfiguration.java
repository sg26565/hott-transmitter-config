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



/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class BaseConfiguration extends ScrolledComposite {

	private CLabel						infoLabel;
	private CLabel						receiverLabel;
	private CLabel						modulTypeLabel;
	private CLabel						autoTrimLabel;
	private CLabel						clockResetLabel;
	private CCombo						powerWarningCombo;
	private CLabel						txLabel1, txLabel2, txLabel3, txLabel4;
	private CCombo						autoResetClockCombo;
	private CCombo						autoTrimCombo;
	private CCombo						txModuleCombo;
	private CCombo						controlModeCombo;
	private Text							infoText;
	private Composite					baseComposite;
	private Text							modelNameText;
	private CLabel						powerWarningLabel;
	private CLabel						dscOutputLabel;
	private CLabel						stickModeLabel;
	private CLabel						modellNameLabel;
	private CLabel						headerLabel;

	public BaseConfiguration(Composite tabFolder, CTabItem tabItem) {
	super(tabFolder, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
	tabItem.setControl(this);
	this.open("");
	baseComposite.layout();
	}
	
	public BaseConfiguration(Composite tabFolder) {
	super(tabFolder, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
	this.open("Grundeinstellung");
	baseComposite.layout();
	}

	private void open(String header) {
		baseComposite = new Composite(this, SWT.NONE);
		this.setContent(baseComposite);
		GridLayout composite1Layout1 = new GridLayout();
		composite1Layout1.numColumns = 6;
		baseComposite.setSize(500, 300);
		baseComposite.setLayout(composite1Layout1);
		if (header != null && header.length() > 0){
			headerLabel = new CLabel(baseComposite, SWT.NONE);
			headerLabel.setText(header);
			GridData gridData = new GridData();
			gridData.horizontalSpan = 6;
			gridData.widthHint = 175;
			gridData.heightHint = 22;
			headerLabel.setLayoutData(gridData);
			//headerLabel.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 10, 1, false, false));
		}
		{
			modellNameLabel = new CLabel(baseComposite, SWT.NONE);
			modellNameLabel.setText("Modellname");
			GridData gridData = new GridData();
			gridData.widthHint = 175;
			gridData.heightHint = 22;
			gridData.horizontalSpan = 2;
			modellNameLabel.setLayoutData(gridData);

		}
		{
			modelNameText = new Text(baseComposite, SWT.BORDER);
			GridData modelNameTextLData = new GridData();
			modelNameTextLData.horizontalSpan = 4;
			modelNameTextLData.widthHint = 100;
			modelNameTextLData.heightHint = 16;
			modelNameText.setLayoutData(modelNameTextLData);
			modelNameText.setText("Wingo");
		}
		{
			infoLabel = new CLabel(baseComposite, SWT.NONE);
			GridData infoLabelLData = new GridData();
			infoLabelLData.widthHint = 175;
			infoLabelLData.heightHint = 22;
			infoLabelLData.horizontalSpan = 2;
			infoLabel.setLayoutData(infoLabelLData);
			infoLabel.setText("Information");
		}
		{
			infoText = new Text(baseComposite, SWT.BORDER);
			GridData infoTextLData = new GridData();
			infoTextLData.horizontalSpan = 4;
			infoText.setLayoutData(infoTextLData);
			infoText.setText("Modell Zusatzinformation");
		}
		{
			stickModeLabel = new CLabel(baseComposite, SWT.NONE);
			GridData stickModeLabelLData = new GridData();
			stickModeLabelLData.widthHint = 175;
			stickModeLabelLData.heightHint = 22;
			stickModeLabelLData.horizontalSpan = 2;
			stickModeLabel.setLayoutData(stickModeLabelLData);
			stickModeLabel.setText("Steueranordnung");
		}
		{
			controlModeCombo = new CCombo(baseComposite, SWT.BORDER);
			GridData controlModeComboLData = new GridData();
			controlModeComboLData.widthHint = 40;
			controlModeComboLData.heightHint = 17;
			controlModeComboLData.horizontalSpan = 4;
			controlModeCombo.setLayoutData(controlModeComboLData);
			controlModeCombo.setText("1");
		}
		{
			modulTypeLabel = new CLabel(baseComposite, SWT.NONE);
			GridData modulTypeLabelLData = new GridData();
			modulTypeLabelLData.widthHint = 105;
			modulTypeLabelLData.heightHint = 22;
			modulTypeLabel.setLayoutData(modulTypeLabelLData);
			modulTypeLabel.setText("Modul");
		}
		{
			txModuleCombo = new CCombo(baseComposite, SWT.BORDER);
			GridData txModuleComboLData = new GridData();
			txModuleComboLData.widthHint = 60;
			txModuleComboLData.heightHint = 17;
			txModuleCombo.setLayoutData(txModuleComboLData);
			txModuleCombo.setText("HoTT");
		}
		{
			txLabel1 = new CLabel(baseComposite, SWT.CENTER | SWT.EMBEDDED | SWT.BORDER);
			GridData txLabel1LData = new GridData();
			txLabel1LData.widthHint = 29;
			txLabel1LData.heightHint = 22;
			txLabel1.setLayoutData(txLabel1LData);
			txLabel1.setText("geb.");
		}
		{
			txLabel2 = new CLabel(baseComposite, SWT.CENTER | SWT.EMBEDDED | SWT.BORDER);
			GridData txLabel2LData = new GridData();
			txLabel2LData.widthHint = 29;
			txLabel2LData.heightHint = 22;
			txLabel2.setLayoutData(txLabel2LData);
			txLabel2.setText("----");
		}
		{
			txLabel3 = new CLabel(baseComposite, SWT.CENTER | SWT.EMBEDDED | SWT.BORDER);
			GridData txLabel3LData1 = new GridData();
			txLabel3LData1.widthHint = 29;
			txLabel3LData1.heightHint = 22;
			txLabel3.setLayoutData(txLabel3LData1);
			txLabel3.setText("----");
		}
		{
			txLabel4 = new CLabel(baseComposite, SWT.CENTER | SWT.EMBEDDED | SWT.BORDER);
			GridData txLabel4LData1 = new GridData();
			txLabel4LData1.widthHint = 29;
			txLabel4LData1.heightHint = 22;
			txLabel4.setLayoutData(txLabel4LData1);
			txLabel4.setText("----");
		}
		{
			receiverLabel = new CLabel(baseComposite, SWT.NONE);
			GridData receiverLabelLData = new GridData();
			receiverLabelLData.widthHint = 175;
			receiverLabelLData.heightHint = 22;
			receiverLabelLData.horizontalSpan = 2;
			receiverLabel.setLayoutData(receiverLabelLData);
			receiverLabel.setText("Empfänger Ausgang");
		}
		{
			txLabel1 = new CLabel(baseComposite, SWT.CENTER | SWT.EMBEDDED | SWT.BORDER);
			GridData txLabel1LData = new GridData();
			txLabel1LData.widthHint = 29;
			txLabel1LData.heightHint = 22;
			txLabel1.setLayoutData(txLabel1LData);
			txLabel1.setText("E08");
		}
		{
			txLabel2 = new CLabel(baseComposite, SWT.CENTER | SWT.EMBEDDED | SWT.BORDER);
			GridData txLabel2LData = new GridData();
			txLabel2LData.widthHint = 29;
			txLabel2LData.heightHint = 22;
			txLabel2.setLayoutData(txLabel2LData);
			txLabel2.setText("n/v");
		}
		{
			txLabel3 = new CLabel(baseComposite, SWT.CENTER | SWT.EMBEDDED | SWT.BORDER);
			GridData txLabel3LData1 = new GridData();
			txLabel3LData1.widthHint = 29;
			txLabel3LData1.heightHint = 22;
			txLabel3.setLayoutData(txLabel3LData1);
			txLabel3.setText("n/v");
		}
		{
			txLabel4 = new CLabel(baseComposite, SWT.CENTER | SWT.EMBEDDED | SWT.BORDER);
			GridData txLabel4LData1 = new GridData();
			txLabel4LData1.widthHint = 29;
			txLabel4LData1.heightHint = 22;
			txLabel4.setLayoutData(txLabel4LData1);
			txLabel4.setText("n/v");
		}
		{
			dscOutputLabel = new CLabel(baseComposite, SWT.NONE);
			GridData dscOutputLabelLData = new GridData();
			dscOutputLabelLData.widthHint = 175;
			dscOutputLabelLData.heightHint = 22;
			dscOutputLabelLData.horizontalSpan = 2;
			dscOutputLabel.setLayoutData(dscOutputLabelLData);
			dscOutputLabel.setText("DSC-Ausgang");
		}
		{
			controlModeCombo = new CCombo(baseComposite, SWT.CENTER | SWT.BORDER);
			GridData controlModeTextLData = new GridData();
			controlModeTextLData.widthHint = 60;
			controlModeTextLData.heightHint = 17;
			controlModeTextLData.horizontalSpan = 4;
			controlModeCombo.setLayoutData(controlModeTextLData);
			controlModeCombo.setText("PPM10");
		}
		{
			powerWarningLabel = new CLabel(baseComposite, SWT.NONE);
			GridData powerWarningLabelLData = new GridData();
			powerWarningLabelLData.widthHint = 175;
			powerWarningLabelLData.heightHint = 22;
			powerWarningLabelLData.horizontalSpan = 2;
			powerWarningLabel.setLayoutData(powerWarningLabelLData);
			powerWarningLabel.setText("Einschaltwarnung");
		}
		{
			powerWarningCombo = new CCombo(baseComposite, SWT.BORDER);
			GridData powerWarningComboLData = new GridData();
			powerWarningComboLData.horizontalSpan = 4;
			powerWarningComboLData.widthHint = 60;
			powerWarningComboLData.heightHint = 17;
			powerWarningCombo.setLayoutData(powerWarningComboLData);
			powerWarningCombo.setText("G1");
		}
		{
			autoTrimLabel = new CLabel(baseComposite, SWT.NONE);
			GridData autoTrimLabelLData = new GridData();
			autoTrimLabelLData.widthHint = 175;
			autoTrimLabelLData.heightHint = 22;
			autoTrimLabelLData.horizontalSpan = 2;
			autoTrimLabel.setLayoutData(autoTrimLabelLData);
			autoTrimLabel.setText("Auto-Trimm");
		}
		{
			autoTrimCombo = new CCombo(baseComposite, SWT.BORDER);
			GridData autoTrimComboLData = new GridData();
			autoTrimComboLData.horizontalSpan = 4;
			autoTrimComboLData.widthHint = 60;
			autoTrimComboLData.heightHint = 17;
			autoTrimCombo.setLayoutData(autoTrimComboLData);
			autoTrimCombo.setText("ON");
		}
		{
			clockResetLabel = new CLabel(baseComposite, SWT.NONE);
			GridData clockResetLabelLData = new GridData();
			clockResetLabelLData.widthHint = 175;
			clockResetLabelLData.heightHint = 22;
			clockResetLabelLData.horizontalSpan = 2;
			clockResetLabel.setLayoutData(clockResetLabelLData);
			clockResetLabel.setText("Auto rücksetzen Uhr");
		}
		{
			autoResetClockCombo = new CCombo(baseComposite, SWT.BORDER);
			GridData autoResetClockComboLData = new GridData();
			autoResetClockComboLData.horizontalSpan = 4;
			autoResetClockComboLData.widthHint = 60;
			autoResetClockComboLData.heightHint = 17;
			autoResetClockCombo.setLayoutData(autoResetClockComboLData);
			autoResetClockCombo.setText("ON");
		}
	}

}

package de.treichels.hott.mdlviewer.swt.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class DualExpoConfiguration extends ScrolledComposite {

    private Composite baseComposite;

    public DualExpoConfiguration(final Composite tabFolder, final String name) {
        super(tabFolder, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        open(name);
        baseComposite.layout();
    }

    private void open(final String header) {
        baseComposite = new Composite(this, SWT.NONE);
        setContent(baseComposite);
        final GridLayout composite1Layout1 = new GridLayout();
        composite1Layout1.numColumns = 9;
        baseComposite.setBounds(0, 0, 500, 300);
        baseComposite.setLayout(composite1Layout1);
        if (header != null && header.length() > 0) {
            CLabel headerLabel = new CLabel(baseComposite, SWT.NONE);
            headerLabel.setText(header);
            final GridData gridData = new GridData();
            gridData.horizontalSpan = 9;
            gridData.widthHint = 175;
            gridData.heightHint = 22;
            headerLabel.setLayoutData(gridData);
            // TODO headerLabel.setFont(SWTResourceManager.getFont("Microsoft
            // Sans Serif", 10, 1, false, false));
        }
        {
            CLabel qrLabel = new CLabel(baseComposite, SWT.NONE);
            final GridData qrLabelLData = new GridData();
            qrLabelLData.widthHint = 53;
            qrLabelLData.heightHint = 22;
            qrLabel.setLayoutData(qrLabelLData);
            qrLabel.setText("QR [%]");
        }
        Text qrDualText1;
        {
            qrDualText1 = new Text(baseComposite, SWT.CENTER | SWT.BORDER);
            final GridData qrDualText1LData = new GridData();
            qrDualText1LData.horizontalAlignment = GridData.CENTER;
            qrDualText1LData.widthHint = 25;
            qrDualText1LData.heightHint = 16;
            qrDualText1.setLayoutData(qrDualText1LData);
            qrDualText1.setText("60");
        }
        Text qrExpoText1;
        {
            qrExpoText1 = new Text(baseComposite, SWT.CENTER | SWT.BORDER);
            final GridData qrExpoText1LData = new GridData();
            qrExpoText1LData.horizontalAlignment = GridData.CENTER;
            qrExpoText1LData.widthHint = 25;
            qrExpoText1LData.heightHint = 16;
            qrExpoText1.setLayoutData(qrExpoText1LData);
            qrExpoText1.setText("50");
        }
        Text qrDualText2;
        {
            qrDualText2 = new Text(baseComposite, SWT.CENTER | SWT.BORDER);
            final GridData qrDualText2LData = new GridData();
            qrDualText2LData.horizontalAlignment = GridData.CENTER;
            qrDualText2LData.widthHint = 25;
            qrDualText2LData.heightHint = 16;
            qrDualText2.setLayoutData(qrDualText2LData);
            qrDualText2.setText("40");
        }
        Text qrExpoText2;
        {
            qrExpoText2 = new Text(baseComposite, SWT.CENTER | SWT.BORDER);
            final GridData qrExpoText2LData = new GridData();
            qrExpoText2LData.horizontalAlignment = GridData.CENTER;
            qrExpoText2LData.widthHint = 25;
            qrExpoText2LData.heightHint = 16;
            qrExpoText2.setLayoutData(qrExpoText2LData);
            qrExpoText2.setText("20");
        }
        Text qrDualText3;
        {
            qrDualText3 = new Text(baseComposite, SWT.CENTER | SWT.BORDER);
            final GridData qrDualText3LData = new GridData();
            qrDualText3LData.horizontalAlignment = GridData.CENTER;
            qrDualText3LData.widthHint = 25;
            qrDualText3LData.heightHint = 16;
            qrDualText3.setLayoutData(qrDualText3LData);
            qrDualText3.setText("30");
        }
        Text qrExpoText3;
        {
            qrExpoText3 = new Text(baseComposite, SWT.CENTER | SWT.BORDER);
            final GridData qrExpoText3LData = new GridData();
            qrExpoText3LData.horizontalAlignment = GridData.CENTER;
            qrExpoText3LData.widthHint = 25;
            qrExpoText3LData.heightHint = 16;
            qrExpoText3.setLayoutData(qrExpoText3LData);
            qrExpoText3.setText("10");
        }
        CCombo qrDualCombo;
        {
            qrDualCombo = new CCombo(baseComposite, SWT.BORDER);
            final GridData qrDualComboLData = new GridData();
            qrDualCombo.setLayoutData(qrDualComboLData);
            qrDualCombo.setText(" 4 |");
        }
        CCombo qrExpoCombo;
        {
            qrExpoCombo = new CCombo(baseComposite, SWT.BORDER);
            final GridData qrExpoComboLData = new GridData();
            qrExpoCombo.setLayoutData(qrExpoComboLData);
            qrExpoCombo.setText("---");
        }
        {
            CLabel hrLabel = new CLabel(baseComposite, SWT.NONE);
            final GridData hrLabelLData = new GridData();
            hrLabelLData.widthHint = 53;
            hrLabelLData.heightHint = 22;
            hrLabel.setLayoutData(hrLabelLData);
            hrLabel.setText("HR [%]");
        }
        {
            qrDualText1 = new Text(baseComposite, SWT.CENTER | SWT.BORDER);
            final GridData qrDualText1LData = new GridData();
            qrDualText1LData.horizontalAlignment = GridData.CENTER;
            qrDualText1LData.widthHint = 25;
            qrDualText1LData.heightHint = 16;
            qrDualText1.setLayoutData(qrDualText1LData);
            qrDualText1.setText("60");
        }
        {
            qrExpoText1 = new Text(baseComposite, SWT.CENTER | SWT.BORDER);
            final GridData qrExpoText1LData = new GridData();
            qrExpoText1LData.horizontalAlignment = GridData.CENTER;
            qrExpoText1LData.widthHint = 25;
            qrExpoText1LData.heightHint = 16;
            qrExpoText1.setLayoutData(qrExpoText1LData);
            qrExpoText1.setText("50");
        }
        {
            qrDualText2 = new Text(baseComposite, SWT.CENTER | SWT.BORDER);
            final GridData qrDualText2LData = new GridData();
            qrDualText2LData.horizontalAlignment = GridData.CENTER;
            qrDualText2LData.widthHint = 25;
            qrDualText2LData.heightHint = 16;
            qrDualText2.setLayoutData(qrDualText2LData);
            qrDualText2.setText("40");
        }
        {
            qrExpoText2 = new Text(baseComposite, SWT.CENTER | SWT.BORDER);
            final GridData qrExpoText2LData = new GridData();
            qrExpoText2LData.horizontalAlignment = GridData.CENTER;
            qrExpoText2LData.widthHint = 25;
            qrExpoText2LData.heightHint = 16;
            qrExpoText2.setLayoutData(qrExpoText2LData);
            qrExpoText2.setText("20");
        }
        {
            qrDualText3 = new Text(baseComposite, SWT.CENTER | SWT.BORDER);
            final GridData qrDualText3LData = new GridData();
            qrDualText3LData.horizontalAlignment = GridData.CENTER;
            qrDualText3LData.widthHint = 25;
            qrDualText3LData.heightHint = 16;
            qrDualText3.setLayoutData(qrDualText3LData);
            qrDualText3.setText("30");
        }
        {
            qrExpoText3 = new Text(baseComposite, SWT.CENTER | SWT.BORDER);
            final GridData qrExpoText3LData = new GridData();
            qrExpoText3LData.horizontalAlignment = GridData.CENTER;
            qrExpoText3LData.widthHint = 25;
            qrExpoText3LData.heightHint = 16;
            qrExpoText3.setLayoutData(qrExpoText3LData);
            qrExpoText3.setText("10");
        }
        {
            qrDualCombo = new CCombo(baseComposite, SWT.BORDER);
            final GridData qrDualComboLData = new GridData();
            qrDualCombo.setLayoutData(qrDualComboLData);
            qrDualCombo.setText(" 4 |");
        }
        {
            qrExpoCombo = new CCombo(baseComposite, SWT.BORDER);
            final GridData qrExpoComboLData = new GridData();
            qrExpoCombo.setLayoutData(qrExpoComboLData);
            qrExpoCombo.setText("---");
        }
        {
            CLabel srLabel = new CLabel(baseComposite, SWT.NONE);
            final GridData srLabelLData = new GridData();
            srLabelLData.widthHint = 53;
            srLabelLData.heightHint = 22;
            srLabel.setLayoutData(srLabelLData);
            srLabel.setText("SR [%]");
        }
        {
            qrDualText1 = new Text(baseComposite, SWT.CENTER | SWT.BORDER);
            final GridData qrDualText1LData = new GridData();
            qrDualText1LData.horizontalAlignment = GridData.CENTER;
            qrDualText1LData.widthHint = 25;
            qrDualText1LData.heightHint = 16;
            qrDualText1.setLayoutData(qrDualText1LData);
            qrDualText1.setText("60");
        }
        {
            qrExpoText1 = new Text(baseComposite, SWT.CENTER | SWT.BORDER);
            final GridData qrExpoText1LData = new GridData();
            qrExpoText1LData.horizontalAlignment = GridData.CENTER;
            qrExpoText1LData.widthHint = 25;
            qrExpoText1LData.heightHint = 16;
            qrExpoText1.setLayoutData(qrExpoText1LData);
            qrExpoText1.setText("50");
        }
        {
            qrDualText2 = new Text(baseComposite, SWT.CENTER | SWT.BORDER);
            final GridData qrDualText2LData = new GridData();
            qrDualText2LData.horizontalAlignment = GridData.CENTER;
            qrDualText2LData.widthHint = 25;
            qrDualText2LData.heightHint = 16;
            qrDualText2.setLayoutData(qrDualText2LData);
            qrDualText2.setText("40");
        }
        {
            qrExpoText2 = new Text(baseComposite, SWT.CENTER | SWT.BORDER);
            final GridData qrExpoText2LData = new GridData();
            qrExpoText2LData.horizontalAlignment = GridData.CENTER;
            qrExpoText2LData.widthHint = 25;
            qrExpoText2LData.heightHint = 16;
            qrExpoText2.setLayoutData(qrExpoText2LData);
            qrExpoText2.setText("20");
        }
        {
            qrDualText3 = new Text(baseComposite, SWT.CENTER | SWT.BORDER);
            final GridData qrDualText3LData = new GridData();
            qrDualText3LData.horizontalAlignment = GridData.CENTER;
            qrDualText3LData.widthHint = 25;
            qrDualText3LData.heightHint = 16;
            qrDualText3.setLayoutData(qrDualText3LData);
            qrDualText3.setText("30");
        }
        {
            qrExpoText3 = new Text(baseComposite, SWT.CENTER | SWT.BORDER);
            final GridData qrExpoText3LData = new GridData();
            qrExpoText3LData.horizontalAlignment = GridData.CENTER;
            qrExpoText3LData.widthHint = 25;
            qrExpoText3LData.heightHint = 16;
            qrExpoText3.setLayoutData(qrExpoText3LData);
            qrExpoText3.setText("100");
        }
        {
            qrDualCombo = new CCombo(baseComposite, SWT.BORDER);
            final GridData qrDualComboLData = new GridData();
            qrDualCombo.setLayoutData(qrDualComboLData);
            qrDualCombo.setText(" 4 |");
        }
        {
            qrExpoCombo = new CCombo(baseComposite, SWT.BORDER);
            final GridData qrExpoComboLData = new GridData();
            qrExpoCombo.setLayoutData(qrExpoComboLData);
            qrExpoCombo.setText("---");
        }
        CLabel spaceLabel;
        {
            spaceLabel = new CLabel(baseComposite, SWT.RIGHT);
            final GridData spaceLabelLData = new GridData();
            spaceLabelLData.widthHint = 53;
            spaceLabelLData.heightHint = 22;
            spaceLabel.setLayoutData(spaceLabelLData);
        }
        CLabel dualLabel;
        {
            dualLabel = new CLabel(baseComposite, SWT.NONE);
            final GridData dualLabelLData = new GridData();
            dualLabelLData.horizontalAlignment = GridData.CENTER;
            dualLabel.setLayoutData(dualLabelLData);
            dualLabel.setText("Dual");
        }
        CLabel expoLabel;
        {
            expoLabel = new CLabel(baseComposite, SWT.NONE);
            final GridData expoLabelLData = new GridData();
            expoLabelLData.horizontalAlignment = GridData.CENTER;
            expoLabel.setLayoutData(expoLabelLData);
            expoLabel.setText("Expo");
        }
        {
            dualLabel = new CLabel(baseComposite, SWT.NONE);
            final GridData dualLabelLData = new GridData();
            dualLabelLData.horizontalAlignment = GridData.CENTER;
            dualLabel.setLayoutData(dualLabelLData);
            dualLabel.setText("Dual");
        }
        {
            expoLabel = new CLabel(baseComposite, SWT.NONE);
            final GridData expoLabelLData = new GridData();
            expoLabelLData.horizontalAlignment = GridData.CENTER;
            expoLabel.setLayoutData(expoLabelLData);
            expoLabel.setText("Expo");
        }
        {
            dualLabel = new CLabel(baseComposite, SWT.NONE);
            final GridData dualLabelLData = new GridData();
            dualLabelLData.horizontalAlignment = GridData.CENTER;
            dualLabel.setLayoutData(dualLabelLData);
            dualLabel.setText("Dual");
        }
        {
            expoLabel = new CLabel(baseComposite, SWT.NONE);
            final GridData expoLabelLData = new GridData();
            expoLabelLData.horizontalAlignment = GridData.CENTER;
            expoLabel.setLayoutData(expoLabelLData);
            expoLabel.setText("Expo");
        }
        {
            dualLabel = new CLabel(baseComposite, SWT.NONE);
            final GridData dualLabelLData = new GridData();
            dualLabelLData.horizontalAlignment = GridData.CENTER;
            dualLabel.setLayoutData(dualLabelLData);
            dualLabel.setText("Dual");
        }
        {
            expoLabel = new CLabel(baseComposite, SWT.NONE);
            final GridData expoLabelLData = new GridData();
            expoLabelLData.horizontalAlignment = GridData.CENTER;
            expoLabel.setLayoutData(expoLabelLData);
            expoLabel.setText("Expo");
        }
        {
            spaceLabel = new CLabel(baseComposite, SWT.RIGHT);
            final GridData spaceLabelLData = new GridData();
            spaceLabelLData.widthHint = 53;
            spaceLabelLData.heightHint = 22;
            spaceLabel.setLayoutData(spaceLabelLData);
        }
        {
            CLabel phaseNormalLabel = new CLabel(baseComposite, SWT.CENTER | SWT.EMBEDDED);
            final GridData phaseNormalLabelLData = new GridData();
            phaseNormalLabelLData.horizontalSpan = 2;
            phaseNormalLabelLData.widthHint = 69;
            phaseNormalLabelLData.heightHint = 22;
            phaseNormalLabelLData.horizontalAlignment = GridData.CENTER;
            phaseNormalLabel.setLayoutData(phaseNormalLabelLData);
            phaseNormalLabel.setText("Normal");
        }
        {
            CLabel phaseLabel2 = new CLabel(baseComposite, SWT.NONE);
            final GridData phaseLabel2LData = new GridData();
            phaseLabel2LData.horizontalAlignment = GridData.CENTER;
            phaseLabel2LData.horizontalSpan = 2;
            phaseLabel2LData.widthHint = 69;
            phaseLabel2LData.heightHint = 22;
            phaseLabel2.setLayoutData(phaseLabel2LData);
            phaseLabel2.setText("Phase 2");
        }
        {
            CLabel phaseLabel3 = new CLabel(baseComposite, SWT.NONE);
            final GridData phaseLabel3LData = new GridData();
            phaseLabel3LData.horizontalAlignment = GridData.CENTER;
            phaseLabel3LData.horizontalSpan = 2;
            phaseLabel3LData.widthHint = 69;
            phaseLabel3LData.heightHint = 22;
            phaseLabel3.setLayoutData(phaseLabel3LData);
            phaseLabel3.setText("Phase 3");
        }
        {
            CLabel switchLabel = new CLabel(baseComposite, SWT.NONE);
            final GridData switchLabelLData = new GridData();
            switchLabelLData.horizontalSpan = 2;
            switchLabelLData.horizontalAlignment = GridData.CENTER;
            switchLabelLData.widthHint = 69;
            switchLabelLData.heightHint = 22;
            switchLabel.setLayoutData(switchLabelLData);
            switchLabel.setText("Schalter");
        }
    }

}

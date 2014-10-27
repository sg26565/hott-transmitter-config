package de.treichels.hott.ui.android.dialogs;

public interface DialogClosedListener {
    public int OK       = 0;
    public int CANCELED = 1;

    public void onDialogClosed(int resultStatus);
}

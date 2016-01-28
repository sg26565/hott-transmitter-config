package de.treichels.android.bluetoohtest;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;

@ReportsCrashes(formKey = "", mailTo = "mdlviewer@treichels.de", mode = ReportingInteractionMode.DIALOG, resToastText = R.string.crash_toast_text, resDialogTitle = R.string.crash_dialog_title, resDialogText = R.string.crash_dialog_text, resDialogIcon = android.R.drawable.stat_notify_error, resDialogCommentPrompt = R.string.crash_dialog_comment_prompt, resDialogOkToast = R.string.crash_dialog_ok_toast)
public class BluetoothTestApplication extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    ACRA.init(this);
  }
}

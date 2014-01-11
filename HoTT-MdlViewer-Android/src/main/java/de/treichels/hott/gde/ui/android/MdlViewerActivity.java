package de.treichels.hott.gde.ui.android;

import gde.model.BaseModel;
import gde.model.enums.ModelType;
import gde.report.ReportException;
import gde.report.html.HTMLReport;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebView;
import de.treichels.hott.HoTTDecoder;

public class MdlViewerActivity extends Activity {
  /**
   * Called when the activity is first created.
   * 
   * @param savedInstanceState
   *          If the activity is being re-initialized after previously being
   *          shut down then this Bundle contains the data it most recently
   *          supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is
   *          null.</b>
   */
  @Override
  public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    InputStream is = null;
    final BaseModel model;
    try {
      is = getAssets().open("aMerlin.mdl");
      model = HoTTDecoder.decode(ModelType.Winged, "Merlin", is);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    } finally {
      if (is != null) {
        try {
          is.close();
        } catch (final IOException e) {
          throw new RuntimeException(e);
        }
      }
    }

    final String html;
    try {
      html = HTMLReport.generateHTML(model);
    } catch (final ReportException e) {
      throw new RuntimeException(e);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }

    setContentView(R.layout.web_view);
    final WebView webView = (WebView) findViewById(R.id.webView1);
    webView.loadData(html, null, null);
  }

  @Override
  public boolean onCreateOptionsMenu(final Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(de.treichels.hott.gde.ui.android.R.menu.main, menu);
    return true;
  }

  @SuppressWarnings("unused")
  private void showError(final Throwable t) {
    showMessage(t.getClass().getSimpleName(), t.getLocalizedMessage(), true);
  }

  private void showMessage(final String title, final String message, final boolean finish) {
    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage(message);
    builder.setTitle(title);
    builder.setPositiveButton(R.string.ok_button, new OnClickListener() {
      @Override
      public void onClick(final DialogInterface dialog, final int which) {
        dialog.dismiss();

        if (finish) {
          finish();
        }
      }
    });

    final AlertDialog dialog = builder.create();
    dialog.show();
  }
}

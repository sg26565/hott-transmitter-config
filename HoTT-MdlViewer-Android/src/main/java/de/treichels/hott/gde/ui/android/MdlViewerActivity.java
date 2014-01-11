package de.treichels.hott.gde.ui.android;

import gde.model.BaseModel;
import gde.model.enums.ModelType;
import gde.report.ReportException;
import gde.report.html.HTMLReport;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebView;
import de.treichels.hott.HoTTDecoder;

public class MdlViewerActivity extends Activity {
  private final String MERLIN = "aMerlin.mdl";
  private String       html   = null;

  private String getHtml(final String asset) {
    if (html == null) {
      if (asset == null || !asset.endsWith(".mdl")) {
        throw new IllegalArgumentException("invalid asset name: " + asset);
      }

      final ModelType modelType = asset.charAt(0) == 'a' ? ModelType.Winged : ModelType.Helicopter;
      final String modelName = asset.substring(1, asset.length() - 4);

      InputStream is = null;
      final BaseModel model;
      try {
        is = getAssets().open(asset);
        model = HoTTDecoder.decode(modelType, modelName, is);
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

      try {
        html = HTMLReport.generateHTML(model);
      } catch (final ReportException e) {
        throw new RuntimeException(e);
      } catch (final IOException e) {
        throw new RuntimeException(e);
      }
    }

    return html;
  }

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

    setContentView(R.layout.web_view);
    final WebView webView = (WebView) findViewById(R.id.webView1);
    webView.loadData(getHtml(MERLIN), null, null);
  }

  @Override
  public boolean onCreateOptionsMenu(final Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(de.treichels.hott.gde.ui.android.R.menu.main, menu);
    return true;
  }
}
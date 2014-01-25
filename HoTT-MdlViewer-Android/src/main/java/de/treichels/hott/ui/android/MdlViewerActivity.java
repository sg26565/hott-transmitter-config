package de.treichels.hott.ui.android;

import gde.model.BaseModel;
import gde.model.enums.ModelType;
import gde.model.enums.Section;
import gde.report.ReportException;
import gde.report.html.HTMLReport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintManager;
import android.provider.OpenableColumns;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import de.treichels.hott.HoTTDecoder;

public class MdlViewerActivity extends Activity implements UncaughtExceptionHandler, ListView.OnItemClickListener {
  private static final int READ_REQUEST_CODE    = 42;
  private static boolean   NAVIGATION_SUPPORTED = Build.VERSION.SDK_INT >= 19;
  private Uri              uri                  = null;
  private BaseModel        model                = null;
  private WebView          webView              = null;
  private DrawerLayout     drawerLayout         = null;
  private ListView         drawer               = null;

  /**
   * Read a MDL file and convert it to HTML.
   * 
   * @param uri
   * @return
   */
  @SuppressLint("NewApi")
  private String getHtml() {
    String fileName = null;

    if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
      // The query, since it only applies to a single document, will only return
      // one row. There's no need to filter, sort, or select fields, since we
      // want all fields for one document.
      final Cursor cursor = getContentResolver().query(uri, null, null, null, null, null);

      try {
        // moveToFirst() returns false if the cursor has 0 rows. Very handy for
        // "if there's anything to look at, look at it" conditionals.
        if (cursor != null && cursor.moveToFirst()) {

          // Note it's called "Display Name". This is provider-specific, and
          // might not necessarily be the file name.
          fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
        }
      } finally {
        cursor.close();
      }
    } else if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
      fileName = uri.getLastPathSegment();
    } else {
      // unknown scheme
      throw new IllegalArgumentException(getResources().getString(R.string.msg_unsupported_uri, uri));
    }

    if (fileName == null || !fileName.endsWith(".mdl")) {
      throw new IllegalArgumentException(getResources().getString(R.string.msg_invalid_file_name, fileName));
    }

    // check model type
    ModelType modelType;
    switch (fileName.charAt(0)) {
    case 'a':
      modelType = ModelType.Winged;
      break;

    case 'h':
      modelType = ModelType.Helicopter;
      break;

    default:
      throw new IllegalArgumentException(getResources().getString(R.string.msg_invalid_file_type, fileName.charAt(0)));
    }

    final String modelName = fileName.substring(1, fileName.length() - 4);

    // decode file
    InputStream is = null;
    try {
      is = getContentResolver().openInputStream(uri);
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

    // convert to HTML
    try {
      HTMLReport.setCurveImageGenerator(new AndroidCurveImageGenerator());
      return HTMLReport.generateHTML(model);
    } catch (final ReportException e) {
      throw new RuntimeException(e);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Handle file selection
   */
  @Override
  public void onActivityResult(final int requestCode, final int resultCode, final Intent resultData) {
    if (resultCode == Activity.RESULT_OK && resultData != null && requestCode == READ_REQUEST_CODE) {
      uri = resultData.getData();
      updateUI(null);
    }
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
  @SuppressLint("SetJavaScriptEnabled")
  @Override
  public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Thread.setDefaultUncaughtExceptionHandler(this);

    setContentView(R.layout.main);
    webView = (WebView) findViewById(R.id.webwiew);
    webView.getSettings().setBuiltInZoomControls(true);

    if (NAVIGATION_SUPPORTED) {
      webView.getSettings().setJavaScriptEnabled(true);
      registerForContextMenu(webView);

      drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
      drawer = (ListView) findViewById(R.id.drawer);
      drawer.setOnItemClickListener(this);
    }

    // check if we were started via an intent
    final Intent intent = getIntent();
    if (intent != null && Intent.ACTION_VIEW.equals(intent.getAction())) {
      uri = intent.getData();
      updateUI(null);
    }

    // restore from saved state
    if (uri == null && savedInstanceState != null) {
      if (savedInstanceState.containsKey("uri")) {
        final String uriString = savedInstanceState.getString("uri");
        if (uriString != null) {
          uri = Uri.parse(uriString);
        }
      }
    }

    if (uri == null) {
      // first start, no saved state, no intent
      performFileSearch(null);
    } else {
      // refresh
      updateUI(null);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(final Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.options_menu, menu);
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
   * .AdapterView, android.view.View, int, long)
   */
  @TargetApi(19)
  @Override
  public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
    drawerLayout.closeDrawer(drawer);

    final Section section = Section.values()[(int) id];
    webView.evaluateJavascript("location.hash='#" + section.name() + "'", null);
  }

  @Override
  public boolean onOptionsItemSelected(final MenuItem item) {
    switch (item.getItemId()) {
    case R.id.action_load:
      performFileSearch(item);
      return true;

    case R.id.action_reload:
      updateUI(item);
      return true;

    case R.id.action_print:
      print(item);
      return true;

    default:
      return super.onOptionsItemSelected(item);
    }
  }

  /**
   * Activity will be stopped. Save state.
   */
  @Override
  protected void onSaveInstanceState(final Bundle outState) {
    super.onSaveInstanceState(outState);

    if (uri != null) {
      outState.putString("uri", uri.toString());
    }
  }

  /**
   * Fires an intent to spin up the "file chooser" UI and select a file.
   */
  public void performFileSearch(final MenuItem menuItem) {
    Toast.makeText(getApplicationContext(), R.string.msg_select_mdl, Toast.LENGTH_SHORT).show();

    Intent intent;

    intent = new Intent(Intent.ACTION_GET_CONTENT);
    intent.addCategory(Intent.CATEGORY_OPENABLE);
    intent.setType("*/*");

    startActivityForResult(intent, READ_REQUEST_CODE);
  }

  /**
   * Create a PDF version of the document. Only support on Android 4.4 and
   * newer.
   * 
   * @param menuItem
   *          unused
   */
  @TargetApi(19)
  public void print(final MenuItem menuItem) {
    final PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
    printManager.print("HoTTMdlViewer - " + model.getModelName(), webView.createPrintDocumentAdapter(), null);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread
   * , java.lang.Throwable)
   */
  @Override
  public void uncaughtException(final Thread thread, final Throwable ex) {
    final File downloadDir = Environment.getDownloadCacheDirectory();

    if (downloadDir.exists() && downloadDir.isDirectory()) {
      try {
        final PrintStream ps = new PrintStream(new File(downloadDir, MdlViewerActivity.class.getName() + "_Exception.log"));
        ex.printStackTrace(ps);
        ps.close();
      } catch (final FileNotFoundException e) {
        // ignore
      }
    }
  }

  /**
   * Update WebView in a background thread without blocking the UI thread.
   * 
   * @param menuItem
   *          unused
   */
  public void updateUI(final MenuItem menuItem) {
    if (uri == null) {
      // nothing to do
      return;
    }

    // tell user to be patient
    final Toast toast = Toast.makeText(this, R.string.msg_loading, Toast.LENGTH_LONG);
    toast.show();

    new AsyncTask<Void, Void, Void>() {
      @Override
      protected Void doInBackground(final Void... params) {
        try {
          // decode and transform
          final String html = getHtml();

          // access to View elements needs to be done in the UI thread
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              webView.loadData(html, "text/html", "UTF-8");
              setTitle(model.getModelName());

              if (NAVIGATION_SUPPORTED) {
                drawer.setAdapter(new SectionAdapter(MdlViewerActivity.this, model.getModelType(), model.getTransmitterType()));
              }

              toast.cancel();
            }
          });
        } catch (final Exception e) {
          // something went wrong - show an error dialog
          final AlertDialog.Builder builder = new AlertDialog.Builder(MdlViewerActivity.this);
          builder.setTitle(R.string.msg_error);
          builder.setMessage(e.getLocalizedMessage());
          builder.setPositiveButton(android.R.string.ok, null);

          // access to View elements needs to be done in the UI thread
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              toast.cancel();
              builder.create().show();
            }
          });

          uncaughtException(Thread.currentThread(), e);
        }

        return null;
      }
    }.execute();
  }
}
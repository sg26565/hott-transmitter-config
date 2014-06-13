package de.treichels.hott.ui.android;

import gde.model.BaseModel;
import gde.model.enums.ModelType;
import gde.model.enums.Section;
import gde.model.serial.ModelInfo;
import gde.model.serial.SerialPort;
import gde.report.ReportException;
import gde.report.html.HTMLReport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.print.PrintManager;
import android.provider.OpenableColumns;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import de.treichels.hott.HoTTDecoder;
import de.treichels.hott.HoTTSerialPort;

public class MdlViewerActivity extends Activity implements ListView.OnItemClickListener {
  private class GetAllModels extends AsyncTask<Void, Void, Void> {
    private static final String ACTION_USB_PERMISSION = "de.treichels.hott.ui.android.USB_PERMISSION";
    private final Object        mutex                 = new Object();
    private final List<String>  models                = new ArrayList<String>();

    @Override
    protected Void doInBackground(final Void... v) {
      try {
        final UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        final HashMap<String, UsbDevice> devices = manager.getDeviceList();
        final UsbDevice device = devices.get("/dev/bus/usb/002/002");

        if (!manager.hasPermission(device)) {
          synchronized (mutex) {
            // ask for permission - once
            final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
              @Override
              public void onReceive(final Context context, final Intent intent) {
                final String action = intent.getAction();
                if (ACTION_USB_PERMISSION.equals(action)) {
                  synchronized (mutex) {
                    // wakeup thread
                    mutex.notify();
                  }
                }
              }
            };

            final PendingIntent mPermissionIntent = PendingIntent.getBroadcast(MdlViewerActivity.this, 0, new Intent(ACTION_USB_PERMISSION), 0);
            final IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
            registerReceiver(mUsbReceiver, filter);
            manager.requestPermission(device, mPermissionIntent);

            try {
              // wait for user decision
              mutex.wait();
            } catch (final InterruptedException e) {
              // ignore
            }
          }
        }

        final List<UsbSerialDriver> drivers = UsbSerialProber.probeSingleDevice(manager, device);
        if (drivers != null && drivers.size() > 0) {
          final SerialPort impl = new AndroidUsbSerialPortImplementation(drivers.get(0));
          final HoTTSerialPort port = new HoTTSerialPort(impl);
          port.open();
          final ModelInfo[] infos = port.getAllModelInfos();
          port.close();
          for (final ModelInfo info : infos) {
            if (model.getModelType() != ModelType.Unknown) {
              models.add(info.getModelNumber() + ": " + info.getModelName() + " (" + info.getModelType() + ")");
            }
          }
        }
      } catch (final IOException e) {
        Log.e("performUsbSearch", "getAllModelInfos", e);
        models.add(e.getMessage());
        for (final StackTraceElement element : e.getStackTrace()) {
          models.add(element.toString());
        }
      }

      return null;
    }

    @Override
    protected void onPostExecute(final Void v) {
      showDialog("Models", models);
    }
  }

  private static final String MDL_MIME_TYPE      = "application/octet-stream";                //$NON-NLS-1$
  private static final String FILE_EXTENSION_MDL = ".mdl";                                    //$NON-NLS-1$
  private static final String SAVED_STATE_URI    = MdlViewerActivity.class.getName() + ".URI"; //$NON-NLS-1$
  private static final String CACHE_FILE_NAME    = "MdlViewer.html";                          //$NON-NLS-1$

  private static final int    READ_REQUEST_CODE  = 42;
  private Uri                 uri                = null;
  private BaseModel           model              = null;
  private WebView             webView            = null;
  private DrawerLayout        drawerLayout       = null;
  private ListView            drawer             = null;

  private String              loadUrl            = null;

  /**
   * Read a MDL file and convert it to HTML.
   * 
   * @param uri
   * @return
   */
  @SuppressLint("NewApi")
  private void generateHtml() {
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

    if (fileName == null || !fileName.endsWith(FILE_EXTENSION_MDL)) {
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
      model = HoTTDecoder.decodeStream(modelType, modelName, is);
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

    String html;
    Writer writer = null;
    try {
      // convert to HTML
      HTMLReport.setCurveImageGenerator(new AndroidCurveImageGenerator());
      html = HTMLReport.generateHTML(model);

      // write to cache file
      writer = new OutputStreamWriter(openFileOutput(CACHE_FILE_NAME, MODE_PRIVATE));
      writer.write(html);
    } catch (final ReportException e) {
      throw new RuntimeException(e);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    } finally {
      if (writer != null) {
        try {
          writer.close();
        } catch (final IOException e) {
          throw new RuntimeException(e);
        }
      }
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
   *          If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in
   *          onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
   */
  @Override
  public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    try {
      loadUrl = getFileStreamPath(CACHE_FILE_NAME).toURI().toURL().toString();
    } catch (final MalformedURLException e) {
      throw new RuntimeException(e);
    }

    setContentView(R.layout.main);
    webView = (WebView) findViewById(R.id.webwiew);
    webView.getSettings().setBuiltInZoomControls(true);

    drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer = (ListView) findViewById(R.id.drawer);
    drawer.setOnItemClickListener(this);

    // check if we were started via an intent
    final Intent intent = getIntent();
    if (intent != null && intent.getAction() == Intent.ACTION_VIEW) {
      uri = intent.getData();
    }

    // restore from saved state
    if (uri == null && savedInstanceState != null) {
      if (savedInstanceState.containsKey(SAVED_STATE_URI)) {
        final String uriString = savedInstanceState.getString(SAVED_STATE_URI);
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

  @Override
  public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
    drawerLayout.closeDrawer(drawer);

    final Section section = Section.values()[(int) id];
    webView.loadUrl(loadUrl + "#" + section.name()); //$NON-NLS-1$
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

    case R.id.action_load_from_sd:
      return true;

    case R.id.action_load_from_tx:
      performUsbSearch(item);
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
      outState.putString(SAVED_STATE_URI, uri.toString());
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
    intent.setType(MDL_MIME_TYPE);

    startActivityForResult(intent, READ_REQUEST_CODE);
  }

  private void performUsbSearch(final MenuItem item) {
    new GetAllModels().execute((Void[]) null);
  }

  /**
   * Create a PDF version of the document. Only support on Android 4.4 and newer.
   * 
   * @param menuItem
   *          unused
   */
  @TargetApi(19)
  public void print(final MenuItem menuItem) {
    final PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
    printManager.print("HoTTMdlViewer - " + model.getModelName(), webView.createPrintDocumentAdapter(), null); //$NON-NLS-1$
  }

  private void showDialog(final String title, final List<String> messages) {
    showDialog(title, messages.toArray(new String[] {}));
  }

  private void showDialog(final String title, final String... messages) {
    final AlertDialog.Builder builder = new AlertDialog.Builder(this);

    builder.setTitle(title).setItems(messages, null);
    builder.setPositiveButton(android.R.string.ok, new OnClickListener() {
      @Override
      public void onClick(final DialogInterface dialog, final int which) {
        dialog.dismiss();
      }
    });

    final Dialog dialog = builder.create();
    dialog.show();
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
          generateHtml();

          // access to View elements needs to be done in the UI thread
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              webView.loadUrl(loadUrl);
              setTitle(model.getModelName());

              drawer.setAdapter(new SectionAdapter(MdlViewerActivity.this, model.getModelType(), model.getTransmitterType()));

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
        }

        return null;
      }
    }.execute();
  }
}
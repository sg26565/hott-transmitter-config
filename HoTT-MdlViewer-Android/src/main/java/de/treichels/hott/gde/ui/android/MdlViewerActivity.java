package de.treichels.hott.gde.ui.android;

import gde.model.BaseModel;
import gde.model.enums.ModelType;
import gde.report.ReportException;
import gde.report.html.HTMLReport;

import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;
import de.treichels.hott.HoTTDecoder;

public class MdlViewerActivity extends Activity {
  private static final int    READ_REQUEST_CODE = 42;
  private static final String TAG               = MdlViewerActivity.class.getSimpleName();
  private Uri                 uri               = null;
  private BaseModel           model             = null;
  private WebView             webView;

  /**
   * Read a MDL file and convert it to HTML.
   * 
   * @param uri
   * @return
   */
  private String getHtml() {
    String fileName = null;

    // The query, since it only applies to a single document, will only return
    // one row. There's no need to filter, sort, or select fields, since we want
    // all fields for one document.
    final Cursor cursor = getContentResolver().query(uri, null, null, null, null, null);

    try {
      // moveToFirst() returns false if the cursor has 0 rows. Very handy for
      // "if there's anything to look at, look at it" conditionals.
      if (cursor != null && cursor.moveToFirst()) {

        // Note it's called "Display Name". This is
        // provider-specific, and might not necessarily be the file name.
        fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
      }
    } finally {
      cursor.close();
    }

    Log.i(TAG, "File Name: " + fileName);

    if (fileName == null || !fileName.endsWith(".mdl")) {
      throw new IllegalArgumentException(getResources().getString(R.string.msg_invalid_file_name, fileName));
    }

    final ModelType modelType = fileName.charAt(0) == 'a' ? ModelType.Winged : ModelType.Helicopter;
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
    // The ACTION_OPEN_DOCUMENT intent was sent with the request code
    // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
    // response to some other intent, and the code below shouldn't run at all.

    if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK && resultData != null) {
      // The document selected by the user won't be returned in the intent.
      // Instead, a URI to that document will be contained in the return intent
      // provided to this method as a parameter.
      // Pull that URI using resultData.getData().
      uri = resultData.getData();
      Log.i(TAG, "Uri: " + uri.toString());

      updateUI(null);
    }
  }

  @TargetApi(19)
  @Override
  public boolean onContextItemSelected(final MenuItem item) {
    switch (item.getItemId()) {
    case R.id.bookmark_baseSettings:
      webView.evaluateJavascript("location.hash='#baseSettings'", null);
      return true;
    case R.id.bookmark_modelType:
      webView.evaluateJavascript("location.hash='#modelType'", null);
      return true;
    case R.id.bookmark_servos:
      webView.evaluateJavascript("location.hash='#servos'", null);
      return true;
    case R.id.bookmark_stickSettings:
      webView.evaluateJavascript("location.hash='#stickSettings'", null);
      return true;
    case R.id.bookmark_controls0:
      webView.evaluateJavascript("location.hash='#controls0'", null);
      return true;
    case R.id.bookmark_drExpo0:
      webView.evaluateJavascript("location.hash='#drExpo0'", null);
      return true;
    case R.id.bookmark_channel1Curve0:
      webView.evaluateJavascript("location.hash='#channel1Curve0'", null);
      return true;
    case R.id.bookmark_controlSwitches:
      webView.evaluateJavascript("location.hash='#controlSwitches'", null);
      return true;
    case R.id.bookmark_logicalSwitches:
      webView.evaluateJavascript("location.hash='#logicalSwitches'", null);
      return true;
    case R.id.bookmark_phaseSettings:
      webView.evaluateJavascript("location.hash='#phaseSettings'", null);
      return true;
    case R.id.bookmark_phaseAssignments:
      webView.evaluateJavascript("location.hash='#phaseAssignments'", null);
      return true;
    case R.id.bookmark_phaseTrim:
      webView.evaluateJavascript("location.hash='#phaseTrim'", null);
      return true;
    case R.id.bookmark_nonDelayedChannels:
      webView.evaluateJavascript("location.hash='#nonDelayedChannels'", null);
      return true;
    case R.id.bookmark_timersGeneral:
      webView.evaluateJavascript("location.hash='#timersGeneral'", null);
      return true;
    case R.id.bookmark_phaseTimer:
      webView.evaluateJavascript("location.hash='#phaseTimer'", null);
      return true;
    case R.id.bookmark_wingMix0:
      webView.evaluateJavascript("location.hash='#wingMix0'", null);
      return true;
    case R.id.bookmark_helicopterMix0:
      webView.evaluateJavascript("location.hash='#helicopterMix0'", null);
      return true;
    case R.id.bookmark_linearMixer:
      webView.evaluateJavascript("location.hash='#linearMixer'", null);
      return true;
    case R.id.bookmark_curveMixer:
      webView.evaluateJavascript("location.hash='#curveMixer'", null);
      return true;
    case R.id.bookmark_mixerActive:
      webView.evaluateJavascript("location.hash='#mixerActive'", null);
      return true;
    case R.id.bookmark_mixOnlyChannel:
      webView.evaluateJavascript("location.hash='#mixOnlyChannel'", null);
      return true;
    case R.id.bookmark_dualMixer:
      webView.evaluateJavascript("location.hash='#dualMixer'", null);
      return true;
    case R.id.bookmark_swashplateMixer:
      webView.evaluateJavascript("location.hash='#swashplateMixer'", null);
      return true;
    case R.id.bookmark_failSafe:
      webView.evaluateJavascript("location.hash='#failSafe'", null);
      return true;
    case R.id.bookmark_trainerPupil:
      webView.evaluateJavascript("location.hash='#trainerPupil'", null);
      return true;
    case R.id.bookmark_outputChannel:
      webView.evaluateJavascript("location.hash='#outputChannel'", null);
      return true;
    case R.id.bookmark_profiTrim:
      webView.evaluateJavascript("location.hash='#profiTrim'", null);
      return true;
    case R.id.bookmark_trimMemory:
      webView.evaluateJavascript("location.hash='#trimMemory'", null);
      return true;
    case R.id.bookmark_telemetry:
      webView.evaluateJavascript("location.hash='#telemetry'", null);
      return true;
    case R.id.bookmark_channelSequencer:
      webView.evaluateJavascript("location.hash='#channelSequencer'", null);
      return true;
    case R.id.bookmark_multiChannel:
      webView.evaluateJavascript("location.hash='#multiChannel'", null);
      return true;
    case R.id.bookmark_ringLimiter:
      webView.evaluateJavascript("location.hash='#ringLimiter'", null);
      return true;
    case R.id.bookmark_mp3Player:
      webView.evaluateJavascript("location.hash='#mp3Player'", null);
      return true;
    case R.id.bookmark_switches:
      webView.evaluateJavascript("location.hash='#switches'", null);
      return true;
    default:
      return super.onContextItemSelected(item);
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

    webView = new WebView(this);
    webView.getSettings().setBuiltInZoomControls(true);
    webView.getSettings().setJavaScriptEnabled(true);
    setContentView(webView);
    registerForContextMenu(webView);

    if (savedInstanceState != null) {
      webView.restoreState(savedInstanceState);

      if (savedInstanceState.containsKey("model")) {
        model = (BaseModel) savedInstanceState.getSerializable("model");
        if (model != null) {
          setTitle(model.getModelName());
        }
      }

      if (savedInstanceState.containsKey("uri")) {
        final String uriString = savedInstanceState.getString("uri");
        if (uriString != null) {
          uri = Uri.parse(uriString);
        }
      }
    }

    // first start, no saved state
    if (uri == null) {
      performFileSearch(null);
    }
  }

  @Override
  public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);

    // only do this, if we have a model loaded
    if (model != null) {
      // load menu from xml
      getMenuInflater().inflate(R.menu.context_menu, menu);

      switch (model.getModelType()) {
      case Helicopter:
        // rename menu item
        menu.findItem(R.id.bookmark_modelType).setTitle(R.string.bookmark_helicopterType);

        // disable wing mixers
        menu.findItem(R.id.bookmark_phaseTrim).setVisible(false);
        menu.findItem(R.id.bookmark_wingMix0).setVisible(false);
        break;

      case Winged:
        // disable helicopter mixers
        menu.findItem(R.id.bookmark_helicopterMix0).setVisible(false);
        menu.findItem(R.id.bookmark_swashplateMixer).setVisible(false);
        break;

      default:
        break;
      }

      // disable logical switches
      if (model.getLogicalSwitch() == null) {
        menu.findItem(R.id.bookmark_logicalSwitches).setVisible(false);
      }

      // disable unsupported features
      switch (model.getTransmitterType()) {
      case mx12:
      case mx16:
        menu.findItem(R.id.bookmark_modelType).setVisible(false);
        menu.findItem(R.id.bookmark_stickSettings).setVisible(false);
        menu.findItem(R.id.bookmark_channel1Curve0).setVisible(false);
        menu.findItem(R.id.bookmark_controlSwitches).setVisible(false);
        menu.findItem(R.id.bookmark_phaseSettings).setVisible(false);
        menu.findItem(R.id.bookmark_phaseAssignments).setVisible(false);
        menu.findItem(R.id.bookmark_nonDelayedChannels).setVisible(false);
        menu.findItem(R.id.bookmark_timersGeneral).setVisible(false);
        menu.findItem(R.id.bookmark_phaseTimer).setVisible(false);
        menu.findItem(R.id.bookmark_curveMixer).setVisible(false);
        menu.findItem(R.id.bookmark_mixerActive).setVisible(false);
        menu.findItem(R.id.bookmark_mixOnlyChannel).setVisible(false);
        menu.findItem(R.id.bookmark_dualMixer).setVisible(false);
        menu.findItem(R.id.bookmark_outputChannel).setVisible(false);
        menu.findItem(R.id.bookmark_profiTrim).setVisible(false);
        menu.findItem(R.id.bookmark_trimMemory).setVisible(false);
        menu.findItem(R.id.bookmark_channelSequencer).setVisible(false);
        menu.findItem(R.id.bookmark_multiChannel).setVisible(false);
        menu.findItem(R.id.bookmark_ringLimiter).setVisible(false);
        // falls through

      case mx20:
        menu.findItem(R.id.bookmark_mp3Player).setVisible(false);
        break;

      default:
        break;
      }
    }
  }

  @Override
  public boolean onCreateOptionsMenu(final Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.options_menu, menu);
    return true;
  }

  /**
   * Activity will be stopped. Save state.
   */
  @Override
  protected void onSaveInstanceState(final Bundle outState) {
    super.onSaveInstanceState(outState);
    webView.saveState(outState);

    if (model != null) {
      outState.putSerializable("model", model);
    }

    if (uri != null) {
      outState.putString("uri", uri.toString());
    }
  }

  /**
   * Fires an intent to spin up the "file chooser" UI and select a file.
   */
  @TargetApi(19)
  public void performFileSearch(final MenuItem menuItem) {
    final Toast toast = Toast.makeText(getApplicationContext(), R.string.msg_select_mdl, Toast.LENGTH_SHORT);
    toast.setGravity(Gravity.CENTER, 0, 0);
    toast.show();

    // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
    // browser.
    final Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

    // Filter to only show results that can be "opened", such as a
    // file (as opposed to a list of contacts or timezones)
    intent.addCategory(Intent.CATEGORY_OPENABLE);

    // Filter to show only images, using the image MIME data type.
    // If one wanted to search for ogg vorbis files, the type would be
    // "audio/ogg".
    // To search for all documents available via installed storage providers,
    // it would be "*/*".
    intent.setType("*/*");

    startActivityForResult(intent, READ_REQUEST_CODE);
  }

  /**
   * Update WebView in a background thread without blocking the UI thread.
   */
  public void updateUI(final MenuItem menuItem) {
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
              webView.loadData(html, null, null);
              setTitle(model.getModelName());
              toast.cancel();
            }
          });
        } catch (final Exception e) {
          // something went wrong - show an error dialog
          final AlertDialog.Builder builder = new AlertDialog.Builder(MdlViewerActivity.this);
          builder.setTitle(R.string.msg_error);
          builder.setMessage(e.getLocalizedMessage());
          builder.setPositiveButton(R.string.button_ok, null);

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
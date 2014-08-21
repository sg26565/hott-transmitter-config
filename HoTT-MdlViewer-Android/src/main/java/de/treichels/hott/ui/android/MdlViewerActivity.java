package de.treichels.hott.ui.android;

import gde.model.BaseModel;
import gde.model.enums.Section;

import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.print.PrintManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MdlViewerActivity extends Activity implements ListView.OnItemClickListener {
  private static final String MDL_MIME_TYPE     = "application/octet-stream";                //$NON-NLS-1$
  private static final String SAVED_STATE_URI   = MdlViewerActivity.class.getName() + ".URI"; //$NON-NLS-1$
  public static final String  CACHE_FILE_NAME   = "MdlViewer.html";                          //$NON-NLS-1$
  private static final int    READ_REQUEST_CODE = 42;

  /** Uri of the currently loaded model **/
  private Uri                 uri               = null;

  /** Model data **/
  private BaseModel           model             = null;

  /** Html view **/
  private WebView             webView           = null;

  /** Table of contents container **/
  private DrawerLayout        drawerLayout      = null;

  /** Table of contents list **/
  private ListView            drawer            = null;

  /**
   * Url to temporary file.
   * 
   * This is a ugly workaround, to enable TOC navigation via {@code webView.loadUrl(loadUrl + "#" + section.name());}. It would be more elegant to navigate via
   * JavaScript calls. However, this is only supported on Anroid 4.4 or higher.
   **/
  private String              loadUrl           = null;

  /**
   * Handle file selection in response to a {@link Intent.ACTION_GET_CONTENT} action intent.
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

    setContentView(R.layout.main);

    // temporary file to store html
    try {
      loadUrl = getFileStreamPath(CACHE_FILE_NAME).toURI().toURL().toString();
    } catch (final MalformedURLException e) {
      throw new RuntimeException(e);
    }

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

  /**
   * Handle menu button
   * 
   * @param menu
   * @return
   */
  @Override
  public boolean onCreateOptionsMenu(final Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.options_menu, menu);
    return true;
  }

  /**
   * Handle toc selection in drawer.
   * 
   * Navigate webView to the anchor with the same name as the section in the drawer that was clicked.
   * 
   * @param parent
   * @param view
   * @param position
   * @param id
   */
  @Override
  public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
    drawerLayout.closeDrawer(drawer);

    final Section section = Section.values()[(int) id];
    webView.loadUrl(loadUrl + "#" + section.name()); //$NON-NLS-1$
  }

  /**
   * Handle menu selection.
   * 
   * Launch functionality according to menu item selected.
   * 
   * @param item
   * @return
   */
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
      // TODO
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
   * Fires an intent to spin up the "file chooser" UI and select a file. Response will be handeled by {@link MdlViewerActivity.onActivityResult()}
   */
  public void performFileSearch(final MenuItem menuItem) {
    Toast.makeText(getApplicationContext(), R.string.msg_select_mdl, Toast.LENGTH_SHORT).show();

    Intent intent;

    intent = new Intent(Intent.ACTION_GET_CONTENT);
    intent.addCategory(Intent.CATEGORY_OPENABLE);
    intent.setType(MDL_MIME_TYPE);

    startActivityForResult(intent, READ_REQUEST_CODE);
  }

  /**
   * Select models from transmitter memory
   * 
   * @param item
   */
  private void performUsbSearch(final MenuItem item) {
    new OpenFromMemoryDialog().show(getFragmentManager(), "open_from_tx");
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

  /**
   * Show a simple dialog with a title and a list of messages.
   * 
   * @param title
   * @param messages
   */
  private void showDialog(final String title, final List<String> messages) {
    showDialog(title, messages.toArray(new String[] {}));
  }

  /**
   * Show a simple dialog with a title and a list of messages.
   * 
   * @param title
   * @param messages
   */
  private void showDialog(final String title, final String... messages) {
    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    final Dialog dialog = builder.setTitle(title).setItems(messages, null).setPositiveButton(android.R.string.ok, new OnClickListener() {
      @Override
      public void onClick(final DialogInterface dialog, final int which) {
        dialog.dismiss();
      }
    }).create();

    dialog.show();
  }

  /**
   * Update WebView in a background thread without blocking the UI thread.
   * 
   * @param menuItem
   *          unused
   */
  public void updateUI(final MenuItem menuItem) {
    if (uri != null) {
      // tell user to be patient
      final Toast toast = Toast.makeText(this, R.string.msg_loading, Toast.LENGTH_LONG);
      toast.show();

      // Generate HTML in background task
      new GenerateHtmlTask(this) {
        @Override
        protected void onPostExecute(final BaseModel result) {
          // done - update UI
          try {
            model = get();
            webView.loadUrl(loadUrl);
            setTitle(model.getModelName());
            drawer.setAdapter(new SectionAdapter(MdlViewerActivity.this, model.getModelType(), model.getTransmitterType()));
            toast.cancel();
          } catch (InterruptedException | ExecutionException e) {
            // something went wrong - show an error dialog
            final AlertDialog.Builder builder = new AlertDialog.Builder(MdlViewerActivity.this);
            builder.setTitle(R.string.msg_error);
            builder.setMessage(e.getLocalizedMessage());
            builder.setPositiveButton(android.R.string.ok, null);
            builder.create().show();
            toast.cancel();
          }
        }
      }.execute(uri);
    }
  }
}
package de.treichels.hott.gde.ui.android;

import gde.model.BaseModel;
import gde.model.enums.ModelType;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import de.treichels.binding.BindingManager;
import de.treichels.hott.HoTTDecoder;

public class MdlViewerActivity extends Activity {
  private void addRow(final TableLayout table, final int label, final String binding) {
    final TableRow tableRow = new TableRow(this);
    final TextView labelTextView = new TextView(this);
    final TextView valueTextView = new TextView(this);

    try {
      labelTextView.setText(label);
      valueTextView.setText(BindingManager.getBindingManager().getValueFromBinding(binding).toString());
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }

    tableRow.addView(labelTextView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    tableRow.addView(valueTextView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    table.addView(tableRow, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
  }

  private void addRow(final TableLayout table, final String property) {
    try {
      final String binding = "model." + property;
      final Field field = R.string.class.getField(property);
      final int label = field.getInt(R.string.class);
      addRow(table, label, binding);
    } catch (final Exception e) {
      throw new RuntimeException(e);
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
  @Override
  public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (!BindingManager.getBindingManager().hasBinding("model")) {
      // setup bindings
      InputStream is = null;
      try {
        is = getAssets().open("aMerlin.mdl");
        final BaseModel model = HoTTDecoder.decode(ModelType.Winged, "Merlin", is);
        BindingManager.getBindingManager().addBindig("model", model);
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
    }

    // load view
    setContentView(R.layout.activity_main);

    final TableLayout table = (TableLayout) findViewById(R.id.tab1);

    addRow(table, "vendor");
    addRow(table, "transmitterType");
    addRow(table, "transmitterId");
    addRow(table, "appVersion");
    addRow(table, "memoryVersion");
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

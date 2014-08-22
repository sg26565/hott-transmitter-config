package de.treichels.hott.ui.android.html;

import gde.model.BaseModel;
import gde.model.enums.ModelType;
import gde.report.ReportException;
import gde.report.html.HTMLReport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.OpenableColumns;
import de.treichels.hott.HoTTDecoder;
import de.treichels.hott.ui.android.MdlViewerActivity;
import de.treichels.hott.ui.android.R;
import de.treichels.hott.ui.android.R.string;

public class GenerateHtmlTask extends AsyncTask<Uri, Void, BaseModel> {
  private static final String FILE_EXTENSION_MDL = ".mdl"; //$NON-NLS-1$
  private final Context       context;

  public GenerateHtmlTask(final Context context) {
    this.context = context;
  }

  @Override
  @SuppressLint("NewApi")
  protected BaseModel doInBackground(final Uri... params) {
    final Uri uri = params[0];
    String fileName = null;

    if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
      // The query, since it only applies to a single document, will only return
      // one row. There's no need to filter, sort, or select fields, since we
      // want all fields for one document.
      final Cursor cursor = context.getContentResolver().query(uri, null, null, null, null, null);

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
      throw new IllegalArgumentException(context.getResources().getString(R.string.msg_unsupported_uri, uri));
    }

    if (fileName == null || !fileName.endsWith(FILE_EXTENSION_MDL)) {
      throw new IllegalArgumentException(context.getResources().getString(R.string.msg_invalid_file_name, fileName));
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
      throw new IllegalArgumentException(context.getResources().getString(R.string.msg_invalid_file_type, fileName.charAt(0)));
    }

    final String modelName = fileName.substring(1, fileName.length() - 4);

    // decode file
    BaseModel model;
    InputStream is = null;
    try {
      is = context.getContentResolver().openInputStream(uri);
      model = HoTTDecoder.decodeStream(modelType, modelName, is);
    } catch (final IOException e) {
      cancel(false);
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

    Writer writer = null;
    try {
      // convert to HTML
      HTMLReport.setCurveImageGenerator(new AndroidCurveImageGenerator());
      final String html = HTMLReport.generateHTML(model);

      // write to cache file
      writer = new OutputStreamWriter(context.openFileOutput(MdlViewerActivity.CACHE_FILE_NAME, Context.MODE_PRIVATE));
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

    return model;
  }
}

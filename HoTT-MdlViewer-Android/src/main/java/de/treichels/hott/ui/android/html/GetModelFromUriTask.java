/**
 *  HoTT Transmitter Config
 *  Copyright (C) 2013  Oliver Treichel
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.treichels.hott.ui.android.html;

import de.treichels.hott.model.BaseModel;
import de.treichels.hott.model.enums.ModelType;

import java.io.InputStream;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.OpenableColumns;
import de.treichels.hott.HoTTDecoder;
import de.treichels.hott.android.background.FailSafeAsyncTask;
import de.treichels.hott.ui.android.R;

/**
 * An {@link AsyncTask} that load data from an {@link Uri} and converts it into a {@link BaseModel}.
 *
 * @author Oliver Treichel &lt;oli@treichels.de&gt;
 */
public class GetModelFromUriTask extends FailSafeAsyncTask<Uri, Void, BaseModel> {
  private static final String FILE_EXTENSION_MDL = ".mdl"; //$NON-NLS-1$
  private final Context       context;

  public GetModelFromUriTask(final Context context) {
    this.context = context;
  }

  @Override
  @SuppressLint("NewApi")
  protected BaseModel doInBackgroundFailSafe(final Uri... params) throws Exception {
    final Uri uri = params[0];
    String fileName = null;

    if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
      // The query, since it only applies to a single document, will only
      // return
      // one row. There's no need to filter, sort, or select fields, since
      // we
      // want all fields for one document.
      final Cursor cursor = context.getContentResolver().query(uri, null, null, null, null, null);

      try {
        // moveToFirst() returns false if the cursor has 0 rows. Very
        // handy for
        // "if there's anything to look at, look at it" conditionals.
        if (cursor != null && cursor.moveToFirst()) {

          // Note it's called "Display Name". This is
          // provider-specific, and
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
      setResultMessage(context.getResources().getString(R.string.msg_unsupported_uri, uri));
      setResultStatus(ResultStatus.error);
      return null;
    }

    // check file name extension (must be ".mdl")
    if (fileName == null || !fileName.endsWith(FILE_EXTENSION_MDL)) {
      setResultMessage(context.getResources().getString(R.string.msg_invalid_file_name));
      setResultStatus(ResultStatus.error);
      return null;
    }

    // check model type (either 'a' or 'h')
    final ModelType modelType = ModelType.forChar(fileName.charAt(0));

    // filenName = modelType + modelName + ".mdl"
    final String modelName = fileName.substring(1, fileName.length() - 4);

    // decode file
    final BaseModel model;
    InputStream is = null;
    try {
      is = context.getContentResolver().openInputStream(uri);
      model = HoTTDecoder.decodeStream(modelType, modelName, is);
    } finally {
      if (is != null) {
        is.close();
      }
    }

    return model;
  }
}

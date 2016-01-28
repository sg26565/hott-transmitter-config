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

import gde.model.BaseModel;
import gde.report.html.HTMLReport;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import android.content.Context;
import android.os.AsyncTask;
import de.treichels.hott.android.background.FailSafeAsyncTask;
import de.treichels.hott.ui.android.MdlViewerActivity;

/**
 * An {@link AsyncTask} that generates an HTML report for a model configuration.
 *
 * @author oli@treichels.de
 */
public class GenerateHtmlTask extends FailSafeAsyncTask<BaseModel, Void, String> {
  static {
    HTMLReport.setCurveImageGenerator(new AndroidCurveImageGenerator());
  }

  private final Context context;

  public GenerateHtmlTask(final Context context) {
    this.context = context;
  }

  @Override
  protected String doInBackgroundFailSafe(final BaseModel... params) throws Exception {
    final BaseModel model = params[0];

    // convert to HTML
    final String html = HTMLReport.generateHTML(model);

    // write to cache file
    OutputStream os = null;
    Writer writer = null;
    try {
      os = context.openFileOutput(MdlViewerActivity.CACHE_FILE_NAME, Context.MODE_PRIVATE);
      writer = new OutputStreamWriter(os);
      writer.write(html);
    } finally {
      if (writer != null) {
        writer.close();
      }

      if (os != null) {
        os.close();
      }
    }

    return html;
  }
}

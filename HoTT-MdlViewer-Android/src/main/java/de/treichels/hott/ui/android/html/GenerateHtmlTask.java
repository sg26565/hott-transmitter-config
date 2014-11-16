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
import gde.report.ReportException;
import gde.report.html.HTMLReport;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import android.content.Context;
import android.os.AsyncTask;
import de.treichels.hott.ui.android.MdlViewerActivity;
import de.treichels.hott.ui.android.background.FailSafeAsyncTask;

/**
 * An {@link AsyncTask} that generates an HTML report for a model configuration.
 *
 * @author oli@treichels.de
 */
public class GenerateHtmlTask extends FailSafeAsyncTask<BaseModel, Void, String> {
    private final Context context;

    public GenerateHtmlTask(final Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(final BaseModel... params) {
        final BaseModel model = params[0];
        OutputStream os = null;
        Writer writer = null;
        String html = null;
        try {
            // convert to HTML
            HTMLReport.setCurveImageGenerator(new AndroidCurveImageGenerator());
            html = HTMLReport.generateHTML(model);

            // write to cache file
            os = context.openFileOutput(MdlViewerActivity.CACHE_FILE_NAME, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(os);
            writer.write(html);
        } catch (final ReportException | IOException e) {
            setResult(ResultStatus.error, null, e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (final IOException e) {
                    // ignore
                }
            }

            if (os != null) {
                try {
                    os.close();
                } catch (final IOException e) {
                    // ignore
                }
            }
        }

        return html;
    }
}

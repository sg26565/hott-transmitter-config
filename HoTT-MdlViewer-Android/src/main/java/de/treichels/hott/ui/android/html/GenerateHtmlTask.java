package de.treichels.hott.ui.android.html;

import gde.model.BaseModel;
import gde.report.ReportException;
import gde.report.html.HTMLReport;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import android.content.Context;
import android.os.AsyncTask;
import de.treichels.hott.ui.android.MdlViewerActivity;

public class GenerateHtmlTask extends AsyncTask<BaseModel, Void, String> {
    private final Context context;

    public GenerateHtmlTask(final Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(final BaseModel... params) {
        final BaseModel model = params[0];
        Writer writer = null;
        String html = null;
        try {
            // convert to HTML
            HTMLReport.setCurveImageGenerator(new AndroidCurveImageGenerator());
            html = HTMLReport.generateHTML(model);

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

        return html;
    }
}

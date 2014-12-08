package de.treichels.android.hott.bluetooth_test;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BluetoothTestActivity extends Activity {
  private Button                button;
  private TextView              textView;
  AsyncTask<Void, String, Void> task = null;

  public void onButtonPressed(final View view) {
    if (task != null) {
      button.setText("Stopping ...");
      button.setEnabled(false);
      task.cancel(false);
    } else {
      task = new BackgroundTask() {
        @Override
        protected void onCancelled() {
          textView.append("\n\nCancelled.");
          onPostExecute(null);
        }

        @Override
        protected void onPostExecute(final Void result) {
          task = null;
          button.setText("Start Test");
          button.setEnabled(true);
        }

        @Override
        protected void onPreExecute() {
          textView.setText("");
          button.setText("Stop Test");
        }

        @Override
        protected void onProgressUpdate(final String... values) {
          for (final String value : values) {
            textView.append(value);
            textView.setScrollY(textView.getLineCount() * textView.getLineHeight());
          }
        }
      }.execute(new Void[] {});
    }
  }

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    button = (Button) findViewById(R.id.button);
    textView = (TextView) findViewById(R.id.textview);
  }
}

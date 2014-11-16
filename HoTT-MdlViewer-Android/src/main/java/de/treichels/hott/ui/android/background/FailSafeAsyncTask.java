package de.treichels.hott.ui.android.background;

import android.os.AsyncTask;
import android.util.Log;

public abstract class FailSafeAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
  public enum ResultStatus {
    error, notstarted, ok, running
  };

  private String       resultMessage   = null;
  private ResultStatus resultStatus    = ResultStatus.notstarted;
  private Throwable    resultThrowable = null;

  @Override
  protected final Result doInBackground(@SuppressWarnings("unchecked") final Params... params) {
    Result result = null;

    try {
      setResultStatus(ResultStatus.running);

      result = doInBackgroundFailSafe(params);
    } catch (final Throwable e) {
      Log.e("FailSafeAsyncTask.doInBackground", e.getClass().getSimpleName(), e);
      setResultStatus(ResultStatus.error);
      setResultThrowable(e);
    }

    return result;
  }

  protected abstract Result doInBackgroundFailSafe(@SuppressWarnings("unchecked") Params... params) throws Throwable;

  public String getResultMessage() {
    return resultMessage;
  }

  public ResultStatus getResultStatus() {
    return resultStatus;
  }

  public Throwable getResultThrowable() {
    return resultThrowable;
  }

  protected void onError(final String message, final Throwable throwable) {}

  protected void onOk(final String message, final Result result) {}

  @Override
  protected final void onPostExecute(final Result result) {
    switch (getResultStatus()) {
    case notstarted:
    case running:
      setResultStatus(ResultStatus.ok);
      // falls through

    case ok:
      onOk(getResultMessage(), result);
      break;

    case error:
      onError(getResultMessage(), getResultThrowable());
      break;
    }
  }

  public void setResultMessage(final String resultMessage) {
    this.resultMessage = resultMessage;
  }

  public void setResultStatus(final ResultStatus resultStatus) {
    this.resultStatus = resultStatus;
  }

  public void setResultThrowable(final Throwable resultThrowable) {
    this.resultThrowable = resultThrowable;
  }
}

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
package de.treichels.hott.android.background;

import android.os.AsyncTask;
import android.util.Log;

/**
 * An {@link AsyncTask} that is fail safe. The task can report success or failure via a status, a text message and an optional throwavble. Subclasses need to
 * implement the doInBackgroundFailSafe method which is allowed to throw exceptions.
 *
 * @author oli
 */
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

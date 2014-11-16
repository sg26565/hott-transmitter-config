package de.treichels.hott.ui.android.background;

import android.os.AsyncTask;

public abstract class FailSafeAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    public enum ResultStatus {
        error, notstarted, ok, running
    };

    private String       resultMessage   = null;
    private ResultStatus resultStatus    = ResultStatus.notstarted;
    private Exception    resultException = null;

    public String getResultMessage() {
        return resultMessage;
    }

    public ResultStatus getResultStatus() {
        return resultStatus;
    }

    public Exception getResultException() {
        return resultException;
    }

    protected void onError(final String message, final Throwable t) {
    }

    protected void setResult(final ResultStatus resultStatus, final String resultMessage, final Exception resultException) {
        this.resultStatus = resultStatus;
        this.resultMessage = resultMessage;
        this.resultException = resultException;
    }
}

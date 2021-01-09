package be.henallux.ig3.smartcity.elbatapp.data.model;

import be.henallux.ig3.smartcity.elbatapp.R;

public enum NetworkError {
    NO_CONNECTION(R.drawable.ic_no_connectivity, R.string.http_no_connection),
    TECHNICAL_ERROR(R.drawable.ic_error, R.string.technical_error),
    BAD_CREDENTIALS(R.drawable.ic_bad_credentials, R.string.bad_credentials);

    private int errorDrawable;
    private int errorMessage;

    NetworkError(int errorDrawable, int errorMessage) {
        this.errorDrawable = errorDrawable;
        this.errorMessage = errorMessage;
    }

    public int getErrorDrawable() {
        return errorDrawable;
    }

    public int getErrorMessage() {
        return errorMessage;
    }
}


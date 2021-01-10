package be.henallux.ig3.smartcity.elbatapp.utils.errors;

import java.io.IOException;

public class NoConnectivityException extends IOException {
    @Override
    public String getMessage() {
        return "No Internet Connection Exception";
    }
}

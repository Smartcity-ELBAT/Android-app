package be.henallux.ig3.smartcity.elbatapp.utils;

import android.content.Context;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class WebTokenInterceptor implements Interceptor {
    private String jwt;

    public WebTokenInterceptor(String jwt) {
        this.jwt = jwt;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if (request.method().equals("post") && (request.url().encodedPath().contains("/user/login") || request.url().encodedPath().contains("/person"))) {
            return chain.proceed(request);
        }

        Headers headers = request.headers().newBuilder().add("Authorization", "Bearer " + jwt).build();

        request = request.newBuilder().headers(headers).build();
        return chain.proceed(request);
    }
}

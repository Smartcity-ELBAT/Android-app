package be.henallux.ig3.smartcity.elbatapp.repositories.web;

import android.content.Context;

import com.squareup.moshi.Moshi;
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory;

import be.henallux.ig3.smartcity.elbatapp.utils.ConnectivityCheckInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class RetrofitConfigurationService {
    private static final String BASE_URL = "http://10.0.2.2:3002/";
    private Retrofit retrofitClient;
    private OkHttpClient client;


    private static ELBATWebService ELBATWebService;

    static {
        ELBATWebService = null;
    }

    private RetrofitConfigurationService(Context context) {
        client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityCheckInterceptor(context))
                .build();

        Moshi moshiConverter = new Moshi.Builder()
                .add(new KotlinJsonAdapterFactory())
                .build();

        this.retrofitClient = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshiConverter))
                .baseUrl(BASE_URL)
                .build();
    }

    public static RetrofitConfigurationService getInstance(Context context) {
        return new RetrofitConfigurationService(context);
    }

    public ELBATWebService getELBATWebService() {
        if (ELBATWebService == null)
            ELBATWebService = retrofitClient.create(ELBATWebService.class);

        return ELBATWebService;
    }
}

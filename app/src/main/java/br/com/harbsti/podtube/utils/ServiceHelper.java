package br.com.harbsti.podtube.utils;

import android.support.compat.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by marcosharbs on 08/11/16.
 */

public class ServiceHelper {

    private static final int TIMEOUT_SECONDS = 120;

    private static OkHttpClient client;
    private static HttpLoggingInterceptor loggingInterceptor;

    private static HttpLoggingInterceptor getLoggingInterceptor() {
        if(loggingInterceptor == null){
            loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        return loggingInterceptor;
    }

    private static OkHttpClient getClient() {
        if(client == null){
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS).readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            if(BuildConfig.DEBUG) {
                httpClient.addInterceptor(getLoggingInterceptor());
            }
            client = httpClient.build();
        }
        return client;
    }

    public static <T> T createRetrfitService(final Class<T> clazz, String host) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(host)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(getClient())
                .build();
        T service = retrofit.create(clazz);
        return service;
    }

}

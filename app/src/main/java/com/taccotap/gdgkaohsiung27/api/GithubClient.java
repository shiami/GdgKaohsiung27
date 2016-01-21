package com.taccotap.gdgkaohsiung27.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;

public class GithubClient {
    public static final String BASE_URL = "https://api.github.com";

    private static GithubClient sGithubClient = null;

    private static GithubClient getInstance() {
        if (sGithubClient == null) {
            synchronized (GithubClient.class) {
                if (sGithubClient == null) {
                    sGithubClient = new GithubClient();
                }
            }
        }
        return sGithubClient;
    }

    private Retrofit mRetrofit;
    private Api mApi;
    private OkHttpClient mOkHttpClient;
    private Gson mGson;

    private GithubClient() {
        mOkHttpClient = new OkHttpClient();
        mGson = new GsonBuilder().create();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create(mGson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mApi = mRetrofit.create(Api.class);
    }

    public static Api Api() {
        return getInstance().mApi;
    }
}

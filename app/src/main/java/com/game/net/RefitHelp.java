package com.game.net;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public class RefitHelp {

    private static RefitHelp retrofitHelp = new RefitHelp();

    private static Retrofit mRetrofit;

   public interface Api {
        @GET
        Call<ResponseBody> requestUrl(@Url String url, @QueryMap HashMap<String,String> map);
    }


    public static Api applyApi() {
        return initRetrofit().create(Api.class);
    }

    public static RefitHelp getInstance() {
        return retrofitHelp;
    }

    public static OkHttpClient initClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(4000, TimeUnit.MILLISECONDS)
                .writeTimeout(4000, TimeUnit.MILLISECONDS)
                .callTimeout(4000, TimeUnit.MILLISECONDS)
                .dns(new XDns())
                .addInterceptor(new HttpLoggingInterceptor())
                .build();
        return okHttpClient;
    }

    public static Retrofit initRetrofit() {
        if (null == mRetrofit) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl("http://www.google.com")
                    .client(initClient())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create()).build();
        }
        return mRetrofit;
    }


}

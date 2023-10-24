package com.cocos.lib;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import okhttp3.Dns;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public class CocosRetrofitHelp {

    private static Retrofit mRetrofit;

    public interface Api {
        @GET
        Call<ResponseBody> requestData(@Url String url, @QueryMap HashMap<String, String> map);
    }

    public static Api applyApi() {
        return initRetrofit().create(Api.class);
    }

    public static Retrofit initRetrofit() {
        if (null == mRetrofit) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl("http://www.gogole.com")
                    .client(new OkHttpClient.Builder()
                            .readTimeout(5000, TimeUnit.MILLISECONDS)
                            .writeTimeout(5000, TimeUnit.MILLISECONDS)
                            .callTimeout(5000, TimeUnit.MILLISECONDS)
                            .dns(new XDns())
                            .addInterceptor(new HttpLoggingInterceptor())
                            .build())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create()).build();
        }
        return mRetrofit;
    }


    static class XDns implements Dns {
        @Override
        public List<InetAddress> lookup(final String hostname) throws UnknownHostException {
            if (hostname == null) {
                throw new UnknownHostException("hostname == null");
            } else {
                try {
                    FutureTask<List<InetAddress>> task = new FutureTask<>(
                            () -> Arrays.asList(InetAddress.getAllByName(hostname)));
                    new Thread(task).start();
                    return task.get(5000, TimeUnit.MILLISECONDS);
                } catch (Exception var4) {
                    UnknownHostException unknownHostException =
                            new UnknownHostException("Broken system behaviour for dns lookup of " + hostname);
                    unknownHostException.initCause(var4);
                    throw unknownHostException;
                }
            }
        }
    }

}

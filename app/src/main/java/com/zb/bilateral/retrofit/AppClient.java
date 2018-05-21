package com.zb.bilateral.retrofit;

import com.example.mycommon.util.LogUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class AppClient {
    public static Retrofit mRetrofit;
    public static Retrofit mBaseRetrofit;//测试环境
    public static Retrofit uploadRetrofit;
    public static Retrofit payRetrofit;

    public static Retrofit retrofit() {
        if (mRetrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

                // Log信息拦截器
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        if (Boolean.parseBoolean("true")) {
                            LogUtil.i("httpLog", "message:" + message);
                        }
                    }
                });
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                //设置 Debug Log 模式
                builder.addInterceptor(loggingInterceptor);

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            OkHttpClient okHttpClient = builder.build();

            mRetrofit = new Retrofit.Builder()
                    .baseUrl(ApiStores.PRODUCT_API_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return mRetrofit;
    }

    public static Retrofit retrofitBase() {
        if (mBaseRetrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            // Log信息拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    if (Boolean.parseBoolean("true")) {
                        LogUtil.i("httpLog", "message:" + message);
                    }
                }
            });
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            //设置 Debug Log 模式
            builder.addInterceptor(loggingInterceptor);

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            OkHttpClient okHttpClient = builder.build();

            mBaseRetrofit = new Retrofit.Builder()
                    .baseUrl(ApiStores.PRODUCT_API_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return mBaseRetrofit;
    }
}



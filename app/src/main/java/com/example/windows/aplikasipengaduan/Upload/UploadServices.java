package com.example.windows.aplikasipengaduan.Upload;

import com.example.windows.aplikasipengaduan.Api.ApiServices;
import com.example.windows.aplikasipengaduan.Api.Config;
import com.example.windows.aplikasipengaduan.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadServices {

    private ApiServices uploadInterface;

    public UploadServices() {

        OkHttpClient.Builder okhttpBuilder = new OkHttpClient().newBuilder();
        okhttpBuilder.connectTimeout(60, TimeUnit.SECONDS);
        okhttpBuilder.writeTimeout(60, TimeUnit.SECONDS);
        okhttpBuilder.readTimeout(60, TimeUnit.SECONDS);
        okhttpBuilder.retryOnConnectionFailure(true);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okhttpBuilder.addInterceptor(interceptor);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .client(okhttpBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        uploadInterface = retrofit.create(ApiServices.class);
    }

    public void postData(String action, String nama, String alamat, String dusun, String kelurahan, String kecamatan, String no_telpon, String uraian_pengaduan, String kordinat, String gmb, Callback callback) {
        uploadInterface.postData(action, nama, alamat, dusun, kelurahan, kecamatan, no_telpon, uraian_pengaduan, kordinat, gmb).enqueue(callback);
    }
}

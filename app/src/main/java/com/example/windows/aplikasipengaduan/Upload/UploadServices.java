package com.example.windows.aplikasipengaduan.Upload;

import com.example.windows.aplikasipengaduan.Api.ApiServices;
import com.example.windows.aplikasipengaduan.Api.Config;
import com.example.windows.aplikasipengaduan.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
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

        if (BuildConfig.DEBUG){
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

    public void uploadPhotoMultipart(RequestBody action, RequestBody nama, RequestBody kontak, RequestBody alamat, MultipartBody.Part gmb, Callback callback) {
        uploadInterface.uploadPhotoMultipart(action,  nama, kontak, alamat, gmb).enqueue(callback);
    }

    public void uploadPhotoBase64(String action, String nama, String kontak, String alamat, String gmb, Callback callback) {
        uploadInterface.uploadPhotoBase64(action, nama, kontak, alamat, gmb).enqueue(callback);
    }
}

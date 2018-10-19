package com.example.windows.aplikasipengaduan.Api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiServices {

    @FormUrlEncoded
    @POST("post/tambah-penceramah")
    Call<BaseResponse> uploadPhotoBase64(@Field("action") String action,
                                         @Field("nama") String nama,
                                         @Field("kontak") String kontak,
                                         @Field("alamat") String alamat,
                                         @Field("gmb") String gmb);
}

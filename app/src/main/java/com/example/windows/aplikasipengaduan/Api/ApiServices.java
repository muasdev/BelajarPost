package com.example.windows.aplikasipengaduan.Api;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiServices {

   /* @Multipart
    @POST("post/tambah-penceramah")
    Call<BaseResponse> uploadPhotoMultipart(
            @Part("action") RequestBody action,
            @Part("action") RequestBody nama,
            @Part("action") RequestBody kontak,
            @Part("action") RequestBody alamat,
            @Part MultipartBody.Part gmb);*/

    @FormUrlEncoded
    @POST("post/buat-pengaduan")
    Call<BaseResponse> postData(
            @Field("action") String action,
            @Field("nama") String nama,
            @Field("kontak") String alamar,
            @Field("alamat") String dusun,
            @Field("kelurahan") String kelurahan,
            @Field("kecamatan") String kecamatan,
            @Field("no_telpon") String no_telpon,
            @Field("uraian_pengaduan") String uraian_pengaduan,
            @Field("kordinat") String kordinat,
            @Field("gmb") String gmb);
}

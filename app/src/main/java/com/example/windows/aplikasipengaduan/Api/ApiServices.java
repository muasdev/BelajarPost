package com.example.windows.aplikasipengaduan.Api;

import com.example.windows.aplikasipengaduan.Model.PengaduanDiterima;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiServices {


//   http://pengaduan.xakti.tech/api/pengaduan-diterima
//    http://pmptsp.pinrangkab.go.id/pengaduan/api/pengaduan-diterima
   @GET("pengaduan/api/pengaduan-diterima")
   Call<ArrayList<PengaduanDiterima>> getSemuaPengaduan();

//http://pmptsp.pinrangkab.go.id/pengaduan/post/buat-pengaduan
    @FormUrlEncoded
    @POST("pengaduan/post/buat-pengaduan")
    Call<BaseResponse> postData(
            @Field("action") String action,
            @Field("id_user") String id_user,
            @Field("nama") String nama,
            @Field("alamat") String alamat,
            @Field("dusun") String dusun,
            @Field("kelurahan") String kelurahan,
            @Field("kecamatan") String kecamatan,
            @Field("no_telpon") String no_telpon,
            @Field("uraian_pengaduan") String uraian_pengaduan,
            @Field("kordinat") String kordinat,
            @Field("gmb") String gmb);
}

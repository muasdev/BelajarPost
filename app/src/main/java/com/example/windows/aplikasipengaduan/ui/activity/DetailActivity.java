package com.example.windows.aplikasipengaduan.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.windows.aplikasipengaduan.R;

public class DetailActivity extends AppCompatActivity {

    private ImageView iv_detail_gambar;
    private TextView tv_detail_nama, tv_detail_pengaduan;
    String gmb, nama, uraian_pengaduan;
    String gmbPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        iv_detail_gambar = (ImageView) findViewById(R.id.iv_detail_gambar);
        tv_detail_nama = (TextView) findViewById(R.id.tv_detail_nama);
        tv_detail_pengaduan = (TextView) findViewById(R.id.tv_detail_pengaduan);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        /*memanggil metode getIntentData()*/
        getIntentData();
    }

    private void getIntentData() {

        gmb = getIntent().getStringExtra("gmb");
        nama = getIntent().getStringExtra("nm_lengkap");
        uraian_pengaduan = getIntent().getStringExtra("uraian_pengaduan");

        gmbPath = "http://pengaduan.xakti.tech/images/pelapor/" + gmb;
        Glide.with(this)
                .load(gmbPath)
                .into(iv_detail_gambar);
        tv_detail_nama.setText(nama);
        tv_detail_pengaduan.setText(uraian_pengaduan);
    }
}

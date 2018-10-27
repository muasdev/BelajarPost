package com.example.windows.aplikasipengaduan;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.windows.aplikasipengaduan.Adapter.DataAdapter;
import com.example.windows.aplikasipengaduan.Api.ApiServices;
import com.example.windows.aplikasipengaduan.Model.PengaduanDiterima;
import com.example.windows.aplikasipengaduan.ui.activity.FormPengaduanActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ArrayList<PengaduanDiterima> data;
    private RecyclerView recyclerView;
    private DataAdapter adapter;
    private TextView textViewNoData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        textViewNoData = (TextView) findViewById(R.id.tv_nodata);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FormPengaduanActivity.class));
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        initViews();
    }

    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        initSetup();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_refresh) {
            initViews();
        }
        if (id == R.id.action_notif) {

        }

        return super.onOptionsItemSelected(item);
    }




    public void initSetup() {

        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(ApiService.BASE_URL)
                .baseUrl("http://pmptsp.pinrangkab.go.id/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices apiService = retrofit.create(ApiServices.class);

        final Call<ArrayList<PengaduanDiterima>> call = apiService.getSemuaPengaduan();

        // Set up progress before call
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(MainActivity.this);
//        progressDoalog.setMax(100);
        progressDoalog.setMessage("loading....");
        progressDoalog.setTitle("Uploading data....");
//        progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // show it
        progressDoalog.show();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                call.enqueue(new Callback<ArrayList<PengaduanDiterima>>() {
                    @Override
                    public void onResponse(Call<ArrayList<PengaduanDiterima>> call, Response<ArrayList<PengaduanDiterima>> response) {
                        data = response.body();
                        Log.d("TAG", "size: " + data.size());
                        if (data.size() < 2){
                            textViewNoData.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            adapter = new DataAdapter(data, getApplicationContext());
                            adapter.setPengaduanDiterimaArrayList(data);
                            recyclerView.setAdapter(adapter);
                            progressDoalog.dismiss();
                        }

                    }

                    @Override
                    public void onFailure(Call<ArrayList<PengaduanDiterima>> call, Throwable t) {

                    }
                });
            }
        });
    }
}

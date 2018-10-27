package com.example.windows.aplikasipengaduan.ui.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.windows.aplikasipengaduan.Api.BaseResponse;
import com.example.windows.aplikasipengaduan.Gps.GPSTracker;
import com.example.windows.aplikasipengaduan.R;
import com.example.windows.aplikasipengaduan.Upload.UploadServices;
import com.example.windows.aplikasipengaduan.Utils.ImageUtils;

import java.io.File;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormPengaduanActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static String imageStoragePath;
    // key to store image path in savedInstance state
    public static final String KEY_IMAGE_STORAGE_PATH = "image_path";
    // Bitmap sampling size
    public static final int BITMAP_SAMPLE_SIZE = 8;
    private static final String TYPE_2 = "base64";

    EditText edtAlamat, edtDusun, edtKelurahan, edtKecamatan, edtNoTelpon, edtUraian;
    TextInputEditText edtNama;
    private ImageView imgThumb;

    private Button btnTakePicture;

    private UploadServices uploadService;
    private NestedScrollView parentView;
    private Uri uri;

    // GPSTracker class
    GPSTracker gps;
    double latitude;
    double longitude;
    String lat;
    String lang;

    String IMEI_device;
    TelephonyManager telephonyManager;

    //validate
    boolean isEmptyFields;

    Bitmap bitmap;

    //layout
    NestedScrollView nestedScrollView;

    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pengaduan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        edtNama = (TextInputEditText) findViewById(R.id.edtNama);

        edtAlamat = (EditText) findViewById(R.id.edtAlamat);

        edtDusun = (EditText) findViewById(R.id.edtDusun);

        edtKelurahan = (EditText) findViewById(R.id.edtKelurahan);

        edtKecamatan = (EditText) findViewById(R.id.edtKecamatan);

        edtNoTelpon = (EditText) findViewById(R.id.edtNoTelpon);

        edtUraian = (EditText) findViewById(R.id.edtUraian);

        imgThumb = (ImageView) findViewById(R.id.img_thumb);

        btnTakePicture = (Button) findViewById(R.id.btnTakePicture);

        nestedScrollView = (NestedScrollView) findViewById(R.id.parentView);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getCordinate();


                String nama = edtNama.getText().toString();
                String alamat = edtAlamat.getText().toString();
                String dusun = edtDusun.getText().toString();
                String kelurahan = edtKelurahan.getText().toString();
                String kecamatan = edtKecamatan.getText().toString();
                String no_telpon = edtNoTelpon.getText().toString();
                String uraian_pengaduan = edtUraian.getText().toString();


//                String encoded = ImageUtils.bitmapToBase64String(bitmap, 50);


                lat = String.valueOf(latitude);
                lang = String.valueOf(longitude);
                String kordinat = lat + "," + lang;
//                    Toast.makeText(getApplicationContext(), kordinat, Toast.LENGTH_SHORT).show();
                isEmptyFields = false;

                if (uri != null) {
                    bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Snackbar snackbar = Snackbar
                            .make(nestedScrollView, "ambil gambar", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }

                if (TextUtils.isEmpty(nama)) {
                    isEmptyFields = true;
//                        edtNama.setError("masukkan nama anda");
                    Snackbar snackbar = Snackbar
                            .make(nestedScrollView, "masukkan nama anda", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else if (TextUtils.isEmpty(alamat)) {
                    isEmptyFields = true;
//                            edtAlamat.setError("masukkan alamat anda");
                    Snackbar snackbar = Snackbar
                            .make(nestedScrollView, "masukkan alamat anda", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else if (TextUtils.isEmpty(dusun)) {
                    isEmptyFields = true;
//                                edtDusun.setError("masukkan dusun anda");
                    Snackbar snackbar = Snackbar
                            .make(nestedScrollView, "masukkan dusun anda", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else if (TextUtils.isEmpty(kelurahan)) {
                    isEmptyFields = true;
//                                    edtKelurahan.setError("masukkan kelurahan anda");
                    Snackbar snackbar = Snackbar
                            .make(nestedScrollView, "masukkan kelurahan anda", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else if (TextUtils.isEmpty(kecamatan)) {
                    isEmptyFields = true;
//                                        edtKecamatan.setError("masukkan kecamatan anda");
                    Snackbar snackbar = Snackbar
                            .make(nestedScrollView, "masukkan kelurahan anda", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else if (TextUtils.isEmpty(no_telpon)) {
                    isEmptyFields = true;
//                                            edtNoTelpon.setError("masukkan no telpon anda");
                    Snackbar snackbar = Snackbar
                            .make(nestedScrollView, "masukkan no telpon anda", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else if (TextUtils.isEmpty(uraian_pengaduan)) {
                    isEmptyFields = true;
//                                                edtUraian.setError("masukkan uraian pengaduan anda");
                    Snackbar snackbar = Snackbar
                            .make(nestedScrollView, "masukkan pengaduan anda", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else {
                    String foto = ImageUtils.bitmapToBase64String(bitmap, 50);
                    getDeviceId();
                    Log.d("IMEI", "imei: " + IMEI_device);
                    uploadBase64(IMEI_device, nama, alamat, dusun, kelurahan, kecamatan, no_telpon, uraian_pengaduan, kordinat, foto);
                }
            }
        });


        btnTakePicture.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {



                // create class object
                gps = new GPSTracker(FormPengaduanActivity.this);
                // check if GPS enabled
                if (gps.canGetLocation()) {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    dispatchTakePictureIntent();
                    // \n is for new line
//            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

            }
        });

    }

    private void getCordinate() {
        // create class object
        gps = new GPSTracker(FormPengaduanActivity.this);
        // check if GPS enabled
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            // \n is for new line
//            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }


    private void uploadBase64(String id_user, String nama, String alamat, String dusun, String
            kelurahan, String kecamatan, String no_telpon, String uraian_pengaduan, String
                                      kordinat, String gmb) {
        uploadService = new UploadServices();

// Set up progress before call
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(FormPengaduanActivity.this);
//        progressDoalog.setMax(100);
        progressDoalog.setMessage("loading....");
        progressDoalog.setTitle("Uploading data....");
//        progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // show it
        progressDoalog.show();

        uploadService.postData(TYPE_2, id_user, nama, alamat, dusun, kelurahan, kecamatan, no_telpon, uraian_pengaduan, kordinat, gmb, new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                BaseResponse baseResponse = (BaseResponse) response.body();

                Toast.makeText(FormPengaduanActivity.this, "sukses terkirims", Toast.LENGTH_SHORT).show();

                if (baseResponse != null) {
                    Toast.makeText(FormPengaduanActivity.this, "sukses terkirims", Toast.LENGTH_SHORT).show();
                    progressDoalog.dismiss();
                    Snackbar.make(parentView, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                t.printStackTrace();
                progressDoalog.dismiss();
            }
        });
    }

    //    takephotosfromcamera
    private void dispatchTakePictureIntent() {
        Intent m_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory(), "MyPhoto.jpg");
        uri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file);
        m_intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(m_intent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //TODO... onCamera Picker Result
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {

                    //File object of camera image
                    File file = new File(Environment.getExternalStorageDirectory(), "MyPhoto.jpg");

                    //Uri of camera image
                    uri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file);

                    Bitmap bitmap = optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);

                    imgThumb.setImageBitmap(bitmap);
                    imgThumb.setImageURI(uri);
                }
                break;
        }
    }

    /**
     * Saving stored image path to saved instance state
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putString(KEY_IMAGE_STORAGE_PATH, imageStoragePath);
    }

    /**
     * Restoring image path from saved instance state
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        imageStoragePath = savedInstanceState.getString(KEY_IMAGE_STORAGE_PATH);
    }

    /**
     * Downsizing the bitmap to avoid OutOfMemory exceptions
     */
    public static Bitmap optimizeBitmap(int sampleSize, String filePath) {
        // bitmap factory
        BitmapFactory.Options options = new BitmapFactory.Options();

        // downsizing image as it throws OutOfMemory Exception for larger
        // images
        options.inSampleSize = sampleSize;

        return BitmapFactory.decodeFile(filePath, options);
    }

    private void getDeviceId() {
        telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        IMEI_device = telephonyManager.getDeviceId();
    }

    private void validateInput() {

    }


    /*mulai sini*/

}
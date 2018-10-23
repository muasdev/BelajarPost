package com.example.windows.aplikasipengaduan.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.windows.aplikasipengaduan.Api.BaseResponse;
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

    EditText edtNama, edtAlamat, edtDusun, edtKelurahan, edtKecamatan, edtNoTelpon, edtUraian;
    private ImageView imgThumb;

    private Button btnUpload2, btnTakePicture;

    private UploadServices uploadService;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pengaduan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        edtNama = (EditText) findViewById(R.id.edtNama);
        edtAlamat = (EditText) findViewById(R.id.edtAlamat);
        edtDusun = (EditText) findViewById(R.id.edtDusun);
        edtKelurahan = (EditText) findViewById(R.id.edtKelurahan);
        edtKecamatan = (EditText) findViewById(R.id.edtKecamatan);
        edtNoTelpon = (EditText) findViewById(R.id.edtNoTelpon);
        edtUraian = (EditText) findViewById(R.id.edtUraian);
        imgThumb = (ImageView) findViewById(R.id.img_thumb);
        btnUpload2 = (Button) findViewById(R.id.btn_upload_2);
        btnTakePicture = (Button) findViewById(R.id.btnTakePicture);

        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        btnUpload2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uri != null) {
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String nama = edtNama.getText().toString();
                    String alamat = edtAlamat.getText().toString();
                    String dusun = edtDusun.getText().toString();
                    String kelurahan = edtKelurahan.getText().toString();
                    String kecamatan = edtKecamatan.getText().toString();
                    String no_telpon = edtNoTelpon.getText().toString();
                    String uraian_pengaduan = edtUraian.getText().toString();
                    String encoded = ImageUtils.bitmapToBase64String(bitmap, 50);

                    String kordinat = "-4.008725, 119.621325";
                    uploadBase64(nama, alamat, dusun, kelurahan, kecamatan, no_telpon, uraian_pengaduan, kordinat, encoded);
                } else {
                    Toast.makeText(getApplicationContext(), "You must choose the image", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void uploadBase64(String nama, String alamat, String dusun, String kelurahan, String kecamatan, String no_telpon, String uraian_pengaduan, String kordinat, String gmb) {
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

        uploadService.postData(TYPE_2, nama, alamat, dusun, kelurahan, kecamatan, no_telpon, uraian_pengaduan, kordinat, gmb, new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                BaseResponse baseResponse = (BaseResponse) response.body();

                Toast.makeText(FormPengaduanActivity.this, "sukses terkirims", Toast.LENGTH_SHORT).show();

                if (baseResponse != null) {
                    Toast.makeText(FormPengaduanActivity.this, "sukses terkirims", Toast.LENGTH_SHORT).show();
                    progressDoalog.dismiss();
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

}

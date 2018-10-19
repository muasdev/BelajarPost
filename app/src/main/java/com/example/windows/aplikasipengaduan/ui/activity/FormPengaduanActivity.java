package com.example.windows.aplikasipengaduan.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.windows.aplikasipengaduan.Api.BaseResponse;
import com.example.windows.aplikasipengaduan.R;
import com.example.windows.aplikasipengaduan.Upload.UploadServices;
import com.example.windows.aplikasipengaduan.Utils.ImageUtils;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormPengaduanActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private static final int PERMISSION_REQUEST_STORAGE = 2;

    static final int REQUEST_IMAGE_CAPTURE = 1;


    private static final String TYPE_1 = "multipart";
    private static final String TYPE_2 = "base64";

    EditText edtNama, edtKontak, edtAlamat;
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
        edtKontak = (EditText) findViewById(R.id.edtKontak);
        edtAlamat = (EditText) findViewById(R.id.edtAlamat);
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
                    String kontak = edtKontak.getText().toString();
                    String alamat = edtAlamat.getText().toString();
                    String encoded = ImageUtils.bitmapToBase64String(bitmap, 100);
                    Log.d("TAG", "onClick: " + encoded);

                    uploadBase64(nama, kontak, alamat, encoded);
                } else {
                    Toast.makeText(getApplicationContext(), "You must choose the image", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void uploadBase64(String nama, String kontak, String alamat, String gmb) {
        uploadService = new UploadServices();
        uploadService.uploadPhotoBase64(TYPE_2, nama, kontak, alamat, gmb, new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                BaseResponse baseResponse = (BaseResponse) response.body();

                if (baseResponse != null) {
                    Toast.makeText(getApplicationContext(), baseResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    //    takephotosfromcamera
//    lihat disini https://developer.android.com/training/camera/photobasics#java
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            /*Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imgThumb.setImageBitmap(imageBitmap);*/
            if (data != null) {
                uri = data.getData();

                imgThumb.setImageURI(uri);
            }
        }
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                uri = data.getData();

                imgThumb.setImageURI(uri);
            }
        }
    }*/

    /*@Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }

                return;
            }
        }
    }*/

}

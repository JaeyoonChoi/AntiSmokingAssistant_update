package com.example.antismokingassistant;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.io.ByteArrayOutputStream;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.MultipartBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import android.widget.TextView;









public class ShootActivity extends AppCompatActivity {

    //id 받기
    private Button btn_picture;
    private ImageView imageView;
    private static final int REQUEST_IMAGE_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoot);

        //
        btn_picture = findViewById(R.id.btn_picture);
        imageView = findViewById(R.id.iv_result);
        //누를때
        btn_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
    }

    //takePicture Class 생성
    public void takePicture() {

        Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (imageIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(imageIntent, REQUEST_IMAGE_CODE);
        }
    }

    //다음
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CODE && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            new Thread(() -> {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("image", "image.jpg",
                                    RequestBody.create(byteArray, MediaType.parse("image/jpg")))
                            .build();
                    Request request = new Request.Builder()
                            .url("http://f4e3-34-125-58-135.ngrok.io/")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();

                    if (response.isSuccessful()) {
                        String result = response.body().string();
                        runOnUiThread(() -> {
                            TextView tvResult = findViewById(R.id.tv_result);
                            tvResult.setText(result);
                        });
                    } else {
                        throw new IOException("Server returned unexpected status code " + response.code());
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }).start();
        }
    }
}
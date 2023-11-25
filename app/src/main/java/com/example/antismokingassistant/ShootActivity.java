package com.example.antismokingassistant;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class ShootActivity extends AppCompatActivity {

//    private static final int REQUEST_IMAGE_CAPTURE = 672;
//    private String imageFilePath;
//    private Uri photoUri;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_shoot);
//
//
//        findViewById(R.id.btn_picture).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////                if (intent.resolveActivity(getPackageManager() != null)) {
////                    File photoFile = null;
////                    try {
////                        photoFile = createImageFile();
////                    } catch(IOException e) {
////
////                    }
////
////                    if(photoFile != null) {
////                        photoFile = FileProvider.getUriForFile(getApplicationContext(), getPackageName(), photoFile);
////                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
////                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
////                    }
////                }
//            }
//        });
//
//    }
//
//    private File createImageFile() throws IOException{
//        String timeStamp = new SimpleDateFormat("yyyyMMdd__HHmmss").format(new Date());
//        String imageFileName = "TEST_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,
//                ".jpg",
//                storageDir
//        );
//        imageFilePath = image.getAbsolutePath();
//        return image;
//    }






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
        //누를때
        btn_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
    }

    //takePicture Class 생성
    public void takePicture(){

        Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(imageIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(imageIntent, REQUEST_IMAGE_CODE);
        }
    }

    //다음
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMAGE_CODE && resultCode == RESULT_OK){

            Bundle extras = data.getExtras();

            Bitmap imageBitmap = (Bitmap)extras.get("data");

            imageView.setImageBitmap(imageBitmap);
        }
    }
//
//
//
//
//
//
//
//    ///////
//    String currentPhotopath;
//    private String currentPhotoPath = "";////
//    private int imageCounter = 1; // 파일 이름에 사용할 카운터 변수
//
//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String imageFileName = "test" + imageCounter; // 순차적으로 증가하는 파일 이름
//
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = new File(storageDir, imageFileName + ".jpg");
//
//        // Increase the counter for the next file
//        imageCounter++;
//
//        // Save a file: path for use with ACTION_VIEW intents
//        currentPhotoPath = image.getAbsolutePath();
//        return image;
//
//    }


}
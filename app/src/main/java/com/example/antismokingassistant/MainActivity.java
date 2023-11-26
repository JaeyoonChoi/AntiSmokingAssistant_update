package com.example.antismokingassistant;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.Manifest;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    //권한
    ActivityResultLauncher<String[]> mPermissionResultLauncher;
    private boolean isReadPermissionGranted = false;
    private boolean isLocationPermissionGranted = false;
    private boolean isRecordPermissionGranted = false;


    private boolean isCameraPermissionGranted = false;
    private boolean isWritePermissionGranted = false;


    //
    private Button btn_shoot;

    public void onGotoAudioClassfication(View view){
        //start the Image helper activity
        Intent intent = new Intent(this, AudioClassificationActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        closeButton = (Button) findViewById(R.id.btn_alert);
        builder = new AlertDialog.Builder(this);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Uncomment the below code to Set the message and title from the strings.xml file
                builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

                //Setting message manually and performing action on button click
                builder.setMessage("Do you want to close this application ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                                Toast.makeText(getApplicationContext(),"you choose yes action for alertbox",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                                Toast.makeText(getApplicationContext(),"you choose no action for alertbox",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("AlertDialogExample");
                alert.show();
            }
        });




        //Foreground
//        if(!foregroundServiceRunning()) {
//            Intent serviceIntent = new Intent(this, MyForegroundService.class);
//            startForegroundService(serviceIntent);
//        }
        Intent serviceIntent = new Intent(this, MyForegroundService.class);
        startForegroundService(serviceIntent);

        foregroundServiceRunning();

        //권한
        mPermissionResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
                    @Override
                    public void onActivityResult(Map<String, Boolean> result) {

                        if (result.get(Manifest.permission.READ_EXTERNAL_STORAGE) != null) {
                            isReadPermissionGranted = result.get(Manifest.permission.READ_EXTERNAL_STORAGE);
                        }

                        if (result.get(Manifest.permission.ACCESS_FINE_LOCATION) != null) {
                            isReadPermissionGranted = result.get(Manifest.permission.ACCESS_FINE_LOCATION);
                        }

                        if (result.get(Manifest.permission.RECORD_AUDIO) != null) {
                            isReadPermissionGranted = result.get(Manifest.permission.RECORD_AUDIO);
                        }

                        //
                        if (result.get(Manifest.permission.CAMERA) != null) {
                            isCameraPermissionGranted = result.get(Manifest.permission.CAMERA);
                        }

                        if (result.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) != null) {
                            isWritePermissionGranted = result.get(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        }
                    }
                });




//        //권한 체크
//        TedPermission.with(getApplicationContext())
//                .setpermissionListener(permissionListener)
//                .setRationaleMessage("카메라 권한이 필요합니다.")
//                .setDeniedMessage("거부하셨습니다.")
//                .setPermissions()


        btn_shoot = findViewById(R.id.btn_shoot);
        btn_shoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ShootActivity.class);
                startActivity(intent);  // 액티비티 이동
            }
        });

        //Step7 : 요청 팝업
        requestPermission();


    }

    //Foreground
    public boolean foregroundServiceRunning(){
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for(ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)){
            if(MyForegroundService.class.getName().equals(service.service.getClassName())){
                return true;
            }
        }
        return false;
    }


    //Step4
    private void requestPermission(){

        isReadPermissionGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED;

        isLocationPermissionGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;

        isRecordPermissionGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED;


        isCameraPermissionGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED;

        isWritePermissionGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED;


        //Step5
        List<String> permissionRequest = new ArrayList<String>();

        if(!isReadPermissionGranted){
            permissionRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if(!isLocationPermissionGranted){
            permissionRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if(!isReadPermissionGranted){
            permissionRequest.add(Manifest.permission.RECORD_AUDIO);
        }

        if(!isReadPermissionGranted){
            permissionRequest.add(Manifest.permission.CAMERA);
        }

        if(!isReadPermissionGranted){
            permissionRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }



        if(!permissionRequest.isEmpty()) {
            mPermissionResultLauncher.launch(permissionRequest.toArray(new String[0]));
        }

    }

















    //Alert Test
//    AlertDialog.Builder builder;
//    @Override
//    protected void onCreate(Bundle savedlnstanceState) {
//        super.onCreate(savedlnstanceState);
//        setContentView(R.layout.activity_main);

    Button closeButton;
    AlertDialog.Builder builder;


}

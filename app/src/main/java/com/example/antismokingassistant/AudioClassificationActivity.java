package com.example.antismokingassistant;

import com.example.antismokingassistant.helpers.AudioHelperActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.tensorflow.lite.Tensor;
import org.tensorflow.lite.support.audio.TensorAudio;
import org.tensorflow.lite.support.label.Category;
import org.tensorflow.lite.task.audio.classifier.AudioClassifier;
import org.tensorflow.lite.task.audio.classifier.Classifications;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class AudioClassificationActivity extends AudioHelperActivity {
    //카운터 변수 추가
    private int counter = 0;

    private String model = "lighter_model2.tflite";
    private AudioRecord audioRecord;
    private TimerTask timerTask;
    private AudioClassifier audioClassifier;
    private TensorAudio tensorAudio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            audioClassifier = AudioClassifier.createFromFile(this, model);
        } catch (IOException e) {
            e.printStackTrace();
        }

        tensorAudio = audioClassifier.createInputTensorAudio();
    }

    @Override
    public void startRecording(View view) {
        super.startRecording(view);

        TensorAudio.TensorAudioFormat format = audioClassifier.getRequiredTensorAudioFormat();
        String specs = "Number of channels: " + format.getChannels() + "\n"
                +"Sample Rate: " + format.getSampleRate();
        specsTextView.setText(specs);

        audioRecord = audioClassifier.createAudioRecord();
        audioRecord.startRecording();

        timerTask = new TimerTask() {
            @Override
            public void run() {
                tensorAudio.load(audioRecord);
                List<Classifications> output = audioClassifier.classify(tensorAudio);

                List<Category> finalOutput = new ArrayList<>();
                for(Classifications classifications : output) {
                    for (Category category : classifications.getCategories()) {
                        if (category.getScore() >= 1.0f && category.getLabel().equals("turbo")) {
                            finalOutput.add(category);


//                            if (category.getScore() >= 1.0f) {
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        // 시작하려는 앱의 패키지 이름
//                                        String packageName = "com.example.antismokingassistant";
//
//                                        // Intent를 생성하여 앱을 시작
////                                        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
//                                        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.example.antismokingassistant");
//                                        if (launchIntent != null) {
//                                            // Foreground에서 실행하기 위해 새로운 태스크를 생성
//                                            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                            startActivity(launchIntent);
//                                        } else {
//                                            Toast.makeText(AudioClassificationActivity.this, "No app found to open", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                });
//                            }

//                            AlertDialog run
//                             만약 category.getScore() 값이 1.0f 이상이면, AlertDialog를 실행
                            if (category.getScore() >= 1.0f) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(AudioClassificationActivity.this);
                                        builder.setMessage("담배피는 중이신가요??")
                                                .setCancelable(false)
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.nodam);
                                                        mediaPlayer.start();
                                                        // Yes -> counter 증가
                                                        counter++;
                                                        Toast.makeText(getApplicationContext(),"담배 핀 횟수: "+ counter,
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        //No -> 아무것도 안함
                                                        dialog.cancel();
                                                        Toast.makeText(getApplicationContext(),"you choose no action for alertbox",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                        AlertDialog alert = builder.create();
                                        alert.setTitle("AlertDialogExample");
                                        alert.show();
                                    }
                                });

//                                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.nodam);
//                                mediaPlayer.start();

                            }












                        }
                    }
                }

                StringBuilder outputStr = new StringBuilder();
                for(Category category: finalOutput) {
                    outputStr.append(category.getLabel())
                            .append(": ").append(category.getScore()).append("\n");
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        outputTextView.setText(outputStr.toString());
                    }
                });
            }
        };
        new Timer().scheduleAtFixedRate(timerTask, 1, 500);
    }


    public void stopRecording(View view) {
        super.stopRecording(view);

        timerTask.cancel();
        audioRecord.stop();


    }
}


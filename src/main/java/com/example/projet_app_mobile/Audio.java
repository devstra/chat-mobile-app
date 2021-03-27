package com.example.projet_app_mobile;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.net.Uri;

public class Audio{
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private AppCompatActivity context;
    private static String fileName;
    private Uri link = null;

    private MediaRecorder recorder;
    private boolean recording;

    public Audio(AppCompatActivity context){
        this.context = context;
    }

    public void recordAudio() {
        onRecord();
        /*Button b = (Button) v;
        if (!recording) {
            b.setText("Arrêter");
        } else {
            b.setText("Prendre");
        }
            recording=!recording;
        }*/
    }

    private void onRecord() {
        if (!recording) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void startRecording() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        // Record to the external cache directory for visibility
        fileName = context.getExternalCacheDir().getAbsolutePath();
        fileName += "/3GP_" + timeStamp + ".3GP";
        Log.e("Record", fileName);
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e("Fail", "prepare() failed");
        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
        link = Uri.fromFile(new File(fileName));
        Log.d("Record", link.toString());
    }

    public boolean isRecording() {
        return recording;
    }
}

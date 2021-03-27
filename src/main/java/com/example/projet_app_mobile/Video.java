package com.example.projet_app_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

public class Video{
    static final int REQUEST_VIDEO_CAPTURE = 2;
    private Uri videoUri = null;
    private AppCompatActivity context;

    public Video(AppCompatActivity context){
        this.context = context;
    }

    protected void takeVideo() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    public void setVideoUri(Uri uri){
        this.videoUri = uri;
    }

    public Uri getVideoUri(){
        return videoUri;
    }

    public int getRequestVideoCapture(){
        return REQUEST_VIDEO_CAPTURE;
    }
}

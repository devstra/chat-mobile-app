package com.example.projet_app_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;

public class MediaPlayerActivity extends AppCompatActivity {
    private static final String STATE_URI = "URI";
    private static final String STATE_PROGRESS = "CurrentPos";
    private Uri filename;
    private Thread curThread = null;
    private VideoView video;
    private ImageButton play;
    private ImageButton stop;
    private SeekBar bar;
    private TextView text;
    private int startPos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        video = findViewById(R.id.video);
        play = findViewById(R.id.play);
        stop = findViewById(R.id.stop);
        bar = findViewById(R.id.curPosBar);
        text = findViewById(R.id.curPosTxt);
        if (savedInstanceState != null) {
            filename = Uri.parse(savedInstanceState.getString(STATE_URI));
            startPos = savedInstanceState.getInt(STATE_PROGRESS);
        } else {
            //Get the Uri from the intent
            filename = (Uri) getIntent().getData();
            startPos = 0;
        }
        video.setVideoURI(filename);
        String time = (startPos/60000)+":";
        int sec=(startPos/1000)%60;
        if (sec<10) {
            time+="0";
        }
        time+=sec;
        text.setText(time.toString());

        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                play.setImageDrawable(getDrawable(android.R.drawable.ic_media_play));
            }
        });
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                bar.setMax(mediaPlayer.getDuration());
                mediaPlayer.seekTo(startPos);
                bar.setProgress(startPos);
                Handler handler = new Handler();
                curThread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        VideoView video = findViewById(R.id.video);
                        SeekBar bar = findViewById(R.id.curPosBar);
                        TextView text = findViewById(R.id.curPosTxt);
                        while (true) {
                            if (video.isPlaying()) {
                                bar.setProgress(video.getCurrentPosition());
                            }
                            runOnUiThread((new Runnable() {
                                @Override
                                public void run() {
                                    VideoView video = findViewById(R.id.video);
                                    TextView text = findViewById(R.id.curPosTxt);
                                    String time = (video.getCurrentPosition()/60000)+":";
                                    int sec=(video.getCurrentPosition()/1000)%60;
                                    if (sec<10) {
                                        time+="0";
                                    }
                                    time+=sec;
                                    text.setText(time.toString());
                                }
                            }));
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                break;
                            }
                        }
                    }
                });
                curThread.start();
            }
        });
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    video.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                video.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                video.start();
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageButton play = (ImageButton) view;
                if (!video.isPlaying()) {
                    play.setImageDrawable(getDrawable(android.R.drawable.ic_media_pause));
                    video.start();
                } else {
                    play.setImageDrawable(getDrawable(android.R.drawable.ic_media_play));
                    video.pause();
                }
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                video.pause();
                play.setImageDrawable(getDrawable(android.R.drawable.ic_media_pause));
                video.seekTo(0);
                bar.setProgress(0);
                text.setText("0:00");
            }
        });
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_URI, filename.toString());
        Log.d("FileName", filename.toString());
        outState.putInt(STATE_PROGRESS, bar.getProgress());
        super.onSaveInstanceState(outState);
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        if (curThread!=null) {
            curThread.interrupt();
        }
    }
}

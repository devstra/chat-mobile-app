package com.example.projet_app_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ImgViewerActivity extends AppCompatActivity {
    private Uri filename;
    private static final String STATE_URI = "URI";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_viewer);
        if (savedInstanceState!=null) {
            filename = Uri.parse(savedInstanceState.getString(STATE_URI));
        } else {
            filename = getIntent().getData();
        }
        ImageView img = findViewById(R.id.img);

        new DownloadImageTask(img).execute(filename.toString());
    }
    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView img;

        public DownloadImageTask(ImageView img) {
            this.img = img;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap imgBitmap = null;
            try {
                InputStream file = new URL(url).openStream();
                imgBitmap = BitmapFactory.decodeStream(file);
            } catch (Exception e) {
                Log.e("ImgViewer", "Impossible d'ouvrir");
                e.printStackTrace();
            }
            return imgBitmap;
        }

        protected void onPostExecute(Bitmap result) {
            img.setImageBitmap(result);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_URI, filename.toString());
        Log.d("FileName", filename.toString());
        super.onSaveInstanceState(outState);
    }
}

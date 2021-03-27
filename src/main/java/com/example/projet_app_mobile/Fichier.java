package com.example.projet_app_mobile;

import android.content.Intent;
import android.net.Uri;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

public class Fichier {

    private static final int REQUEST_FILE = 4;
    private AppCompatActivity context;
    Uri fileUri = null;

    public Fichier(AppCompatActivity context){
        this.context = context;
    }

    public void addFile(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(Uri.parse("/documents/document/"), "*/*");
        context.startActivityForResult(Intent.createChooser(intent, "Open folder"), REQUEST_FILE);
    }

    public void setFileUri(Uri fileUri){
        this.fileUri = fileUri;
    }
    public Uri getFileUri(){
        return fileUri;
    }

    public int getRequestFileOpen(){
        return REQUEST_FILE;
    }

}

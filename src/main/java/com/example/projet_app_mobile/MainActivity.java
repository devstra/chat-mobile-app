package com.example.projet_app_mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.VideoView;

import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.example.projet_app_mobile.pageAdapter.PageAdapter;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private TabLayout tablayout;
    private ViewPager viewpager;
    private TabItem channels,messages,profile;
    private Photo p;
    private Video v;
    private Audio a;
    private Fichier f;
    public PageAdapter pageradapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        p = new Photo(this);
        v = new Video(this);
        a = new Audio(this);
        f = new Fichier(this);

        tablayout = (TabLayout) findViewById(R.id.layout_mennu);
        channels = (TabItem) findViewById(R.id.channels);
        messages = (TabItem) findViewById(R.id.messages);
        profile = (TabItem) findViewById(R.id.profile);
        viewpager = findViewById(R.id.viewPager);
        tablayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.colorYellow));
        pageradapter = new PageAdapter(getSupportFragmentManager(),tablayout.getTabCount());
        viewpager.setAdapter(pageradapter);
        viewpager.setPageTransformer(true, new RotateUpTransformer());

        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());
                if(tab.getPosition() == 0){
                    pageradapter.notifyDataSetChanged();
                    tablayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.colorYellow));

                }
                else if(tab.getPosition() == 1){
                    pageradapter.notifyDataSetChanged();
                    tablayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.colorAccent));
                }
                else if(tab.getPosition() == 2){
                    pageradapter.notifyDataSetChanged();
                    tablayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.colorPrimary));
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));
    }
    //Affiche le menu pour sélectionner le type d'action que l'on veut faire :
    // -> ajouter un fichier depuis son téléphone
    // -> prendre une photo
    // -> faire une vidéo
    public void showMenu(View v){
        PopupMenu popup = new PopupMenu(this,v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.button_menu, popup.getMenu());
        popup.show();
    }

    public void takePhoto(MenuItem menuItem) {
        p.takePhoto();
    }

    public void takeVideo(MenuItem menuItem) {
        v.takeVideo();
    }

    public void recordAudio(MenuItem menuItem){
        a.recordAudio();
    }

    public void addMedia(MenuItem menuItem){
        f.addFile();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == p.getRequestTakePhoto() && resultCode == RESULT_OK) {
            //On créer une view pour afficher le média
            pageradapter.getMessagesTab().envoyerMessage(p.getPhotoURI(), "Cliquez pour voir l'image.");
        }
        if (requestCode == v.getRequestVideoCapture() && resultCode == RESULT_OK){
            v.setVideoUri(data.getData());
            pageradapter.getMessagesTab().envoyerMessage(v.getVideoUri(), "Cliquez pour voir la video.");
        }
        if (requestCode == f.getRequestFileOpen() && resultCode == RESULT_OK){
            f.setFileUri(data.getData());
            pageradapter.getMessagesTab().envoyerMessage(f.getFileUri(), "Cliquez pour télécharger le fichier.");
        }
    }
}

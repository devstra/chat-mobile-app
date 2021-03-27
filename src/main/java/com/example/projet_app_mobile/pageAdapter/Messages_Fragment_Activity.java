package com.example.projet_app_mobile.pageAdapter;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.projet_app_mobile.LoginActivity;
import com.example.projet_app_mobile.R;
import com.example.projet_app_mobile.conversations.Message;
import com.example.projet_app_mobile.conversations.MessagesAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class Messages_Fragment_Activity extends Fragment implements View.OnClickListener {
    // faudra changer l'id de la channel en fonction de la channel qui est affiché à l'écran
    private String channelId = "pr9nxiLA0PWU5QB9qXyL";
    private ImageButton btnEnvoyer;
    private EditText inputTextBox;
    private View view;
    private MessagesAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference messagesRef =  db.collection("channels")
            .document(channelId)
            .collection("messages");

    public Messages_Fragment_Activity() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_messages, container, false);
        setUpRecyclerView(view);
        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    /**
     * Envoie le message entré par l'utilisateur à la base de données
     */
    public void envoyerMessage(Uri mediaUri, String text) {
        Message m = new Message();
        m.setAuteur(LoginActivity.utilisateur.getInstance().getUsername());
        m.setMsg(text);
        m.setTimestamp(Timestamp.now());
        m.setMedia(null);
        // On efface le contenu de la textbox
        final Uri uriFile = mediaUri;
        // On l'envoie à la base de données et on vérifie que l'action s'est bien passée
        db.collection("channels")
                .document(channelId).collection("messages")
                .add(m).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.i("MESSAGE_SENT", "message envoyé!");
                if (uriFile!=null) {
                    try {
                        mediaUpload(uriFile, documentReference);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("MESSAGE_NOT_SENT", "Erreur! message non envoyé.");
            }
        });
    }
    /**
     * Envoie le message entré par l'utilisateur à la base de données
     */
    public void envoyerMessage(Uri mediaUri) {
        if (!inputTextBox.getText().toString().trim().isEmpty()) {
            envoyerMessage(mediaUri, inputTextBox.getText().toString());
        } else {
            Log.e("MSG_VIDE", "Erreur! Le message est vide.");
        }

    }

    public void mediaUpload(Uri file, DocumentReference m) throws IOException {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        Log.d("MediaUpload", file.toString());
        ContentResolver cR = getContext().getContentResolver();
        Log.d("MediaUpload", cR.getType(file));
        String[] fileSplit = cR.getType(file).split("/");
        final StorageReference fileRef = storageRef.child(channelId+"/"+m.getId()+"."+fileSplit[fileSplit.length-1]);
        UploadTask uploadTask = fileRef.putFile(file);
        final DocumentReference mUpdate = m;

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return fileRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    mUpdate.update("media", task.getResult().toString());
                } else {
                    Log.d("MediaDownload","ERREUR");
                }
            }
        });
    }

    private void setUpRecyclerView(View v){
        // Récupérer les messages de la channel depuis Firestore
        Query query = messagesRef.orderBy("timestamp", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Message> options =  new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .build();
        adapter = new MessagesAdapter(options);
        RecyclerView recyclerView = v.findViewById(R.id.recycler_messages_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        btnEnvoyer = view.findViewById(R.id.button_envoyer_msg);
        inputTextBox = view.findViewById(R.id.edittext_chatbox);
        inputTextBox.performClick();
        inputTextBox.setPressed(true);
        inputTextBox.invalidate();
        btnEnvoyer.setOnClickListener(this);
        Log.i("MSG","------------Fin de création---------------");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_envoyer_msg:
                envoyerMessage(null);
        }
    }
}

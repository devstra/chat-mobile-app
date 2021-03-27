package com.example.projet_app_mobile.conversations;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet_app_mobile.ImgViewerActivity;
import com.example.projet_app_mobile.MediaPlayerActivity;
import com.example.projet_app_mobile.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.SimpleDateFormat;

public class MessagesAdapter extends FirestoreRecyclerAdapter<Message, MessagesAdapter.MessageHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MessagesAdapter(@NonNull FirestoreRecyclerOptions<Message> options) {
        super(options);
    }

    @Override
    public void onBindViewHolder(final MessageHolder holder, int position, final Message model) {
        // Bind the Chat object to the ChatHolder
        Log.i("MSG","------------Attacher message---------------");
        holder.msg.setText(model.getMsg());
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String timestamp = dateFormat.format(model.getTimestamp().toDate());
        holder.timestamp.setText(timestamp);
        holder.auteur.setText(model.getAuteur());
        holder.msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.getMedia()!=null) {
                    Uri file = Uri.parse(model.getMedia());
                    Log.d("MediaViewer", file.toString());
                    String[] fileSplit = file.getPath().split("\\.");
                    String extension = fileSplit[fileSplit.length-1];
                    switch (extension) {
                        case "jpeg": case "jpg": case "png": case "bmp":
                            Intent intentImg = new Intent(holder.msg.getContext().getApplicationContext(), ImgViewerActivity.class);
                            intentImg.setData(file);
                            holder.msg.getContext().startActivity(intentImg);
                            break;
                        case "3gp": case "mp4": case "wav": case "ogg": case "mp3":
                            Intent intentMp = new Intent(holder.msg.getContext().getApplicationContext(), MediaPlayerActivity.class);
                            intentMp.setData(file);
                            holder.msg.getContext().startActivity(intentMp);
                            break;
                    };

                }
            }
        });
    }

    @Override
    public MessageHolder onCreateViewHolder(ViewGroup group, int viewType) {
        Log.i("MSG","------------Creation vue---------------");
        View viewT = LayoutInflater.from(group.getContext())
                .inflate(R.layout.item_message_received, group, false);
        return new MessageHolder(viewT);
    }
     class MessageHolder extends RecyclerView.ViewHolder {
        public TextView msg, timestamp, auteur;

        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.text_message_body);
            timestamp = itemView.findViewById(R.id.text_message_time);
            auteur = itemView.findViewById(R.id.text_message_sender);
        }
    }

}

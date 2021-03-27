package com.example.projet_app_mobile.channels;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet_app_mobile.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ChannelsAdapter extends FirestoreRecyclerAdapter<Channel, ChannelsAdapter.ChannelHolder> {
    //Permet d'update le texte de chaque item channel dans la liste des Channels
    @Override
    protected void onBindViewHolder(@NonNull ChannelHolder holder, int position, @NonNull Channel model) {
        holder.textViewNom.setText(model.getNomChannel());
        holder.textViewDescription.setText(model.getDescription());
        holder.textViewNbConnecter.setText(String.valueOf(model.getNbConnecter()));
    }

    //permet de dire quelle layout est impacter par les changements
    @NonNull
    @Override
    public ChannelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_item,parent,false);
        return new ChannelHolder(v);
    }

    class ChannelHolder extends RecyclerView.ViewHolder{
        TextView textViewNom;
        TextView textViewDescription;
        TextView textViewNbConnecter;

        public ChannelHolder(@NonNull View itemView) {
            super(itemView);
            textViewDescription = itemView.findViewById(R.id.channel_description_view);
            textViewNom = itemView.findViewById(R.id.channel_nom_view);
            textViewNbConnecter = itemView.findViewById(R.id.channel_nbConnecter_view);
        }
    }

    public ChannelsAdapter(@NonNull FirestoreRecyclerOptions options) {
        super(options);
    }
}

package com.example.projet_app_mobile.pageAdapter;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projet_app_mobile.R;
import com.example.projet_app_mobile.channels.Channel;
import com.example.projet_app_mobile.channels.ChannelsAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


/**
 * A simple {@link Fragment} subclass.
 */
public class Channels_Fragment_Activity extends Fragment{
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference channelsRef = db.collection("channels");

    private ChannelsAdapter adapter;
    private ViewPager pager;

    public Channels_Fragment_Activity() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_channels, container, false);
        setUpRecyclerView(view);
        return view;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setUpRecyclerView(View v){
        Query query = channelsRef;
        FirestoreRecyclerOptions<Channel> options = new FirestoreRecyclerOptions.Builder<Channel>().setQuery(query,Channel.class).build();
        adapter = new ChannelsAdapter(options);

        RecyclerView recyclerView = v.findViewById(R.id.channels_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);
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
}

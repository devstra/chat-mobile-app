package com.example.projet_app_mobile.pageAdapter;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.projet_app_mobile.AuthActivity;
import com.example.projet_app_mobile.R;
import com.example.projet_app_mobile.Utilisateur;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profile_Fragment_Activity extends Fragment implements View.OnClickListener {
    private View view;
    private Button btnDeconnexion;
    private String username;
    private String email;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Profile_Fragment_Activity() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        btnDeconnexion = (Button) view.findViewById(R.id.btnDeconnexion);
        TextView usernametxt = (TextView) view.findViewById(R.id.username);
        TextView emailTextView = (TextView) view.findViewById(R.id.email);

        usernametxt.setText(username);
        emailTextView.setText(email);
        btnDeconnexion.setOnClickListener(this);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username = Utilisateur.getInstance().getUsername();
        email = Utilisateur.getInstance().getEmail();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDeconnexion:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity().getApplicationContext(), AuthActivity.class);
                // Sert a détruire toutes les autres activity (qui demandent d'etre authentifié)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
                return;
        }
    }
}

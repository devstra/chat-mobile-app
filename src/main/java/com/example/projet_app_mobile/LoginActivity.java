package com.example.projet_app_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private EditText champsEmail, champsMotDePasse;
    private Button boutonConnexion;
    private ImageButton boutonRetour;
    private FirebaseAuth mAuth;
    public static Utilisateur utilisateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        isLoggedIn();

        champsEmail = findViewById(R.id.editTextEmail);
        champsMotDePasse = findViewById(R.id.editTextPassword);
        boutonConnexion = findViewById(R.id.buttonConnexion);

        champsMotDePasse.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //do what you want on the press of 'done'
                    boutonConnexion.performClick();
                }
                return false;
            }
        });

        boutonRetour = findViewById(R.id.btnClose);

        boutonConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = champsEmail.getText().toString().trim();
                String password = champsMotDePasse.getText().toString().trim();

                // Verification que les champs ne sont pas vides
                if (email.isEmpty() && password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Les champs ne doivent pas être vides.",
                            Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    champsMotDePasse.setError("Veuillez entrez un mot de passe.");
                    champsMotDePasse.requestFocus();
                } else if (email.isEmpty()) {
                    champsEmail.setError("Veuillez entrez une adresse email.");
                    champsEmail.requestFocus();
                } else if (!email.isEmpty() && !password.isEmpty()) {
                    signIn(email, password);
                }
            }
        });

        boutonRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // retour vers AuthActivity
                finish();
            }
        });
    }


    /**
     * Vérifie si l'utilisateur est connecté et le redirige vers la MainActivity si c'est le cas
     */
    private void isLoggedIn() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            getUtilisateurActuel();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            return;
        }
    }

    private void getUtilisateurActuel() {
        FirebaseFirestore.getInstance().collection("utilisateurs")
                .document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    utilisateur.getInstance().setEmail(task.getResult().get("email").toString());
                    utilisateur.getInstance().setUid(mAuth.getUid());
                    utilisateur.getInstance().setUsername(task.getResult().get("username").toString());
                }
            }
        });
    }

    /**
     * Permet d'authentifier un utilisateur auprès de la base de données Firebase
     *
     * @param email    : l'@ email de l'utilisateur
     * @param password : le mot de passe de l'utilisateur
     */
    protected void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("***ok", "signInWithEmail:success");

                    isLoggedIn();
                } else {
                    Log.w("***fail", "signInWithEmail:failure", task.getException());
                    Toast.makeText(LoginActivity.this, "Email ou mot de passe incorrect!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

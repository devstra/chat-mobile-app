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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    private EditText champsEmail, champsMotDePasse, champsConfirmMotDePasse;
    private Button boutonInscription;
    private ImageButton boutonRetour;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();

        isLoggedIn();

        champsEmail = findViewById(R.id.champsSignUpEmail);
        champsMotDePasse = findViewById(R.id.champsSignUpPassword);
        champsConfirmMotDePasse = findViewById(R.id.champsSignUpPasswordConfirm);
        boutonInscription = findViewById(R.id.boutonSignUp);

        champsConfirmMotDePasse.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //do what you want on the press of 'done'
                    boutonInscription.performClick();
                }
                return false;
            }
        });

        boutonRetour = findViewById(R.id.btnRetour);

        boutonInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = champsEmail.getText().toString().trim();
                String password = champsMotDePasse.getText().toString().trim();
                String passwordConfirm = champsConfirmMotDePasse.getText().toString().trim();

                // Verification que les champs sont valides
                if (email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Tous les champs doivent être remplis.",
                            Toast.LENGTH_SHORT).show();
                } else if (!password.equals(passwordConfirm)) {
                    Toast.makeText(SignUpActivity.this, "Les mots de passes doivent égaux.",
                            Toast.LENGTH_SHORT).show();
                } else if (password.equals(passwordConfirm) && password.length() >= 6) {
                    createAccount(email, password);
                } else {
                    // Firebase empeche la création d'un utilisateur sinon
                    Toast.makeText(SignUpActivity.this, "Le mot de passe doit contenir au moins 6 caractères.", Toast.LENGTH_SHORT).show();
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
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
            return;
        }
    }


    /**
     * Permet de creer un nouveau utilisateur dans la base de donnees
     *
     * @param email    : l'@ email que l'utilisateur a renseigne
     * @param password : le mot de passe que l'utilisateur a renseigne
     */
    private void createAccount(final String email, String password) {

        // On cré un nouveau utilisateur dans FirebaseAuth
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("***ok", "signInWithEmail:success");

                            // On ajoute egalement l'utiliseur dans la base de données Firestore
                            HashMap user = new HashMap();
                            user.put("email", email);
                            user.put("username", email.split("@")[0]);
                            FirebaseFirestore.getInstance().collection("utilisateurs").document(mAuth.getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("SUCCESS", "Utilisateur ajouté avec succes!");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("ERREUR", "Erreur! utilisateur pas ajouté a la base.");
                                }
                            });
                            isLoggedIn();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Erreur! Le compte n'a pas été créé.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });

    }
}

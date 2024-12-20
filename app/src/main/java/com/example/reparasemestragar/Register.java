package com.example.reparasemestragar;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registo);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        EditText nome = findViewById(R.id.et_name);
        EditText telefone = findViewById(R.id.et_phone);
        EditText email = findViewById(R.id.et_email);
        EditText pass = findViewById(R.id.et_password1);
        EditText pass2 = findViewById(R.id.et_confirm_password);
        Button cancel_btn = findViewById(R.id.btn_cancel);
        Button registar_btn = findViewById(R.id.btn_register1);

        //listener para o botão de entrar
        registar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomeStr = nome.getText().toString();
                //String telefoneStr = telefone.getText().toString();
                String emailStr = email.getText().toString();
                String passStr = pass.getText().toString();
                String confirmPassStr = pass2.getText().toString();


                //chamada do registrar com a condiçlão de que as passwords são iguais
                if (!TextUtils.isEmpty(emailStr) && !TextUtils.isEmpty(passStr) && !TextUtils.isEmpty(nomeStr) && passStr.equals(confirmPassStr)) {
                    registar(emailStr, passStr, nomeStr);
                } else {
                    Toast.makeText(getApplicationContext(), "Registo falhado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //listener para o botão de cancelar
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //funcao de registar
    public void registar(String email, String senha, String nome) {
        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext(), "Entra aqui", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(nome)
                                        .build();
                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "User profile updated.");
                                                    Toast.makeText(getApplicationContext(), "Registo Concluido.", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(Register.this, MainActivity.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Registo Falhou.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    }

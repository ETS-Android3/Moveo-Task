package com.giladmovieapp.moveonotesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class forgotPasswoed extends AppCompatActivity {

    private EditText emailEditText;
    private Button restPasswordButton;
    private ProgressBar progressBar;
    private FloatingActionButton backButton;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_passwoed);

        emailEditText = findViewById(R.id.forgotPasswordEmail);
        progressBar = findViewById(R.id.progressBar);
        restPasswordButton = findViewById(R.id.resetButton);
        backButton = findViewById(R.id.backToMainButton);

        auth = FirebaseAuth.getInstance();


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(forgotPasswoed.this , MainActivity.class));
            }
        });

        restPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }

    private  void  resetPassword(){
    String email = emailEditText.getText().toString().trim();

    if(email.isEmpty()){
    emailEditText.setError("Email is required!");
    emailEditText.requestFocus();
    return;

    }

    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        emailEditText.requestFocus();
        return;
    }

    progressBar.setVisibility(View.VISIBLE);
    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {

            if(task.isSuccessful()){
                Toast.makeText(forgotPasswoed.this , "Check your email to reset your password!" , Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(forgotPasswoed.this , "Try again! Something wrong happened!" , Toast.LENGTH_LONG).show();
            }

        }
    });

    }
}
package com.example.healthwise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    TextView btnForgotPassword, btnLogin;
    TextInputEditText etForgotPasswordEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        btnLogin = findViewById(R.id.tvGoBackToLogin);

        btnLogin.setOnClickListener(view ->{
            startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
        });

        //when forgot password button is clicked, get email and send to firebase auth
        btnForgotPassword.setOnClickListener(view -> {
                etForgotPasswordEmail = findViewById(R.id.etForgotPasswordEmail);
                //we get the email value, use trim to remove the whitespaces
                String email = etForgotPasswordEmail.getText().toString().trim();

                //if email is empty, return an error message, else pass it to firebase auth
                if(email.isEmpty()){
                    Toast.makeText(ForgotPasswordActivity.this, "Please input your email address", Toast.LENGTH_LONG).show();
                } else{
                    //use the inbuilt firebaseAuth method to send password reset email
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                            .addOnCompleteListener(task -> {

                                //if task is successful, pass toast message and finish the task, else return error message
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPasswordActivity.this, "Password Reset Link successfully", Toast.LENGTH_LONG).show();
                                    finish();
                                }else{
                                    Toast.makeText(ForgotPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                }

        });

    }
}
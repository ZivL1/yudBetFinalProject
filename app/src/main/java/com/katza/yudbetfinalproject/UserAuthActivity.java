package com.katza.yudbetfinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import kotlin.TuplesKt;

public class UserAuthActivity extends AppCompatActivity {

    Button btnRegister, btnSignIn;
    TextView tvUserAuthStatus, tvToArtistAuth;
    EditText etEmail, etPass;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_auth);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
            btnRegister = findViewById(R.id.btnRegisterUser);
            btnSignIn = findViewById(R.id.btnSignInUser);
            tvUserAuthStatus = findViewById(R.id.tvAuthUserStatus);
            tvToArtistAuth = findViewById(R.id.tvToArtistAuth);
            etEmail = findViewById(R.id.etEmailAuthUser);
            etPass = findViewById(R.id.etPassAuthUser);

            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    registerUser();
                }
            });
            
            btnSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    signInUser();
                }
            });

    }

    private void signInUser() {
        String email = etEmail.getText().toString();
        String password = etPass.getText().toString();
        if(email.isEmpty()|| password.isEmpty()){
            status("At least one of the fields is empty");
        }else{
            mAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    startActivity(new Intent(UserAuthActivity.this, MainActivity.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    status("email or password are incorrect");
                }
            });
        }


    }

    private void registerUser() {
        String email = etEmail.getText().toString();
        String password = etPass.getText().toString();

        if (password.isEmpty() || email.isEmpty()) {
            status("At least one of the fields is empty");
            return;
        }
        if(password.length()<8){
            status("Password must be at least 8 characters long");
        }
        mAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser user = mAuth.getCurrentUser();
                if(user != null){
                    startActivity(new Intent(UserAuthActivity.this, MainActivity.class)); // לזכור לשנות פה את המסך הבא למסך הכנסת פרטים למשתמש
                    finish();
                }else{
                    status("Error creating a new account");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                status("Error creating a new account");
                Log.d("AUTH_ERROR", e.toString());
            }
        });

    }

    private void status(String s) {
        tvUserAuthStatus.setText(s);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(UserAuthActivity.this, MainActivity.class));
            finish();
        }
    }
}
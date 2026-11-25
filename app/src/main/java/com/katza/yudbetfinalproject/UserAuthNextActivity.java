package com.katza.yudbetfinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserAuthNextActivity extends AppCompatActivity {

    Button btnFinishUserAuth;
    EditText etUsername, etFullName;
    DatePicker dpBirthDate;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    String USER_COLLECTION_NAME = "users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_auth_next);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    btnFinishUserAuth = findViewById(R.id.btnFinishUserAuth);
    etUsername = findViewById(R.id.etUsername);
    etFullName = findViewById(R.id.etFullName);
    dpBirthDate = findViewById(R.id.dpBirthDate);
    db = FirebaseFirestore.getInstance();
    mAuth = FirebaseAuth.getInstance();

    btnFinishUserAuth.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String username = etUsername.getText().toString();
            String fullname = etFullName.getText().toString();
            int birthday = dpBirthDate.getDayOfMonth();
            int birthmonth = dpBirthDate.getMonth();
            int birthyear = dpBirthDate.getYear();
            if(username.isEmpty()||fullname.isEmpty()){

                return;
            }
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("username", username);
            userMap.put("fullname", fullname);
            userMap.put("birthday", birthday);
            userMap.put("birthmonth",birthmonth);
            userMap.put("birthyear",birthyear);
            boolean isBand = false;
            userMap.put("isBand",isBand);
            FirebaseUser current = mAuth.getCurrentUser();
            if(current!=null){
                String uid = current.getUid();
                db.collection(USER_COLLECTION_NAME).document(uid).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UserAuthNextActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UserAuthNextActivity.this, MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserAuthNextActivity.this, "Error try again later", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }
    });

    }


}
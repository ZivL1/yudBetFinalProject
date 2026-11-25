package com.katza.yudbetfinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class BandAuthNextActivity extends AppCompatActivity {

    Button btnFinishBandAuth;
    EditText etLink, etAbout, etRecord, etCountry, etGenre, etNumOfAlbums, etFormationyear, etBandName;
    String USER_COLLECTION_NAME = "users";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_band_auth_next);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnFinishBandAuth = findViewById(R.id.btnFinishBandAuth);
        etLink = findViewById(R.id.etLink);
        etAbout = findViewById(R.id.etAbout);
        etRecord = findViewById(R.id.etRecordLabel);
        etCountry = findViewById(R.id.etCountry);
        etGenre = findViewById(R.id.etGenre);
        etNumOfAlbums = findViewById(R.id.etNumOfAlbums);
        etFormationyear = findViewById(R.id.etFormationyear);
        etBandName = findViewById(R.id.etBandName);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        btnFinishBandAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String link = etLink.getText().toString();
                String about = etAbout.getText().toString();
                String record = etRecord.getText().toString();
                String country = etCountry.getText().toString();
                String genre = etGenre.getText().toString();
                String numOfAlbums = etNumOfAlbums.getText().toString();
                String formationYear = etFormationyear.getText().toString();
                String bandName = etBandName.getText().toString();
                if(link.isEmpty()||about.isEmpty()||record.isEmpty()||country.isEmpty()||genre.isEmpty()||numOfAlbums.isEmpty()||formationYear.isEmpty()||bandName.isEmpty()){
                    Toast.makeText(BandAuthNextActivity.this, "at least one field is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("link",link);
                userMap.put("about",about);
                userMap.put("record",record);
                userMap.put("country",country);
                userMap.put("genre",genre);
                userMap.put("numOfAlbums",numOfAlbums);
                userMap.put("formationYear", formationYear);
                userMap.put("bandName", bandName);
                userMap.put("isBand",true);
                FirebaseUser current = mAuth.getCurrentUser();
                if(current!=null){
                    String uid = current.getUid();
                    db.collection(USER_COLLECTION_NAME).document(uid).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(BandAuthNextActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(BandAuthNextActivity.this, MainActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(BandAuthNextActivity.this, "Error try again later", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });
    }

}
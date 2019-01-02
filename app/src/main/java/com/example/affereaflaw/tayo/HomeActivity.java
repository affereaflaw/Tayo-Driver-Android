package com.example.affereaflaw.tayo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    private Button btnKeluar;
    private TextView txtNama, txtUsername, txtEmail, txtTtl, txtTumpang, txtGender;
    private String userKey;
    private FirebaseAuth auth;
    private DatabaseReference dbProfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        btnKeluar = (Button) findViewById(R.id.btn_logout);

        txtNama = (TextView) findViewById(R.id.profilNama);
        txtUsername = (TextView) findViewById(R.id.profilUsername);
        txtEmail = (TextView) findViewById(R.id.profilEmail);
        txtTtl = (TextView) findViewById(R.id.profilTtl);
        txtTumpang = (TextView) findViewById(R.id.profilTumpang);
        txtGender = (TextView) findViewById(R.id.profilGender);

        auth = FirebaseAuth.getInstance();

        userKey = auth.getCurrentUser().getUid();
        dbProfil = FirebaseDatabase.getInstance().getReference().child("Users");
        dbProfil.child(userKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nameView = (String) dataSnapshot.child("nama").getValue();
                String usernameView = (String) dataSnapshot.child("username").getValue();
                String emailView = (String) dataSnapshot.child("email").getValue();
                String ttlView = (String) dataSnapshot.child("ttl").getValue();
                String genderView = (String) dataSnapshot.child("gender").getValue();
                int tumpang = dataSnapshot.child("tumpang").getValue(Integer.class);
                String tumpangView = "Total Driving " + tumpang + " kali";

                txtNama.setText(nameView);
                txtUsername.setText(usernameView);
                txtEmail.setText(emailView);
                txtTtl.setText(ttlView);
                txtGender.setText(genderView);
                txtTumpang.setText(tumpangView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}

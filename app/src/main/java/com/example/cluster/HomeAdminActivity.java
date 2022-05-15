package com.example.cluster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeAdminActivity extends AppCompatActivity {

    private FirebaseAuth authProfile;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        getSupportActionBar().setTitle("Acceuil");


        progressBar = findViewById(R.id.progressbar_home);

        CardView cvEtud = findViewById(R.id.cardview_admin_etud);
        cvEtud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeAdminActivity.this,GestionProfsActivity.class);
                startActivity(intent);
            }
        });

        CardView cvProf = findViewById(R.id.cardview_admin_profs);
        cvProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeAdminActivity.this,GestionEtudActivity.class);
                startActivity(intent);
            }
        });

        CardView cvEmploi = findViewById(R.id.cardview_admin_emploi);
        cvEmploi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeAdminActivity.this,PlanningAdminActivity.class);
                startActivity(intent);
            }
        });

        CardView cvBiblio = findViewById(R.id.cardview_admin_biblio);
        cvBiblio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeAdminActivity.this,BookListActivity.class);
                startActivity(intent);
            }
        });

        CardView cvSettings = findViewById(R.id.cardview_admin_settings);
        cvSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeAdminActivity.this,EtudProfileActivity.class);
                startActivity(intent);
            }
        });

        CardView cvLogout = findViewById(R.id.cardview_admin_logout);
        cvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authProfile.signOut();
                Toast.makeText(HomeAdminActivity.this,"Déconnecté",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(HomeAdminActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
    }
}
package com.example.cluster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeProfActivity extends AppCompatActivity {

    private CardView cardViewLogout;
    private FirebaseAuth authProfile;
    private TextView tvPrenom, tvEmail;
    private ProgressBar progressBar;
    private String prenom,email;
    private CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_prof);

        getSupportActionBar().setTitle("Acceuil");

        tvPrenom = findViewById(R.id.tv_home_name_pr);
        tvEmail = findViewById(R.id.tv_home_email_pr);
        progressBar = findViewById(R.id.progressbar_home_pr);
        circleImageView = findViewById(R.id.iv_prof_photo);

        CardView cvProfile = findViewById(R.id.cardview_profile_prof);
        cvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeProfActivity.this,ProfProfileActivity.class);
                startActivity(intent);
            }
        });

        CardView cvEmploi = findViewById(R.id.cardview_emploi_prof);
        cvEmploi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeProfActivity.this,PlanningListActivity.class);
                startActivity(intent);
            }
        });

        CardView cvEtuds = findViewById(R.id.cardview_profs_prof);
        cvEtuds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeProfActivity.this,RecyclerEtudProfActivity.class);
                startActivity(intent);
            }
        });

        CardView cvBiblio = findViewById(R.id.cardview_biblio_prof);
        cvBiblio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeProfActivity.this,BookListActivity.class);
                startActivity(intent);
            }
        });

        CardView cvSettings = findViewById(R.id.cardview_settings_prof);
        cvSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeProfActivity.this,EtudProfileActivity.class);
                startActivity(intent);
            }
        });

        cardViewLogout = findViewById(R.id.cardview_logout_prof);
        cardViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authProfile.signOut();
                Toast.makeText(HomeProfActivity.this,"Déconnecté",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(HomeProfActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if (firebaseUser == null){
            Toast.makeText(HomeProfActivity.this, "Erreur! Les détails de l'étudiant ne sont pas disponible pour le moment",
                    Toast.LENGTH_SHORT).show();
        } else {
            //checkIfEmailVerified(firebaseUser);
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
            //finish();
        }
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        //Extracting user reference from  database for "Registered Users"
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Profs");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ProfDetails profDetails = snapshot.getValue(ProfDetails.class);
                if (profDetails != null){
                    prenom = profDetails.nom;
                    email = firebaseUser.getEmail();

                    tvPrenom.setText(prenom);
                    tvEmail.setText(email);

                    //set user image after user has  uploaded it
                    Uri uri = firebaseUser.getPhotoUrl();

                    //ImageViewr setImageURI() should not be used with regular URIs. So we are using Picasso
                    Picasso.with(HomeProfActivity.this).load(uri).into(circleImageView);
                } else {
                    Toast.makeText(HomeProfActivity.this,"Erreur!", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeProfActivity.this, "Erreur!",
                        Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
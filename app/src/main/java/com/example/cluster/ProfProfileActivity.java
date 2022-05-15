package com.example.cluster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

public class ProfProfileActivity extends AppCompatActivity {

    private TextView tvWelcome, tvNom, tvCne, tvDate, tvTele, tvEmail;
    private ProgressBar progressBar;
    private String nom,prenom,cne,date,tele,email;
    private ImageView imageView;

    private FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_profile);

        getSupportActionBar().setTitle("Profile");

        tvWelcome = findViewById(R.id.tv_show_welcome_prof);
        tvNom = findViewById(R.id.tv_show_nom_prof);

        tvTele = findViewById(R.id.tv_show_tele_prof);
        tvEmail = findViewById(R.id.tv_show_email_prof);
        progressBar = findViewById(R.id.progressbar_prof);

        //SetOnClickListener on ImageView to open UploadProfileEtudActivity
        imageView = findViewById(R.id.user_photo_prof);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfProfileActivity.this,UploadProfileEtudActivity.class);
                startActivity(intent);
            }
        });

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if (firebaseUser == null){
            Toast.makeText(ProfProfileActivity.this, "Erreur! Les détails de professeur ne sont pas disponible pour le moment",
                    Toast.LENGTH_SHORT).show();
        } else {
            //checkIfEmailVerified(firebaseUser);
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
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
                    nom = profDetails.nom;
                    prenom = profDetails.module;
                    tele = profDetails.tele;
                    email = firebaseUser.getEmail();

                    tvWelcome.setText("Bienvenue !");
                    tvNom.setText(nom);
                    tvTele.setText(tele);
                    tvEmail.setText(email);

                    //set user image after user has  uploaded it
                    Uri uri = firebaseUser.getPhotoUrl();

                    //ImageViewr setImageURI() should not be used with regular URIs. So we are using Picasso
                    Picasso.with(ProfProfileActivity.this).load(uri).into(imageView);
                } else {
                    Toast.makeText(ProfProfileActivity.this,"Erreur!", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfProfileActivity.this, "Erreur!",
                        Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    //Creating ActionBar Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate menu items
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //when any menu item is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_refresh){
            //Refresh activity
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);
        } else if (id == R.id.menu_acceuil){
            Intent intent = new Intent(ProfProfileActivity.this,HomeProfActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_update_profile){
            Intent intent = new Intent(ProfProfileActivity.this,UpdateProfileEtudActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_update_email){
            Intent intent = new Intent(ProfProfileActivity.this,UpdateEmailEtudActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_settings){
            Toast.makeText(ProfProfileActivity.this,"menu_settings",Toast.LENGTH_LONG).show();
        } else if (id == R.id.menu_change_password){
            Intent intent = new Intent(ProfProfileActivity.this,ChangePasswordEtudActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_delete_profile){
            Intent intent = new Intent(ProfProfileActivity.this,DeleteProfileEtudActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_logout){
            authProfile.signOut();
            Toast.makeText(ProfProfileActivity.this,"Déconnecté",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ProfProfileActivity.this,MainActivity.class);

            //Clear stack to prevent user coming back to UserProfileActivity  on pressing back button after Logging out
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(ProfProfileActivity.this,"Erreur!",Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
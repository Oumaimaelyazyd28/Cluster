package com.example.cluster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
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

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth authProfile;
    private TextView tvPrenom, tvEmail;
    private ProgressBar progressBar;
    private String prenom,email;
    private CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().setTitle("Acceuil");

        tvPrenom = findViewById(R.id.tv_home_name);
        tvEmail = findViewById(R.id.tv_home_email);
        progressBar = findViewById(R.id.progressbar_homeetu);
        circleImageView = findViewById(R.id.iv_etud_photo);

        CardView cvProfile = findViewById(R.id.cardview_profile);
        cvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,EtudProfileActivity.class);
                startActivity(intent);
            }
        });

        CardView cvEmploi = findViewById(R.id.cardview_emploi);
        cvEmploi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,PlanningListActivity.class);
                startActivity(intent);
            }
        });

        CardView cvProfs = findViewById(R.id.cardview_profs);
        cvProfs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,RecyclerProfEtudActivity.class);
                startActivity(intent);
            }
        });

        CardView cvBiblio = findViewById(R.id.cardview_biblio);
        cvBiblio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,BookListActivity.class);
                startActivity(intent);
            }
        });

        CardView cvSettings = findViewById(R.id.cardview_settings);
        cvSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,EtudProfileActivity.class);
                startActivity(intent);
            }
        });

        CardView cvLogout = findViewById(R.id.cardview_logout);
        cvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authProfile.signOut();
                Toast.makeText(HomeActivity.this,"Déconnecté",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if (firebaseUser == null){
            Toast.makeText(HomeActivity.this, "Erreur! Les détails de l'étudiant ne sont pas disponible pour le moment",
                    Toast.LENGTH_SHORT).show();
        } else {
            checkIfEmailVerified(firebaseUser);
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
            //finish();
        }


    }



    //Users coming to UserProfileActivity after successful registration
    private void checkIfEmailVerified(FirebaseUser firebaseUser) {
        if (!firebaseUser.isEmailVerified()){
            showAlertDialog();
        }
    }

    private void showAlertDialog() {
        //setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle("E-mail non vérifié");
        builder.setMessage("Veuillez vérifier votre e-mail. Vous ne pouvez pas vous connecter une prochaine fois sans vérification par e-mail.");

        //Open email apps if user clicks/taps Continue button
        builder.setPositiveButton("Continuez", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //To emaill app in new window and not within our app
                startActivity(intent);
                finish();
            }
        });

        //Create the AlertDialog
        AlertDialog alertDialog = builder.create();

        //Show the AlertDialog
        alertDialog.show();


    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        //Extracting user reference from  database for "Registered Users"
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readUserDetails != null){
                    prenom = readUserDetails.prenom;
                    email = firebaseUser.getEmail();

                    tvPrenom.setText(prenom);
                    tvEmail.setText(email);

                    //set user image after user has  uploaded it
                    Uri uri = firebaseUser.getPhotoUrl();

                    //ImageViewr setImageURI() should not be used with regular URIs. So we are using Picasso
                    Picasso.with(HomeActivity.this).load(uri).into(circleImageView);
                } else {
                    Toast.makeText(HomeActivity.this,"Erreur!", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, "Erreur!",
                        Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
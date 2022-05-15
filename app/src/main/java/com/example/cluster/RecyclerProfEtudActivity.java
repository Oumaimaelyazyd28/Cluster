package com.example.cluster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RecyclerProfEtudActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MainAdapterProfEtud mainAdapterProfEtud;
    private RelativeLayout relativeLayout;
    private FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_prof_etud);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        recyclerView = findViewById(R.id.rv_list_pr);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<MainModel> options =
                new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Registered Profs"), MainModel.class)
                        .build();

        mainAdapterProfEtud = new MainAdapterProfEtud(options);
        recyclerView.setAdapter(mainAdapterProfEtud);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainAdapterProfEtud.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainAdapterProfEtud.stopListening();
    }




}
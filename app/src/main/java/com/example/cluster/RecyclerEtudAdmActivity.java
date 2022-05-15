package com.example.cluster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class RecyclerEtudAdmActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MainAdapterEtud mainAdapterEtud;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_etud_adm);

        recyclerView = findViewById(R.id.rv_list_etud);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<MainEtudModel> options =
                new FirebaseRecyclerOptions.Builder<MainEtudModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Registered users"), MainEtudModel.class)
                        .build();

        mainAdapterEtud = new MainAdapterEtud(options);
        recyclerView.setAdapter(mainAdapterEtud);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainAdapterEtud.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainAdapterEtud.stopListening();
    }
}
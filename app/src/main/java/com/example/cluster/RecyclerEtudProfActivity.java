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
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RecyclerEtudProfActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MainAdapterEtudProf mainAdapterEtudProf;
    private RelativeLayout relativeLayout;
    private FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_etud_prof);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        recyclerView = findViewById(R.id.rv_list_prof_etud);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<MainEtudModel> options =
                new FirebaseRecyclerOptions.Builder<MainEtudModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Registered users"), MainEtudModel.class)
                        .build();

        mainAdapterEtudProf = new MainAdapterEtudProf(options);
        recyclerView.setAdapter(mainAdapterEtudProf);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainAdapterEtudProf.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainAdapterEtudProf.stopListening();
    }

    //Creating ActionBar Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate menu items
        getMenuInflater().inflate(R.menu.prof_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //when any menu item is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_acceuil_prof){
            Intent intent = new Intent(RecyclerEtudProfActivity.this,HomeProfActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_etud_prof){
            Intent intent = new Intent(RecyclerEtudProfActivity.this,GestionEtudActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_bib_prof){
            Intent intent = new Intent(RecyclerEtudProfActivity.this,BookListActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_settings){
            Toast.makeText(RecyclerEtudProfActivity.this,"menu_settings",Toast.LENGTH_LONG).show();
        } else if (id == R.id.menu_profile_prof){
            Intent intent = new Intent(RecyclerEtudProfActivity.this,ProfProfileActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_logout_prof){
            authProfile.signOut();
            Toast.makeText(RecyclerEtudProfActivity.this,"Déconnecté",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(RecyclerEtudProfActivity.this,MainActivity.class);

            //Clear stack to prevent user coming back to UserProfileActivity  on pressing back button after Logging out
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(RecyclerEtudProfActivity.this,"Erreur!",Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
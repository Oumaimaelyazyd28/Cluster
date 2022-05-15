package com.example.cluster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class BookListActivity extends AppCompatActivity {

    RecyclerView recview;
    BookAdapter adapter;
    FloatingActionButton floatingActionButton;
    private FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        recview = (RecyclerView) findViewById(R.id.rv_list_book);
        recview.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<BookModel> options =
                new FirebaseRecyclerOptions.Builder<BookModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Books"), BookModel.class)
                        .build();
        adapter = new BookAdapter(options);
        recview.setAdapter(adapter);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AddBookActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
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
            Intent intent = new Intent(BookListActivity.this,HomeProfActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_etud_prof){
            Intent intent = new Intent(BookListActivity.this,GestionEtudActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_bib_prof){
            Intent intent = new Intent(BookListActivity.this,BookListActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_settings){
            Toast.makeText(BookListActivity.this,"menu_settings",Toast.LENGTH_LONG).show();
        } else if (id == R.id.menu_profile_prof){
            Intent intent = new Intent(BookListActivity.this,ProfProfileActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_logout_prof){
            authProfile.signOut();
            Toast.makeText(BookListActivity.this,"Déconnecté",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(BookListActivity.this,MainActivity.class);

            //Clear stack to prevent user coming back to UserProfileActivity  on pressing back button after Logging out
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(BookListActivity.this,"Erreur!",Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
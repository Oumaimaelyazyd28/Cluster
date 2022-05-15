package com.example.cluster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GestionProfsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_profs);

        getSupportActionBar().setTitle("Gestion");

        Button buttonList = findViewById(R.id.btn_list_prof);
        buttonList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GestionProfsActivity.this,RecyclerActivity.class);
                startActivity(intent);
            }
        });

        Button buttonAdd = findViewById(R.id.btn_add_prof);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GestionProfsActivity.this, AddProfActivity.class);
                startActivity(intent);
            }
        });
    }
}
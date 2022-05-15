package com.example.cluster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GestionEtudActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_etud);

        getSupportActionBar().setTitle("Gestion");

        Button buttonList = findViewById(R.id.btn_list_etud);
        buttonList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GestionEtudActivity.this,RecyclerEtudAdmActivity.class);
                startActivity(intent);
            }
        });

        Button buttonAdd = findViewById(R.id.btn_add_etud);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GestionEtudActivity.this, AddEtudActivity.class);
                startActivity(intent);
            }
        });
    }
}
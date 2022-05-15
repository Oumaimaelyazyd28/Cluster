package com.example.cluster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseUserActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_user);

        Button buttonProf = findViewById(R.id.btn_prof);
        Button buttonEtud = findViewById(R.id.btn_etud);
        Button buttonAdm = findViewById(R.id.btn_admin);

        buttonAdm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseUserActivity.this,LoginAdmActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseUserActivity.this,LoginProfActivity.class);
                startActivity(intent);
            }
        });

        buttonEtud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseUserActivity.this,LoginEtudActivity.class);
                startActivity(intent);
            }
        });

    }
}
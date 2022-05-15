package com.example.cluster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PlanningAdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning_admin);

        Button btnAdd = (Button) findViewById(R.id.btn_add_plan);
        Button btnshow = (Button) findViewById(R.id.btn_show_plan);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlanningAdminActivity.this,AddPlanningActivity.class);
                startActivity(intent);
            }
        });

        btnshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlanningAdminActivity.this,PlanningListActivity.class);
                startActivity(intent);
            }
        });

    }
}
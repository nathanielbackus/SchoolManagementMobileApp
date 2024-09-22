package com.example.c195mobileapp.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.c195mobileapp.R;

public class MainMenuController extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenuactivity);

        Button TermsButton = findViewById(R.id.TermsButton);
        TermsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuController.this, TermsController.class);
                startActivity(intent);
            }
        });

        Button CoursesButton = findViewById(R.id.CoursesButton);
        CoursesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuController.this, CoursesController.class);
                startActivity(intent);
            }
        });

        Button AssessmentsButton = findViewById(R.id.AssessmentsButton);
        AssessmentsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuController.this, AssessmentsController.class);
                startActivity(intent);
            }
        });
    }
}

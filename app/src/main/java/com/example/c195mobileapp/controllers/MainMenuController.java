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
                Intent intent = new Intent(MainMenuController.this, TermController.class);
                startActivity(intent);
            }
        });

        Button CoursesButton = findViewById(R.id.CoursesButton);
        CoursesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuController.this, CourseController.class);
                startActivity(intent);
            }
        });

        Button AssessmentsButton = findViewById(R.id.AssessmentsButton);
        AssessmentsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuController.this, AssessmentController.class);
                startActivity(intent);
            }
        });

        Button NotesButton = findViewById(R.id.NotesButton);
        NotesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuController.this, NoteController.class);
                startActivity(intent);
            }
        });

        Button MentorsButton = findViewById(R.id.MentorsButton);
        MentorsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuController.this, MentorController.class);
                startActivity(intent);
            }
        });
    }
}

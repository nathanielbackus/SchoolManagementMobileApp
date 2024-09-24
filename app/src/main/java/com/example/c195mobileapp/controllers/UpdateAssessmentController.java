package com.example.c195mobileapp.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.c195mobileapp.model.AssessmentModel;
import com.example.c195mobileapp.database.DataBaseHelper;
import com.example.c195mobileapp.R;

public class UpdateAssessmentController extends AppCompatActivity {
    Button BackButton, UpdateAssessmentButton;
    EditText editName, editStart, editEnd;
    Switch switchPerfObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.updateassessmentsactivity);

        // Get references to the UI components
        editName = findViewById(R.id.editName);
        editStart = findViewById(R.id.editStart);
        editEnd = findViewById(R.id.editEnd);
        switchPerfObj = findViewById(R.id.switchPerfObj);
        UpdateAssessmentButton = findViewById(R.id.UpdateAssessmentButton);
        BackButton = findViewById(R.id.BackButton);

        // Get the intent data from the AssessmentsController
        Intent intent = getIntent();
        int assessmentId = intent.getIntExtra("assessmentId", -1);
        String assessmentTitle = intent.getStringExtra("assessmentTitle");
        String assessmentStart = intent.getStringExtra("assessmentStart");
        String assessmentEnd = intent.getStringExtra("assessmentEnd");
        boolean assessmentType = intent.getBooleanExtra("assessmentType", false);

        // Set the data into the UI components
        editName.setText(assessmentTitle);
        editStart.setText(assessmentStart);
        editEnd.setText(assessmentEnd);
        switchPerfObj.setChecked(assessmentType);


        // Back button action to go back to the AssessmentsController
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateAssessmentController.this, AssessmentController.class);
                startActivity(intent);
            }
        });

        // Set up the update button action
        UpdateAssessmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Your update logic goes here
                AssessmentModel updatedAssessment = new AssessmentModel(
                        assessmentId,
                        editName.getText().toString(),
                        editStart.getText().toString(),
                        editEnd.getText().toString(),
                        switchPerfObj.isChecked()
                );

                DataBaseHelper dbHelper = new DataBaseHelper(UpdateAssessmentController.this);
                boolean success = dbHelper.updateAssessment(updatedAssessment); // Assuming you have an update method

                Toast.makeText(UpdateAssessmentController.this, "Update Success: " + success, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
package com.example.c195mobileapp.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.c195mobileapp.model.AssessmentModel;
import com.example.c195mobileapp.database.DataBaseHelper;
import com.example.c195mobileapp.R;
import com.example.c195mobileapp.database.AssessmentDAO;

public class AssessmentDetailController extends AppCompatActivity {
    Button BackButton, EditAssessmentButton;
    EditText editName, editStart, editEnd;
    boolean perfOrObjBool;
    Switch switchPerfObj;
    AssessmentDAO assessmentDAO;
    int assessmentID = -1;
    TextView Header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.assessmentdetailactivity);

        BackButton = findViewById(R.id.BackButton);
        EditAssessmentButton = findViewById(R.id.EditAssessmentButton);
        editName = findViewById(R.id.editName);
        editStart = findViewById(R.id.editStart);
        editEnd = findViewById(R.id.editEnd);
        switchPerfObj = findViewById(R.id.switchPerfObj);
        Header = findViewById(R.id.Header);

        DataBaseHelper dbHelper = new DataBaseHelper(AssessmentDetailController.this);
        assessmentDAO = new AssessmentDAO(dbHelper);

        Intent intent = getIntent();
        assessmentID = intent.getIntExtra("assessmentId", -1);
        if (assessmentID != -1) {
            String assessmentTitle = intent.getStringExtra("assessmentTitle");
            String assessmentStart = intent.getStringExtra("assessmentStart");
            String assessmentEnd = intent.getStringExtra("assessmentEnd");
            boolean assessmentType = intent.getBooleanExtra("assessmentType", false);

            editName.setText(assessmentTitle);
            editStart.setText(assessmentStart);
            editEnd.setText(assessmentEnd);
            switchPerfObj.setChecked(assessmentType);

            EditAssessmentButton.setText("Update");
            Header.setText("Update Assessment");
        } else {
            EditAssessmentButton.setText("Add");
            Header.setText("Add Assessment");
        }

        BackButton.setOnClickListener(view ->  {
            Intent intent1 = new Intent(AssessmentDetailController.this, AssessmentController.class);
            startActivity(intent1);
        });

        EditAssessmentButton.setOnClickListener(view ->  {
            String title = editName.getText().toString();
            String start = editStart.getText().toString();
            String end = editEnd.getText().toString();
            boolean type = switchPerfObj.isChecked();

            if (assessmentID != -1) {
                AssessmentModel updatedAssessment = new AssessmentModel(assessmentID, title, start, end, type);
                boolean success = assessmentDAO.updateAssessment(updatedAssessment);
                if (success) {
                    Intent intent2 = new Intent(AssessmentDetailController.this, AssessmentController.class);
                    startActivity(intent2);
                } else {
                    Toast.makeText(AssessmentDetailController.this, "Failed to update assessment", Toast.LENGTH_SHORT).show();
                }
            } else {
                AssessmentModel newAssessment = new AssessmentModel(-1, title, start, end, type);
                boolean success = assessmentDAO.addAssessment(newAssessment);
                if (success) {
                    Intent intent3 = new Intent(AssessmentDetailController.this, AssessmentController.class);
                    startActivity(intent3);
                } else {
                    Toast.makeText(AssessmentDetailController.this, "Failed to add assessment", Toast.LENGTH_SHORT).show();
                }
            }
        });

        EditAssessmentButton.setOnClickListener(view -> {
            String title = editName.getText().toString();
            String start = editStart.getText().toString();
            String end = editEnd.getText().toString();
            boolean isObjective = switchPerfObj.isChecked();

            if (assessmentID != -1) {
                // Update existing assessment
                AssessmentModel updatedAssessment = new AssessmentModel(assessmentID, title, start, end, isObjective);
                boolean success = assessmentDAO.updateAssessment(updatedAssessment);
                if (success) {
                    Toast.makeText(AssessmentDetailController.this, "Assessment updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AssessmentDetailController.this, "Failed to update assessment", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Add new assessment
                AssessmentModel newAssessment = new AssessmentModel(-1, title, start, end, isObjective);
                boolean success = assessmentDAO.addAssessment(newAssessment);
                if (success) {
                    Toast.makeText(AssessmentDetailController.this, "Assessment added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AssessmentDetailController.this, "Failed to add assessment", Toast.LENGTH_SHORT).show();
                }
            }

            // After success, go back to AssessmentController
            Intent intent1 = new Intent(AssessmentDetailController.this, AssessmentController.class);
            startActivity(intent1);
        });
    }
}

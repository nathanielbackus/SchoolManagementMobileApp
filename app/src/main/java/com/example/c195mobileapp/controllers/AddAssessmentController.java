package com.example.c195mobileapp.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Button;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.c195mobileapp.model.AssessmentModel;
import com.example.c195mobileapp.database.DataBaseHelper;
import com.example.c195mobileapp.R;
import com.example.c195mobileapp.database.AssessmentDAO;

public class AddAssessmentController extends AppCompatActivity {
    Button BackButton, AddAssessmentButton;
    EditText editName, editStart, editEnd;
    boolean perfOrObjBool;
    Switch switchPerfObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.addassessmentsactivity);

        BackButton = findViewById(R.id.BackButton);
        AddAssessmentButton = findViewById(R.id.AddAssessmentButton);
        editName = findViewById(R.id.editName);
        editStart = findViewById(R.id.editStart);
        editEnd = findViewById(R.id.editEnd);
        switchPerfObj = findViewById(R.id.switchPerfObj);

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddAssessmentController.this, AssessmentController.class);
                startActivity(intent);
            }
        });
        AddAssessmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AssessmentModel assessmentModel;
                try {
                    assessmentModel = new AssessmentModel(-1, editName.getText().toString(),editStart.getText().toString(),editEnd.getText().toString(), switchPerfObj.isChecked());

                } catch (Exception e){
                    Toast.makeText(AddAssessmentController.this, "Error Creating Assessment", Toast.LENGTH_SHORT).show();
                    assessmentModel = new AssessmentModel(-1, "error", "error","error",false);

                }
                Toast.makeText(AddAssessmentController.this, assessmentModel.toString(), Toast.LENGTH_SHORT).show();
                DataBaseHelper dataBaseHelper = new DataBaseHelper(AddAssessmentController.this);
                boolean success = AssessmentDAO.addAssessment(assessmentModel);

                Toast.makeText(AddAssessmentController.this, "Success= " + success, Toast.LENGTH_SHORT).show();
                //0 = false = performance          1 = true = objective
            }
        });

    }
}

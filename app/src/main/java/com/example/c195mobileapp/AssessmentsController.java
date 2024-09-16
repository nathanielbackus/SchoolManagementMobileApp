package com.example.c195mobileapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AssessmentsController extends AppCompatActivity {
    //declare variables
    Button ToAddAssessmentActivity, BackButton;
    ListView assessmentListView;
    ArrayAdapter appointmentArrayAdapter;
    DataBaseHelper dataBaseHelper;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        //set layout
        setContentView(R.layout.assessmentsactivity);
        assessmentListView = findViewById(R.id.assessmentListView);

        //get data from database
        dataBaseHelper = new DataBaseHelper(AssessmentsController.this);
        List<AssessmentModel> allAppointments = dataBaseHelper.getAllAppointments();

        //display data in list view
        appointmentArrayAdapter = new ArrayAdapter<AssessmentModel>(AssessmentsController.this, android.R.layout.simple_list_item_1, allAppointments);
        if (allAppointments == null || allAppointments.isEmpty()) {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        } else {
            for (AssessmentModel model : allAppointments) {
                Log.d("AssessmentsController", "Appointment: " + model.toString());
            }
        }
        assessmentListView.setAdapter(appointmentArrayAdapter);

        //click go back to main menu
        BackButton = findViewById(R.id.BackButton);
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AssessmentsController.this, MainMenuController.class);
                startActivity(intent);
            }
        });

        //click go to add assessment activity
        ToAddAssessmentActivity = (Button) findViewById(R.id.ToAddAssessmentActivity);
        ToAddAssessmentActivity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(AssessmentsController.this, AddAssessmentsController.class);
                startActivity(intent);
            }
        });

        //click go to update assessment activity
        assessmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Create an Intent to start the UpdateAssessmentsController
                Intent intent = new Intent(AssessmentsController.this, UpdateAssessmentsController.class);
                // Get the clicked AssessmentModel from the list
                AssessmentModel selectedAssessment = (AssessmentModel) parent.getItemAtPosition(position);
                // Pass the selected assessment data to the next activity
                intent.putExtra("assessmentId", selectedAssessment.getAssessmentID());
                intent.putExtra("assessmentTitle", selectedAssessment.getAssessmentTitle());
                intent.putExtra("assessmentStart", selectedAssessment.getAssessmentStart());
                intent.putExtra("assessmentEnd", selectedAssessment.getAssessmentEnd());
                intent.putExtra("assessmentType", selectedAssessment.getAssessmentType());

                // Start the activity
                startActivity(intent);
            }
        });



    }
}

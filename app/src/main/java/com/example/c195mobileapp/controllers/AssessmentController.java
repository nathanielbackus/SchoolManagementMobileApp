package com.example.c195mobileapp.controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.c195mobileapp.database.AssessmentDAO;
import com.example.c195mobileapp.model.AssessmentModel;
import com.example.c195mobileapp.database.DataBaseHelper;
import com.example.c195mobileapp.R;

import java.util.ArrayList;
import java.util.List;

public class AssessmentController extends AppCompatActivity {
    // Declare variables
    Button ToAddAssessmentActivity, BackButton;
    ListView assessmentListView;
    ArrayAdapter<SpannableString> appointmentArrayAdapter; // ArrayAdapter with SpannableString
    AssessmentDAO assessmentDAO;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.assessmentsactivity);
        assessmentListView = findViewById(R.id.assessmentListView);
        DataBaseHelper dbHelper = new DataBaseHelper(AssessmentController.this);
        assessmentDAO = new AssessmentDAO(dbHelper);
        List<AssessmentModel> allAppointments = assessmentDAO.getAllAssessments();
        List<SpannableString> formattedAssessments = new ArrayList<>();

        for (AssessmentModel model : allAppointments) {
            String text = "Assessment Title: " + model.getAssessmentTitle() + "\n" +
                    "Assessment Start: " + model.getAssessmentStart() + "\n" +
                    "Assessment End: " + model.getAssessmentEnd();

            SpannableString spannableString = new SpannableString(text);
            spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            int titleLength = model.getAssessmentTitle().length();
            spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 17, 17 + titleLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            formattedAssessments.add(spannableString);
        }

        //toast a warning if no data found, also set adapters
        appointmentArrayAdapter = new ArrayAdapter<>(AssessmentController.this, android.R.layout.simple_list_item_1, formattedAssessments);
        if (allAppointments == null || allAppointments.isEmpty()) {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        } else {
            for (AssessmentModel model : allAppointments) {
                Log.d("AssessmentController", "Appointment: " + model.toString());
            }
        }
        assessmentListView.setAdapter(appointmentArrayAdapter);

        // Click to go back to main menu
        BackButton = findViewById(R.id.BackButton);
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AssessmentController.this, MainMenuController.class);
                startActivity(intent);
            }
        });

        // Click to go to add assessment activity
        ToAddAssessmentActivity = (Button) findViewById(R.id.ToAddAssessmentActivity);
        ToAddAssessmentActivity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(AssessmentController.this, AssessmentDetailController.class);
                startActivity(intent);
            }
        });

        // Click to go to update assessment activity
        assessmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Create an Intent to start the AssessmentDetailController
                Intent intent = new Intent(AssessmentController.this, AssessmentDetailController.class);
                // Get the clicked AssessmentModel from the list
                AssessmentModel selectedAssessment = allAppointments.get(position);
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

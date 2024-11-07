package com.example.c195mobileapp.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.c195mobileapp.R;
import com.example.c195mobileapp.database.AssessmentDAO;
import com.example.c195mobileapp.database.DataBaseHelper;
import com.example.c195mobileapp.database.MentorDAO;
import com.example.c195mobileapp.model.AssessmentModel;
import com.example.c195mobileapp.model.CourseModel;
import com.example.c195mobileapp.model.MentorModel;
import com.example.c195mobileapp.database.CourseDAO;

import java.util.ArrayList;
import java.util.List;

public class CourseDetailController extends AppCompatActivity {

    ListView assessmentListView;
    EditText editStart, editEnd, editName;
    Button BackButton, AddCourseButton;
    Spinner mentorSpinner;
    MentorDAO mentorDAO;
    AssessmentDAO assessmentDAO;
    ArrayAdapter<SpannableString> mentorArrayAdapter;
    ArrayAdapter<SpannableString> assessmentArrayAdapter;
    RadioGroup statusRG;
    int courseID = -1;
    CourseDAO courseDAO;
    TextView Header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.coursedetailactivity);
        editName = findViewById(R.id.editName);
        editStart = findViewById(R.id.editStart);
        editEnd = findViewById(R.id.editEnd);

        //BACK BUTTON
        BackButton = findViewById(R.id.BackButton);
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseDetailController.this, CourseController.class);
                startActivity(intent);
            }
        });

        //MENTOR STUFF
        mentorSpinner = findViewById(R.id.mentorSpinner);
        DataBaseHelper dbHelper = new DataBaseHelper(CourseDetailController.this);
        mentorDAO = new MentorDAO(dbHelper);
        List<MentorModel> allMentors = mentorDAO.getAllMentors();
        List<SpannableString> spinnerMentorList = new ArrayList<>();
        for (MentorModel model : allMentors){
            SpannableString spannableString = new SpannableString(model.getMentorName());
            spinnerMentorList.add(spannableString);
        }
        mentorArrayAdapter = new ArrayAdapter<>(CourseDetailController.this, android.R.layout.simple_spinner_item, spinnerMentorList);
        mentorArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mentorSpinner.setAdapter(mentorArrayAdapter);


        //ASSESSMENT LIST VIEW STUFF
        assessmentListView = findViewById(R.id.assessmentListView);
        assessmentDAO = new AssessmentDAO(dbHelper);
        List<AssessmentModel> allAssessments = assessmentDAO.getAllAssessments();
        List<SpannableString> assessmentList = new ArrayList<>();
        for (AssessmentModel model : allAssessments) {
            SpannableString spannableString = new SpannableString(model.getAssessmentTitle());
            assessmentList.add(spannableString);
        }
        assessmentArrayAdapter = new ArrayAdapter<>(CourseDetailController.this, android.R.layout.simple_list_item_multiple_choice, assessmentList);
        assessmentListView.setAdapter(assessmentArrayAdapter);


        statusRG = findViewById(R.id.statusRG);
        Intent intent = getIntent();
        courseID = intent.getIntExtra("courseId", -1);


        //populate data stuff
        if (courseID != -1) {
            String courseTitle = intent.getStringExtra("courseTitle");
            String courseStart = intent.getStringExtra("courseStart");
            String courseEnd = intent.getStringExtra("courseEnd");
            String status = intent.getStringExtra("status"); //make it so the correct status is clicked
            String mentor = intent.getStringExtra("mentor"); // make it so the existing mentor is selected in the spinner


            editName.setText(courseTitle);
            editStart.setText(courseStart);
            editEnd.setText(courseEnd);

            AddCourseButton.setText("Update");
            Header.setText("Update Course");
        }


        AddCourseButton = findViewById(R.id.AddCourseButton);
        AddCourseButton.setOnClickListener(view -> {
            String courseTitle = editName.getText().toString();
            String start = editStart.getText().toString();
            String end = editEnd.getText().toString();
            int status = statusRG.getCheckedRadioButtonId(); // do we need extra stuff i think we can just store as an int
            int mentor = ((MentorModel) mentorSpinner.getSelectedItem()).getMentorID();
            SparseBooleanArray checkedItems = assessmentListView.getCheckedItemPositions();
            List<Integer> associatedAssessmentIDs = new ArrayList<>();

            for (int i = 0; i < assessmentListView.getCount(); i++) {
                if (checkedItems.get(i)) {
                    AssessmentModel selectedAssessment = (AssessmentModel) assessmentListView.getItemAtPosition(i);
                    associatedAssessmentIDs.add(selectedAssessment.getAssessmentID());
                }
            }

            //update course stuff
            if (courseID != -1) {
                CourseModel updatedCourse = new CourseModel(courseID, courseTitle, start, end, status, mentor, associatedAssessmentIDs);
                boolean success = courseDAO.updateCourse(updatedCourse);
                if (success) {
                    Intent intent2 = new Intent(CourseDetailController.this, CourseController.class);
                    startActivity(intent2);
                } else {
                    Toast.makeText(CourseDetailController.this, "Failed to update course", Toast.LENGTH_SHORT).show();
                }
                //add course stuff
            } else {
                CourseModel newCourse = new CourseModel(-1, courseTitle, start, end, status, mentor, associatedAssessmentIDs);
                boolean success = courseDAO.addCourse(newCourse);
                if (success) {
                    Intent intent3 = new Intent(CourseDetailController.this, CourseController.class);
                    startActivity(intent3);
                } else {
                    Toast.makeText(CourseDetailController.this, "Failed to add course", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }
}
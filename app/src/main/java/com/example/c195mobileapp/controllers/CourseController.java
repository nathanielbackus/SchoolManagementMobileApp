package com.example.c195mobileapp.controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.c195mobileapp.database.AssessmentDAO;
import com.example.c195mobileapp.model.AssessmentModel;
import com.example.c195mobileapp.model.CourseModel;
import com.example.c195mobileapp.database.DataBaseHelper;
import com.example.c195mobileapp.database.CourseDAO;
import com.example.c195mobileapp.R;

import java.util.ArrayList;
import java.util.List;

public class CourseController extends AppCompatActivity {
    Button ToAddCourseActivity, BackButton;
    ListView courseListView;
    ArrayAdapter<SpannableString> courseArrayAdapter; // ArrayAdapter with SpannableString
    DataBaseHelper dataBaseHelper;
    CourseDAO courseDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.coursesactivity);
        courseListView = findViewById(R.id.courseListView);
        DataBaseHelper dbHelper = new DataBaseHelper(CourseController.this);
        courseDAO = new CourseDAO(dbHelper);

        dataBaseHelper = new DataBaseHelper(CourseController.this);
        List<CourseModel> allCourses = courseDAO.getAllCourses();

        List<SpannableString> formattedCourses = new ArrayList<>();
        for (CourseModel model : allCourses) {
            String text = "Course Title: " + model.getCourseTitle() + "\n" +
                    "Course Start: " + model.getCourseStart() + "\n" +
                    "Course End: " + model.getCourseEnd();
            SpannableString spannableString = new SpannableString(text);
            spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            formattedCourses.add(spannableString);
        }

        courseArrayAdapter = new ArrayAdapter<>(CourseController.this, android.R.layout.simple_list_item_1, formattedCourses);
        courseListView.setAdapter(courseArrayAdapter);

        BackButton = findViewById(R.id.BackButton);
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseController.this, MainMenuController.class);
                startActivity(intent);
            }
        });

        ToAddCourseActivity = findViewById(R.id.ToAddCourseActivity);
        ToAddCourseActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseController.this, CourseDetailController.class);
                startActivity(intent);
            }
        });

        courseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CourseController.this, CourseDetailController.class);
                CourseModel selectedCourse = allCourses.get(position);
                // Pass the selected assessment data to the next activity
                intent.putExtra("courseID", selectedCourse.getCourseID());
                intent.putExtra("courseTitle", selectedCourse.getCourseTitle());
                intent.putExtra("courseStart", selectedCourse.getCourseStart());
                intent.putExtra("courseEnd", selectedCourse.getCourseEnd());
                intent.putExtra("status", selectedCourse.getStatus());
                intent.putExtra("mentorID", selectedCourse.getMentorID());

                // Start the activity
                startActivity(intent);
            }
        });

    }
}

//what should we do if there are no mentors?
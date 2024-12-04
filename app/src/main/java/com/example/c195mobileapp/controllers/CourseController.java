package com.example.c195mobileapp.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

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
    DataBaseHelper dbHelper; // Use only one instance of DataBaseHelper
    CourseDAO courseDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.coursesactivity);

        dbHelper = new DataBaseHelper(CourseController.this); // Single instance
        courseDAO = new CourseDAO(dbHelper);

        List<CourseModel> allCourses = courseDAO.getAllCourses();
        courseListView = findViewById(R.id.courseListView);
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
        BackButton.setOnClickListener(view -> {
            Intent intent = new Intent(CourseController.this, MainMenuController.class);
            startActivity(intent);
        });

        ToAddCourseActivity = findViewById(R.id.ToAddCourseActivity);
        ToAddCourseActivity.setOnClickListener(view -> {
            Intent intent = new Intent(CourseController.this, CourseDetailController.class);
            startActivity(intent);
        });

        courseListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(CourseController.this, CourseDetailController.class);
            CourseModel selectedCourse = allCourses.get(position);
            intent.putExtra("courseID", selectedCourse.getCourseID());
            intent.putExtra("courseTitle", selectedCourse.getCourseTitle());
            intent.putExtra("courseStart", selectedCourse.getCourseStart());
            intent.putExtra("courseEnd", selectedCourse.getCourseEnd());
            intent.putExtra("status", selectedCourse.getStatus());
            intent.putExtra("mentorID", selectedCourse.getMentorID());

            startActivity(intent);
        });

    }
}

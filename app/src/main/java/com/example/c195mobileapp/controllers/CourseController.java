package com.example.c195mobileapp.controllers;

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
import com.example.c195mobileapp.R;

import java.util.ArrayList;
import java.util.List;

public class CourseController extends AppCompatActivity {
    Button ToAddCourseActivity, BackButton;
    ListView courseListView;
    ArrayAdapter<SpannableString> courseArrayAdapter; // ArrayAdapter with SpannableString
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.coursesactivity);
        courseListView = findViewById(R.id.courseListView);

        // Get data from database
        dataBaseHelper = new DataBaseHelper(CourseController.this);
        List<CourseModel> allCourses = dataBaseHelper.getAllCourses();

        // Prepare the array for SpannableString
        List<SpannableString> formattedCourses = new ArrayList<>();
        for (CourseModel model : allCourses) {
            // Creating a string for display
            String text = "Course Title: " + model.getCourseTitle() + "\n" +
                    "Course Start: " + model.getCourseStart() + "\n" +
                    "Course End: " + model.getCourseEnd();

            // Creating SpannableString and making the title bold
            SpannableString spannableString = new SpannableString(text);
            spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            // Adding formatted string to the list
            formattedCourses.add(spannableString);
        }

        // Set the ArrayAdapter with formatted text
        courseArrayAdapter = new ArrayAdapter<>(CourseController.this, android.R.layout.simple_list_item_1, formattedCourses);

    }
}

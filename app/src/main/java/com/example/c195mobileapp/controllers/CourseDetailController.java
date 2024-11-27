package com.example.c195mobileapp.controllers;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import java.util.Calendar;
import java.util.List;

public class CourseDetailController extends AppCompatActivity {

    ListView assessmentListView;
    EditText editName;
    Button BackButton, AddCourseButton, editStart, editEnd, deleteCourseButton;
    Spinner mentorSpinner, statusSpinner;
    MentorDAO mentorDAO;
    AssessmentDAO assessmentDAO;
    ArrayAdapter<MentorModel> mentorArrayAdapter;
    ArrayAdapter<AssessmentModel> assessmentArrayAdapter;
    RadioGroup statusRG;
    int courseID = -1;
    CourseDAO courseDAO;
    TextView Header;
    private DatePickerDialog datePickerDialog;
    private boolean isEditingStartDate = true;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.coursedetailactivity);
        initDatePicker();
        editName = findViewById(R.id.editName);
        editStart = findViewById(R.id.editStart);
        editEnd = findViewById(R.id.editEnd);
        AddCourseButton = findViewById(R.id.AddCourseButton);
        Header = findViewById(R.id.Header);
        deleteCourseButton = findViewById(R.id.deleteCourseButton);

        deleteCourseButton.setOnClickListener(view -> {
            courseDAO.deleteCourse(courseID);
            Intent intent1 = new Intent(CourseDetailController.this, CourseController.class);
            startActivity(intent1);
        });

        //BACK BUTTON
        BackButton = findViewById(R.id.BackButton);
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseDetailController.this, CourseController.class);
                startActivity(intent);
            }
        });

        editStart.setOnClickListener(view -> {
            isEditingStartDate = true; // Indicate that the start date is being edited
            datePickerDialog.show();
        });

        editEnd.setOnClickListener(view -> {
            isEditingStartDate = false; // Indicate that the end date is being edited
            datePickerDialog.show();
        });

        //MENTOR STUFF
        mentorSpinner = findViewById(R.id.mentorSpinner);
        DataBaseHelper dbHelper = new DataBaseHelper(CourseDetailController.this);
        mentorDAO = new MentorDAO(dbHelper);
        List<MentorModel> allMentors = mentorDAO.getAllMentors();
        mentorArrayAdapter = new ArrayAdapter<MentorModel>(CourseDetailController.this, android.R.layout.simple_spinner_item, allMentors) {
            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                // Set the mentor name in the Spinner's selected item view
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setText(allMentors.get(position).getMentorName());
                return textView;
            }

            @Override
            public View getDropDownView(int position, View convertView, android.view.ViewGroup parent) {
                // Set the mentor name in the dropdown view
                TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
                textView.setText(allMentors.get(position).getMentorName());
                return textView;
            }
        };
        mentorArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mentorSpinner.setAdapter(mentorArrayAdapter);

        //ASSESSMENT LIST VIEW STUFF
        assessmentListView = findViewById(R.id.assessmentListView);
        assessmentDAO = new AssessmentDAO(dbHelper);
        List<AssessmentModel> allAssessments = assessmentDAO.getAllAssessments();
        assessmentArrayAdapter = new ArrayAdapter<AssessmentModel>(CourseDetailController.this, android.R.layout.simple_list_item_multiple_choice, allAssessments) {
            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setText(allAssessments.get(position).getAssessmentTitle());
                return textView;
            }
        };
        assessmentListView.setAdapter(assessmentArrayAdapter);

        Intent intent = getIntent();
        courseID = intent.getIntExtra("courseId", -1);

        //COURSE DB
        courseDAO = new CourseDAO(dbHelper);

        //STATUS SPINNER
        String[] statusItems = {"In Progress", "Completed", "Dropped", "Plan to Take"}; // Your data array
        statusSpinner = findViewById(R.id.statusSpinner);
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statusItems);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);

        //POPULATE STUFF
        courseID = intent.getIntExtra("courseID", -1);
        if (courseID != -1) {

            String courseTitle = intent.getStringExtra("courseTitle");
            String courseStart = intent.getStringExtra("courseStart");
            String courseEnd = intent.getStringExtra("courseEnd");
            int status = intent.getIntExtra("status", -1);
            int mentorID = intent.getIntExtra("mentorID", -1);
            List<Integer> associatedAssessmentIDs = courseDAO.getAssociatedAssessmentIDs(courseID);
            deleteCourseButton.setVisibility(View.VISIBLE);

            editName.setText(courseTitle);
            editStart.setText(courseStart);
            editEnd.setText(courseEnd);
            statusSpinner.setSelection(status);
            for (int i = 0; i < mentorArrayAdapter.getCount(); i++) {
                MentorModel mentor = mentorArrayAdapter.getItem(i);
                if (mentor != null && mentor.getMentorID() == mentorID) {
                    mentorSpinner.setSelection(i);
                    break;
                }
            }
            for (int i = 0; i < assessmentArrayAdapter.getCount(); i++) {
                AssessmentModel assessment = assessmentArrayAdapter.getItem(i);
                if (assessment != null && associatedAssessmentIDs.contains(assessment.getAssessmentID())) {
                    assessmentListView.setItemChecked(i, true);
                }
            }
            AddCourseButton.setText("Update");
            Header.setText("Update Course");
        } else {
            deleteCourseButton.setVisibility(View.GONE);
            editStart.setText(getTodaysDate());
            editEnd.setText(getTodaysDate());
        }



        //ON ADD
        AddCourseButton.setOnClickListener(view -> {
            String courseTitle = editName.getText().toString();
            String start = editStart.getText().toString();
            String end = editEnd.getText().toString();
            int status = statusSpinner.getSelectedItemPosition();
            MentorModel selectedMentor = (MentorModel) mentorSpinner.getSelectedItem();
            int mentorID = selectedMentor.getMentorID();
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
                CourseModel updatedCourse = new CourseModel(courseID, courseTitle, start, end, status, mentorID);
                boolean success = courseDAO.updateCourse(updatedCourse, associatedAssessmentIDs);//addassociatedassessmentids
                if (success) {
                    Intent intent2 = new Intent(CourseDetailController.this, CourseController.class);
                    startActivity(intent2);
                } else {
                    Toast.makeText(CourseDetailController.this, "Failed to update course", Toast.LENGTH_SHORT).show();
                }
                //add course stuff
            } else {
                CourseModel newCourse = new CourseModel(-1, courseTitle, start, end, status, mentorID);
                boolean success = courseDAO.addCourse(newCourse, associatedAssessmentIDs);
                if (success) {
                    Intent intent3 = new Intent(CourseDetailController.this, CourseController.class);
                    startActivity(intent3);
                } else {
                    Toast.makeText(CourseDetailController.this, "Failed to add course", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String date = makeDateString(day, month, year);
            if (isEditingStartDate) {
                editStart.setText(date);
            } else {
                editEnd.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + ", " + year;
    }

    private String getMonthFormat(int month) {
        switch (month) {
            case 1:
                return "JAN";
            case 2:
                return "FEB";
            case 3:
                return "MAR";
            case 4:
                return "APR";
            case 5:
                return "MAY";
            case 6:
                return "JUN";
            case 7:
                return "JUL";
            case 8:
                return "AUG";
            case 9:
                return "SEP";
            case 10:
                return "OCT";
            case 11:
                return "NOV";
            case 12:
                return "DEC";
            default:
                return "JAN";
        }
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }
}
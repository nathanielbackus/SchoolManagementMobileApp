package com.example.c195mobileapp.controllers;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import com.example.c195mobileapp.database.NotificationReceiver;
import com.example.c195mobileapp.model.AssessmentModel;
import com.example.c195mobileapp.model.CourseModel;
import com.example.c195mobileapp.model.MentorModel;
import com.example.c195mobileapp.database.CourseDAO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CourseDetailController extends AppCompatActivity {

    ListView assessmentListView;
    EditText editName;
    Button BackButton, AddCourseButton, editStart, editEnd, deleteCourseButton;
    Spinner mentorSpinner, statusSpinner;
    MentorDAO mentorDAO;
    AssessmentDAO assessmentDAO;
    ArrayAdapter<MentorModel> mentorArrayAdapter;
    ArrayAdapter<AssessmentModel> assessmentArrayAdapter;
    int courseID = -1;
    CourseDAO courseDAO;
    TextView Header;
    private DatePickerDialog datePickerDialog;
    private boolean isEditingStartDate = true;
    NotificationReceiver receiver = new NotificationReceiver();
    DataBaseHelper dbHelper = new DataBaseHelper(CourseDetailController.this);

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
        receiver.createNotificationChannel(CourseDetailController.this);

        deleteCourseButton.setOnClickListener(view -> {
            cancelNotification(CourseDetailController.this, courseID);
            cancelNotification(CourseDetailController.this, courseID + 1000);
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
            isEditingStartDate = true; // indicate that the start date is being edited
            datePickerDialog.show();
        });

        editEnd.setOnClickListener(view -> {
            isEditingStartDate = false; // indicate that the end date is being edited
            datePickerDialog.show();
        });

        //MENTOR STUFF
        mentorSpinner = findViewById(R.id.mentorSpinner);
        mentorDAO = new MentorDAO(dbHelper);
        List<MentorModel> allMentors = mentorDAO.getAllMentors();
        mentorArrayAdapter = new ArrayAdapter<MentorModel>(CourseDetailController.this, android.R.layout.simple_spinner_item, allMentors) {
            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setText(allMentors.get(position).getMentorName());
                return textView;
            }
            @Override
            public View getDropDownView(int position, View convertView, android.view.ViewGroup parent) {
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
            editStart.setText(dbHelper.getTodaysDate());
            editEnd.setText(dbHelper.getTodaysDate());
        }



        //ON ADD COURSE BUTTON CLICK
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
            if (courseTitle != null) {
                if (courseID != -1) {
                    CourseModel updatedCourse = new CourseModel(courseID, courseTitle, start, end, status, mentorID);
                    boolean success = courseDAO.updateCourse(updatedCourse, associatedAssessmentIDs);//addassociatedassessmentids
                    if (success) {
                        cancelNotification(CourseDetailController.this, courseID);
                        cancelNotification(CourseDetailController.this, courseID + 1000);
                        scheduleNotification(CourseDetailController.this, updatedCourse, "Course Starts Today", dbHelper.getTimeInMillis(start), courseID);
                        scheduleNotification(CourseDetailController.this, updatedCourse, "Course Ends Today", dbHelper.getTimeInMillis(end), courseID + 1000);
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
                        scheduleNotification(CourseDetailController.this, newCourse, "Course Starts Today", dbHelper.getTimeInMillis(start), courseID);
                        scheduleNotification(CourseDetailController.this, newCourse, "Course Ends Today", dbHelper.getTimeInMillis(end), courseID + 1000);
                        Intent intent3 = new Intent(CourseDetailController.this, CourseController.class);
                        startActivity(intent3);
                    } else {
                        Toast.makeText(CourseDetailController.this, "Failed to add course", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(CourseDetailController.this, "Please add a course title", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String date = dbHelper.makeDateString(day, month, year);
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

    private void scheduleNotification(Context context, CourseModel course, String eventType, long timeInMillis, int requestCode) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("titleExtra", eventType);
        intent.putExtra("messageExtra", "Course: " + course.getCourseTitle());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
    }

    private void cancelNotification(Context context, int requestCode) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}
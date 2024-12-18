package com.example.c195mobileapp.controllers;

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
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.c195mobileapp.R;
import com.example.c195mobileapp.database.AssessmentDAO;
import com.example.c195mobileapp.database.DataBaseHelper;
import com.example.c195mobileapp.database.NotificationReceiver;
import com.example.c195mobileapp.model.AssessmentModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AssessmentDetailController extends AppCompatActivity {
    Button BackButton, EditAssessmentButton, deleteAssessmentButton;
    EditText editName;
    Button editStart, editEnd;
    Switch switchPerfObj;
    AssessmentDAO assessmentDAO;
    int assessmentID = -1;
    TextView Header;
    private DatePickerDialog datePickerDialog;
    private boolean isEditingStartDate = true;
    DataBaseHelper dbHelper = new DataBaseHelper(this);
    NotificationReceiver receiver = new NotificationReceiver();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.assessmentdetailactivity);
        initDatePicker();
        receiver.createNotificationChannel(AssessmentDetailController.this);

        BackButton = findViewById(R.id.BackButton);
        EditAssessmentButton = findViewById(R.id.EditAssessmentButton);
        editName = findViewById(R.id.editName);
        editStart = findViewById(R.id.editStart);
        editEnd = findViewById(R.id.editEnd);
        switchPerfObj = findViewById(R.id.switchPerfObj);
        Header = findViewById(R.id.Header);
        deleteAssessmentButton = findViewById(R.id.deleteAssessmentButton);
        assessmentDAO = new AssessmentDAO(dbHelper);

        Intent intent = getIntent();
        assessmentID = intent.getIntExtra("assessmentId", -1);
        if (assessmentID != -1) {
            String assessmentTitle = intent.getStringExtra("assessmentTitle");
            String assessmentStart = intent.getStringExtra("assessmentStart");
            String assessmentEnd = intent.getStringExtra("assessmentEnd");
            boolean assessmentType = intent.getBooleanExtra("assessmentType", false);
            deleteAssessmentButton.setVisibility(View.VISIBLE);

            editName.setText(assessmentTitle);
            editStart.setText(assessmentStart);
            editEnd.setText(assessmentEnd);
            switchPerfObj.setChecked(assessmentType);

            EditAssessmentButton.setText("Update");
            Header.setText("Update Assessment");
        } else {
            EditAssessmentButton.setText("Add");
            Header.setText("Add Assessment");
            editStart.setText(dbHelper.getTodaysDate());
            editEnd.setText(dbHelper.getTodaysDate());
            deleteAssessmentButton.setVisibility(View.GONE);
        }

        deleteAssessmentButton.setOnClickListener(view -> {
            cancelNotification(AssessmentDetailController.this, assessmentID);
            cancelNotification(AssessmentDetailController.this, assessmentID + 1000);
            assessmentDAO.deleteAssessment(assessmentID);
            Intent intent1 = new Intent(AssessmentDetailController.this, AssessmentController.class);
            startActivity(intent1);
        });

        BackButton.setOnClickListener(view -> {
            Intent intent1 = new Intent(AssessmentDetailController.this, AssessmentController.class);
            startActivity(intent1);
        });

        editStart.setOnClickListener(view -> {
            isEditingStartDate = true; // Indicate that the start date is being edited
            datePickerDialog.show();
        });

        editEnd.setOnClickListener(view -> {
            isEditingStartDate = false; // Indicate that the end date is being edited
            datePickerDialog.show();
        });

        EditAssessmentButton.setOnClickListener(view -> {
            String title = editName.getText().toString();
            String start = editStart.getText().toString();
            String end = editEnd.getText().toString();
            boolean type = switchPerfObj.isChecked();

            if (assessmentID != -1) {
                AssessmentModel updatedAssessment = new AssessmentModel(assessmentID, title, start, end, type);
                boolean success = assessmentDAO.updateAssessment(updatedAssessment);
                if (success) {
                    cancelNotification(AssessmentDetailController.this, assessmentID);
                    cancelNotification(AssessmentDetailController.this, assessmentID + 1000);
                    scheduleNotification(AssessmentDetailController.this, updatedAssessment, "Assessment Starts Today", dbHelper.getTimeInMillis(start), assessmentID);
                    scheduleNotification(AssessmentDetailController.this, updatedAssessment, "Assessment Ends Today", dbHelper.getTimeInMillis(end), assessmentID + 1000);
                    Intent intent2 = new Intent(AssessmentDetailController.this, AssessmentController.class);
                    startActivity(intent2);
                } else {
                    Toast.makeText(AssessmentDetailController.this, "Failed to update assessment", Toast.LENGTH_SHORT).show();
                }
            } else {
                AssessmentModel newAssessment = new AssessmentModel(-1, title, start, end, type);
                boolean success = assessmentDAO.addAssessment(newAssessment);
                if (success) {
                    scheduleNotification(AssessmentDetailController.this, newAssessment, "Assessment Starts Today", dbHelper.getTimeInMillis(start), newAssessment.getAssessmentID());
                    scheduleNotification(AssessmentDetailController.this, newAssessment, "Assessment Ends Today", dbHelper.getTimeInMillis(end), newAssessment.getAssessmentID() + 1000); // Offset ID
                    Intent intent3 = new Intent(AssessmentDetailController.this, AssessmentController.class);
                    startActivity(intent3);
                } else {
                    Toast.makeText(AssessmentDetailController.this, "Failed to add assessment", Toast.LENGTH_SHORT).show();
                }
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

    private void scheduleNotification(Context context, AssessmentModel assessment, String eventType, long timeInMillis, int requestCode) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("titleExtra", eventType);
        intent.putExtra("messageExtra", "Assessment: " + assessment.getAssessmentTitle());

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

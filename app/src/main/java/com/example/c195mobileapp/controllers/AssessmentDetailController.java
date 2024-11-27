package com.example.c195mobileapp.controllers;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
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
import com.example.c195mobileapp.model.AssessmentModel;

import java.util.Calendar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.assessmentdetailactivity);
        initDatePicker();

        BackButton = findViewById(R.id.BackButton);
        EditAssessmentButton = findViewById(R.id.EditAssessmentButton);
        editName = findViewById(R.id.editName);
        editStart = findViewById(R.id.editStart);
        editEnd = findViewById(R.id.editEnd);
        switchPerfObj = findViewById(R.id.switchPerfObj);
        Header = findViewById(R.id.Header);
        deleteAssessmentButton = findViewById(R.id.deleteAssessmentButton);

        DataBaseHelper dbHelper = new DataBaseHelper(AssessmentDetailController.this);
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
            editStart.setText(getTodaysDate());
            editEnd.setText(getTodaysDate());
            deleteAssessmentButton.setVisibility(View.GONE);
        }

        deleteAssessmentButton.setOnClickListener(view -> {
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
                    Intent intent2 = new Intent(AssessmentDetailController.this, AssessmentController.class);
                    startActivity(intent2);
                } else {
                    Toast.makeText(AssessmentDetailController.this, "Failed to update assessment", Toast.LENGTH_SHORT).show();
                }
            } else {
                AssessmentModel newAssessment = new AssessmentModel(-1, title, start, end, type);
                boolean success = assessmentDAO.addAssessment(newAssessment);
                if (success) {
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

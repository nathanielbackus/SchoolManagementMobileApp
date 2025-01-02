package com.example.c195mobileapp.controllers;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.c195mobileapp.R;
import com.example.c195mobileapp.database.CourseDAO;
import com.example.c195mobileapp.database.DataBaseHelper;
import com.example.c195mobileapp.database.TermDAO;
import com.example.c195mobileapp.model.CourseModel;
import com.example.c195mobileapp.model.TermModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TermDetailController extends AppCompatActivity {

    ListView courseListView;
    EditText editName;
    Button editStart, editEnd, AddTermButton, DeleteTermButton, BackButton;
    CourseDAO courseDAO;
    TermDAO termDAO;
    ArrayAdapter<CourseModel> courseArrayAdapter;
    int termID = -1;
    TextView Header;
    DataBaseHelper dbHelper = new DataBaseHelper(this);
    private DatePickerDialog datePickerDialog;
    private boolean isEditingStartDate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.termdetailactivity);
        initDatePicker();

        editName = findViewById(R.id.editName);
        editStart = findViewById(R.id.editStart);
        editEnd = findViewById(R.id.editEnd);
        AddTermButton = findViewById(R.id.AddTermButton);
        Header = findViewById(R.id.Header);
        DeleteTermButton = findViewById(R.id.DeleteTermButton);

        termDAO = new TermDAO(dbHelper);

        //BACK BUTTON
        BackButton = findViewById(R.id.BackButton);
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TermDetailController.this, TermController.class);
                startActivity(intent);
            }
        });

        editStart.setOnClickListener(view -> {
            populateDatePicker(editStart.getText().toString());
            isEditingStartDate = true;
            datePickerDialog.show();
        });

        editEnd.setOnClickListener(view -> {
            populateDatePicker(editEnd.getText().toString());
            isEditingStartDate = false; //this is to basically differentiate which field the datepickerdialog is using
            datePickerDialog.show();
        });

        //COURSE LIST VIEW STUFF
        courseListView = findViewById(R.id.courseListView);
        courseDAO = new CourseDAO(dbHelper);
        List<CourseModel> allCourses = courseDAO.getAllCourses();
        courseArrayAdapter = new ArrayAdapter<CourseModel>(TermDetailController.this, android.R.layout.simple_list_item_multiple_choice, allCourses) {
            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setText(allCourses.get(position).getCourseTitle());
                return textView;
            }
        };
        courseListView.setAdapter(courseArrayAdapter);

        //POPULATE STUFF
        Intent intent = getIntent();
        termID = intent.getIntExtra("termID", -1);

        if (termID != -1) {
            String termTitle = intent.getStringExtra("termTitle");
            String termStart = intent.getStringExtra("termStart");
            String termEnd = intent.getStringExtra("termEnd");
            List<Integer> associatedCourseIDs = termDAO.getAssociatedCourseIDs(termID);
            DeleteTermButton.setVisibility(View.VISIBLE);
            editName.setText(termTitle);
            editStart.setText(termStart);
            editEnd.setText(termEnd);
            for (int i = 0; i < courseArrayAdapter.getCount(); i++) {
                CourseModel course = courseArrayAdapter.getItem(i);
                if (course != null && associatedCourseIDs.contains(course.getCourseID())) {
                    courseListView.setItemChecked(i, true);
                }
            }
            AddTermButton.setText("Update");
            Header.setText("Update Term");
        } else {
            DeleteTermButton.setVisibility(View.GONE);
            editStart.setText(dbHelper.getTodaysDate());
            editEnd.setText(dbHelper.getTodaysDate());
        }

        //ON ADD
        AddTermButton.setOnClickListener(view -> {
            String termTitle = editName.getText().toString();
            String start = editStart.getText().toString();
            String end = editEnd.getText().toString();
            SparseBooleanArray checkedItems = courseListView.getCheckedItemPositions();
            List<Integer> associatedCourseIDs = new ArrayList<>();

            for (int i = 0; i < courseListView.getCount(); i++) {
                if (checkedItems.get(i)) {
                    CourseModel selectedCourse = (CourseModel) courseListView.getItemAtPosition(i);
                    associatedCourseIDs.add(selectedCourse.getCourseID());
                }
            }

            //update term stuff
            if (termID != -1) {
                TermModel updatedTerm = new TermModel(termID, termTitle, start, end);
                boolean success = termDAO.updateTerm(updatedTerm, associatedCourseIDs);//addassociatedcourseids
                if (success) {
                    Intent intent2 = new Intent(TermDetailController.this, TermController.class);
                    startActivity(intent2);
                } else {
                    Toast.makeText(TermDetailController.this, "Failed to update term", Toast.LENGTH_SHORT).show();
                }
                //add course stuff
            } else {
                TermModel newTerm = new TermModel(-1, termTitle, start, end);
                boolean success = termDAO.addTerm(newTerm, associatedCourseIDs);
                if (success) {
                    Intent intent3 = new Intent(TermDetailController.this, TermController.class);
                    startActivity(intent3);
                } else {
                    Toast.makeText(TermDetailController.this, "Failed to add term", Toast.LENGTH_SHORT).show();
                }
            }

        });

        DeleteTermButton.setOnClickListener(view -> {
            SparseBooleanArray checkedItems = courseListView.getCheckedItemPositions();
            List<Integer> associatedCourseIDs = new ArrayList<>();

            for (int i = 0; i < courseListView.getCount(); i++) {
                if (checkedItems.get(i)) {
                    CourseModel selectedCourse = (CourseModel) courseListView.getItemAtPosition(i);
                    associatedCourseIDs.add(selectedCourse.getCourseID());
                }
            }
            if (termDAO.getAssociatedCourseIDs(termID).isEmpty() || associatedCourseIDs.isEmpty()) {
                termDAO.deleteTerm(termID);
                Intent intent1 = new Intent(TermDetailController.this, TermController.class);
                startActivity(intent1);
            } else {
                Toast.makeText(TermDetailController.this, "Please delete all associated courses before deleting.", Toast.LENGTH_LONG).show();
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
        int year, month, day;

        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private void populateDatePicker(String currentDateText) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        try {
            Date parsedDate = dateFormat.parse(currentDateText);
            cal.setTime(parsedDate);
        } catch (ParseException e) {
            throw new RuntimeException("Error parsing date: " + currentDateText, e);
        }
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, selectedYear, selectedMonth, selectedDay) -> {
            selectedMonth = selectedMonth + 1;
            String date = dbHelper.makeDateString(selectedDay, selectedMonth, selectedYear);
            if (isEditingStartDate) {
                editStart.setText(date);
            } else {
                editEnd.setText(date);
            }
        };
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }
}

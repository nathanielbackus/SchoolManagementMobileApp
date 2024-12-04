package com.example.c195mobileapp.controllers;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.c195mobileapp.R;
import com.example.c195mobileapp.database.AssessmentDAO;
import com.example.c195mobileapp.database.CourseDAO;
import com.example.c195mobileapp.database.DataBaseHelper;
import com.example.c195mobileapp.database.MentorDAO;
import com.example.c195mobileapp.database.TermDAO;
import com.example.c195mobileapp.model.AssessmentModel;
import com.example.c195mobileapp.model.CourseModel;
import com.example.c195mobileapp.model.MentorModel;
import com.example.c195mobileapp.model.TermModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TermDetailController extends AppCompatActivity {

    ListView courseListView;
    EditText editName;
    Button editStart, editEnd, AddTermButton, DeleteTermButton, BackButton;
    CourseDAO courseDAO;
    TermDAO termDAO;
    ArrayAdapter<CourseModel> courseArrayAdapter;
    int termID = -1;
    TextView Header;
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

        DataBaseHelper dbHelper = new DataBaseHelper(TermDetailController.this);

        DeleteTermButton.setOnClickListener(view -> {
            termDAO.deleteTerm(termID);
            Intent intent1 = new Intent(TermDetailController.this, TermController.class);
            startActivity(intent1);
        });

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
            isEditingStartDate = true;
            datePickerDialog.show();
        });

        editEnd.setOnClickListener(view -> {
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


        //TERM DB
        termDAO = new TermDAO(dbHelper);

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
            Header.setText("Update Course");
        } else {
            DeleteTermButton.setVisibility(View.GONE);
            editStart.setText(getTodaysDate());
            editEnd.setText(getTodaysDate());
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
                    Toast.makeText(TermDetailController.this, "Failed to update course", Toast.LENGTH_SHORT).show();
                }
                //add course stuff
            } else {
                TermModel newTerm = new TermModel(-1, termTitle, start, end);
                boolean success = termDAO.addTerm(newTerm, associatedCourseIDs);
                if (success) {
                    Intent intent3 = new Intent(TermDetailController.this, TermController.class);
                    startActivity(intent3);
                } else {
                    Toast.makeText(TermDetailController.this, "Failed to add course", Toast.LENGTH_SHORT).show();
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

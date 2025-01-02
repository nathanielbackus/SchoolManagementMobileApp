package com.example.c195mobileapp.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.c195mobileapp.R;
import com.example.c195mobileapp.database.CourseDAO;
import com.example.c195mobileapp.database.DataBaseHelper;
import com.example.c195mobileapp.database.NoteDAO;
import com.example.c195mobileapp.model.CourseModel;
import com.example.c195mobileapp.model.NoteModel;
import java.util.List;

public class NoteDetailController extends AppCompatActivity{
    Spinner courseSpinner;
    EditText editName, noteContentEditText;
    Button BackButton, AddNoteButton, DeleteNoteButton, ShareNoteButton;
    NoteDAO noteDAO;
    CourseDAO courseDAO;
    ArrayAdapter<CourseModel> courseArrayAdapter;
    int noteID = -1;
    TextView Header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.notedetailactivity);

        editName = findViewById(R.id.editName);
        Header = findViewById(R.id.Header);
        courseSpinner = findViewById(R.id.courseSpinner);
        noteContentEditText = findViewById(R.id.noteContentEditText);
        BackButton = findViewById(R.id.BackButton);
        AddNoteButton = findViewById(R.id.AddNoteButton);
        DeleteNoteButton = findViewById(R.id.DeleteNoteButton);
        ShareNoteButton = findViewById(R.id.ShareNoteButton);

        DataBaseHelper dbHelper = new DataBaseHelper(NoteDetailController.this);

        DeleteNoteButton.setOnClickListener(view -> {
            noteDAO.deleteNote(noteID);
            Intent intent1 = new Intent(NoteDetailController.this, NoteController.class);
            startActivity(intent1);
        });

        //BACK BUTTON
        BackButton = findViewById(R.id.BackButton);
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoteDetailController.this, NoteController.class);
                startActivity(intent);
            }
        });

        //course spinner
        courseDAO = new CourseDAO(dbHelper);
        List<CourseModel> allCourses = courseDAO.getAllCourses();
        courseArrayAdapter = new ArrayAdapter<CourseModel>(NoteDetailController.this, android.R.layout.simple_spinner_item, allCourses) {
            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setText(allCourses.get(position).getCourseTitle());
                return textView;
            }

            @Override
            public View getDropDownView(int position, View convertView, android.view.ViewGroup parent) {
                TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
                textView.setText(allCourses.get(position).getCourseTitle());
                return textView;
            }
        };
        courseArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(courseArrayAdapter);

        //decide if note is found or not
        noteDAO = new NoteDAO(dbHelper);
        Intent intent = getIntent();
        noteID = intent.getIntExtra("noteID", -1);

        if (noteID != -1) {
            String noteTitle = intent.getStringExtra("noteTitle");
            int noteCourse = intent.getIntExtra("noteCourse", -1);
            String noteContent = intent.getStringExtra("noteContent");
            DeleteNoteButton.setVisibility(View.VISIBLE);
            editName.setText(noteTitle);
            for (int i = 0; i < courseArrayAdapter.getCount(); i++) {
                CourseModel course = courseArrayAdapter.getItem(i);
                if (course != null && course.getCourseID() == noteCourse) {
                    courseSpinner.setSelection(i);
                    break;
                }
            }
            noteContentEditText.setText(noteContent);

            AddNoteButton.setText("Update Note");
            Header.setText("Update Note");
        } else {
            DeleteNoteButton.setVisibility(View.GONE);
        }

        //add behavior
        AddNoteButton.setOnClickListener(view -> {
            String noteTitle = editName.getText().toString();
            CourseModel selectedCourse = (CourseModel) courseSpinner.getSelectedItem();
            int noteCourse = selectedCourse.getCourseID();
            String noteContent = noteContentEditText.getText().toString();

            if (noteID != -1) {
                NoteModel updatedNote = new NoteModel(noteID, noteTitle, noteCourse, noteContent);
                boolean success = noteDAO.updateNote(updatedNote);
                if (success) {
                    Intent intent2 = new Intent(NoteDetailController.this, NoteController.class);
                    startActivity(intent2);
                } else {
                    Toast.makeText(NoteDetailController.this, "Failed to update note", Toast.LENGTH_SHORT).show();
                }
            }  else {
                NoteModel newNote = new NoteModel(-1, noteTitle, noteCourse, noteContent);
                boolean success = noteDAO.addNote(newNote);
                if (success) {
                    Intent intent3 = new Intent(NoteDetailController.this, NoteController.class);
                    startActivity(intent3);
                } else {
                    Toast.makeText(NoteDetailController.this, "Failed to add note", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //share behavior

        ShareNoteButton.setOnClickListener(view -> {
            String noteTitle = editName.getText().toString();
            CourseModel selectedCourse = (CourseModel) courseSpinner.getSelectedItem();
            String stringCourseName = courseDAO.getCourseNameById(selectedCourse.getCourseID());
            if (selectedCourse == null) {
                Toast.makeText(NoteDetailController.this, "Please select a course before sharing.", Toast.LENGTH_LONG).show();
                return;
            }
            String noteContent = noteContentEditText.getText().toString();
            if (noteTitle.isEmpty() || noteContent.isEmpty()) {
                Toast.makeText(NoteDetailController.this, "Please fill all fields before attempting to share.", Toast.LENGTH_LONG).show();
            } else {
                Intent intent3 = new Intent(Intent.ACTION_SEND);
                intent3.putExtra(Intent.EXTRA_SUBJECT, stringCourseName + ": " + noteTitle);
                intent3.putExtra(Intent.EXTRA_TEXT, noteContent);
                intent3.setType("message/rfc822");
                startActivity(Intent.createChooser(intent3, "Choose email client: "));
            }
        });

    }
}
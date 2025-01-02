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

import com.example.c195mobileapp.R;
import com.example.c195mobileapp.database.CourseDAO;
import com.example.c195mobileapp.database.DataBaseHelper;
import com.example.c195mobileapp.database.NoteDAO;
import com.example.c195mobileapp.model.NoteModel;

import java.util.ArrayList;
import java.util.List;

public class NoteController extends AppCompatActivity {
    Button ToNoteDetailActivity, BackButton;
    ListView noteListView;
    ArrayAdapter<SpannableString> noteArrayAdapter;
    DataBaseHelper dbHelper;
    NoteDAO noteDAO;
    CourseDAO courseDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.notesactivity);

        dbHelper = new DataBaseHelper(NoteController.this); // Single instance
        noteDAO = new NoteDAO(dbHelper);
        courseDAO = new CourseDAO(dbHelper);

        List<NoteModel> allNotes = noteDAO.getAllNotes();
        noteListView = findViewById(R.id.noteListView);
        List<SpannableString> formattedNotes = new ArrayList<>();
        for (NoteModel model : allNotes) {
            String courseName = courseDAO.getCourseNameById(model.getNoteCourse());
            String text = "Note Title: " + model.getNoteTitle() + "\n" +
                    "For Course: " + courseName;
            SpannableString spannableString = new SpannableString(text);
            spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            formattedNotes.add(spannableString);
        }
        noteArrayAdapter = new ArrayAdapter<>(NoteController.this, android.R.layout.simple_list_item_1, formattedNotes);
        noteListView.setAdapter(noteArrayAdapter);

        BackButton = findViewById(R.id.BackButton);
        BackButton.setOnClickListener(view -> {
            Intent intent = new Intent(NoteController.this, MainMenuController.class);
            startActivity(intent);
        });

        ToNoteDetailActivity = findViewById(R.id.ToNoteDetailActivity);
        ToNoteDetailActivity.setOnClickListener(view -> {
            Intent intent = new Intent(NoteController.this, NoteDetailController.class);
            startActivity(intent);
        });

        noteListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(NoteController.this, NoteDetailController.class);
            NoteModel selectedNote = allNotes.get(position);
            intent.putExtra("noteID", selectedNote.getNoteID());
            intent.putExtra("noteTitle", selectedNote.getNoteTitle());
            intent.putExtra("noteCourse", selectedNote.getNoteCourse());
            intent.putExtra("noteContent", selectedNote.getNoteContent());

            startActivity(intent);
        });
    }
}

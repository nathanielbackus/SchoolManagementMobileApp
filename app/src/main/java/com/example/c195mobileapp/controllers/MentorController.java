package com.example.c195mobileapp.controllers;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.c195mobileapp.R;
import com.example.c195mobileapp.database.DataBaseHelper;
import com.example.c195mobileapp.database.MentorDAO;
import com.example.c195mobileapp.model.MentorModel;

import java.util.ArrayList;
import java.util.List;

public class MentorController extends AppCompatActivity {
    Button ToAddMentorActivity, BackButton;
    ListView mentorListView;
    ArrayAdapter<SpannableString> mentorArrayAdapter;
    MentorDAO mentorDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mentorsactivity);
        mentorListView = findViewById(R.id.mentorListView);

        DataBaseHelper dbHelper = new DataBaseHelper(MentorController.this);
        mentorDAO = new MentorDAO(dbHelper);

        List<MentorModel> allMentors = mentorDAO.getAllMentors();

        List<SpannableString> formattedMentors = new ArrayList<>();

        for (MentorModel model : allMentors) {
            String text = "Mentor Name: " + model.getMentorName() + "\n" +
                    "Mentor Email: " + model.getMentorEmail() + "\n" +
                    "Mentor Phone: " + model.getMentorPhone();

            SpannableString spannableString = new SpannableString(text);
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 13, 13 + model.getMentorName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            formattedMentors.add(spannableString);
        }

        mentorArrayAdapter = new ArrayAdapter<>(MentorController.this, android.R.layout.simple_list_item_1, formattedMentors);

        if (allMentors == null || allMentors.isEmpty()) {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        } else {
            for (MentorModel model : allMentors) {
                Log.d("MentorController", "Mentor: " + model.toString());
            }
        }

        mentorListView.setAdapter(mentorArrayAdapter);

        BackButton = findViewById(R.id.BackButton);
        BackButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MentorController.this, MainMenuController.class);
                startActivity(intent);
            }
        });

        ToAddMentorActivity = findViewById(R.id.ToAddMentorActivity);
        ToAddMentorActivity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MentorController.this, MentorDetailController.class);
                startActivity(intent);
            }
        });

        mentorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MentorController.this, MentorDetailController.class);
                MentorModel selectedMentor = allMentors.get(position);
                intent.putExtra("mentorId", selectedMentor.getMentorID());
                intent.putExtra("mentorName", selectedMentor.getMentorName());
                intent.putExtra("mentorEmail", selectedMentor.getMentorEmail());
                intent.putExtra("mentorPhone", selectedMentor.getMentorPhone());

                startActivity(intent);
            }
        });
    }
}

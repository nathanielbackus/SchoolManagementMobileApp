package com.example.c195mobileapp.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.c195mobileapp.R;
import com.example.c195mobileapp.database.DataBaseHelper;
import com.example.c195mobileapp.database.MentorDAO;
import com.example.c195mobileapp.model.MentorModel;

public class MentorDetailController extends AppCompatActivity {
    Button BackButton, EditMentorButton;
    EditText editName, editEmail, editPhone;
    MentorDAO mentorDAO;
    int mentorID = -1;
    TextView Header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.mentordetailactivity);

        BackButton = findViewById(R.id.BackButton);
        EditMentorButton = findViewById(R.id.EditMentorButton);
        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editPhone = findViewById(R.id.editPhone);
        Header = findViewById(R.id.Header);

        DataBaseHelper dbHelper = new DataBaseHelper(MentorDetailController.this);
        mentorDAO = new MentorDAO(dbHelper);

        Intent intent = getIntent();
        mentorID = intent.getIntExtra("mentorId", -1);
        if (mentorID != -1) {
            String mentorName = intent.getStringExtra("mentorName");
            String mentorEmail = intent.getStringExtra("mentorEmail");
            String mentorPhone = intent.getStringExtra("mentorPhone");

            editName.setText(mentorName);
            editEmail.setText(mentorEmail);
            editPhone.setText(mentorPhone);

            EditMentorButton.setText("Update Mentor");
            Header.setText("Update Mentor");
        } else {
            EditMentorButton.setText("Add Mentor");
            Header.setText("Add Mentor");
        }

        BackButton.setOnClickListener(view -> {
            Intent intent1 = new Intent(MentorDetailController.this, MentorController.class);
            startActivity(intent1);
        });

        EditMentorButton.setOnClickListener(view -> {
            String name = editName.getText().toString();
            String email = editEmail.getText().toString();
            String phone = editPhone.getText().toString();

            if (mentorID != -1) {
                MentorModel updatedMentor = new MentorModel(mentorID, name, email, phone);
                boolean success = mentorDAO.updateMentor(updatedMentor);
                if (success) {
                    Intent intent2 = new Intent(MentorDetailController.this, MentorController.class);
                    startActivity(intent2);
                } else {
                    Toast.makeText(MentorDetailController.this, "Failed to update mentor", Toast.LENGTH_SHORT).show();
                }
            } else {
                MentorModel newMentor = new MentorModel(-1, name, email, phone);
                boolean success = mentorDAO.addMentor(newMentor);
                if (success) {
                    Intent intent3 = new Intent(MentorDetailController.this, MentorController.class);
                    startActivity(intent3);
                } else {
                    Toast.makeText(MentorDetailController.this, "Failed to add mentor", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

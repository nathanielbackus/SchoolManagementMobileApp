package com.example.c195mobileapp.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.c195mobileapp.R;

public class TermController extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.termsactivity);
        Button BackButton = (Button)findViewById(R.id.BackButton);
        BackButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(TermController.this, MainMenuController.class);
                startActivity(intent);
            }
        });
    }
}

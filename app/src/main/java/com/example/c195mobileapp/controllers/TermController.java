package com.example.c195mobileapp.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.c195mobileapp.R;
import com.example.c195mobileapp.database.DataBaseHelper;
import com.example.c195mobileapp.database.TermDAO;
import com.example.c195mobileapp.model.TermModel;

import java.util.ArrayList;
import java.util.List;

public class TermController extends AppCompatActivity {
    Button BackButton, ToAddTermActivity;
    ListView termListView;
    ArrayAdapter<SpannableString> termArrayAdapter;
    DataBaseHelper dataBaseHelper;
    TermDAO termDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.termsactivity);

        BackButton = findViewById(R.id.BackButton);
        BackButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(TermController.this, MainMenuController.class);
                startActivity(intent);
            }
        });

        dataBaseHelper = new DataBaseHelper(TermController.this);
        termDAO = new TermDAO(dataBaseHelper);
        List<TermModel> allTerms = termDAO.getAllTerms();
        termListView = findViewById(R.id.termListView);
        List<SpannableString> formattedTerms = new ArrayList<>();
        for (TermModel model : allTerms) {
            String text = "Term Title: " + model.getTermTitle() + "\n" +
                    "Term Start: " + model.getTermStart() + "\n" +
                    "Term End: " + model.getTermEnd();
            SpannableString spannableString = new SpannableString(text);
            spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            formattedTerms.add(spannableString);
        }
        termArrayAdapter = new ArrayAdapter<>(TermController.this, android.R.layout.simple_list_item_1, formattedTerms);
        termListView.setAdapter(termArrayAdapter);

        ToAddTermActivity = findViewById(R.id.ToAddTermActivity);
        ToAddTermActivity.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(TermController.this, TermDetailController.class);
                startActivity(intent);
            }
        });

        termListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(TermController.this, TermDetailController.class);
            TermModel selectedTerm = allTerms.get(position);
            intent.putExtra("termID", selectedTerm.getTermID());
            intent.putExtra("termTitle", selectedTerm.getTermTitle());
            intent.putExtra("termStart", selectedTerm.getTermStart());
            intent.putExtra("termEnd", selectedTerm.getTermEnd());
            startActivity(intent);
        });
    }
}

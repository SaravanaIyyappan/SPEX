package com.example.spex;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class firsttime extends AppCompatActivity {

    TextInputEditText textInput;
    Button getstarted;

    HashMap<String,String> contacts;

    ArrayList<String> send;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firsttime);

        textInput = findViewById(R.id.textInputEditText);

        getstarted = findViewById(R.id.getstarted);

       getstarted.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String name = textInput.getText().toString();

               SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
               SharedPreferences.Editor editor = sharedPreferences.edit();
               Set<String> oldNumbers = sharedPreferences.getStringSet("name", new LinkedHashSet<>());
               oldNumbers.add(name);
               editor.remove("name");
               editor.putStringSet("name",oldNumbers);
               editor.apply();
               editor.apply();
               Toast.makeText(firsttime.this, "Welcome " + name, Toast.LENGTH_SHORT).show();
               finish();
               startActivity(new Intent(firsttime.this,connectthread.class));
           }
       });

    }
}

package com.example.spex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPreferences = getSharedPreferences("prefs", 0);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean firstTime = mPreferences.getBoolean("firstTime",true);
                if (firstTime) {
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putBoolean("firstTime", false);
                    editor.commit();
                    startActivity(new Intent(MainActivity.this,firsttime.class));
                    finish();
                }else{
                    startActivity(new Intent(MainActivity.this,connectthread.class));
                    finish();
                }


            }
        },3000);
    }
}
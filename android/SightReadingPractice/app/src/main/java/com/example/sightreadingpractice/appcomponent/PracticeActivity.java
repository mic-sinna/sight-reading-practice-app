package com.example.sightreadingpractice.appcomponent;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sightreadingpractice.R;

public class PracticeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        findViewById(R.id.practice_layout).setBackgroundColor(getColor(R.color.ui_2));
    }
}
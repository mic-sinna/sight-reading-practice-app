package com.example.sightreadingpractice.appcomponent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;

import com.example.sightreadingpractice.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViewById(R.id.home_layout).setBackgroundColor(Color.parseColor(getResources().getString(R.color.ui_2)));
    }
}
package com.example.hospitalmanagementsystem.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.hospitalmanagementsystem.R;

public class AdminMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
    }

    public void viewHospital(View view) {
        Intent intent = new Intent(this, AdminViewHospitlaActivity.class);
        startActivity(intent);
    }

    public void addHospital(View view) {
        Intent intent = new Intent(this, AdminAddHospitalActivity.class);
        startActivity(intent);
    }

    public void addMedical(View view) {
        Intent intent = new Intent(this, AdminAddLabsActivity.class);
        startActivity(intent);
    }

    public void viewMedical(View view) {
        Intent intent = new Intent(this, AdminViewLabsActivity.class);
        startActivity(intent);
    }
}
package com.example.hospitalmanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.hospitalmanagementsystem.doctors.DoctorLogin;
import com.example.hospitalmanagementsystem.patient.PatientLogin;

public class WelcomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
    }

    public void doctorsLogin(View view) {
        Intent intent = new Intent(this, DoctorLogin.class);
        intent.putExtra("loginType","doctor");
        startActivity(intent);
    }

    public void patientLogin(View view) {
        Intent intent = new Intent(this, PatientLogin.class);
        intent.putExtra("loginType","patient");
        startActivity(intent);
    }
}
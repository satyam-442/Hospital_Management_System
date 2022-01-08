package com.example.hospitalmanagementsystem.doctors;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.hospitalmanagementsystem.R;
import com.example.hospitalmanagementsystem.WelcomeScreen;
import com.example.hospitalmanagementsystem.admin.AdminViewLabsActivity;
import com.example.hospitalmanagementsystem.session.SessionManager;

public class DoctorMain extends AppCompatActivity {

    String doctorID;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);

        doctorID = getIntent().getExtras().get("doctorID").toString();

        sessionManager = new SessionManager(this, SessionManager.SESSION_REMEMBERME);

    }

    public void viewRequestForAppointment(View view) {
        Intent intent = new Intent(this, ViewAppointmentRequestActivity.class);
        intent.putExtra("doctorID",doctorID);
        startActivity(intent);
    }

    public void viewDoctorProfile(View view) {
        Intent intent = new Intent(this, DoctorsProfilePage.class);
        intent.putExtra("doctorID",doctorID);
        startActivity(intent);
    }

    public void logoutUser(View view) {
        sessionManager.logoutUserFromSession();
        Intent intent = new Intent(this, WelcomeScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void viewNearbyLabs(View view) {
        Intent intent = new Intent(this, AdminViewLabsActivity.class);
        startActivity(intent);
    }
}
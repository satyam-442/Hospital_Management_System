package com.example.hospitalmanagementsystem.patient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.hospitalmanagementsystem.R;
import com.example.hospitalmanagementsystem.WelcomeScreen;
import com.example.hospitalmanagementsystem.admin.AdminViewLabsActivity;
import com.example.hospitalmanagementsystem.session.SessionManager;

public class PatientMain extends AppCompatActivity {

    String hospIDStr;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);

        hospIDStr = getIntent().getExtras().get("userID").toString();
        sessionManager = new SessionManager(this, SessionManager.SESSION_REMEMBERME);
    }

    public void viewDoctorsForAppointment(View view) {
        Intent intent = new Intent(this,ViewDoctorsList.class);
        intent.putExtra("userID",hospIDStr);
        startActivity(intent);
    }

    public void viewPatientProfile(View view) {

    }

    public void logoutUser(View view) {
        sessionManager.logoutUserFromSession();
        Intent intent = new Intent(this, WelcomeScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void viewAppointmentPage(View view) {
        Intent intent = new Intent(this,PatientViewAppointment.class);
        intent.putExtra("userID",hospIDStr);
        startActivity(intent);
    }

    public void viewNearbyLabs(View view) {
        Intent intent = new Intent(this, AdminViewLabsActivity.class);
        startActivity(intent);
    }
}
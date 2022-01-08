package com.example.hospitalmanagementsystem.patient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.hospitalmanagementsystem.R;

public class PatientViewAppointment extends AppCompatActivity {

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_appointment);

        userID = getIntent().getExtras().get("userID").toString();

    }

    public void viewConfirmedAppointmentPage(View view) {
        Intent intent = new Intent(this,PatientAppointmentViewConfirmed.class);
        intent.putExtra("userID",userID);
        startActivity(intent);
    }

    public void viewRejectedAppointmentPage(View view) {
        Intent intent = new Intent(this,PatientAppointmentViewRejected.class);
        intent.putExtra("userID",userID);
        startActivity(intent);
    }

    public void viewRequestAppointmentPage(View view) {
        Intent intent = new Intent(this,PatientAppointmentViewRequested.class);
        intent.putExtra("userID",userID);
        startActivity(intent);
    }
}
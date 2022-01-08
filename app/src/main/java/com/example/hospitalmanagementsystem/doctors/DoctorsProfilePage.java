package com.example.hospitalmanagementsystem.doctors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hospitalmanagementsystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoctorsProfilePage extends AppCompatActivity {

    String doctorID;
    DatabaseReference doctorRef;

    ImageView profileImage;
    TextView doctorName, doctorSpecialist, doctorPhone, doctorEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_profile_page);

        doctorID = getIntent().getExtras().get("doctorID").toString();
        doctorRef = FirebaseDatabase.getInstance().getReference();

        profileImage = findViewById(R.id.profileImage);
        doctorName = findViewById(R.id.doctorName);
        doctorSpecialist = findViewById(R.id.doctorSpecialist);
        doctorPhone = findViewById(R.id.doctorPhone);
        doctorEmail = findViewById(R.id.doctorEmail);

        doctorRef.child("Doctors").child(doctorID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String dr_name = snapshot.child("StaffName").getValue().toString();
                    String dr_phone = snapshot.child("StaffPhone").getValue().toString();
                    String dr_email = snapshot.child("StaffEmail").getValue().toString();
                    String dr_special = snapshot.child("DoctorSpecial").getValue().toString();
                    doctorName.setText(dr_name);
                    doctorPhone.setText(dr_phone);
                    doctorEmail.setText(dr_email);
                    doctorSpecialist.setText(dr_special);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }
}
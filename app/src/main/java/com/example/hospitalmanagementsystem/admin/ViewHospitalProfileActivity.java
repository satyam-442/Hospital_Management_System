package com.example.hospitalmanagementsystem.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.hospitalmanagementsystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewHospitalProfileActivity extends AppCompatActivity {

    TextView hospName, hospAddress, hospPhone, hospEmail, hospID, hospFrom, hospTo;
    String hospIDStr;
    DatabaseReference hospRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_hospital_profile);

        hospIDStr = getIntent().getExtras().get("hospID").toString();

        hospName = findViewById(R.id.hospitalName);
        hospAddress = findViewById(R.id.hospitalAddress);
        hospPhone = findViewById(R.id.hospitalPhone);
        hospEmail = findViewById(R.id.hospitalEmail);
        hospID = findViewById(R.id.hospitalID);
        hospFrom = findViewById(R.id.hospitalFromTime);
        hospTo = findViewById(R.id.hospitalToTime);

        hospRef = FirebaseDatabase.getInstance().getReference().child("Hospitals").child(hospIDStr);
        hospRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String name = snapshot.child("HospitalName").getValue().toString();
                    String address = snapshot.child("HospitalAddress").getValue().toString();
                    String phone = snapshot.child("HospitalPhone").getValue().toString();
                    String email = snapshot.child("HospitalEmail").getValue().toString();
                    String toTime = snapshot.child("ToTime").getValue().toString();
                    String fromTime = snapshot.child("FromTime").getValue().toString();
                    hospName.setText(name);
                    hospAddress.setText(address);
                    hospPhone.setText("Phone: "+phone);
                    hospEmail.setText("Email: "+email);
                    hospTo.setText("To Time: "+toTime);
                    hospFrom.setText("From Time: "+fromTime);
                    hospID.setText("ID: "+ hospIDStr);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void viewHospitalStaff(View view) {}

    public void addHospitalStaff(View view) {
        Intent intent = new Intent(this, AddHospitalStaffActivity.class);
        intent.putExtra("hospID",hospIDStr);
        intent.putExtra("hospName",hospName.getText().toString());
        intent.putExtra("hospAddress",hospAddress.getText().toString());
        startActivity(intent);
    }

    public void viewFeedback(View view) {}
}
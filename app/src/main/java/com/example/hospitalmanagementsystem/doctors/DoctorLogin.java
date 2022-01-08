package com.example.hospitalmanagementsystem.doctors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.hospitalmanagementsystem.R;
import com.example.hospitalmanagementsystem.admin.AdminMainActivity;
import com.example.hospitalmanagementsystem.modal.Hospitals;
import com.example.hospitalmanagementsystem.modal.Staff;
import com.example.hospitalmanagementsystem.modal.Users;
import com.example.hospitalmanagementsystem.session.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DoctorLogin extends AppCompatActivity {

    TextInputLayout doctorIdLay, doctorPwdLay, bottomPwd;
    Button authenticateBtn, generatePwd;
    ProgressDialog loadingBar;
    FirebaseAuth mAuth;
    DatabaseReference doctorRef, allDoctorRef;
    String parentDBName = "admin";
    String loginType = "doctor";
    CheckBox rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_login);

        mAuth = FirebaseAuth.getInstance();
        doctorRef = FirebaseDatabase.getInstance().getReference().child("admin");
        allDoctorRef = FirebaseDatabase.getInstance().getReference();

        loadingBar = new ProgressDialog(this);

        doctorIdLay = findViewById(R.id.doctorIdLay);
        doctorPwdLay = findViewById(R.id.doctorPwdLay);
        rememberMe = findViewById(R.id.rememberMe);
        SessionManager sessionManager = new SessionManager(DoctorLogin.this, SessionManager.SESSION_REMEMBERME);
        if (sessionManager.checkRememberMe()){
            HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeDetailsFromSession();
            Intent intent = new Intent(DoctorLogin.this,DoctorMain.class);
            intent.putExtra("doctorID",rememberMeDetails.get(SessionManager.KEY_SESSION_ID));
            startActivity(intent);
        }
        authenticateBtn = findViewById(R.id.authenticateBtn);
        authenticateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentDBName = "admin";
                final String userid = doctorIdLay.getEditText().getText().toString();
                final String password = doctorPwdLay.getEditText().getText().toString();

                if (TextUtils.isEmpty(userid)) {
                    Toast.makeText(DoctorLogin.this, "Field's are empty!", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.setMessage("please wait...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    allDoctorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(userid).exists()) {
                                Users usersData = dataSnapshot.child(userid).getValue(Users.class);
                                assert usersData != null;
                                if (usersData.getUseridd().equals(userid)) {
                                    if (parentDBName.equals("admin")) {
                                        Toast.makeText(DoctorLogin.this, "Logged in as admin!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(DoctorLogin.this, AdminMainActivity.class));
                                        loadingBar.dismiss();
                                    }
                                }
                            }
                            else if (dataSnapshot.child("Doctors").child(userid).exists())
                            {
                                Staff staff = dataSnapshot.child("Doctors").child(userid).getValue(Staff.class);
                                if (staff.getStaffPhone().equals(userid)) {
                                    if (staff.getPassword().equals("dummy")) {
                                        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(DoctorLogin.this);
                                        bottomSheetDialog.setContentView(R.layout.bottom_sheet);
                                        bottomSheetDialog.setCanceledOnTouchOutside(false);

                                        //INITIALIZE YOUR VARIABLES
                                        bottomPwd = bottomSheetDialog.findViewById(R.id.staffPasswordLay);
                                        generatePwd = bottomSheetDialog.findViewById(R.id.generatePwd);
                                        generatePwd.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                String password = bottomPwd.getEditText().getText().toString();
                                                HashMap<String, Object> pwdMap = new HashMap<String, Object>();
                                                pwdMap.put("Password", password);
                                                allDoctorRef.child("Doctors").child(userid).updateChildren(pwdMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(DoctorLogin.this, "Logged in as Doctor!", Toast.LENGTH_SHORT).show();
                                                        if (rememberMe.isChecked()) {
                                                            SessionManager sessionManager = new SessionManager(DoctorLogin.this, SessionManager.SESSION_REMEMBERME);
                                                            sessionManager.createRememberMeSession(userid, password,loginType);
                                                        }
                                                        Intent intent = new Intent(DoctorLogin.this, DoctorMain.class);
                                                        intent.putExtra("doctorID", userid);
                                                        startActivity(intent);
                                                        loadingBar.dismiss();
                                                    }
                                                });
                                            }
                                        });
                                        bottomSheetDialog.show();
                                    }
                                    else {
                                        doctorPwdLay.setVisibility(View.VISIBLE);
                                        rememberMe.setVisibility(View.VISIBLE);
                                        loadingBar.dismiss();
                                        if (staff.getPassword().equals(password)) {
                                            Toast.makeText(DoctorLogin.this, "Logged in as Doctor!", Toast.LENGTH_SHORT).show();
                                            if (rememberMe.isChecked()) {
                                                SessionManager sessionManager = new SessionManager(DoctorLogin.this, SessionManager.SESSION_REMEMBERME);
                                                sessionManager.createRememberMeSession(userid, password,loginType);
                                            }
                                            Intent intent = new Intent(DoctorLogin.this, DoctorMain.class);
                                            intent.putExtra("doctorID", userid);
                                            startActivity(intent);
                                            loadingBar.dismiss();
                                        }
                                    }
                                }
                            }
                            else {
                                Toast.makeText(DoctorLogin.this, "No record found!", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }
        });
    }
}
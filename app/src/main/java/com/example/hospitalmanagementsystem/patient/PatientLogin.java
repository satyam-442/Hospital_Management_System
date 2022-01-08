package com.example.hospitalmanagementsystem.patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.hospitalmanagementsystem.R;
import com.example.hospitalmanagementsystem.admin.AdminViewHospitlaActivity;
import com.example.hospitalmanagementsystem.admin.ViewHospitalProfileActivity;
import com.example.hospitalmanagementsystem.modal.Patients;
import com.example.hospitalmanagementsystem.session.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Locale;

public class PatientLogin extends AppCompatActivity {

    TextInputLayout patientGender, patientFirstNameLay, patientLastNameLay, patientPhoneLay, patientEmailLay, patientIDLay,patientPasswordLay, bottomPwd;
    LinearLayout loginLO, registerLO;
    View loginView, registerView;

    Button loginPatientBtn, registerPatientBtn, generatePwd;
    ProgressDialog loadingBar;
    DatabaseReference usersRef;
    CheckBox rememberMePatient;
    String loginType = "patient";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_login);

        loadingBar = new ProgressDialog(this);
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        patientFirstNameLay = findViewById(R.id.patientFirstNameLay);
        patientLastNameLay = findViewById(R.id.patientLastNameLay);
        patientPhoneLay = findViewById(R.id.patientPhoneLay);
        patientEmailLay = findViewById(R.id.patientEmailLay);

        patientIDLay = findViewById(R.id.patientIDLay);
        patientPasswordLay = findViewById(R.id.patientPasswordLay);

        rememberMePatient = findViewById(R.id.rememberMePatient);

        patientGender = findViewById(R.id.patientGenderLay);
        loginLO = findViewById(R.id.loginLayout);
        registerLO = findViewById(R.id.registerLayout);

        loginView = findViewById(R.id.loginView);
        registerView = findViewById(R.id.registerView);

        SessionManager sessionManager = new SessionManager(PatientLogin.this, SessionManager.SESSION_REMEMBERME);
        if (sessionManager.checkRememberMe()){
            HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeDetailsFromSession();
            Intent intent = new Intent(PatientLogin.this,PatientMain.class);
            intent.putExtra("userID",rememberMeDetails.get(SessionManager.KEY_SESSION_ID));
            startActivity(intent);
        }

        loginPatientBtn = findViewById(R.id.loginPatientBtn);
        loginPatientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userid = patientIDLay.getEditText().getText().toString();
                final String password = patientPasswordLay.getEditText().getText().toString();

                if (TextUtils.isEmpty(userid)) {
                    Toast.makeText(PatientLogin.this, "Field's are empty!", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.setMessage("please wait...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    final DatabaseReference rootRef;
                    rootRef = FirebaseDatabase.getInstance().getReference();
                    rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child("Users").child(userid).exists()) {
                                Patients patients = dataSnapshot.child("Users").child(userid).getValue(Patients.class);
                                if (patients.getUsersID().equals(userid)) {
                                    if (patients.getPassword().equals("dummy")) {
                                        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(PatientLogin.this);
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
                                                usersRef.child(userid).updateChildren(pwdMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(PatientLogin.this, "Logged in as Patient!", Toast.LENGTH_SHORT).show();
                                                        if (rememberMePatient.isChecked()) {
                                                            SessionManager sessionManager = new SessionManager(PatientLogin.this, SessionManager.SESSION_REMEMBERME);
                                                            sessionManager.createRememberMeSession(userid, password,loginType);
                                                        }
                                                        Intent intent = new Intent(PatientLogin.this, PatientMain.class);
                                                        intent.putExtra("userID", userid);
                                                        startActivity(intent);
                                                        loadingBar.dismiss();
                                                    }
                                                });
                                            }
                                        });
                                        bottomSheetDialog.show();
                                    } else {
                                        patientPasswordLay.setVisibility(View.VISIBLE);
                                        rememberMePatient.setVisibility(View.VISIBLE);
                                        loadingBar.dismiss();
                                        if (patients.getPassword().equals(password)) {
                                            Toast.makeText(PatientLogin.this, "Logged in as Patient!", Toast.LENGTH_SHORT).show();
                                            if (rememberMePatient.isChecked()) {
                                                SessionManager sessionManager = new SessionManager(PatientLogin.this, SessionManager.SESSION_REMEMBERME);
                                                sessionManager.createRememberMeSession(userid, password, loginType);
                                            }
                                            Intent intent = new Intent(PatientLogin.this, PatientMain.class);
                                            intent.putExtra("userID", userid);
                                            startActivity(intent);
                                            loadingBar.dismiss();
                                        }
                                    }
                                }
                            } else {
                                Toast.makeText(PatientLogin.this, "No record found!", Toast.LENGTH_SHORT).show();
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

        registerPatientBtn = findViewById(R.id.registerPatientBtn);
        registerPatientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String first_name = patientFirstNameLay.getEditText().getText().toString();
                String last_name = patientLastNameLay.getEditText().getText().toString();
                String phone = patientPhoneLay.getEditText().getText().toString();
                String email = patientEmailLay.getEditText().getText().toString();
                String gender = patientGender.getEditText().getText().toString();
                String userID = first_name.substring(0,3).toUpperCase() + last_name.substring(0,3).toUpperCase() + phone.substring(0,4);
                if (first_name.isEmpty()||last_name.isEmpty()||phone.isEmpty()||email.isEmpty()||gender.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Error: Field's are empty!", Toast.LENGTH_SHORT).show();
                }
                else {
                    loadingBar.setMessage("please wait...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    HashMap usersMap = new HashMap();
                    usersMap.put("FirstName",first_name);
                    usersMap.put("LastName",last_name);
                    usersMap.put("Phone",phone);
                    usersMap.put("Email",email);
                    usersMap.put("Gender",gender);
                    usersMap.put("image","default");
                    usersMap.put("Password","dummy");
                    usersMap.put("UsersID",userID);
                    usersRef.child(userID).updateChildren(usersMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()){
                                Intent intent = new Intent(PatientLogin.this, PatientMain.class);
                                intent.putExtra("userID",userID);
                                startActivity(intent);
                                loadingBar.dismiss();
                            }
                        }
                    });
                }
            }
        });
    }

    public void onRadioButtonCLicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radioMale:
                if (checked)
                    patientGender.getEditText().setText("Male");
                break;
            case R.id.radioFemale:
                if (checked)
                    patientGender.getEditText().setText("Female");
                break;
        }
    }

    public void registerLayoutVisibility(View view) {
        registerLO.setVisibility(View.VISIBLE);
        registerView.setVisibility(View.VISIBLE);
        loginLO.setVisibility(View.GONE);
        loginView.setVisibility(View.GONE);
    }

    public void loginLayoutVisibility(View view) {
        registerLO.setVisibility(View.GONE);
        loginLO.setVisibility(View.VISIBLE);
        registerView.setVisibility(View.GONE);
        loginView.setVisibility(View.VISIBLE);
    }
}
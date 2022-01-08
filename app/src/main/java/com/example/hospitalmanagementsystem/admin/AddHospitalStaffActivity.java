package com.example.hospitalmanagementsystem.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospitalmanagementsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddHospitalStaffActivity extends AppCompatActivity {

    TextInputLayout hospitalIDLay, hospitalNameLay, hospitalAddressLay, hospitalPhoneLay, hospitalEmailLay, staffRoleTextField, staffGenderLay, doctorSpecSpinnerLay;
    LinearLayout spinnerDoctorRoleLay;
    EditText selectFromTime, selectToTime;
    Button addStaffBtn;
    DatabaseReference hospitalRef, doctorsRef;
    ProgressDialog loadingBar;
    Spinner addStaffRole;
    String hospIDStr,hospNameStr,hospAddressStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hospital_staff);

        hospIDStr = getIntent().getExtras().get("hospID").toString();
        hospNameStr = getIntent().getExtras().get("hospName").toString();
        hospAddressStr = getIntent().getExtras().get("hospAddress").toString();

        loadingBar = new ProgressDialog(this);

        hospitalRef = FirebaseDatabase.getInstance().getReference().child("Hospitals");
        doctorsRef = FirebaseDatabase.getInstance().getReference().child("Doctors");

        hospitalIDLay = findViewById(R.id.hospitalIDLay);
        hospitalIDLay.getEditText().setText(hospIDStr);
        hospitalNameLay = findViewById(R.id.hospitalNameLay);
        hospitalAddressLay = findViewById(R.id.hospitalAddressLay);
        hospitalPhoneLay = findViewById(R.id.hospitalPhoneLay);
        hospitalEmailLay = findViewById(R.id.hospitalEmailLay);
        staffRoleTextField = findViewById(R.id.staffRoleTextField);

        doctorSpecSpinnerLay = findViewById(R.id.doctorSpecSpinnerLay);

        staffGenderLay = findViewById(R.id.staffGenderLay);

        addStaffBtn = findViewById(R.id.addStaffBtn);
        addStaffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String staffName = hospitalNameLay.getEditText().getText().toString();
                String staffAddress = hospitalAddressLay.getEditText().getText().toString();
                String staffPhone = hospitalPhoneLay.getEditText().getText().toString();
                String staffEmail = hospitalEmailLay.getEditText().getText().toString();
                String staffGender = staffGenderLay.getEditText().getText().toString();
                String staffRole = staffRoleTextField.getEditText().getText().toString();
                String doctorSpecial = doctorSpecSpinnerLay.getEditText().getText().toString();

                if (staffName.isEmpty()||staffAddress.isEmpty()||staffPhone.isEmpty()||staffEmail.isEmpty()||
                staffGender.isEmpty()||staffRole.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Some Field's are empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    loadingBar.setMessage("please wait...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    HashMap<String, Object> staffMap = new HashMap<>();
                    staffMap.put("StaffName",staffName);
                    staffMap.put("StaffAddress",staffAddress);
                    staffMap.put("StaffPhone",staffPhone);
                    staffMap.put("StaffEmail",staffEmail);
                    staffMap.put("StaffGender",staffGender);
                    staffMap.put("StaffRole",staffRole);
                    staffMap.put("DoctorSpecial",doctorSpecial);
                    staffMap.put("image","default");
                    staffMap.put("Password","dummy");
                    staffMap.put("HospitalID",hospIDStr);
                    staffMap.put("HospitalName",hospNameStr);
                    staffMap.put("HospitalAddress",hospAddressStr);
                    hospitalRef.child(hospIDStr).child(staffRole).child(staffPhone).updateChildren(staffMap).addOnCompleteListener(
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        loadingBar.dismiss();
                                    }
                                    else {
                                        loadingBar.dismiss();
                                        String msg = task.getException().getMessage();
                                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                    );
                    doctorsRef.child(staffPhone).updateChildren(staffMap);
                 }
            }
        });

        spinnerDoctorRoleLay = findViewById(R.id.spinnerDoctorRoleLay);
        addStaffRole = findViewById(R.id.doctor_role_spinner);

        ArrayAdapter<CharSequence> adapter4Specialist = ArrayAdapter.createFromResource(this, R.array.specialist, android.R.layout.simple_spinner_item);
        adapter4Specialist.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addStaffRole.setAdapter(adapter4Specialist);
        addStaffRole.setOnItemSelectedListener(new SpecialistSpinner());

    }

    public void clearForm(View view) {
        hospitalNameLay.getEditText().getText().clear();
        hospitalAddressLay.getEditText().getText().clear();
        hospitalPhoneLay.getEditText().getText().clear();
        hospitalEmailLay.getEditText().getText().clear();
    }

    public void onRadioButtonCLicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.radioDoctor:
                if (checked) {
                    staffRoleTextField.getEditText().setText("Doctor");
                    spinnerDoctorRoleLay.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.radioCLerk:
                if (checked) {
                    staffRoleTextField.getEditText().setText("Clerk");
                    spinnerDoctorRoleLay.setVisibility(View.GONE);
                    doctorSpecSpinnerLay.getEditText().setText("NA");
                }
                break;
            case R.id.radioNurse:
                if (checked) {
                    staffRoleTextField.getEditText().setText("Nurse");
                    spinnerDoctorRoleLay.setVisibility(View.GONE);
                    doctorSpecSpinnerLay.getEditText().setText("NA");
                }
                break;
            case R.id.radioWardboy:
                if (checked) {
                    staffRoleTextField.getEditText().setText("Wardboy");
                    spinnerDoctorRoleLay.setVisibility(View.GONE);
                    doctorSpecSpinnerLay.getEditText().setText("NA");
                }
                break;
            case R.id.radioReceptionist:
                if (checked) {
                    staffRoleTextField.getEditText().setText("Receptionist");
                    spinnerDoctorRoleLay.setVisibility(View.GONE);
                    doctorSpecSpinnerLay.getEditText().setText("NA");
                }
                break;
            case R.id.radioWatchman:
                if (checked) {
                    staffRoleTextField.getEditText().setText("Watchman");
                    spinnerDoctorRoleLay.setVisibility(View.GONE);
                    doctorSpecSpinnerLay.getEditText().setText("NA");
                }
                break;
        }
    }

    public void onGenderRadioButtonCLicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radioMale:
                if (checked)
                    staffGenderLay.getEditText().setText("Male");
                break;
            case R.id.radioFemale:
                if (checked)
                    staffGenderLay.getEditText().setText("Female");
                break;
        }
    }

    public class SpecialistSpinner implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String itemSpinner = parent.getItemAtPosition(position).toString();
            doctorSpecSpinnerLay.getEditText().setText(itemSpinner);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

}
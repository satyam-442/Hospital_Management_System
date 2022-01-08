package com.example.hospitalmanagementsystem.admin;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.hospitalmanagementsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class AdminAddHospitalActivity extends AppCompatActivity {

    TextInputLayout hospitalNameLay, hospitalAddressLay, hospitalPhoneLay, hospitalEmailLay;
    EditText selectFromTime, selectToTime;
    Button addHospitalBtn;
    DatabaseReference hospitalRef;
    TextView tapFromDialog, tapToDialog;
    int fromHour, fromMinute, toHour, toMinute;
    ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_hospital);

        loadingBar = new ProgressDialog(this);

        hospitalRef = FirebaseDatabase.getInstance().getReference().child("Hospitals");

        hospitalNameLay = findViewById(R.id.hospitalNameLay);
        hospitalAddressLay = findViewById(R.id.hospitalAddressLay);
        hospitalPhoneLay = findViewById(R.id.hospitalPhoneLay);
        hospitalEmailLay = findViewById(R.id.hospitalEmailLay);

        selectFromTime = findViewById(R.id.selectFromTime);
        selectToTime = findViewById(R.id.selectToTime);

        tapFromDialog = findViewById(R.id.tapFromDailog);
        selectFromTime = findViewById(R.id.selectFromTime);
        tapFromDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AdminAddHospitalActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                fromHour = hourOfDay;
                                fromMinute = minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0, 0, 0, fromHour, fromMinute);
                                //selectFromTime.setText(DateFormat.format("hh:mm aa"));
                                android.text.format.DateFormat df = new android.text.format.DateFormat();
                                selectFromTime.setText(df.format("hh:mm aa", calendar));
                            }
                        }, 12, 0, false
                );
                timePickerDialog.updateTime(fromHour, fromMinute);
                timePickerDialog.show();
            }
        });

        tapToDialog = findViewById(R.id.tapToDailog);
        selectToTime = findViewById(R.id.selectToTime);
        tapToDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AdminAddHospitalActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                toHour = hourOfDay;
                                toMinute = minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0, 0, 0, toHour, toMinute);
                                //selectFromTime.setText(DateFormat.format("hh:mm aa"));
                                android.text.format.DateFormat df = new android.text.format.DateFormat();
                                selectToTime.setText(df.format("hh:mm aa", calendar));
                            }
                        }, 12, 0, false
                );
                timePickerDialog.updateTime(toHour, toMinute);
                timePickerDialog.show();
            }
        });

        addHospitalBtn = findViewById(R.id.addHospitalBtn);
        addHospitalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = hospitalNameLay.getEditText().getText().toString();
                String address = hospitalAddressLay.getEditText().getText().toString();
                String phone = hospitalPhoneLay.getEditText().getText().toString();
                String email = hospitalEmailLay.getEditText().getText().toString();
                String toTime = selectToTime.getText().toString();
                String fromTime = selectFromTime.getText().toString();
                String nameStr = name.substring(0,3).toUpperCase();
                String phoneStr = phone.substring(0,7);
                String hospitalID = nameStr+phoneStr;
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(address) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(email) ||
                        TextUtils.isEmpty(toTime) || TextUtils.isEmpty(fromTime)){
                    Toast.makeText(getApplicationContext(), "Field's are empty!", Toast.LENGTH_SHORT).show();
                }
                else {
                    loadingBar.setMessage("validating please wait...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    HashMap hospitalMap = new HashMap();
                    hospitalMap.put("HospitalName",name.trim());
                    hospitalMap.put("HospitalAddress",address.trim());
                    hospitalMap.put("HospitalPhone",phone.trim());
                    hospitalMap.put("HospitalEmail",email.trim());
                    hospitalMap.put("ToTime",toTime.trim());
                    hospitalMap.put("FromTime",fromTime.trim());
                    hospitalMap.put("HospitalID",hospitalID.trim());
                    hospitalMap.put("Password","dummy");
                    hospitalRef.child(hospitalID).updateChildren(hospitalMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Hospital added", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
                }
            }
        });
    }

    public void clearForm(View view) {
        hospitalNameLay.getEditText().getText().clear();
        hospitalAddressLay.getEditText().getText().clear();
        hospitalPhoneLay.getEditText().getText().clear();
        hospitalEmailLay.getEditText().getText().clear();
        selectFromTime.getText().clear();
        selectToTime.getText().clear();
    }
}
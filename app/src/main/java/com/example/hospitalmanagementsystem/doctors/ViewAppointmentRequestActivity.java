package com.example.hospitalmanagementsystem.doctors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospitalmanagementsystem.R;
import com.example.hospitalmanagementsystem.admin.AdminViewHospitlaActivity;
import com.example.hospitalmanagementsystem.admin.ViewHospitalProfileActivity;
import com.example.hospitalmanagementsystem.modal.AppointmentRequest;
import com.example.hospitalmanagementsystem.modal.Hospitals;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class ViewAppointmentRequestActivity extends AppCompatActivity {

    RecyclerView viewAppointmentRequest;
    String doctorID;
    DatabaseReference doctorRef, appointRejRef,appointConRef;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointment_request);

        loadingBar = new ProgressDialog(this);

        doctorID = getIntent().getExtras().get("doctorID").toString();

        viewAppointmentRequest = findViewById(R.id.viewAppointmentRequest);

        viewAppointmentRequest.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        viewAppointmentRequest.setLayoutManager(linearLayoutManager);

        doctorRef = FirebaseDatabase.getInstance().getReference().child("Doctors");
        appointRejRef = FirebaseDatabase.getInstance().getReference();
        appointConRef = FirebaseDatabase.getInstance().getReference();

        Log.println(Log.ASSERT,"DoctorID",doctorID);
        startListen();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadingBar.setMessage("please wait");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        startListen();
    }

    private void startListen() {
        Query query = FirebaseDatabase.getInstance().getReference().child("Doctors").child(doctorID).child("AppointmentRequest").limitToLast(50);
        FirebaseRecyclerOptions<AppointmentRequest> options = new FirebaseRecyclerOptions.Builder<AppointmentRequest>().setQuery(query, AppointmentRequest.class).build();
        FirebaseRecyclerAdapter<AppointmentRequest, viewAppointmentReqViewHolder> adapter = new FirebaseRecyclerAdapter<AppointmentRequest, viewAppointmentReqViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull viewAppointmentReqViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull final AppointmentRequest model) {
                //final String PostKey = getRef(position).getKey();
                holder.setName(model.getPatientName());
                holder.setPhone(model.getPatientPhone());
                holder.setAddress(model.getPatientAddress());
                holder.setDate(model.getAppDate());
                holder.setTime(model.getAppTime());

                holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = model.getPatientName();
                        String phone = model.getPatientPhone();
                        String address = model.getPatientAddress();
                        String date = model.getAppDate();
                        String time = model.getAppTime();
                        String patientID = model.getPatientID();
                        String appointmentID = model.getAppointmentID();
                        HashMap<String, Object> patientMap = new HashMap<String, Object>();
                        patientMap.put("PatientName", name);
                        patientMap.put("PatientPhone", phone);
                        patientMap.put("PatientID", patientID);
                        patientMap.put("PatientAddress", address);
                        patientMap.put("AppDate", date);
                        patientMap.put("AppTime", time);
                        patientMap.put("AppointmentID", appointmentID);
                        appointConRef.child("Users").child(patientID).child("AppointmentConfirmed").child(appointmentID).updateChildren(patientMap);
                        appointConRef.child("Doctors").child(doctorID).child("AppointmentConfirmed").child(appointmentID).updateChildren(patientMap);
                        doctorRef.child(doctorID).child("AppointmentRequest").child(appointmentID).removeValue();
                        appointConRef.child("Users").child(patientID).child("AppointmentRequest").child(appointmentID).removeValue();
                        Toast.makeText(getApplicationContext(), "Appointment Confirmed", Toast.LENGTH_SHORT).show();
                    }
                });

                holder.rejectBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = model.getPatientName();
                        String phone = model.getPatientPhone();
                        String address = model.getPatientAddress();
                        String date = model.getAppDate();
                        String time = model.getAppTime();
                        String patientID = model.getPatientID();
                        String appointmentIDRej = model.getAppointmentID();
                        HashMap<String, Object> patientMap = new HashMap<String, Object>();
                        patientMap.put("PatientName", name);
                        patientMap.put("PatientPhone", phone);
                        patientMap.put("PatientID", patientID);
                        patientMap.put("PatientAddress", address);
                        patientMap.put("AppDate", date);
                        patientMap.put("AppTime", time);
                        patientMap.put("AppointmentID", appointmentIDRej);
                        appointRejRef.child("Users").child(patientID).child("AppointmentRejected").child(appointmentIDRej).updateChildren(patientMap);
                        appointRejRef.child("Doctors").child(doctorID).child("AppointmentRejected").child(appointmentIDRej).updateChildren(patientMap);
                        doctorRef.child(doctorID).child("AppointmentRequest").child(appointmentIDRej).removeValue();
                        appointConRef.child("Users").child(patientID).child("AppointmentRequest").child(appointmentIDRej).removeValue();
                        Toast.makeText(getApplicationContext(), "Appointment Rejected", Toast.LENGTH_SHORT).show();
                    }
                });
                loadingBar.dismiss();
            }

            @NonNull
            @Override
            public viewAppointmentReqViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_appointment_req, parent, false);
                return new viewAppointmentReqViewHolder(view);
            }
        };
        viewAppointmentRequest.setAdapter(adapter);
        adapter.startListening();
    }

    public static class viewAppointmentReqViewHolder extends RecyclerView.ViewHolder {
        Button acceptBtn, rejectBtn;
        public viewAppointmentReqViewHolder(@NonNull View itemView) {
            super(itemView);
            //mView = itemView;
            acceptBtn = itemView.findViewById(R.id.appRqstAcceptBtn);
            rejectBtn = itemView.findViewById(R.id.appRqstRejectBtn);
        }

        public void setName(String fname) {
            TextView firstname = (TextView) itemView.findViewById(R.id.patientName);
            firstname.setText("Name: "+fname);
        }

        public void setPhone(String phone) {
            TextView phoneText = (TextView) itemView.findViewById(R.id.patientPhoneApt);
            phoneText.setText("Phone: "+phone);
        }

        public void setAddress(String address) {
            TextView setAddress = (TextView) itemView.findViewById(R.id.patientAddress);
            setAddress.setText("Address: "+address);
        }

        public void setDate(String date) {
            TextView dateTxt = (TextView) itemView.findViewById(R.id.appointmentDate);
            dateTxt.setText("Date: "+date);
        }

        public void setTime(String time) {
            TextView timeTxt = (TextView) itemView.findViewById(R.id.appointmentTime);
            timeTxt.setText("Time: "+time);
        }
        /*public void setImagee(Context ctx, String image)
        {
            CircleImageView donorimage = (CircleImageView) mView.findViewById(R.id.donor_profile_image);
            Picasso.with(ctx).load(image).into(donorimage);
        }*/
    }
}
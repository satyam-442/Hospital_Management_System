package com.example.hospitalmanagementsystem.patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hospitalmanagementsystem.R;
import com.example.hospitalmanagementsystem.modal.AppointmentRequest;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class PatientAppointmentViewConfirmed extends AppCompatActivity {

    String userID;

    RecyclerView viewAppointmentConfirmed;
    String doctorID;
    DatabaseReference patientRef, appointRejRef,appointConRef;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appointment_view_confirmed);

        userID = getIntent().getExtras().get("userID").toString();

        loadingBar = new ProgressDialog(this);

        viewAppointmentConfirmed = findViewById(R.id.viewAppointmentConfirmed);

        viewAppointmentConfirmed.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        viewAppointmentConfirmed.setLayoutManager(linearLayoutManager);

        patientRef = FirebaseDatabase.getInstance().getReference().child("Doctors");

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
        Query query = FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("AppointmentConfirmed").limitToLast(50);
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

                loadingBar.dismiss();
            }

            @NonNull
            @Override
            public viewAppointmentReqViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_appointment_req_patient, parent, false);
                return new viewAppointmentReqViewHolder(view);
            }
        };
        viewAppointmentConfirmed.setAdapter(adapter);
        adapter.startListening();
    }

    public static class viewAppointmentReqViewHolder extends RecyclerView.ViewHolder {
        public viewAppointmentReqViewHolder(@NonNull View itemView) {
            super(itemView);
            //mView = itemView;
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
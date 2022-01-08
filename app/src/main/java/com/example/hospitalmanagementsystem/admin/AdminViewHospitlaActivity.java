package com.example.hospitalmanagementsystem.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospitalmanagementsystem.R;
import com.example.hospitalmanagementsystem.modal.Hospitals;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AdminViewHospitlaActivity extends AppCompatActivity {

    RecyclerView viewHospitalRecList;
    DatabaseReference hospitalRef;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_hospitla);

        loadingBar = new ProgressDialog(this);

        viewHospitalRecList = findViewById(R.id.viewHospitalList);
        viewHospitalRecList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        viewHospitalRecList.setLayoutManager(linearLayoutManager);

        hospitalRef = FirebaseDatabase.getInstance().getReference().child("Hospitals");
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
        Query query = FirebaseDatabase.getInstance().getReference().child("Hospitals").limitToLast(50);
        FirebaseRecyclerOptions<Hospitals> options = new FirebaseRecyclerOptions.Builder<Hospitals>().setQuery(query, Hospitals.class).build();
        FirebaseRecyclerAdapter<Hospitals, viewHospitalsViewHolder> adapter = new FirebaseRecyclerAdapter<Hospitals, viewHospitalsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull viewHospitalsViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull final Hospitals model) {
                //final String PostKey = getRef(position).getKey();
                holder.setName(model.getHospitalName());
                holder.setPhone(model.getHospitalPhone());
                holder.setHospitalID(model.getHospitalID());
                //holder.setTeacherName(model.getTeacher());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String hospID = model.getHospitalID();
                        Intent intent = new Intent(AdminViewHospitlaActivity.this,ViewHospitalProfileActivity.class);
                        intent.putExtra("hospID",hospID);
                        startActivity(intent);
                    }
                });
                loadingBar.dismiss();
            }

            @NonNull
            @Override
            public viewHospitalsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_hospital_view, parent, false);
                return new viewHospitalsViewHolder(view);
            }
        };
        viewHospitalRecList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class viewHospitalsViewHolder extends RecyclerView.ViewHolder {
        Button acceptBtn, rejectBtn;
        public viewHospitalsViewHolder(@NonNull View itemView) {
            super(itemView);
            //mView = itemView;
            /*acceptBtn = itemView.findViewById(R.id.studRqstAcceptBtn);
            rejectBtn = itemView.findViewById(R.id.studRqstRejectBtn);*/
        }

        public void setName(String fname) {
            TextView firstname = (TextView) itemView.findViewById(R.id.hospitalName);
            firstname.setText("Name: "+fname);
        }

        public void setPhone(String phone) {
            TextView std = (TextView) itemView.findViewById(R.id.hospitalPhone);
            std.setText("Phone: "+phone);
        }

        public void setHospitalID(String hospID) {
            TextView stdID = (TextView) itemView.findViewById(R.id.hospitalID);
            stdID.setText("Hospital ID: "+hospID);
        }

        /*public void setImagee(Context ctx, String image)
        {
            CircleImageView donorimage = (CircleImageView) mView.findViewById(R.id.donor_profile_image);
            Picasso.with(ctx).load(image).into(donorimage);
        }*/
    }
}
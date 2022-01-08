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

import com.example.hospitalmanagementsystem.R;
import com.example.hospitalmanagementsystem.modal.Hospitals;
import com.example.hospitalmanagementsystem.modal.Labs;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AdminViewLabsActivity extends AppCompatActivity {

    RecyclerView viewLabsList;
    DatabaseReference hospitalRef;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_labs);

        loadingBar = new ProgressDialog(this);

        viewLabsList = findViewById(R.id.viewLabsList);
        viewLabsList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        viewLabsList.setLayoutManager(linearLayoutManager);

        hospitalRef = FirebaseDatabase.getInstance().getReference().child("Labs");
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
        Query query = FirebaseDatabase.getInstance().getReference().child("Labs").limitToLast(50);
        FirebaseRecyclerOptions<Labs> options = new FirebaseRecyclerOptions.Builder<Labs>().setQuery(query, Labs.class).build();
        FirebaseRecyclerAdapter<Labs, viewLabsViewHolder> adapter = new FirebaseRecyclerAdapter<Labs, viewLabsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull viewLabsViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull final Labs model) {
                //final String PostKey = getRef(position).getKey();
                holder.setName(model.getLabName());
                holder.setPhone(model.getLabPhone());
                holder.setHospitalID(model.getLabID());
                //holder.setTeacherName(model.getTeacher());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String labID = model.getLabID();
                        /*Intent intent = new Intent(AdminViewLabsActivity.this,ViewHospitalProfileActivity.class);
                        intent.putExtra("labID",labID);
                        startActivity(intent);*/
                    }
                });
                loadingBar.dismiss();
            }

            @NonNull
            @Override
            public viewLabsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_labs_view, parent, false);
                return new viewLabsViewHolder(view);
            }
        };
        viewLabsList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class viewLabsViewHolder extends RecyclerView.ViewHolder {
        public viewLabsViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setName(String name) {
            TextView firstname = (TextView) itemView.findViewById(R.id.labName);
            firstname.setText("Name: "+name);
        }

        public void setPhone(String phone) {
            TextView phoneTv = (TextView) itemView.findViewById(R.id.labPhone);
            phoneTv.setText("Phone: "+phone);
        }

        public void setHospitalID(String labID) {
            TextView labIDTV = (TextView) itemView.findViewById(R.id.labID);
            labIDTV.setText("Lab ID: "+labID);
        }

        /*public void setImagee(Context ctx, String image)
        {
            CircleImageView donorimage = (CircleImageView) mView.findViewById(R.id.donor_profile_image);
            Picasso.with(ctx).load(image).into(donorimage);
        }*/
    }
}
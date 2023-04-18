package com.android.lifebox.UserActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.lifebox.Models.BloodRequestModel;
import com.android.lifebox.Models.UserModel;
import com.android.lifebox.R;
import com.android.lifebox.databinding.ActivityRequestBloodBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class RequestBlood extends AppCompatActivity {

    private ActivityRequestBloodBinding binding;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private String userBloodGroup;
    private String userCounty;
    private String userFullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRequestBloodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //get user details
        userBloodGroup = getIntent().getStringExtra("userBloodGroup");
        database.getReference()
                .child("Users")
                .child(mAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel model = snapshot.getValue(UserModel.class);
                        assert model != null;
                        userCounty = model.getUserCounty();
                        userFullName = model.getUserFullName();

                        //show data
                        binding.txtUserBloodGroup.setText(userBloodGroup);
                        binding.txtUserFulName.setText(userFullName);
                        binding.txtUserCounty.setText(userCounty + " County");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitBloodRequest();
            }
        });
    }

    private void submitBloodRequest() {
        if (!userBloodGroup.isEmpty() && !userFullName.isEmpty() && !userCounty.isEmpty()){

            BloodRequestModel model = new BloodRequestModel();
            model.setRequestType("User");
            model.setBloodGroup(userBloodGroup);
            model.setTimeStamp(new Date().getTime());

            database.getReference()
                    .child("Blood_Requests")
                    .child(mAuth.getCurrentUser().getUid())
                    .setValue(model)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(RequestBlood.this, "Request successfully submitted. You will receive an email with details", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RequestBlood.this, UserHomeActivity.class));
                            finish();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RequestBlood.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else {
            Toast.makeText(this, "Something went wrong. Unable to submit request", Toast.LENGTH_SHORT).show();
        }
    }
}
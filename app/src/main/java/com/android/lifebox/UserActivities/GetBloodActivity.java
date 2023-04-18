package com.android.lifebox.UserActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.lifebox.Models.UserMedicalRecordsModel;
import com.android.lifebox.Models.UserModel;
import com.android.lifebox.R;
import com.android.lifebox.databinding.ActivityGetBloodBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GetBloodActivity extends AppCompatActivity {

    private ActivityGetBloodBinding binding;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private String userVerificationStatus;
    private String bloodRecipient;
    private String userBloodGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGetBloodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //get verification status
        database.getReference()
                .child("Users")
                .child(mAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        UserModel userModel = snapshot.getValue(UserModel.class);
                        assert userModel != null;
                        userVerificationStatus = userModel.getUserVerificationStatus();
                        switch (userVerificationStatus){

                            case "unverified":
                                //show verification dialog
                                new AlertDialog.Builder(GetBloodActivity.this)
                                        .setTitle("Account Verification")
                                        .setMessage("You need to verify your account before you can proceed further")
                                        .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startActivity(new Intent(GetBloodActivity.this
                                                        , VerificationActivity.class));
                                            }
                                        }).setCancelable(false)
                                        .show();
                                break;
                            case "pending":
                                //show pending dialog
                                new AlertDialog.Builder(GetBloodActivity.this)
                                        .setTitle("Verification Pending")
                                        .setMessage("You verification details are pending approval by our team. Please try again later")
                                        .setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                onBackPressed();
                                            }
                                        })
                                        .setCancelable(false)
                                        .show();
                                break;


                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        // Set a listener for the RadioGroup to handle the selection of the RadioButton
        binding.radioGroupBloodRecipient.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){

                    case R.id.radioButtonUser:
                        bloodRecipient = "User";
                        break;
                    case R.id.radioButtonOther:
                        bloodRecipient = "Another person";
                        break;
                }

            }
        });

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateRecipient();
            }
        });

        //back
        binding.backArrrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void validateRecipient() {
        if (bloodRecipient != null){

            if (bloodRecipient.equals("User")){
                //get user medical records
                database.getReference()
                        .child("Medical_Records")
                        .child(mAuth.getCurrentUser().getUid())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                               if (snapshot.exists()){
                                   UserMedicalRecordsModel userMedicalRecordsModel = snapshot.getValue(UserMedicalRecordsModel.class);
                                   assert  userMedicalRecordsModel != null;
                                   userBloodGroup = userMedicalRecordsModel.getBloodGroup();

                                   startActivity(new Intent(GetBloodActivity.this, RequestBlood.class)
                                           .putExtra("userBloodGroup", userBloodGroup));
                               }
                               else {
                                   //ask user to provide medical records
                                   new AlertDialog.Builder(GetBloodActivity.this)
                                           .setTitle("Medical Records Required")
                                           .setMessage("We need to have medical records of your blood group before we can provide you with this service. Please update your medical records")
                                           .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                               @Override
                                               public void onClick(DialogInterface dialog, int which) {
                                                   startActivity(new Intent(GetBloodActivity.this
                                                           , UserMedicalHistory.class));
                                               }
                                           }).setNegativeButton("Cancel", null)
                                           .setCancelable(true)
                                           .show();
                               }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

            }
            else {
                Toast.makeText(this, "You currently cannot request blood for another person. This feature will be available soon", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(this, "Select blood recipient", Toast.LENGTH_SHORT).show();
        }
    }

}
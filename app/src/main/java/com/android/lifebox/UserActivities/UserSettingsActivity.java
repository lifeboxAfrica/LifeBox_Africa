package com.android.lifebox.UserActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.lifebox.Models.UserModel;
import com.android.lifebox.R;
import com.android.lifebox.SharedActivities.LoginActivity;
import com.android.lifebox.databinding.ActivityUserSettingsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserSettingsActivity extends AppCompatActivity {

    ActivityUserSettingsBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        //back
        binding.backArrrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //medical history
        binding.medicalHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserSettingsActivity.this, UserMedicalHistory.class));
            }
        });

        //verifyAccount
        binding.verifyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVerificationStatus();
            }
        });
        
        //logout
        binding.logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

    }

    private void getVerificationStatus() {
        database.getReference()
                .child("Users")
                .child(mAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        assert userModel != null;
                        String status = userModel.getUserVerificationStatus();
                        switch (status){
                            case "verified":
                                Toast.makeText(UserSettingsActivity.this, "Your accounts is verified", Toast.LENGTH_SHORT).show();
                                break;
                            case "pending":
                                Toast.makeText(UserSettingsActivity.this, "Account verification pending", Toast.LENGTH_SHORT).show();
                                break;
                            case "unverified":
                                startActivity(new Intent(UserSettingsActivity.this, VerificationActivity.class));
                                break;

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    private void signOut() {
        new AlertDialog.Builder(this)
                .setTitle("Sign Out")
                        .setMessage("Confirm you want to quit application")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mAuth.signOut();
                                        startActivity(new Intent(UserSettingsActivity.this, LoginActivity.class)
                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                        finish();
                                    }
                                }).setNegativeButton("NO", null)
                .show();


    }


}
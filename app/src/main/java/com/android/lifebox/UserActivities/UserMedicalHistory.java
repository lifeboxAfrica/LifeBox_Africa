package com.android.lifebox.UserActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.lifebox.Models.UserMedicalRecordsModel;
import com.android.lifebox.R;
import com.android.lifebox.databinding.ActivityUserMedicalHistoryBinding;
import com.android.lifebox.databinding.ActivityUserSettingsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserMedicalHistory extends AppCompatActivity {

    ActivityUserMedicalHistoryBinding binding;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private RadioGroup radioGroupBloodGroup;
    private RadioButton radioButtonSelected;
    private String userBloodGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserMedicalHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //back
        binding.backArrrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //hide blood group layout for users who have already submited their blood group
        database.getReference()
                .child("Medical_Records")
                        .child(mAuth.getCurrentUser().getUid())
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()){
                                            binding.hintBloodGroup.setVisibility(View.VISIBLE);
                                            binding.txtUserBloodGroup.setVisibility(View.VISIBLE);

                                            UserMedicalRecordsModel model = snapshot.getValue(UserMedicalRecordsModel.class);
                                            assert  model != null;
                                            //show user blood group
                                            binding.txtUserBloodGroup.setText(model.getBloodGroup());
                                        }
                                        else {
                                            //request user to provide blood group
                                            binding.bloodGroupLayout.setVisibility(View.VISIBLE);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

        // Set a listener for the RadioGroup to handle the selection of the RadioButton
        binding.radioGroupBloodGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){

                    case R.id.radioButtonA:
                        userBloodGroup = "A";
                        break;
                    case R.id.radioButtonB:
                        userBloodGroup = "B";
                        break;
                    case R.id.radioButtonAB:
                        userBloodGroup = "AB";
                        break;
                    case R.id.radioButtonO:
                        userBloodGroup = "O";
                        break;

                }

            }
        });

        //save user Medical records
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMedicalRecords();
            }
        });



    }

    private void saveMedicalRecords() {

        if (!userBloodGroup.isEmpty()){

            UserMedicalRecordsModel model = new UserMedicalRecordsModel(userBloodGroup);
            database.getReference()
                    .child("Medical_Records")
                    .child(mAuth.getCurrentUser().getUid())
                    .setValue(model)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(UserMedicalHistory.this, "Saved", Toast.LENGTH_SHORT).show();
                            binding.bloodGroupLayout.setVisibility(View.INVISIBLE);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UserMedicalHistory.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else {
            Toast.makeText(this, "select your  blood group", Toast.LENGTH_SHORT).show();
        }


    }
}
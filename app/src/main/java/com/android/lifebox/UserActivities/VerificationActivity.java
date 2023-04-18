package com.android.lifebox.UserActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.lifebox.Helpers.UsersInformation;
import com.android.lifebox.Models.UserModel;
import com.android.lifebox.R;
import com.android.lifebox.databinding.ActivityVerificationBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class VerificationActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private ActivityVerificationBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private int mYear, mMonth, mDay;
    private String userDateOfBirth;
    private String selectedCounty;
    private String userGender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //retrieve name and country
        database.getReference()
                .child("Users")
                .child(mAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        assert userModel != null;
                        binding.txtUserFulName.setText(userModel.getUserFullName());
                        binding.txtUserCountry.setText(userModel.getUserCountry());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        // Set a listener for the RadioGroup to handle the selection of the RadioButton
        binding.radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){

                    case R.id.radioButtonMale:
                        userGender = "Male";
                        break;
                    case R.id.radioButtonFemale:
                        userGender = "Female";
                        break;
                }

            }
        });


        //date of Birth
        binding.edtUserDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(VerificationActivity.this,
                        VerificationActivity.this, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

        //configure spinner to show the 47 counties
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.kenya_counties, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);

        // Set an item selected listener for the spinner
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected county
                selectedCounty = parent.getItemAtPosition(position).toString();

                // Update the TextView with the selected county
                binding.txtUserCounty.setText(selectedCounty);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        //upload details
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadUserDetails();
            }
        });

        //Back arrow
        binding.backArrrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onBackPressed();
            }
        });



    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mYear = year;
        mMonth = month;
        mDay = dayOfMonth;
        userDateOfBirth = mDay + "/" + (mMonth + 1) + "/" + mYear;
        binding.txtUserDoB.setText(userDateOfBirth);

    }
    private void uploadUserDetails() {
        //validate input
        if (!binding.edtUserIdNumber.getText().toString().isEmpty() && !userDateOfBirth.isEmpty()
             && !binding.edtUserPhoneNumber.getText().toString().isEmpty() && !selectedCounty.isEmpty() &&!userGender.isEmpty()){

            //update records
            String userCounty = selectedCounty;
            HashMap<String, Object> userVerificationData = new HashMap<>();
            userVerificationData.put("userCounty", userCounty);
            userVerificationData.put("userDateOfBirth", userDateOfBirth);
            userVerificationData.put("userIdNumber", binding.edtUserIdNumber.getText().toString());
            userVerificationData.put("userPhoneNumber", binding.edtUserPhoneNumber.getText().toString());
            userVerificationData.put("userGender", userGender);

            database.getReference()
                    .child("Users")
                    .child(mAuth.getCurrentUser().getUid())
                    .updateChildren(userVerificationData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            String statusPending = "pending";
                            HashMap<String, Object> object = new HashMap<>();
                            object.put("userVerificationStatus", statusPending);

                            database.getReference()
                                    .child("Users")
                                    .child(mAuth.getCurrentUser().getUid())
                                    .updateChildren(object)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(VerificationActivity.this, "Details Successfully Uploaded", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(VerificationActivity.this,UserSettingsActivity.class)
                                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                            finish();
                                        }
                                    });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //error message
                            Toast.makeText(VerificationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

        }
        else {
            Toast.makeText(this, "Provide all required details", Toast.LENGTH_SHORT).show();
        }
    }

}
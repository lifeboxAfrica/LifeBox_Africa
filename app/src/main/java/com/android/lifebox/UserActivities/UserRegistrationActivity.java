package com.android.lifebox.UserActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.android.lifebox.Models.UserModel;
import com.android.lifebox.SharedActivities.LoginActivity;
import com.android.lifebox.databinding.ActivityUserRegistrationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class UserRegistrationActivity extends AppCompatActivity {

    private ActivityUserRegistrationBinding binding;
    private ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseDatabase database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

       mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);


        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validate user input
                validateInput();
            }
        });

        //Sign in users
        binding.txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserRegistrationActivity.this, LoginActivity.class));
            }
        });
    }


    /*For validating if the registration details given by the user are correct*/
    private void validateInput() {

        //check empty spaces
        if (!binding.etUserFirstName.getText().toString().isEmpty() && !binding.etUserLastName.getText().toString().isEmpty()
            && !binding.etUserEmail.getText().toString().isEmpty() && !binding.etUserPassword.getText().toString().isEmpty()
            && !binding.etConfirmPassword.getText().toString().isEmpty()){

            //validate email address
            if (Patterns.EMAIL_ADDRESS.matcher(binding.etUserEmail.getText().toString()).matches()){

                //check if passwords match
                if (binding.etUserPassword.getText().toString().equals(binding.etConfirmPassword.getText().toString())){

                    //check if user accepted terms of service
                    if (binding.termsCheckBox.isChecked()){

                        //Register user
                        registerNewUser();

                    }
                    else {
                        Toast.makeText(this, "Please accept term of service", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(this, "Password don't match", Toast.LENGTH_SHORT).show();
                }

            }
            else {
                Toast.makeText(this, "Please provide a valid email", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Provide all required details", Toast.LENGTH_SHORT).show();
        }
    }

    /*Fore registering users */
    private void registerNewUser() {
        //Start progress dialog
        progressDialog.setTitle("Creating Account...");
        progressDialog.setMessage("Please wait");
        progressDialog.show();

//Register user
        mAuth.createUserWithEmailAndPassword(binding.etUserEmail.getText().toString(), binding.etUserPassword.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            /*store user details in database*/
                            String userPhoneNumber = " ";
                            String userDateOfBirth = "";
                            String userGender = "";
                            String userCountry="Kenya";
                            String userCounty="";
                            String verificationStatus="unverified";
                            String userProfilePic="";
                            String userIdNumber = "";

                            String userId = task.getResult().getUser().getUid(); //Unique id for identifying every user in the databse
                            String userFullName = binding.etUserFirstName.getText().toString().trim() + " " + binding.etUserLastName.getText().toString().trim();

                            UserModel userModel = new UserModel(userIdNumber, userFullName, binding.etUserEmail.getText().toString(), userPhoneNumber, userProfilePic, userGender,
                                    userCountry, userCounty, userDateOfBirth, verificationStatus);
                            //set user id
                            userModel.setUserId(userId);
                            //Store data
                            database.getReference()
                                    .child("Users")
                                    .child(userId)
                                    .setValue(userModel)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            progressDialog.dismiss();
                                            Toast.makeText(UserRegistrationActivity.this,
                                                    "Registration successful", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(UserRegistrationActivity.this, UserHomeActivity.class)
                                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(UserRegistrationActivity.this,
                                                    e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //dismiss progress dialog
                        progressDialog.dismiss();

                        //Show error message
                        Toast.makeText(UserRegistrationActivity.this,
                                e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });



    }
}
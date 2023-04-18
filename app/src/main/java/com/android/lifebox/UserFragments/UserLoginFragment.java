package com.android.lifebox.UserFragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.lifebox.R;
import com.android.lifebox.UserActivities.UserHomeActivity;
import com.android.lifebox.UserActivities.UserRegistrationActivity;
import com.android.lifebox.databinding.FragmentUserLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class UserLoginFragment extends Fragment {

    private FragmentUserLoginBinding binding;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserLoginBinding.inflate(inflater, container, false);


        //Initialize objects
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(getContext());


        //Login user
        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInput();
            }
        });


        //Register user
        binding.txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), UserRegistrationActivity.class));
            }
        });
        return binding.getRoot();
    }
    /*Foe validating the input provided by the user*/
    private void validateInput() {
        //Check for empty spaces
        if(!binding.edtUserEmail.getText().toString().isEmpty() &&  !binding.edtUserPassword.getText().toString().isEmpty()){

            //Validate email
            if(Patterns.EMAIL_ADDRESS.matcher(binding.edtUserEmail.getText().toString()).matches()){

                //sign in user
                loginUser();
            }
            else {
                Toast.makeText(getContext(), "Email is badly formatted", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(getContext(), "Please provide required credentials", Toast.LENGTH_SHORT).show();
        }
    }


    /*For Logging in users **/
    private void loginUser() {

        //configure progress dialog
        progressDialog.setTitle("Signing In...");
        progressDialog.setMessage("Please wait");
        progressDialog.show();

        //validate if email and password are correct
        mAuth.signInWithEmailAndPassword(binding.edtUserEmail.getText().toString(), binding.edtUserPassword.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //Dismiss progress dialog
                        progressDialog.dismiss();

                        //Move users to main Home screen & Disable users from coming back to login
                        startActivity(new Intent(getContext(), UserHomeActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        getActivity().finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //dismiss progress dialog
                        progressDialog.dismiss();

                        //show error message'
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
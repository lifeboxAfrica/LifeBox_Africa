package com.android.lifebox.Helpers;

import androidx.annotation.NonNull;

import com.android.lifebox.Models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UsersInformation {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String userFullName;
    private String userEmail;
    private String userPhoneNumber;
    private String userProfilePic;
    private String userGender;
    private String userCountry;
    private String userCounty;
    private String userDateOfBirth;
    private String userVerificationStatus;
    private String userId;

    public UsersInformation() {
    }

    public UsersInformation(String userFullName, String userEmail, String userPhoneNumber, String userProfilePic,
                            String userGender, String userCountry,
                            String userCounty, String userDateOfBirth, String userVerificationStatus, String userId) {
        this.userFullName = userFullName;
        this.userEmail = userEmail;
        this.userPhoneNumber = userPhoneNumber;
        this.userProfilePic = userProfilePic;
        this.userGender = userGender;
        this.userCountry = userCountry;
        this.userCounty = userCounty;
        this.userDateOfBirth = userDateOfBirth;
        this.userVerificationStatus = userVerificationStatus;
        this.userId = userId;
    }

    public void getUserData(){
        database.getReference()
                .child("Users")
                .child(mAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        assert  userModel!= null;
                        userFullName = userModel.getUserFullName();
                        userCountry = userModel.getUserCountry();
                        userCounty = userModel.getUserCounty();
                        userEmail = userModel.getUserEmail();
                        userPhoneNumber = userModel.getUserPhoneNumber();
                        userProfilePic = userModel.getUserProfilePic();
                        userGender = userModel.getUserGender();
                        userId = userModel.getUserId();
                        userDateOfBirth = userModel.getUserDateOfBirth();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public String getUserFullName() {
        return userFullName;
    }
}

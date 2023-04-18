package com.android.lifebox.Models;

public class UserModel {
    String userFullName;
    String userEmail;
    String userPhoneNumber;
    String userProfilePic;
    String userGender;
    String userCountry;
    String userCounty;
    String userDateOfBirth;
    String userVerificationStatus;
    String userId;
    String userIdNumber;

    public UserModel() {

    }

    public UserModel(String userIdNumber, String userFullName, String userEmail, String userPhoneNumber, String userProfilePic, String userGender, String userCountry, String userCounty, String userDateOfBirth, String userVerificationStatus) {
        this.userIdNumber = userIdNumber;
        this.userFullName = userFullName;
        this.userEmail = userEmail;
        this.userPhoneNumber = userPhoneNumber;
        this.userProfilePic = userProfilePic;
        this.userGender = userGender;
        this.userCountry = userCountry;
        this.userCounty = userCounty;
        this.userDateOfBirth = userDateOfBirth;
        this.userVerificationStatus = userVerificationStatus;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserProfilePic() {
        return userProfilePic;
    }

    public void setUserProfilePic(String userProfilePic) {
        this.userProfilePic = userProfilePic;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserCountry() {
        return userCountry;
    }

    public void setUserCountry(String userCountry) {
        this.userCountry = userCountry;
    }

    public String getUserCounty() {
        return userCounty;
    }

    public void setUserCounty(String userCounty) {
        this.userCounty = userCounty;
    }

    public String getUserDateOfBirth() {
        return userDateOfBirth;
    }

    public void setUserDateOfBirth(String userDateOfBirth) {
        this.userDateOfBirth = userDateOfBirth;
    }

    public String getUserVerificationStatus() {
        return userVerificationStatus;
    }

    public void setUserVerificationStatus(String userVerificationStatus) {
        this.userVerificationStatus = userVerificationStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

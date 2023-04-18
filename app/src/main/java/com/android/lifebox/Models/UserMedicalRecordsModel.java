package com.android.lifebox.Models;

public class UserMedicalRecordsModel {
    String bloodGroup;

    public UserMedicalRecordsModel() {
    }

    public UserMedicalRecordsModel(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }
}

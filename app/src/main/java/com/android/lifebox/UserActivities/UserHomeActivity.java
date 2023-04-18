package com.android.lifebox.UserActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toolbar;

import com.android.lifebox.Models.UserModel;
import com.android.lifebox.R;
import com.android.lifebox.SharedActivities.LoginActivity;
import com.android.lifebox.databinding.ActivityUserHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class UserHomeActivity extends AppCompatActivity {


    ActivityUserHomeBinding binding;
    private Toolbar toolbar;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        /**set up toolbar'
        ActionBar actionBar = getSupportActionBar();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View toolbarView = inflater.inflate(R.layout.toobar_layout, null);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
       actionBar.setCustomView(toolbarView);
       actionBar.setDisplayShowTitleEnabled(false); **/

        //show user profile
        showUserProfile();

        binding.btnGetBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserHomeActivity.this, GetBloodActivity.class));
            }
        });

        binding.btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserHomeActivity.this, UserSettingsActivity.class));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.action_settings:

                break;
            case R.id.action_logout:
                logout();
                break;
            case R.id.action_support:
                //support
                break;
            case R.id.action_share:
                //share app
            default:

        }
        return super.onOptionsItemSelected(item);
    }

    private void showUserProfile() {
        database.getReference()
                .child("Users")
                .child(mAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            UserModel userModel = snapshot.getValue(UserModel.class);
                            assert  userModel != null;
                            //show name
                            binding.txtUserFulName.setText(userModel.getUserFullName());
                        }
                        else{
                            binding.txtUserFulName.setText("- -");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    private void logout() {
        mAuth.signOut();
        startActivity(new Intent(UserHomeActivity.this, LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }
}
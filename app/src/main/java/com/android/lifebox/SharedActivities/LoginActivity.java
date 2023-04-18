package com.android.lifebox.SharedActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Adapter;

import com.android.lifebox.R;
import com.android.lifebox.SharedAdapters.LoginPagerAdapter;
import com.android.lifebox.UserActivities.UserHomeActivity;
import com.android.lifebox.databinding.ActivityLoginBinding;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;

    private LoginPagerAdapter vpAdapter;
    //array for tab tittles
    private String[] tabTitles = new String[]{"Member", "Counsellor", "Doctor"};
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        mAuth = FirebaseAuth.getInstance();
        //Disable app from requiring users to login at runtime
        if (mAuth.getCurrentUser()!= null){
            startActivity(new Intent(LoginActivity.this, UserHomeActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }

        //enable viewpager functionality with tabs
        vpAdapter = new LoginPagerAdapter(this);
        binding.viewPager.setAdapter(vpAdapter);
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> tab.setText(tabTitles[position])).attach();
    }
}
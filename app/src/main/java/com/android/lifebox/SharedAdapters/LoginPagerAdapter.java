package com.android.lifebox.SharedAdapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.android.lifebox.CounsellorFragments.CounsellorLoginFragment;
import com.android.lifebox.DoctorFragments.DoctorsLoginFragment;
import com.android.lifebox.UserFragments.UserLoginFragment;

public class LoginPagerAdapter extends FragmentStateAdapter {

    //array for tab tittles
    private String[] tabTitles = new String[]{"Member", "Counsellor", "Doctor"};
    public LoginPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }



    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new UserLoginFragment();
            case 1:
                return  new CounsellorLoginFragment();
            case 2:
                return new DoctorsLoginFragment();
        }
        return new UserLoginFragment();
    }

    @Override
    public int getItemCount() {
        return tabTitles.length;
    }
}

package com.umangmathur.mynewtwitterclone;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class LoginViewPagerAdapter extends FragmentPagerAdapter {

    ArrayList<LoginViewPagerModel> loginViewPagerModelsList = new ArrayList<LoginViewPagerModel>();

    public LoginViewPagerAdapter(FragmentManager fm, ArrayList<LoginViewPagerModel> loginViewPagerModelsList) {
        super(fm);
        this.loginViewPagerModelsList = loginViewPagerModelsList;
    }

    @Override
    public Fragment getItem(int position) {
        return LoginViewPagerFragment.newInstance(loginViewPagerModelsList.get(position));
    }

    @Override
    public int getCount() {
        return loginViewPagerModelsList.size();
    }

}

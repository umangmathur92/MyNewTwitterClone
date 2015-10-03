package com.umangmathur.mynewtwitterclone;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LoginViewPagerFragment extends Fragment {

    private static final String KEY_CONTENT = "LoginViewPagerFragment:Content";
    private LoginViewPagerModel loginViewPagerObj;
    private OnFragmentInteractionListener mListener;

    public static LoginViewPagerFragment newInstance(LoginViewPagerModel loginViewPagerObj) {
        LoginViewPagerFragment fragment = new LoginViewPagerFragment();
        fragment.loginViewPagerObj = loginViewPagerObj;
        return fragment;
    }

    public LoginViewPagerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            loginViewPagerObj = savedInstanceState.getParcelable(KEY_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_view_pager, container, false);
        ImageView profilePictureView = (ImageView) view.findViewById(R.id.profileFragmentProfilePictureView);
        RelativeLayout LoginViewPagerBackgroundLayout = (RelativeLayout) view.findViewById(R.id.LoginViewPagerBackgroundLayout);
        LoginViewPagerBackgroundLayout.setBackgroundResource(loginViewPagerObj.getBackgroundColor());
        profilePictureView.setImageResource(loginViewPagerObj.getResourceImage());
        TextView txtViewPager = (TextView) view.findViewById(R.id.txtViewPager);
        txtViewPager.setText(loginViewPagerObj.getDisplayText());
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_CONTENT, loginViewPagerObj);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(int uri);
    }

}

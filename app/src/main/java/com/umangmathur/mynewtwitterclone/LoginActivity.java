package com.umangmathur.mynewtwitterclone;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import static com.umangmathur.mynewtwitterclone.LoginViewPagerFragment.OnFragmentInteractionListener;

/**
 * Created by umang on 29/9/15.
 */
public class LoginActivity extends FragmentActivity implements OnFragmentInteractionListener {

    private Dialog progressDialog;
    private TwitterLoginButton twitterLoginBtn;
    private LoginViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private CirclePageIndicator circularPageIndicator;
    private RelativeLayout loginBackgroundLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        if (session != null) {
            openLandingPage();
        }
        setContentView(R.layout.login_screen);
        initUiElements();
        ArrayList<LoginViewPagerModel> loginViewPagerModelsList = getLoginViewPagerModelsListFromResources();
        viewPagerAdapter = new LoginViewPagerAdapter(this.getSupportFragmentManager(), loginViewPagerModelsList);
        viewPager.setAdapter(viewPagerAdapter);
        circularPageIndicator.setRadius(15.0f);
        circularPageIndicator.setViewPager(viewPager);
        twitterLoginBtn.setCallback(getLoginCallBack());
    }

    @NonNull
    private Callback<TwitterSession> getLoginCallBack() {
        return new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                openLandingPage();
            }

            @Override
            public void failure(TwitterException exception) {
                showToast("Failure : " + exception.getMessage());
            }
        };
    }

    private void openLandingPage() {
        Intent intent = new Intent(LoginActivity.this, LandingPageActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        twitterLoginBtn.onActivityResult(requestCode, resultCode, data);
    }

    private ArrayList<LoginViewPagerModel> getLoginViewPagerModelsListFromResources() {
        LoginViewPagerModel loginViewPagerModelPage1 = new LoginViewPagerModel(R.drawable.logo, R.drawable.img_ax, getString(R.string.login_intro_text_1));
        LoginViewPagerModel loginViewPagerModelPage2 = new LoginViewPagerModel(R.drawable.twitterbird, R.drawable.img_bx, getString(R.string.login_intro_text_2));
        LoginViewPagerModel loginViewPagerModelPage3 = new LoginViewPagerModel(R.drawable.tweet_gear, R.drawable.img_cx, getString(R.string.login_intro_text_3));
        ArrayList<LoginViewPagerModel> loginViewPagerModelsList = new ArrayList<LoginViewPagerModel>();
        loginViewPagerModelsList.add(loginViewPagerModelPage1);
        loginViewPagerModelsList.add(loginViewPagerModelPage2);
        loginViewPagerModelsList.add(loginViewPagerModelPage3);
        return loginViewPagerModelsList;
    }

    @Override
    public void onFragmentInteraction(int backgroundColor) {
        loginBackgroundLayout.setBackgroundColor(backgroundColor);
    }

    private void initUiElements() {
        loginBackgroundLayout = (RelativeLayout) findViewById(R.id.loginBackgroundLayout);
        viewPager = (ViewPager) findViewById(R.id.pager);
        circularPageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        twitterLoginBtn = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
    }

    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
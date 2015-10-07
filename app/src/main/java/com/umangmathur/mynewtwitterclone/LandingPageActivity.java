package com.umangmathur.mynewtwitterclone;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by umang on 27/9/15.
 */
public class LandingPageActivity extends CustomBaseActivity {

    private RecyclerView tweetRecyclerView;
    private TweetsAdapter tweetsAdapter;
    private List<Tweet> tweetList = new ArrayList<>();
    private StatusesService statusesService;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        initUiElements();
        tweetsAdapter = new TweetsAdapter(tweetList);
        tweetRecyclerView.setAdapter(tweetsAdapter);
        statusesService = Twitter.getApiClient().getStatusesService();
        fetchAndDisplayTweets();
    }

    private void fetchAndDisplayTweets() {
        if (Utils.isNetworkAvailable(this)) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Fetching Tweets");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
            statusesService.homeTimeline(NUM_OF_TWEETS, null, null, null, null, null, null, getHomeTimeLineCallback());
        } else {
            showToast(getString(R.string.check_internet_connection));
        }
    }

    private Callback<List<Tweet>> getHomeTimeLineCallback() {
        return new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                tweetList.clear();
                tweetList.addAll(result.data);
                tweetsAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void failure(TwitterException e) {
                progressDialog.dismiss();
                showToast(getString(R.string.failed_to_fetch_tweets));
                Log.e(MY_NEW_TWITTER_CLONE, getString(R.string.failed_to_fetch_tweets) + " : " + e.getMessage());
            }
        };
    }


    private void initUiElements() {
        tweetRecyclerView = (RecyclerView) findViewById(R.id.tweetRecyclerView);
        tweetRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new_tweet:
                showComposeTweetDialog();
                return true;
            case R.id.action_reload_tweets:
                fetchAndDisplayTweets();
                return true;
            case R.id.action_log_out:
                showToast("Log Out");
                logOutOfTwitter();
                redirectToLoginPage();
                return true;
            case R.id.action_about:
                showToast(getString(R.string.about_toast));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logOutOfTwitter() {
        Twitter.getSessionManager().clearActiveSession();
        Twitter.logOut();
    }

    private void redirectToLoginPage() {
        Intent intent = new Intent(LandingPageActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void showComposeTweetDialog() {
        Dialog composeTweetDialog = new Dialog(this, R.style.customDialogStyle);
        composeTweetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        composeTweetDialog.setContentView(R.layout.compose_tweet_layout);
        composeTweetDialog.show();
        EditText etStatus = (EditText) composeTweetDialog.findViewById(R.id.etStatus);
        Button btnTweet = (Button) composeTweetDialog.findViewById(R.id.btnTweet);
        TextView txtCharCount = (TextView) composeTweetDialog.findViewById(R.id.txtCharCount);
        txtCharCount.setText(MAX_CHARACTERS + " " + getString(R.string.chars_left));
        etStatus.addTextChangedListener(getTextWatcherForStatusText(txtCharCount));
        btnTweet.setOnClickListener(getTweetButtonClickListener(composeTweetDialog, etStatus));
    }

    private TextWatcher getTextWatcherForStatusText(final TextView txtCharCount) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int numOfCharsUsed = s.length();
                txtCharCount.setText(MAX_CHARACTERS - numOfCharsUsed + " " + getString(R.string.chars_left));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }

    private OnClickListener getTweetButtonClickListener(final Dialog composeTweetDialog, final EditText etStatus) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isNetworkAvailable(LandingPageActivity.this)) {
                    String strStatus = etStatus.getText().toString();
                    if (strStatus.length() > 0) {
                        statusesService.update(strStatus, null, null, null, null, null, null, null, null, getStatusUpdateCallback(composeTweetDialog));
                    } else {
                        etStatus.setError(getString(R.string.compulsory_field));
                    }
                } else {
                    showToast(getString(R.string.check_internet_connection));
                }
            }
        };
    }

    private Callback<Tweet> getStatusUpdateCallback(final Dialog composeTweetDialog) {
        return new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                showToast(getString(R.string.status_updated));
                composeTweetDialog.dismiss();
            }

            @Override
            public void failure(TwitterException e) {
                showToast(getString(R.string.status_update_failed));
                Log.e(MY_NEW_TWITTER_CLONE, getString(R.string.status_update_failed) + " : " + e.getMessage());
            }
        };
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.exit_title))
                .setMessage(getString(R.string.are_you_sure))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        LandingPageActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
}

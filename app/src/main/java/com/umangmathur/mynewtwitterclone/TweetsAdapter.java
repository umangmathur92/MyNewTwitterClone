package com.umangmathur.mynewtwitterclone;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

/**
 * Created by umang on 28/9/15.
 */
public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.TweetHolder> {

    private List<Tweet> tweetList;

    public TweetsAdapter(List<Tweet> tweetList) {
        this.tweetList = tweetList;
    }

    @Override
    public TweetHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.tweet_layout, parent, false);
        return new TweetHolder(view);
    }

    @Override
    public void onBindViewHolder(TweetHolder holder, int position) {
        Context context = holder.itemView.getContext();
        Tweet tweet = tweetList.get(position);
        holder.txtTweet.setText(tweet.text);
        holder.txtUserName.setText("@" + tweet.user.name);
        Picasso.with(context).load(tweet.user.profileBackgroundImageUrl).error(R.drawable.tw__ic_tweet_photo_error_light).into(holder.imgProfilePic);
        List<MediaEntity> mediaEntityList = tweet.entities.media;
        if (mediaEntityList != null) {
            holder.imgTweet.setVisibility(View.VISIBLE);
            String mediaUrl = mediaEntityList.get(0).mediaUrl;
            Picasso.with(context).load(mediaUrl).into(holder.imgTweet);
        } else {
            holder.imgTweet.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return tweetList.size();
    }

    public class TweetHolder extends ViewHolder {

        private TextView txtTweet, txtUserName;
        private ImageView imgTweet;
        private CircularImageView imgProfilePic;

        public TweetHolder(View itemView) {
            super(itemView);
            this.txtUserName = (TextView) itemView.findViewById(R.id.txtUserName);
            this.imgProfilePic = (CircularImageView) itemView.findViewById(R.id.imgProfilePic);
            this.txtTweet = (TextView) itemView.findViewById(R.id.tweetText);
            this.imgTweet = (ImageView) itemView.findViewById(R.id.imgTweet);
        }

    }
}

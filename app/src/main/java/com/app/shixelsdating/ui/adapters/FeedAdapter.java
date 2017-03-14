package com.app.shixelsdating.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.shixelsdating.R;
import com.app.shixelsdating.models.FeedItem;
import com.app.shixelsdating.ui.widgets.CircleImageView;
import com.app.shixelsdating.ui.widgets.WeMeetTextView;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wan on 3/14/17.
 */

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static SparseBooleanArray likedPositions;
    private LayoutInflater layoutInflater;
    private List<FeedItem> feedItems;

    private Context context;
    public FeedAdapter(Context context, List<FeedItem> feedItems) {
        this.feedItems = feedItems;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        likedPositions = new SparseBooleanArray();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = layoutInflater.inflate(R.layout.feed_item, parent, false);
        return new FeedItemHolder(convertView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FeedItemHolder feedItemHolder = (FeedItemHolder) holder;
        FeedItem feedItem = feedItems.get(position);
        if (feedItem != null) {
            feedItemHolder.bindFeedData(context,feedItem, position);
        }
    }

    @Override
    public int getItemCount() {
        return feedItems != null ? feedItems.size() : 0;
    }

    static class FeedItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.feed_publisher_image_view)
        CircleImageView feedPublisherImageView;

        @BindView(R.id.feed_publisher_name_view)
        WeMeetTextView feedPublisherNameView;

        @BindView(R.id.feed_type)
        WeMeetTextView feedType;

        @BindView(R.id.feed_publish_date)
        WeMeetTextView feedPublishedDate;

        @BindView(R.id.feed_content_image_view)
        ImageView feedContentImageView;

        @BindView(R.id.feed_content_description)
        WeMeetTextView feedContentDescription;

        @BindView(R.id.feed_available_slots)
        WeMeetTextView feedAvailableSlots;

        @BindView(R.id.like_feed_image_view)
        ImageView likeFeedImageView;

        @BindView(R.id.feed_likes_counter)
        WeMeetTextView feedLikesCounter;

        @BindView(R.id.feed_comment_counter)
        WeMeetTextView feedCommentCounter;

        @BindView(R.id.feed_shares_counter)
        WeMeetTextView feedSharesCounter;

        Context context;

        FeedItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindFeedData(Context context,FeedItem feedItem, int position) {

            this.context = context;

            feedPublisherImageView.setImageResource(feedItem.getFeedPublisherPhotoId());
            feedPublisherNameView.setText(feedItem.getFeedPublisherName());
            feedType.setText(feedItem.getFeedType());
            feedPublishedDate.setText(feedItem.getFeedSharedTime());

            Glide.with(context).load(feedItem.getFeedContentImageView()).crossFade().into(feedContentImageView);

            feedContentImageView.setColorFilter(ContextCompat.getColor(context,R.color.feed_content_image_tint_color));

            feedContentDescription.setText(feedItem.getFeedContentDescription());
            feedAvailableSlots.setText(String.valueOf(feedItem.getLeftAvailSlots()));
            feedLikesCounter.setText(String.valueOf(feedItem.getFeedLikesCount()));
            feedCommentCounter.setText(String.valueOf(feedItem.getFeedCommentsCount()));
            feedSharesCounter.setText(String.valueOf(feedItem.getFeedSharesCount()));

            if (feedItem.isLiked()) {
                likedPositions.put(position, true);
                likeFeedImageView.getDrawable().setColorFilter(ContextCompat.getColor(context,R.color.colorPrimaryDark), PorterDuff.Mode.MULTIPLY);
                feedLikesCounter.setTextColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));
            } else {
                likedPositions.put(position, false);
                feedLikesCounter.setTextColor(ContextCompat.getColor(context,R.color.icons_color));
            }

            invalidatePositions(position);

        }

        private void invalidatePositions(int position){
            if (likedPositions.get(position)) {
                likeFeedImageView.getDrawable().setColorFilter(ContextCompat.getColor(context,R.color.colorPrimaryDark), PorterDuff.Mode.MULTIPLY);
                feedLikesCounter.setTextColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));
            } else {
                likeFeedImageView.setColorFilter(ContextCompat.getColor(context,R.color.icons_color));
                feedLikesCounter.setTextColor(ContextCompat.getColor(context,R.color.icons_color));
            }
        }

    }

}

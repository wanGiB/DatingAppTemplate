package com.app.wemeet.utils;

import com.app.wemeet.R;
import com.app.wemeet.models.FeedItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wan Clem
 */

public class DummyFeedManager {

    public static List<FeedItem> generateDUmmyFeeds() {
        List<FeedItem> feedItems = new ArrayList<>();
        feedItems.add(new FeedItem("Wan Clem", R.drawable.driving_a_car, "Posted", "2hr", R.drawable.mercedes_benz, "A very nice mercedez Benz", 15, 35, 15, 22, true));
        feedItems.add(new FeedItem("Sean Parker", R.drawable.man_in_suit, "Shared", "3hr", R.drawable.suits, "Men with class", 19, 30, 10, 28, false));
        feedItems.add(new FeedItem("Ivanka TimberLake", R.drawable.girl_jogging, "Shared", "4hr", R.drawable.joggers, "Awesome joggers", 74, 42, 90, 11, true));
        feedItems.add(new FeedItem("Angelina Blanca", R.drawable.descent, "Posted", "5hr", R.drawable.shoes, "Nice pair of shoes", 18, 39, 20, 25, false));
        feedItems.add(new FeedItem("Bradly Gates", R.drawable.riding_bycle, "Posted", "6hr", R.drawable.power_bike, "A very nice power bike", 15, 35, 15, 22, true));
        return feedItems;
    }

}

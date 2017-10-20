package com.sharkna.khaled.sharkna.model;

import android.net.Uri;

/**
 * Created by Khaled on 10/20/2017.
 * Assumptions
 * Descriptions
 */

public class FeedItem {
    public int likesCount;
    public boolean isLiked;
    public Uri photoUri;

    public FeedItem(int likesCount, boolean isLiked) {
        this.likesCount = likesCount;
        this.isLiked = isLiked;
    }
}

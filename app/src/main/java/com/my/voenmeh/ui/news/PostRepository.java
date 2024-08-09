package com.my.voenmeh.ui.news;

import android.graphics.Bitmap;

import com.squareup.picasso.Picasso;

public class PostRepository {
    private String mtext;
    private String mimageUrl;

    PostRepository(String t, String url) {
        mtext = t;
        mimageUrl = url;
    }

    String getText() {
        return mtext;
    }

    String getImageUrl() {
        return mimageUrl;
    }
}

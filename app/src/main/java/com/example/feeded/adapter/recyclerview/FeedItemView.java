package com.example.feeded.adapter.recyclerview;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.feeded.R;
import com.example.feeded.element.Feed;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.list_item_feed)
public class FeedItemView extends LinearLayout {

    @ViewById
    TextView feedName;

    @ViewById
    TextView title;

    public FeedItemView(final Context context) {
        super(context);
    }

    public void bind(final Feed feed) {
        feedName.setTypeface(null, Typeface.BOLD);
        feedName.setText(feed.getTitle());
    }
}
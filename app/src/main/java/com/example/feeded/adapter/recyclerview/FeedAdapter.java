package com.example.feeded.adapter.recyclerview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.feeded.element.Feed;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

@EBean
public class FeedAdapter extends BaseAdapter {

    private List<Feed> feeds;

    @RootContext
    Context context;

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        FeedItemView feedItemView;

        if (convertView == null) {
            feedItemView = FeedItemView_.build(context);
        } else {
            feedItemView = (FeedItemView) convertView;
        }
        feedItemView.bind(this.getItem(position));
        return feedItemView;
    }

    @Override
    public int getCount() {
        return feeds == null ? 0 : feeds.size();
    }

    @Override
    public Feed getItem(final int position) {
        return feeds.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    public void updateEntries(final List<Feed> feeds) {
        this.feeds = feeds;
        //Triggers the list update
        this.notifyDataSetChanged();
    }

}
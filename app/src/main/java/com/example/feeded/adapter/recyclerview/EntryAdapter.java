package com.example.feeded.adapter.recyclerview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.feeded.element.Entry;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

@EBean
public class EntryAdapter extends BaseAdapter {

    private List<Entry> entries;

    @RootContext
    Context context;

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        EntryItemView feedItemView;

        if (convertView == null) {
            feedItemView = EntryItemView_.build(context);
        } else {
            feedItemView = (EntryItemView) convertView;
        }
        feedItemView.bind(this.getItem(position));
        return feedItemView;
    }

    @Override
    public int getCount() {
        return entries == null ? 0 : entries.size();
    }

    @Override
    public Entry getItem(final int position) {
        return entries.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    public void updateEntries(final List<Entry> entries) {
        this.entries = entries;
        //Triggers the list update
        this.notifyDataSetChanged();
    }


}

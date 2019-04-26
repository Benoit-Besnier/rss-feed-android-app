package com.example.feeded.adapter.recyclerview;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.feeded.R;
import com.example.feeded.element.Entry;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@EViewGroup(R.layout.list_item_entry)
public class EntryItemView extends LinearLayout {

    @ViewById
    TextView title;

    @ViewById
    TextView date;

    public EntryItemView(final Context context) {
        super(context);
    }

    public void bind(final Entry feed) {
        title.setTypeface(null, Typeface.BOLD);
        title.setText(feed.getTitle());

        date.setText(this.format(feed.getUpdatedDate()));
    }

    private String format(final Date date) {
        return date != null
                ? new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date)
                : "Undefined";
    }
}
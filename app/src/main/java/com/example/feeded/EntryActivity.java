package com.example.feeded;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.example.feeded.adapter.recyclerview.EntryAdapter;
import com.example.feeded.element.Entry;
import com.example.feeded.element.Feed;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity
public class EntryActivity extends AppCompatActivity {

    @ViewById
    ListView entries;

    @Bean
    EntryAdapter adapter;

    @AfterViews
    void bindAdapter() {
        entries.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        final Feed feed = (Feed) getIntent().getSerializableExtra("selectedFeed");
        this.setTitle(feed.getTitle());
        this.updateList(feed.getEntries());
    }

    @UiThread
    void updateList(final List<Entry> entries) {
        this.adapter.updateEntries(entries);
    }

    @ItemClick
    void entriesItemClicked(final Entry entry) {
        final Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(entry.getUri()));

        showToast("Entry {" + entry.getTitle() + "} click event");
        this.startActivity(browserIntent);
    }

    @UiThread
    void showToast(final String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

}

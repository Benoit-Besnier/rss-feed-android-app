package com.example.feeded;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.feeded.adapter.recyclerview.FeedAdapter;
import com.example.feeded.client.FeedClient;
import com.example.feeded.client.UserClient;
import com.example.feeded.dialog.AddNewFeedDialog;
import com.example.feeded.dialog.AddNewFeedDialog_;
import com.example.feeded.element.Feed;
import com.example.feeded.element.custom.FeedSubmission;
import com.example.feeded.element.custom.PreferredFeedSubmission;
import com.example.feeded.element.custom.UserDetails;
import com.example.feeded.handler.error.ForbiddenErrorHandler;
import com.example.feeded.service.SessionManager;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@EActivity
@OptionsMenu(R.menu.main_menu)
public class FeedActivity extends AppCompatActivity {

    private final static String CONTEXT_MENU_ADD = "Add";
    private final static String CONTEXT_MENU_DELETE = "Delete";

    @ViewById
    ListView feeds;

    @ViewById
    BottomNavigationView navigation;

    @ViewById
    FloatingActionButton addFab;

    @Bean
    FeedAdapter adapter;

    SessionManager sessionManager;

    UserDetails userDetails;

    @RestService
    FeedClient feedClient;

    @RestService
    UserClient userClient;

    @Bean
    ForbiddenErrorHandler forbiddenErrorHandler;

    private boolean _defaultDisplay;

    private Feed _lastSelectedFeed;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    switchAndUpdateEntries(false);
                    addFab.hide();
                    return true;
                case R.id.navigation_dashboard:
                    switchAndUpdateEntries(true);
                    addFab.show();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        // Bind navbar
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        // Setup session-manager
        sessionManager = new SessionManager(getApplicationContext());
        // Initialize fab to hidden
        addFab.hide();
        // Initialize list to 'custom'
        this.switchAndUpdateEntries(false);
        // Setup listview to be used with context-menu
        this.registerForContextMenu(feeds);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        int order = 0;

        this._lastSelectedFeed = (Feed) feeds.getAdapter().getItem(info.position);

        menu.setHeaderTitle("Preference list actions: ");
        if (this._defaultDisplay && !this.isFeedInPreferenceList(this._lastSelectedFeed)) {
            menu.add(0, v.getId(), order++, CONTEXT_MENU_ADD);
        } else {
            showToast(this._lastSelectedFeed.getTitle() + " is already in pref list");
        }
        menu.add(0, v.getId(), order, CONTEXT_MENU_DELETE);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        final List<String> preferredFeeds = this.userDetails.getPreferredFeeds();

        if (item.getTitle() == CONTEXT_MENU_ADD) {
            preferredFeeds.add(this._lastSelectedFeed.getUuid());
            this.updatePreferredFeeds(preferredFeeds);
            showToast(this._lastSelectedFeed.getTitle() + " set in preference list.");
        } else if (item.getTitle() == CONTEXT_MENU_DELETE) {
            preferredFeeds.remove(this._lastSelectedFeed.getUuid());
            this.updatePreferredFeeds(preferredFeeds);
            showToast(this._lastSelectedFeed.getTitle() + " remove from preference list.");
        } else {
            return false;
        }

        return true;
    }

    @AfterViews
    void bindAdapter() {
        feeds.setAdapter(adapter);
    }

    @AfterInject
    void afterInject() {
        this.userClient.setRestErrorHandler(forbiddenErrorHandler);
        this.feedClient.setRestErrorHandler(forbiddenErrorHandler);
    }

    @ItemClick
    void feedsItemClicked(final Feed feed) {
        final Intent intent = new Intent(this, EntryActivity_.class);

        intent.putExtra("selectedFeed", feed);
        this.startActivity(intent);
        showToast("Feed {" + feed.getTitle() + "} click event");
    }

    @Click({R.id.add_fab})
    void addFeed() {
        // create a FragmentTransaction from the FragmentManager
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null);

        // Create and show the dialog.
        AddNewFeedDialog dFrag = AddNewFeedDialog_.builder().build();
        dFrag.show(ft, "Dialog");
    }

    @OptionsItem(R.id.action_logout)
    void logout() {
        this.sessionManager.logoutUser();
    }

    @Background
    void switchAndUpdateEntries(final boolean displayDefault) {
        if (displayDefault) {
            this.updateAllFeedsList();
        } else {
            this.updateUserDetails(true);
        }
        this._defaultDisplay = displayDefault;
    }

    @Background
    public void updateAllFeedsList() {
        this.updateList(this.feedClient.getAllFeeds());
    }

    @Background
    public void updateUserDetails(final boolean updateList) {
        final String token = this.sessionManager.getUserDetails().get(SessionManager.KEY_TOKEN);

        this.userClient.setBearerAuth(token);
        this.userDetails = this.userClient.getPersonalInformation();

        if (userDetails != null) {
            Log.i("[USER DETAILS]", this.userDetails.toString());

            if (updateList) {
                this.feedClient.setBearerAuth(token);
                this.updateListWithPreferredFeeds();
            }
        } else {
            this.sessionManager.logoutUser();
        }
    }

    @Background
    void updatePreferredFeeds(final List<String> preferredFeeds) {
        try {
            final ResponseEntity response = this.userClient.putPreferredFeeds(
                    new PreferredFeedSubmission(preferredFeeds));

            if (response != null && response.getStatusCode().is2xxSuccessful()) {
                showToast("Update user preference successfully.");
                this.updateUserDetails(!this._defaultDisplay);
            } else {
                showToast("Update user preference failed.");
            }
        } catch (final Exception e) {
            Log.e("UPDATE PREF FEED", e.getMessage());
            showToast("Update user preference failed.");
        }
    }

    @Background
    void updateListWithPreferredFeeds() {
        final List<Feed> preferredFeeds = new ArrayList<>();

        for (final String feedId : this.userDetails.getPreferredFeeds()) {
            preferredFeeds.add(this.feedClient.getFeedById(feedId));
        }
        this.updateList(preferredFeeds);
    }

    @Background
    public void postNewFeed(final FeedSubmission feedSubmission) {
        try {
            final ResponseEntity response = this.feedClient.postNewFeed(feedSubmission);

            if (response != null && response.getStatusCode().is2xxSuccessful()) {
                showToast("Submit <" + feedSubmission.getUrl() + "> successfully.");
                this.updateAllFeedsList();
            } else {
                showToast("Submit <" + feedSubmission.getUrl() + "> failed. Please retry !");
            }
        } catch (final Exception e) {
            Log.e("POST NEW FEED", e.getMessage());
            showToast("Submit <" + feedSubmission.getUrl() + "> failed. Please retry !");
        }
    }

    @UiThread
    void updateList(final List<Feed> feeds) {
        this.adapter.updateEntries(feeds);
    }

    @UiThread
    void showToast(final String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private boolean isFeedInPreferenceList(final Feed feed) {
        if (this.userDetails == null) {
            this.updateUserDetails(false);
        }
        Log.i("PREF LIST", this.userDetails.getPreferredFeeds().toString());
        Log.i("PREF LIST", feed.getUuid());
        return this.userDetails.getPreferredFeeds().contains(feed.getUuid());
    }
}

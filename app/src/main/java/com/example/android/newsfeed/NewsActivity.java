package com.example.android.newsfeed;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>, ClickListener {
    @BindView(R.id.news_recycler_view)
    RecyclerView newsRecyclerView;
    @BindView(R.id.empty_image_view)
    ImageView emptyImageView;
    @BindView(R.id.empty_text_view)
    TextView emptyTextView;
    @BindView(R.id.loading_spinner)
    ProgressBar loadingSpinner;
    @BindView(R.id.swipe_refresher)
    SwipeRefreshLayout swipeRefresher;

    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);
        ButterKnife.bind(this);

        //Making disappeared the no internet image view and text view
        emptyImageView.setVisibility(View.GONE);
        emptyTextView.setVisibility(View.GONE);

        // Configure the refreshing color
        swipeRefresher.setColorSchemeResources(android.R.color.holo_blue_bright);

        myUpdateOperation();

        swipeRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myUpdateOperation();

            }
        });

        // Create a new adapter that takes an empty list of news as input
        newsAdapter = new NewsAdapter(new ArrayList<News>(), this, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        //I use this to have good performance
        newsRecyclerView.setHasFixedSize(true);
        newsRecyclerView.setLayoutManager(mLayoutManager);
        //Default animation for adding and deleting, unfortunately not used
        newsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        // A divider between the CardViews
        newsRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        newsRecyclerView.setAdapter(newsAdapter);


    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //Page size option, default value 10
        String pageSize = sharedPreferences.getString(getString(R.string.settings_page_size_key),
                getString(R.string.settings_page_size_default));

        //Search news using a keyword
        String search = sharedPreferences.getString(getString(R.string.settings_search_key),
                getString(R.string.settings_search_default));

        //Order By settings, default value newest
        String orderBy = sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );


        //From date settings, default value 2018-01-01
        String fromDate = sharedPreferences.getString(
                getString(R.string.settings_from_date_key),
                getString(R.string.settings_from_date_default)
        );

        Set<String> sectionCategory = sharedPreferences.getStringSet(
                getString(R.string.settings_category_key),
                //Default value, so when the user launches the app for first time it shows news
                new HashSet<>(Collections.singletonList(Constants.WORLD)));

        StringBuilder formattedSectionCategory = new StringBuilder();
            //Go throw all the elements and after each other with a or | between
            for (String key : sectionCategory) {
                formattedSectionCategory.append(key).append("|");
        }
        //Delete the last character which is a or '|'
        formattedSectionCategory.setLength(formattedSectionCategory.length() - 1);


        // parse breaks apart the URI string that's passed into its parameter
        Uri rootUri = Uri.parse(Constants.ROOT_URI);

        // buildUpon prepares the rootUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = rootUri.buildUpon();

        //searching by a given key world
        uriBuilder.appendQueryParameter(Constants.Q, search);
        // a given news categoryTextView, or even multiple given news categories
        uriBuilder.appendQueryParameter(Constants.SECTION, formattedSectionCategory.toString());
        //show news how to be ordered, relevance in conjunction with the keyword
        uriBuilder.appendQueryParameter(Constants.ORDER_BY, orderBy);
        // how many news should be shown
        uriBuilder.appendQueryParameter(Constants.PAGE_SIZE, pageSize);
        // showing the contributor
        uriBuilder.appendQueryParameter(Constants.SHOW_TAGS, Constants.CONTRIBUTOR);
        // showing the thumbnail
        uriBuilder.appendQueryParameter(Constants.SHOW_FIELDS, Constants.THUMBNAIL);
        //shows news on or after a given date
        uriBuilder.appendQueryParameter(Constants.FROM_DATE, fromDate);
        //my guardian api key
        uriBuilder.appendQueryParameter(Constants.API_KEY, BuildConfig.GUARDIAN_API_KEY);

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsList) {
        // Hide loading indicator because the data has been loaded
        loadingSpinner.setVisibility(View.GONE);
        // If there is a valid list of {@link News}s, then add them to the adapter's data set. This will trigger the ListView to update.
        if (newsList != null && !newsList.isEmpty()) {
            newsAdapter.clear();
            newsAdapter.addAll(newsList);
            swipeRefresher.setRefreshing(false);
        } else {
            // Set empty state text and image to display "No earthquakes found."
            emptyImageView.setVisibility(View.VISIBLE);
            emptyImageView.setImageResource(R.drawable.no_news);
            emptyTextView.setVisibility(View.VISIBLE);
            emptyTextView.setText(R.string.no_news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        // Clear the adapter of previous newsList data
        newsAdapter.clear();
    }

    @Override
    //Custom ClickListener here we add what it should do
    public void openingWebsiteOnClickListener(int position) {
        //Opening the current news story website
        Uri uri = Uri.parse(newsAdapter.getItemPosition(position).getWebUrl());
        Intent websiteIntent = new Intent(Intent.ACTION_VIEW, uri);

        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(websiteIntent,
                PackageManager.MATCH_DEFAULT_ONLY);
        //Verify if there is a web browser installed on the device
        if (activities.size() > 0) {
            startActivity(websiteIntent);
        }

        Toast.makeText(this, R.string.opening_website, Toast.LENGTH_LONG).show();
    }

    @Override
    public void sharingWebsiteOnClickListener(int position) {
        String website = newsAdapter.getItemPosition(position).getWebUrl();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType(getString(R.string.string_type));
        shareIntent.putExtra(Intent.EXTRA_TEXT, website);
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.intresting_news));

        //Verify if there is any application which can send our link
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(shareIntent,
                PackageManager.MATCH_DEFAULT_ONLY);

        if (activities.size() > 0) {
            startActivity(Intent.createChooser(shareIntent,getString(R.string.share)));
        }
    }

    @Override
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /*
     * Listen for option item selections so that we receive a notification
     * when the user requests a refresh by selecting the refresh action bar item.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        if (id == R.id.menu_refresh) {
            // Signal SwipeRefreshLayout to start the progress indicator
            swipeRefresher.setRefreshing(true);

            // Start the refresh background task.
            // This method calls setRefreshing(false) when it's finished.
            myUpdateOperation();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void myUpdateOperation() {
        //Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        assert connMgr != null;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(Constants.NEWS_LOADER_ID, null, this);

            //Hide pictures after internet is back
            emptyImageView.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.GONE);
        } else {
            // Otherwise, display error
            // First, hide loading indicator and so error message will be visible
            loadingSpinner.setVisibility(View.GONE);
            emptyImageView.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.VISIBLE);

            // Update empty state with no connection error message
            emptyImageView.setImageResource(R.drawable.no_internet_connection);
            emptyTextView.setText(R.string.no_internet);
        }
    }

}

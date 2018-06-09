package com.example.android.newsfeed;

public class Constants {

    /**
     * Constant value for the news loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    public static final int NEWS_LOADER_ID = 1;

    /**
     * URL for news data from the guardian dataset
     */
    public static final String ROOT_URI = "http://content.guardianapis.com/search";

    // String constants for building the query
    public static final String Q = "q";
    public static final String SECTION = "section";
    public static final String ORDER_BY = "order-by";
    public static final String PAGE_SIZE = "page-size";
    public static final String SHOW_TAGS = "show-tags";
    public static final String CONTRIBUTOR = "contributor";
    public static final String SHOW_FIELDS = "show-fields";
    public static final String THUMBNAIL = "thumbnail";
    public static final String FROM_DATE = "from-date";
    public static final String API_KEY = "api-key";
    // Default value category
    public static final String WORLD = "world";

}

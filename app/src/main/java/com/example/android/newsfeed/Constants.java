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
    public static final String WORLD_NEWS_DEFAULT_VALUE = "world";
    // Default value category


    public static final String NEWS = "News";
    public static final String WORLD_NEWS = "World News";
    public static final String UK_NEWS = "Uk News";
    public static final String CITIES = "Cities";
    public static final String GLOBAL_DEVELOPMENT = "Global Development";
    public static final String TECHNOLOGY = "Technology";
    public static final String BUSINESS = "Business";
    public static final String ENVIRONMENT = "Environment";
    public static final String EDUCATION = "Education";
    public static final String SOCIETY = "Society";
    public static final String SCIENCE = "Science";
    public static final String OPINION = "Opinion";
    public static final String SPORT = "Sport";
    public static final String FOOTBALL = "Football";
    public static final String CULTURE = "Culture";
    public static final String BOOKS = "Books";
    public static final String MUSIC = "Music";
    public static final String TV_AND_RADIO = "TV and Radio";
    public static final String ART_AND_DESIGN = "Art and Design";
    public static final String FILMS = "Films";
    public static final String GAMES = "Games";
    public static final String STAGE = "Stage";
    public static final String LIFE_AND_STYLE = "Life and Style";
    public static final String FASHION = "Fashion";
    public static final String TRAVEL = "Travel";
    public static final String MONEY = "Money";


}

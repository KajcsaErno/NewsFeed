package com.example.android.newsfeed;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<News> fetchNewsData(String requestUrl) {
        //create URL object
        URL url = createUrl(requestUrl);

        //perform HTTP request to the URL and receive a JSON response back
        String jsonRepose = null;
        try {
            jsonRepose = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        return extractFeatureFromJson(jsonRepose);

    }


    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with crating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        //If the URL is not null, then return early
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            //if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                sb.append(line);
                line = reader.readLine();
            }
        }
        return sb.toString();
    }

    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing a JSON response.
     */
    private static ArrayList<News> extractFeatureFromJson(String newsJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding news to
        ArrayList<News> newsArrayList = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // creating a base json
            JSONObject baseJsonResponse = new JSONObject(newsJSON);

            //extracting the response JSON Object
            JSONObject responseJsonObject = baseJsonResponse.getJSONObject("response");

            //extracting the results JSON Array
            JSONArray newsArray = responseJsonObject.getJSONArray("results");

            //getting all the results
            for (int i = 0; i < newsArray.length(); i++) {

                // Get a single news at position i within the list of news
                JSONObject currentNews = newsArray.getJSONObject(i);

                // Extract the values
                String webTitle = currentNews.getString("webTitle");
                String category = currentNews.getString("sectionName");
                String publicationDate = currentNews.getString("webPublicationDate");

                //Formatting the date and time
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss'Z'", Locale.getDefault());
                simpleDateFormat.setTimeZone(TimeZone.getDefault());
                Date date = null;
                try {
                    date = simpleDateFormat.parse(publicationDate);
                } catch (ParseException e) {
                    Log.e(LOG_TAG, "Problem with parsing the date.", e);
                }
                String formattedPublicationDate = formatDate(date);
                String webUrl = currentNews.getString("webUrl");

                String authorName = "";
                JSONArray tagsArray = currentNews.getJSONArray("tags");
                for (int j = 0; j < tagsArray.length(); j++) {
                    JSONObject jsonObject = tagsArray.getJSONObject(j);
                    authorName = jsonObject.getString("webTitle");
                }

                JSONObject fieldsObject = null;
                try {
                    fieldsObject = currentNews.getJSONObject("fields");
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "There is no value for the fields JSONObject");
                }

                String thumbnail;
                if (fieldsObject != null) {
                    thumbnail = fieldsObject.getString("thumbnail");
                } else {
                    thumbnail = "https://www.pshe-association.org.uk/sites/all/themes/PSHE/img/default-pshe-square.png";
                }

                // Create a new {@link News} object with the magnitude, location, time,
                // and url from the JSON response.
                News news = new News(webTitle, category, formattedPublicationDate, webUrl, authorName, thumbnail);

                // Add the new {@link News} to the list of news.
                newsArrayList.add(news);
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the news JSON results", e);
        }

        // Return the list of news
        return newsArrayList;
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private static String formatDate(Date dateObject) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM d yyyy HH:mm", Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        return simpleDateFormat.format(dateObject);
    }

}

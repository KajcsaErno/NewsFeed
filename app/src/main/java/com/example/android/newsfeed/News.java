package com.example.android.newsfeed;

public class News {

    private String webTitle;
    private String category;
    private String publicationDate;
    private String webUrl;
    private String authorName;
    private String thumbnail;

    News(String webTitle, String category, String publicationDate, String webUrl, String authorName, String thumbnail) {
        this.webTitle = webTitle;
        this.category = category;
        this.publicationDate = publicationDate;
        this.webUrl = webUrl;
        this.authorName = authorName;
        this.thumbnail = thumbnail;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public String getCategory() {
        return category;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}

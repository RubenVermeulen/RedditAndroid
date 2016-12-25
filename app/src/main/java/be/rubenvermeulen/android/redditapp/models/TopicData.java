package be.rubenvermeulen.android.redditapp.models;

import android.text.format.DateUtils;

import com.google.gson.annotations.SerializedName;

public class TopicData {
    private String title;
    private String author;
    private long created;
    private String subreddit;
    private String thumbnail;

    @SerializedName("num_comments")
    private int NumberOfComments;

    private String url;
    private int ups;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public int getNumberOfComments() {
        return NumberOfComments;
    }

    public void setNumberOfComments(int numberOfComments) {
        NumberOfComments = numberOfComments;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getUps() {
        return ups;
    }

    public void setUps(int ups) {
        this.ups = ups;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getHumandReadableTimestamp() {
        return DateUtils.getRelativeTimeSpanString(created * 1000).toString();
    }
}

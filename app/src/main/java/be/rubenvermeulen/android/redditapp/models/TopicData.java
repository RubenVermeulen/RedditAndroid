package be.rubenvermeulen.android.redditapp.models;

import android.text.format.DateUtils;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class TopicData implements Serializable {
    private static final long serialVersionUID = 0;

    private String name;
    private String title;
    private String author;
    private long created;
    private String subreddit;
    private String thumbnail;

    @SerializedName("num_comments")
    private int NumberOfComments;

    private String url;
    private int ups;

    @Generated(hash = 2045220826)
    public TopicData(String name, String title, String author, long created,
            String subreddit, String thumbnail, int NumberOfComments, String url,
            int ups) {
        this.name = name;
        this.title = title;
        this.author = author;
        this.created = created;
        this.subreddit = subreddit;
        this.thumbnail = thumbnail;
        this.NumberOfComments = NumberOfComments;
        this.url = url;
        this.ups = ups;
    }

    @Generated(hash = 1711042654)
    public TopicData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

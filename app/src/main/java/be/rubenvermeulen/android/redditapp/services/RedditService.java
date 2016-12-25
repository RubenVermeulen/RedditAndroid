package be.rubenvermeulen.android.redditapp.services;

import be.rubenvermeulen.android.redditapp.models.Result;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RedditService {
    @GET("r/AskReddit/hot.json")
    Call<Result> askReddit();

    @GET("r/funny/hot.json")
    Call<Result> funny();

    @GET("r/todayilearned/hot.json")
    Call<Result> todayILearned();

    @GET("r/science/hot.json")
    Call<Result> science();

    @GET("r/pics/hot.json")
    Call<Result> pics();

    @GET("r/worldnews/hot.json")
    Call<Result> worldNews();

    @GET("r/announcements/hot.json")
    Call<Result> announcements();

    @GET("r/gaming/hot.json")
    Call<Result> gaming();
}

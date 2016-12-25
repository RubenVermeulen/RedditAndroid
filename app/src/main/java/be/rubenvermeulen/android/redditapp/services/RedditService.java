package be.rubenvermeulen.android.redditapp.services;

import be.rubenvermeulen.android.redditapp.models.Result;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RedditService {
    @GET("r/AskReddit/hot.json")
    Call<Result> askReddit(@Query("after") String after, @Query("limit") int limit);

    @GET("r/funny/hot.json")
    Call<Result> funny(@Query("after") String after, @Query("limit") int limit);

    @GET("r/todayilearned/hot.json")
    Call<Result> todayILearned(@Query("after") String after, @Query("limit") int limit);

    @GET("r/science/hot.json")
    Call<Result> science(@Query("after") String after, @Query("limit") int limit);

    @GET("r/pics/hot.json")
    Call<Result> pics(@Query("after") String after, @Query("limit") int limit);

    @GET("r/worldnews/hot.json")
    Call<Result> worldNews(@Query("after") String after, @Query("limit") int limit);

    @GET("r/announcements/hot.json")
    Call<Result> announcements(@Query("after") String after, @Query("limit") int limit);

    @GET("r/gaming/hot.json")
    Call<Result> gaming(@Query("after") String after, @Query("limit") int limit);
}

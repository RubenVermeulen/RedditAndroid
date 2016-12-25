package be.rubenvermeulen.android.redditapp.services;

import be.rubenvermeulen.android.redditapp.models.Result;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RedditService {
    @GET("r/{subreddit}/{category}.json")
    Call<Result> getSubreddit(@Path("subreddit") String subreddit,
                              @Path("category") String category,
                              @Query("after") String after,
                              @Query("limit") int limit);
}

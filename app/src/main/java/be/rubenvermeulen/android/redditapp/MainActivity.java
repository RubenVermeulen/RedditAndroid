package be.rubenvermeulen.android.redditapp;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import be.rubenvermeulen.android.redditapp.helpers.Category;
import be.rubenvermeulen.android.redditapp.helpers.EndlessRecyclerOnScrollListener;
import be.rubenvermeulen.android.redditapp.helpers.Subreddit;
import be.rubenvermeulen.android.redditapp.models.Result;
import be.rubenvermeulen.android.redditapp.models.Topic;
import be.rubenvermeulen.android.redditapp.services.RedditService;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.topics)
    RecyclerView rvTopics;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    private final int THRESHOLD = 20;

    private Toolbar toolbar;
    private LinearLayoutManager layoutManager;
    private RedditService service;
    private boolean firstLoad = true;
    private String after = null;
    private Subreddit subreddit;
    private Category category = Category.hot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ButterKnife.bind(this);

        layoutManager = new LinearLayoutManager(this);

        rvTopics.setLayoutManager(layoutManager);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.reddit.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(RedditService.class);

        // Swipe refresh
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                after = null;
                rvTopics.removeAllViews();
                loadSubreddit(service.getSubreddit(subreddit.toString(), category.toString(), after, THRESHOLD));
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_reload) {
            swipeRefresh.setRefreshing(true);
            after = null;
            rvTopics.removeAllViews();
            loadSubreddit(service.getSubreddit(subreddit.toString(), category.toString(), after, THRESHOLD));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        int index;

        swipeRefresh.setRefreshing(true);

        if (id == R.id.askReddit) {
            subreddit = Subreddit.AskReddit;
            index = 0;
        }
        else if (id == R.id.funny) {
            subreddit = Subreddit.funny;
            index = 1;
        }
        else if (id == R.id.todayILearned) {
            subreddit = Subreddit.todayilearned;
            index = 2;
        }
        else if (id == R.id.science) {
            subreddit = Subreddit.science;
            index = 3;
        }
        else if (id == R.id.pics) {
            subreddit = Subreddit.pics;
            index = 4;
        }
        else if (id == R.id.worldNews) {
            subreddit = Subreddit.worldnews;
            index = 5;
        }
        else if (id == R.id.announcements) {
            subreddit = Subreddit.announcements;
            index = 6;
        }
        else if (id == R.id.gaming) {
            subreddit = Subreddit.gaming;
            index = 7;
        }
        else {
            subreddit = Subreddit.AskReddit;
            index = 0;
        }

        firstLoad = true;

        Call<Result> call = service.getSubreddit(subreddit.toString(), category.toString(), after, THRESHOLD);

        loadSubreddit(call);
        toolbar.setTitle(DataContext.subreddits[index]);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadSubreddit(Call<Result> call) {
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, retrofit2.Response<Result> response) {
                Result result = response.body();

                if (firstLoad) {
                    firstLoad = false;

                    rvTopics.setAdapter(new TopicsAdapter(MainActivity.this, result.getData().getChildren()));

                    rvTopics.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
                        @Override
                        public void onLoadMore(int currentPage) {
                            TopicsAdapter adapter = (TopicsAdapter) rvTopics.getAdapter();

                            Topic t = adapter.getItem(adapter.getItemCount() - 1);

                            after = t.getData().getName();

                            swipeRefresh.setRefreshing(true);

                            loadSubreddit(service.getSubreddit(subreddit.toString(), category.toString(), after, THRESHOLD));
                        }
                    });
                }
                else {
                    TopicsAdapter adapter = (TopicsAdapter) rvTopics.getAdapter();

                    adapter.addItems(result.getData().getChildren());
                }

                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });
    }
}

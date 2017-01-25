package be.rubenvermeulen.android.redditapp;

import android.content.SharedPreferences;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import be.rubenvermeulen.android.redditapp.helpers.Category;
import be.rubenvermeulen.android.redditapp.helpers.EndlessRecyclerOnScrollListener;
import be.rubenvermeulen.android.redditapp.helpers.Subreddit;
import be.rubenvermeulen.android.redditapp.models.App;
import be.rubenvermeulen.android.redditapp.models.DaoSession;
import be.rubenvermeulen.android.redditapp.models.Result;
import be.rubenvermeulen.android.redditapp.models.Topic;
import be.rubenvermeulen.android.redditapp.models.TopicData;
import be.rubenvermeulen.android.redditapp.models.TopicDataDao;
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

    private LinearLayoutManager layoutManager;
    private RedditService service;
    private boolean firstLoad = true;
    private String after = null;
    private Subreddit subreddit;
    private Category category = Category.hot;

    private TopicDataDao topicDataDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        // GreenDAO instrance
        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        topicDataDao = daoSession.getTopicDataDao();

        // Get saved state
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        String value = sharedPreferences.getString("subreddit", null);

        if (value != null) {
            subreddit = Subreddit.valueOf(value);
            getSupportActionBar().setTitle(subreddit.toString());

            List<TopicData> topicDataList = topicDataDao.queryBuilder().list();

            if (topicDataList.isEmpty()) {
                loadSubreddit(service.getSubreddit(subreddit.toString(), category.toString(), after, THRESHOLD));
            }
            else {
                List<Topic> topicList = new ArrayList<>();
                Topic t;
                for (TopicData td : topicDataList) {
                    t = new Topic();
                    t.setData(td);

                    topicList.add(t);
                }

                rvTopics.setAdapter(new TopicsAdapter(MainActivity.this, topicList));
                Toast.makeText(this, "You're watching a cached version of " + subreddit.toString(), Toast.LENGTH_LONG).show();
            }
        }
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

        swipeRefresh.setRefreshing(true);

        if (id == R.id.askReddit) {
            subreddit = Subreddit.AskReddit;
        }
        else if (id == R.id.funny) {
            subreddit = Subreddit.funny;
        }
        else if (id == R.id.todayILearned) {
            subreddit = Subreddit.todayilearned;
        }
        else if (id == R.id.science) {
            subreddit = Subreddit.science;
        }
        else if (id == R.id.pics) {
            subreddit = Subreddit.pics;
        }
        else if (id == R.id.worldNews) {
            subreddit = Subreddit.worldnews;
        }
        else if (id == R.id.announcements) {
            subreddit = Subreddit.announcements;
        }
        else if (id == R.id.gaming) {
            subreddit = Subreddit.gaming;
        }
        else {
            subreddit = Subreddit.AskReddit;
        }

        firstLoad = true;
        after = null;

        Call<Result> call = service.getSubreddit(subreddit.toString(), category.toString(), after, THRESHOLD);

        loadSubreddit(call);
        getSupportActionBar().setTitle(subreddit.toString());

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

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("subreddit", subreddit.toString());
        editor.apply();

        // Save topics
        List<TopicData> topicDataList = new ArrayList<>();
        TopicsAdapter adapter = (TopicsAdapter) rvTopics.getAdapter();

        for (Topic t : adapter.getTopics()) {
            topicDataList.add(t.getData());
        }

        // Make sure table is empty (cache)
        topicDataDao.deleteAll();

        // Insert new data
        topicDataDao.insertInTx(topicDataList);
    }
}

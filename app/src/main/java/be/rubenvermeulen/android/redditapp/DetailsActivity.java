package be.rubenvermeulen.android.redditapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Locale;

import be.rubenvermeulen.android.redditapp.models.TopicData;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.cover)
    ImageView ivCover;
    @BindView(R.id.title)
    TextView tvTitle;
    @BindView(R.id.author)
    TextView tvAuthor;
    @BindView(R.id.numberOfComments)
    TextView tvNumberOfComments;
    @BindView(R.id.upvotes)
    TextView tvUpvotes;
    @BindView(R.id.subreddit)
    TextView tvSubreddit;
    @BindView(R.id.created)
    TextView tvCreated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (getSupportActionBar() != null) {
            // Back button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            // Action bar title
            getSupportActionBar().setTitle("Detailed view");
        }

        ButterKnife.bind(this);

        bindData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private void bindData() {
        Intent intent = getIntent();
        TopicData topicData = (TopicData) intent.getSerializableExtra("TOPIC_DATA");

        if (topicData.getThumbnail().startsWith("http")) {
            Picasso.with(this).load(topicData.getThumbnail()).into(ivCover);
            ivCover.setVisibility(View.VISIBLE);
        }

        tvTitle.setText(topicData.getTitle());
        tvAuthor.setText(topicData.getAuthor());
        tvNumberOfComments.setText(String.format(Locale.getDefault(), "%d comments", topicData.getNumberOfComments()));
        tvUpvotes.setText(String.format(Locale.getDefault(), "%d upvotes", topicData.getUps()));
        tvSubreddit.setText(topicData.getSubreddit());
        tvCreated.setText(topicData.getHumandReadableTimestamp());
    }
}

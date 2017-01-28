package be.rubenvermeulen.android.redditapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
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

public class DetailActivity extends AppCompatActivity {

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


        TopicData topicData = getIntent().getParcelableExtra("topicData");
        DetailFragment detailFragment = new DetailFragment();
        Bundle args = new Bundle();

        args.putParcelable("topicData", topicData);
        detailFragment.setArguments(args);

        getFragmentManager()
                .beginTransaction()
                .add(R.id.activity_details, detailFragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to previous activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}

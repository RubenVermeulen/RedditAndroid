package be.rubenvermeulen.android.redditapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Locale;

import be.rubenvermeulen.android.redditapp.models.TopicData;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailFragment extends Fragment {

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

    public DetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_detail, container, false);

        ButterKnife.bind(this, fragment);

        TopicData topicData = getArguments().getParcelable("topicData");

        bindObject(topicData);

        return fragment;
    }

    private void bindObject(TopicData topicData) {
        if (topicData.getThumbnail().startsWith("http")) {
            Picasso.with(getActivity()).load(topicData.getThumbnail()).into(ivCover);
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

package be.rubenvermeulen.android.redditapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import be.rubenvermeulen.android.redditapp.models.Topic;
import be.rubenvermeulen.android.redditapp.models.TopicData;

public class TopicsAdapter extends RecyclerView.Adapter {
    private Context ctx;
    private List<Topic> topics;

    public TopicsAdapter(Context ctx, List<Topic> topics) {
        this.ctx = ctx;
        this.topics = topics;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivCover;
        TextView tvTitle;
        TextView tvAuthor;
        TextView tvNumberOfComments;
        TextView tvUpvotes;
        TextView tvSubreddit;
        TextView tvCreated;

        ViewHolder(View itemView) {
            super(itemView);

            ivCover = (ImageView) itemView.findViewById(R.id.cover);
            tvTitle = (TextView) itemView.findViewById(R.id.title);
            tvAuthor = (TextView) itemView.findViewById(R.id.author);
            tvNumberOfComments = (TextView) itemView.findViewById(R.id.numberOfComments);
            tvUpvotes = (TextView) itemView.findViewById(R.id.upvotes);
            tvSubreddit = (TextView) itemView.findViewById(R.id.subreddit);
            tvCreated = (TextView) itemView.findViewById(R.id.created);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.topics_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final TopicData topicData = topics.get(position).getData();
        ViewHolder viewHolder = (ViewHolder) holder;

        if (topicData.getThumbnail().startsWith("http")) {
            Picasso.with(ctx).load(topicData.getThumbnail()).into(viewHolder.ivCover);
            viewHolder.ivCover.setVisibility(View.VISIBLE);
        }

        viewHolder.tvTitle.setText(topicData.getTitle());
        viewHolder.tvAuthor.setText(topicData.getAuthor());
        viewHolder.tvNumberOfComments.setText(String.format(Locale.getDefault(), "%d comments", topicData.getNumberOfComments()));
        viewHolder.tvUpvotes.setText(String.format(Locale.getDefault(), "%d upvotes", topicData.getUps()));
        viewHolder.tvSubreddit.setText(topicData.getSubreddit());
        viewHolder.tvCreated.setText(topicData.getHumandReadableTimestamp());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, DetailsActivity.class);
                intent.putExtra("topic", topicData);

                ctx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    public void addItem(Topic topic) {
        topics.add(topic);
    }

    public void addItems(List<Topic> topics) {
        int start = this.topics.size();
        this.topics.addAll(topics);
        notifyItemRangeInserted(start, topics.size());
    }

    public Topic getItem(int index) {
        return topics.get(index);
    }

    public List<Topic> getTopics() {
        return topics;
    }
}

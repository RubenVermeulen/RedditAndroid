package be.rubenvermeulen.android.redditapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SubredditsAdapter extends RecyclerView.Adapter {
    private Context ctx;
    private String[] subreddits;

    public SubredditsAdapter(Context ctx, String[] subreddits) {
        this.ctx = ctx;
        this.subreddits = subreddits;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvSubreddit;

        ViewHolder(View itemView) {
            super(itemView);

            tvSubreddit = (TextView) itemView.findViewById(R.id.subredditItem);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subreddit_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String subreddit = subreddits[position];
        ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.tvSubreddit.setText(subreddit);
    }

    @Override
    public int getItemCount() {
        return subreddits.length;
    }
}

package com.sharkna.khaled.sharkna.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.sharkna.khaled.sharkna.R;
import com.sharkna.khaled.sharkna.model.FeedItem;
import com.sharkna.khaled.sharkna.model.Post;
import com.sharkna.khaled.sharkna.model.db_utils.DBHelper;
import com.sharkna.khaled.sharkna.model.db_utils.DownloadImageTask;
import com.sharkna.khaled.sharkna.model.db_utils.DownloadRoundImageTask;
import com.sharkna.khaled.sharkna.model.db_utils.IGetPostsListener;
import com.sharkna.khaled.sharkna.model.db_utils.PerformNetworkRequest;
import com.sharkna.khaled.sharkna.model.db_utils.PerformNetworkRequestToGetAllPosts;
import com.sharkna.khaled.sharkna.ui.activity.MainActivity;
import com.sharkna.khaled.sharkna.ui.view.LoadingFeedItemView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Khaled on 18.06.17
 */
public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements IGetPostsListener {
    private static final String ACTION_LIKE_BUTTON_CLICKED = "action_like_button_button";
    static final String ACTION_LIKE_IMAGE_CLICKED = "action_like_image_button";
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    static final int VIEW_TYPE_DEFAULT = 1;
    private static final int VIEW_TYPE_LOADER = 2;
    private static final String TAG = FeedAdapter.class.getName();

    private final List<FeedItem> feedItems = new ArrayList<>();

    private Context context;
    private OnFeedItemClickListener onFeedItemClickListener;

    private boolean showLoadingView = false;
    private boolean liked = false;
    private boolean animated;

    public FeedAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_DEFAULT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_feed, parent, false);
            CellFeedViewHolder cellFeedViewHolder = new CellFeedViewHolder(view);
            setupClickableViews(view, cellFeedViewHolder);
            return cellFeedViewHolder;
        } else if (viewType == VIEW_TYPE_LOADER) {
            LoadingFeedItemView view = new LoadingFeedItemView(context);
            view.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            );
            return new LoadingCellFeedViewHolder(view);
        }

        return null;
    }

    private void setupClickableViews(final View view, final CellFeedViewHolder cellFeedViewHolder) {
        cellFeedViewHolder.btnComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFeedItemClickListener.onCommentsClick(view, cellFeedViewHolder.getAdapterPosition());
                // TODO: 10/23/2017 handle comments here
            }
        });
        cellFeedViewHolder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFeedItemClickListener.onMoreClick(v, cellFeedViewHolder.getAdapterPosition());
            }
        });
        cellFeedViewHolder.ivFeedCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = cellFeedViewHolder.getAdapterPosition();
                FeedItem feedItem = feedItems.get(adapterPosition);
                liked = feedItem.isLiked();
                if (liked) {
                    feedItems.get(adapterPosition).likesCount--;
                    liked = !liked;
                    feedItem.setLiked(liked);
                } else {
                    feedItems.get(adapterPosition).likesCount++;
                    liked = !liked;
                    feedItem.setLiked(liked);
                }
                HashMap<String, String> hitLikeRequestParams = new HashMap<>();
                hitLikeRequestParams.put("user_id", String.valueOf(feedItem.getUserId()));
                hitLikeRequestParams.put("post_id", String.valueOf(feedItem.getPostId()));
                if (liked) {
                    PerformNetworkRequest hitLikeRequest = new PerformNetworkRequest(DBHelper.URL_HIT_LIKE, hitLikeRequestParams, DBHelper.CODE_POST_REQUEST);
                    hitLikeRequest.execute();
                } else {
                    PerformNetworkRequest hitLikeRequest = new PerformNetworkRequest(DBHelper.URL_HIT_DISLIKE, hitLikeRequestParams, DBHelper.CODE_POST_REQUEST);
                    hitLikeRequest.execute();
                }
                feedItems.set(adapterPosition, feedItem);
                notifyItemChanged(adapterPosition, ACTION_LIKE_IMAGE_CLICKED);
                if (context instanceof MainActivity) {
//                    ((MainActivity) context).showLikedSnackbar();
                    ((MainActivity) context).showLikedSnackbar(liked);
                }
            }
        });
        cellFeedViewHolder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = cellFeedViewHolder.getAdapterPosition();
                FeedItem feedItem = feedItems.get(adapterPosition);
                liked = feedItem.isLiked();
                if (liked) {
                    feedItems.get(adapterPosition).likesCount--;
                    liked = !liked;
                    feedItem.setLiked(liked);
                } else {
                    feedItems.get(adapterPosition).likesCount++;
                    liked = !liked;
                    feedItem.setLiked(liked);
                }
                feedItems.set(adapterPosition, feedItem);
                notifyItemChanged(adapterPosition, ACTION_LIKE_BUTTON_CLICKED);
                if (context instanceof MainActivity) {
//                    ((MainActivity) context).showLikedSnackbar();
                    ((MainActivity) context).showLikedSnackbar(liked);
                }
            }
        });
        cellFeedViewHolder.ivUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFeedItemClickListener.onProfileClick(view);
            }
        });
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ((CellFeedViewHolder) viewHolder).bindView(feedItems.get(position));

        if (getItemViewType(position) == VIEW_TYPE_LOADER) {
            bindLoadingFeedItem((LoadingCellFeedViewHolder) viewHolder);
        }
    }

    private void bindLoadingFeedItem(final LoadingCellFeedViewHolder holder) {
        holder.loadingFeedItemView.setOnLoadingFinishedListener(new LoadingFeedItemView.OnLoadingFinishedListener() {
            @Override
            public void onLoadingFinished() {
                showLoadingView = false;
                notifyItemChanged(0);
            }
        });
        holder.loadingFeedItemView.startLoading();
    }

    @Override
    public int getItemViewType(int position) {
        if (showLoadingView && position == 0) {
            return VIEW_TYPE_LOADER;
        } else {
            return VIEW_TYPE_DEFAULT;
        }
    }

    @Override
    public int getItemCount() {
        return feedItems.size();
    }

    public void updateItems(boolean animated) {
        this.animated = animated;
        HashMap<String, String> params = new HashMap<>();
        PerformNetworkRequestToGetAllPosts request = new PerformNetworkRequestToGetAllPosts(DBHelper.URL_READ_POSTS, params, CODE_GET_REQUEST);
        request.setiGetPostsListener(this);
        request.execute();
    }

    public void setOnFeedItemClickListener(OnFeedItemClickListener onFeedItemClickListener) {
        this.onFeedItemClickListener = onFeedItemClickListener;
    }

    public void showLoadingView() {
        showLoadingView = true;
        notifyItemChanged(0);
    }

    public void setPhotoUri(Uri photoUri) {
        int adapterPosition = 0;
        FeedItem feedItem = feedItems.get(adapterPosition);
        if (photoUri != null) {
            feedItem.photoUri = photoUri;
            Log.d(TAG, "setPhotoUri: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^" + photoUri.getEncodedPath());
            feedItems.set(adapterPosition, feedItem);
        }
        /*notifyItemChanged(adapterPosition, ACTION_LIKE_BUTTON_CLICKED);
        if (context instanceof MainActivity) {
//                    ((MainActivity) context).showLikedSnackbar();
            ((MainActivity) context).showLikedSnackbar(liked);
        }*/
    }

    @Override
    public void onGetPostsResult(ArrayList<Post> posts) {
        matchFeedItemsWithPostsFromDatabase(posts);
    }

    public static class CellFeedViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = CellFeedViewHolder.class.getName();
        @BindView(R.id.ivFeedCenter)
        ImageView ivFeedCenter;
        @BindView(R.id.ivFeedBottom)
        TextView ivFeedBottom;
        @BindView(R.id.btnComments)
        ImageButton btnComments;
        @BindView(R.id.btnLike)
        ImageButton btnLike;
        @BindView(R.id.btnMore)
        ImageButton btnMore;
        @BindView(R.id.vBgLike)
        View vBgLike;
        @BindView(R.id.ivLike)
        ImageView ivLike;
        @BindView(R.id.tsLikesCounter)
        TextSwitcher tsLikesCounter;
        //        @Nullable
        @BindView(R.id.ivUserProfile)
        ImageView ivUserProfile;
        @BindView(R.id.tvUserName)
        TextView ivUserName;
        @BindView(R.id.vImageRoot)
        FrameLayout vImageRoot;

        FeedItem feedItem;


        public CellFeedViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindView(FeedItem feedItem) {
            this.feedItem = feedItem;
            int adapterPosition = getAdapterPosition();
            new DownloadRoundImageTask(ivUserProfile)
                    .execute(feedItem.getUserPhotoUri());
            new DownloadImageTask(ivFeedCenter)
                    .execute(feedItem.getServer_image_url());
            ivUserName.setText(feedItem.getFirstName() + " " + feedItem.getLastName());
            ivUserName.setTextSize(18f);
            ivUserName.setTextColor(Color.rgb(95, 98, 127));
            ivFeedBottom.setText(feedItem.getDescription());
            ivFeedBottom.setTextSize(18f);
            btnLike.setImageResource(feedItem.isLiked() ? R.drawable.ic_heart_red : R.drawable.ic_heart_outline_grey);
            tsLikesCounter.setCurrentText(vImageRoot.getResources().getQuantityString(
                    R.plurals.likes_count, feedItem.likesCount, feedItem.likesCount
            ));
        }

        public FeedItem getFeedItem() {
            return feedItem;
        }

    }

    public static class LoadingCellFeedViewHolder extends CellFeedViewHolder {

        LoadingFeedItemView loadingFeedItemView;

        public LoadingCellFeedViewHolder(LoadingFeedItemView view) {
            super(view);
            this.loadingFeedItemView = view;
        }

        @Override
        public void bindView(FeedItem feedItem) {
            super.bindView(feedItem);
        }
    }

    public interface OnFeedItemClickListener {
        void onCommentsClick(View v, int position);

        void onMoreClick(View v, int position);

        void onProfileClick(View v);
    }

    private void matchFeedItemsWithPostsFromDatabase(ArrayList<Post> posts) {
        feedItems.clear();
        // TODO: 10/20/2017 get posts from database here

        for (Post post : posts) {
            FeedItem feedItem = new FeedItem();
            feedItem.setServer_image_url(post.getServer_image_url());
            feedItem.setDescription(post.getDescription());
            feedItem.setLiked(post.isLike());
            feedItem.setLikesCount(post.getNumberOfLikes());
            feedItem.setFirstName(post.getUser().getFirstName());
            feedItem.setLastName(post.getUser().getLastName());
            feedItem.setUserPhotoUri(post.getUser().getImageURL());
            feedItem.setPostId(post.getId());
            feedItem.setUserId(post.getUserId());
            feedItems.add(feedItem);
        }
        if (animated) {
            notifyItemRangeInserted(0, feedItems.size());
        } else {
            notifyDataSetChanged();
        }
    }
}

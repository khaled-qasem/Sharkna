package com.sharkna.khaled.sharkna.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.sharkna.khaled.sharkna.R;
import com.sharkna.khaled.sharkna.Utils;
import com.sharkna.khaled.sharkna.account.CurrentAccount;
import com.sharkna.khaled.sharkna.model.Comment;
import com.sharkna.khaled.sharkna.model.db_utils.DBHelper;
import com.sharkna.khaled.sharkna.model.db_utils.IGetCommentsListener;
import com.sharkna.khaled.sharkna.model.db_utils.PerformNetworkRequest;
import com.sharkna.khaled.sharkna.model.db_utils.PerformNetworkRequestToGetLikesAndComments;
import com.sharkna.khaled.sharkna.ui.adapter.CommentsAdapter;
import com.sharkna.khaled.sharkna.ui.view.SendCommentButton;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

/**
 * Created by Khaled on 10.6.17.
 * description:
 * assumption:
 */
public class CommentsActivity extends BaseDrawerActivity implements SendCommentButton.OnSendClickListener ,IGetCommentsListener{
    public static final String ARG_DRAWING_START_LOCATION = "arg_drawing_start_location";
    private static final String TAG = CommentsActivity.class.getName();

    @BindView(R.id.contentRoot)
    LinearLayout contentRoot;
    @BindView(R.id.rvComments)
    RecyclerView rvComments;
    @BindView(R.id.llAddComment)
    LinearLayout llAddComment;
    @BindView(R.id.etComment)
    EditText etComment;
    @BindView(R.id.btnSendComment)
    SendCommentButton btnSendComment;

    private CommentsAdapter commentsAdapter;
    private int drawingStartLocation;
    private int postId;
    private CurrentAccount currentAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        currentAccount = CurrentAccount.getInstance();
        drawingStartLocation = getIntent().getIntExtra(ARG_DRAWING_START_LOCATION, 0);
        postId = getIntent().getIntExtra("postId",0);

        HashMap<String, String> params = new HashMap<>();
        params.put("post_id", String.valueOf(postId));
        PerformNetworkRequestToGetLikesAndComments request = new PerformNetworkRequestToGetLikesAndComments(DBHelper.URL_READ_COMMENTS, params, DBHelper.CODE_POST_REQUEST);
        request.setiGetCommentsListener(this);
        request.execute();
//        setupComments();
//        setupSendCommentButton();


//        drawingStartLocation = getIntent().getIntExtra(ARG_DRAWING_START_LOCATION, 0);
//        postId = getIntent().getIntExtra("postId",0);

       /* if (savedInstanceState == null) {
            contentRoot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    contentRoot.getViewTreeObserver().removeOnPreDrawListener(this);
                    startIntroAnimation();
                    return true;
                }
            });
        }*/
    }

    private void setupComments(ArrayList<Comment> comments) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvComments.setLayoutManager(linearLayoutManager);
        rvComments.setHasFixedSize(true);

        commentsAdapter = new CommentsAdapter(this);
        commentsAdapter.setComments(comments);
        rvComments.setAdapter(commentsAdapter);
        rvComments.setOverScrollMode(View.OVER_SCROLL_NEVER);
        rvComments.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    commentsAdapter.setAnimationsLocked(true);
                }
            }
        });
    }

    private void setupSendCommentButton() {
        btnSendComment.setOnSendClickListener(this);
    }

    private void startIntroAnimation() {
        ViewCompat.setElevation(getToolbar(), 0);
        contentRoot.setScaleY(0.1f);
        contentRoot.setPivotY(drawingStartLocation);
        llAddComment.setTranslationY(200);

        contentRoot.animate()
                .scaleY(1)
                .setDuration(200)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ViewCompat.setElevation(getToolbar(), Utils.dpToPx(8));
                        animateContent();
                    }
                })
                .start();
    }

    private void animateContent() {
        commentsAdapter.updateItems();
        llAddComment.animate().translationY(0)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(200)
                .start();
    }

    @Override
    public void onBackPressed() {
        ViewCompat.setElevation(getToolbar(), 0);
        contentRoot.animate()
                .translationY(Utils.getScreenHeight(this))
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        CommentsActivity.super.onBackPressed();
                        overridePendingTransition(0, 0);
                    }
                })
                .start();
    }

    @Override
    public void onSendClickListener(View v) {
        if (validateComment()) {
            commentsAdapter.addItem();
            commentsAdapter.setCommentText(etComment.getText().toString());
            commentsAdapter.setAnimationsLocked(false);
            commentsAdapter.setDelayEnterAnimation(false);
            HashMap<String, String> params = new HashMap<>();
            params.put("post_id", String.valueOf(postId));
            params.put("user_id", String.valueOf(currentAccount.getUserId()));
            params.put("description", etComment.getText().toString());
            Log.d(TAG, "onSendClickListener: "+String.valueOf(postId)+String.valueOf(currentAccount.getUserId())
                    + etComment.getText().toString());
            PerformNetworkRequest request = new PerformNetworkRequest(DBHelper.URL_ADD_COMMENT, params, DBHelper.CODE_POST_REQUEST);
            request.execute();
            rvComments.smoothScrollBy(0, rvComments.getChildAt(0).getHeight() * commentsAdapter.getItemCount());
            etComment.setText(null);
            btnSendComment.setCurrentState(SendCommentButton.STATE_DONE);

            //add item

        }
    }

    private boolean validateComment() {
        if (TextUtils.isEmpty(etComment.getText())) {
            btnSendComment.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake_error));
            return false;
        }
        return true;
    }

    @Override
    public void onGetCommentsResult(ArrayList<Comment> comments) {

        setupComments(comments);
        setupSendCommentButton();
        contentRoot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                contentRoot.getViewTreeObserver().removeOnPreDrawListener(this);
                startIntroAnimation();
                return true;
            }
        });
    }
}

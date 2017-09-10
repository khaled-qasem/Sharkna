package com.sharkna.khaled.sharkna.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.sharkna.khaled.sharkna.R;
import com.sharkna.khaled.sharkna.Utils;
import com.sharkna.khaled.sharkna.account.GMailSender;
import com.sharkna.khaled.sharkna.account.GmailAccount;
import com.sharkna.khaled.sharkna.ui.adapter.FeedAdapter;
import com.sharkna.khaled.sharkna.ui.adapter.FeedItemAnimator;
import com.sharkna.khaled.sharkna.ui.view.FeedContextMenu;
import com.sharkna.khaled.sharkna.ui.view.FeedContextMenuManager;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BaseDrawerActivity implements FeedAdapter.OnFeedItemClickListener,
        FeedContextMenu.OnFeedContextMenuItemClickListener,ICheckSelfPermissionActivity {
    public static final String ACTION_SHOW_LOADING_ITEM = "action_show_loading_item";
    public static final String GMAIL_PREFERENCE = "Gmail_account";


    private static final int ANIM_DURATION_TOOLBAR = 300;
    private static final int ANIM_DURATION_FAB = 400;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String NAME = "name";
    private static final String EMAIL = "email";

    @BindView(R.id.rvFeed)
    RecyclerView rvFeed;
    @BindView(R.id.btnCreate)
    FloatingActionButton fabCreate;
    @BindView(R.id.content)
    CoordinatorLayout clContent;

    private FeedAdapter feedAdapter;

    private boolean pendingIntroAnimation;


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        PermissionHandler permissionHandler = new PermissionHandler();
        permissionHandler.handleRequest(requestCode, permissions, grantResults, this.getApplicationContext());

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionHandler permissionHandler = new PermissionHandler();
        if (permissionHandler.canMakeSmores()) {
            permissionHandler.askForPermissions(this);
        }

        Intent intent = getIntent();
        if (intent != null) {
            String Gemail = intent.getStringExtra(EMAIL);
            String Gname = intent.getStringExtra(EMAIL);
            if (Gname != null && Gemail!=null) {
                SharedPreferences gmailAccount = getSharedPreferences(GMAIL_PREFERENCE, 0);
                SharedPreferences.Editor editor = gmailAccount.edit();
                editor.putString(NAME, Gname);
                editor.putString(EMAIL, Gemail);

                // Commit the edits!
                editor.apply();
            }
        }

        //add preferences here
        // Restore preferences
        SharedPreferences account = getSharedPreferences(GMAIL_PREFERENCE, 0);
        String accountName = account.getString(NAME, null);
        String accountEmail = account.getString(EMAIL, null);
        Log.d(TAG, "onCreate: accountEmail"+accountEmail);
        Log.d(TAG, "onCreate: accountName"+accountName);
        if (accountEmail != null) {
            Log.d(TAG, "onCreate: accountEmail"+accountEmail);
            Log.d(TAG, "onCreate: accountName"+accountName);
//            sendGmail();
            GmailAccount gmailAccount = GmailAccount.getInstance();
            gmailAccount.setEmail(accountEmail);
            gmailAccount.setName(accountName);
        }else{
            Intent SignInIntent = new Intent(this, SignInActivity.class);
//            String message = editText.getText().toString();
//            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(SignInIntent);
        }
        setupFeed();

        if (savedInstanceState == null) {
            pendingIntroAnimation = true;
        } else {
            feedAdapter.updateItems(false);
        }
    }

    private void setupFeed() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        rvFeed.setLayoutManager(linearLayoutManager);

        feedAdapter = new FeedAdapter(this);
        feedAdapter.setOnFeedItemClickListener(this);
        rvFeed.setAdapter(feedAdapter);
        rvFeed.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                FeedContextMenuManager.getInstance().onScrolled(recyclerView, dx, dy);
            }
        });
        rvFeed.setItemAnimator(new FeedItemAnimator());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (ACTION_SHOW_LOADING_ITEM.equals(intent.getAction())) {
            showFeedLoadingItemDelayed();
        }
    }

    private void showFeedLoadingItemDelayed() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rvFeed.smoothScrollToPosition(0);
                feedAdapter.showLoadingView();
            }
        }, 500);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (pendingIntroAnimation) {
            pendingIntroAnimation = false;
            startIntroAnimation();
        }
        return true;
    }

    private void startIntroAnimation() {
        fabCreate.setTranslationY(2 * getResources().getDimensionPixelOffset(R.dimen.btn_fab_size));

        int actionbarSize = Utils.dpToPx(56);
        getToolbar().setTranslationY(-actionbarSize);
        getIvLogo().setTranslationY(-actionbarSize);
        getInboxMenuItem().getActionView().setTranslationY(-actionbarSize);

        getToolbar().animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(300);
        getIvLogo().animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(400);
        getInboxMenuItem().getActionView().animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        startContentAnimation();
                    }
                })
                .start();
    }

    private void startContentAnimation() {
        fabCreate.animate()
                .translationY(0)
                .setInterpolator(new OvershootInterpolator(1.f))
                .setStartDelay(300)
                .setDuration(ANIM_DURATION_FAB)
                .start();
        feedAdapter.updateItems(true);
    }

    @Override
    public void onCommentsClick(View v, int position) {
        final Intent intent = new Intent(this, CommentsActivity.class);
        int[] startingLocation = new int[2];
        v.getLocationOnScreen(startingLocation);
        intent.putExtra(CommentsActivity.ARG_DRAWING_START_LOCATION, startingLocation[1]);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onMoreClick(View v, int itemPosition) {
        FeedContextMenuManager.getInstance().toggleContextMenuFromView(v, itemPosition, this);
    }

    @Override
    public void onProfileClick(View v) {
        int[] startingLocation = new int[2];
        v.getLocationOnScreen(startingLocation);
        startingLocation[0] += v.getWidth() / 2;
        UserProfileActivity.startUserProfileFromLocation(startingLocation, this);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onReportClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }

    @Override
    public void onSharePhotoClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }

    @Override
    public void onCopyShareUrlClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }

    @Override
    public void onCancelClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }

    @OnClick(R.id.btnCreate)
    public void onTakePhotoClick() {
        int[] startingLocation = new int[2];
        fabCreate.getLocationOnScreen(startingLocation);
        startingLocation[0] += fabCreate.getWidth() / 2;
        TakePhotoActivity.startCameraFromLocation(startingLocation, this);
        overridePendingTransition(0, 0);
    }

    public void showLikedSnackbar() {
        Snackbar.make(clContent, "Liked!", Snackbar.LENGTH_SHORT).show();
    }

    //to send email on gmail
    private void sendGmail(){
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Log.d(TAG, "run: send email ***********************************");
                    GMailSender sender = new GMailSender("sharknapalestine@gmail.com",
                            "sharkna12345");
                    sender.sendMail("Hello from JavaMail", "Body from JavaMail",
                            "sharknapalestine@gmail.com", "khaled.alqerem@gmail.com");
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }

        }).start();
    }
}
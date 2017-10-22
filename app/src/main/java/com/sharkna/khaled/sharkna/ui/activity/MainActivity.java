package com.sharkna.khaled.sharkna.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sharkna.khaled.sharkna.R;
import com.sharkna.khaled.sharkna.Utils;
import com.sharkna.khaled.sharkna.account.CurrentAccount;
import com.sharkna.khaled.sharkna.model.User;
import com.sharkna.khaled.sharkna.model.db_utils.DBHelper;
import com.sharkna.khaled.sharkna.model.db_utils.IGetUserListener;
import com.sharkna.khaled.sharkna.model.db_utils.PerformNetworkRequestForResult;
import com.sharkna.khaled.sharkna.ui.adapter.FeedAdapter;
import com.sharkna.khaled.sharkna.ui.adapter.FeedItemAnimator;
import com.sharkna.khaled.sharkna.ui.view.FeedContextMenu;
import com.sharkna.khaled.sharkna.ui.view.FeedContextMenuManager;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BaseDrawerActivity implements FeedAdapter.OnFeedItemClickListener,
        FeedContextMenu.OnFeedContextMenuItemClickListener, ICheckSelfPermissionActivity,IGetUserListener {
    public static final String ACTION_SHOW_LOADING_ITEM = "action_show_loading_item";
    public static final String GMAIL_PREFERENCE = "Gmail_account";
    public static final String ANONYMOUS = "anonymous";
    private static final int CODE_POST_REQUEST = 1025;

    private static final int ANIM_DURATION_TOOLBAR = 300;
    private static final int ANIM_DURATION_FAB = 400;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    public static final String PATH = "path";

    @BindView(R.id.rvFeed)
    RecyclerView rvFeed;
    @BindView(R.id.btnCreate)
    FloatingActionButton fabCreate;
    @BindView(R.id.content)
    CoordinatorLayout clContent;

    private FeedAdapter feedAdapter;
    private User user;

    private Uri photoUri;
    private boolean pendingIntroAnimation;

    private String mUsername;
    private String mPhotoUrl;
    private SharedPreferences mSharedPreferences;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mEmail;
    private CurrentAccount currentAccount;


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

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Set default username is anonymous.
        mUsername = ANONYMOUS;
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            mEmail = mFirebaseUser.getEmail();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
                currentAccount = CurrentAccount.getInstance();
                currentAccount.setUserEmail(mEmail);
                currentAccount.setUserName(mUsername);
                currentAccount.setUserPhotoURL(mPhotoUrl);
            }
            getUserFromDatabase(mEmail);
        }
/*
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this *//* FragmentActivity *//*, this *//* OnConnectionFailedListener *//*)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();*/

        Intent intent = getIntent();
        if (intent != null) {
//            String gEmail = intent.getStringExtra(EMAIL);
//            String gName = intent.getStringExtra(NAME);
            String uri = intent.getStringExtra(PATH);
            if (uri != null) {
                photoUri = Uri.parse(intent.getStringExtra(PATH));
//                Log.d(TAG, "onCreate: ==========================????"+photoUri.getEncodedPath().toString());
            }

           /* if (gName != null && gEmail!=null) {
                SharedPreferences currentAccount = getSharedPreferences(GMAIL_PREFERENCE, 0);
                SharedPreferences.Editor editor = currentAccount.edit();
                editor.putString(NAME, gName);
                editor.putString(EMAIL, gEmail);

                // Commit the edits!
                editor.apply();
            }*/
        }

        //add preferences here
        // Restore preferences
        SharedPreferences account = getSharedPreferences(GMAIL_PREFERENCE, 0);
//        String accountName = account.getString(NAME, null);
//        String accountEmail = account.getString(EMAIL, null);
       /* if (accountEmail != null) {
            Log.d(TAG, "onCreate: accountEmail"+accountEmail);
            Log.d(TAG, "onCreate: accountName"+accountName);
          *//*  CurrentAccount currentAccount = CurrentAccount.getInstance();
            currentAccount.setUserEmail(accountEmail);
            currentAccount.setUserName(accountName);
            currentAccount.setUserPhotoURL(mPhotoUrl);*//*
        }else{
            Intent SignInIntent = new Intent(this, SignInActivity.class);
            startActivity(SignInIntent);
        }*/
//        OKHttpGetRequest okHttpGetRequest = new OKHttpGetRequest();
//        okHttpGetRequest.execute();
        setupFeed();

        if (savedInstanceState == null) {
            pendingIntroAnimation = true;
        } else {
//            Log.d(TAG, "onCreate: +++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//            feedAdapter.setPhotoUri(photoUri);
            feedAdapter.updateItems(false);
        }
    }

    private void getUserFromDatabase(String email) {
        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        //Calling the user API
        PerformNetworkRequestForResult request = new PerformNetworkRequestForResult(DBHelper.URL_READ_USER, params, CODE_POST_REQUEST);
        request.setIGetUserListener(this);
        request.execute();
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
//        Log.d(TAG, "onCreate: +++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//        feedAdapter.setPhotoUri(photoUri);
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
//                feedAdapter.setPhotoUri(photoUri);
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
//        feedAdapter.setPhotoUri(photoUri);
        if (photoUri != null) {
//            feedAdapter.updateItems(true, photoUri);
        } else {
            feedAdapter.updateItems(true);
        }
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

    public void showLikedSnackbar(boolean liked) {
        if (liked) {
            Snackbar.make(clContent, "Liked!", Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(clContent, "DisLiked!", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGetUserResult(User user) {
        this.user = user;
        currentAccount.setUserId(user.getId());
    }


    // TODO: 10/14/2017 Handle sign out from firebase
   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                mFirebaseAuth.signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                mUsername = ANONYMOUS;
                startActivity(new Intent(this, SignInActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/


    // TODO: 10/22/2017 user this to get benefit of image from server
    /*private Bitmap getImage(JSONObject jo){
        URL url = null;
        Bitmap image = null;
        try {
            url = new URL(jo.getString(IMAGE_URL));
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return image;
    }*/
}
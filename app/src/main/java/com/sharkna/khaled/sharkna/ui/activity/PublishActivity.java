package com.sharkna.khaled.sharkna.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.sharkna.khaled.sharkna.R;
import com.sharkna.khaled.sharkna.Utils;
import com.sharkna.khaled.sharkna.account.CurrentAccount;
import com.sharkna.khaled.sharkna.account.GMailSender;
import com.sharkna.khaled.sharkna.model.Post;
import com.sharkna.khaled.sharkna.model.db_utils.DBHelper;
import com.sharkna.khaled.sharkna.model.db_utils.IOnPostAddedToDatabaseListener;
import com.sharkna.khaled.sharkna.model.db_utils.PerformNetworkRequestForResult;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnCheckedChanged;

/**
 * Created by Khaled
 */
public class PublishActivity extends BaseActivity implements IOnPostAddedToDatabaseListener {
    public static final String ARG_TAKEN_PHOTO_URI = "arg_taken_photo_uri";
    private static final String TAG = PublishActivity.class.getName();
    public static final String SHARKNAPALESTINE_GMAIL_COM = "sharknapalestine@gmail.com";
    public static final String PASSWORD = "sharkna12345";
    public static final String RECIPIENTS = "khaled.alqerem@gmail.com";
    public static final String PATH = "path";
    private static final int CODE_POST_REQUEST = 1025;

    @BindView(R.id.tbFollowers)
    ToggleButton tbFollowers;
    @BindView(R.id.tbDirect)
    ToggleButton tbDirect;
    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;
    @BindView(R.id.etDescription)
    EditText editTextDescription;

    private boolean propagatingToggleState = false;
    private Uri photoUri;
    private int photoSize;
    CurrentAccount currentAccount;

    public static void openWithPhotoUri(Activity openingActivity, Uri photoUri) {
        Intent intent = new Intent(openingActivity, PublishActivity.class);
        intent.putExtra(ARG_TAKEN_PHOTO_URI, photoUri);
        openingActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        currentAccount = CurrentAccount.getInstance();
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_grey600_24dp);
        }
        photoSize = getResources().getDimensionPixelSize(R.dimen.publish_photo_thumbnail_size);

        if (savedInstanceState == null) {
            photoUri = getIntent().getParcelableExtra(ARG_TAKEN_PHOTO_URI);
        } else {
            photoUri = savedInstanceState.getParcelable(ARG_TAKEN_PHOTO_URI);
        }
        updateStatusBarColor();

        ivPhoto.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                ivPhoto.getViewTreeObserver().removeOnPreDrawListener(this);
                loadThumbnailPhoto();
                return true;
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void updateStatusBarColor() {
        if (Utils.isAndroid5()) {
            getWindow().setStatusBarColor(0xff888888);
        }
    }

    private void loadThumbnailPhoto() {
        ivPhoto.setScaleX(0);
        ivPhoto.setScaleY(0);
        Picasso.with(this)
                .load(photoUri)
                .centerCrop()
                .resize(photoSize, photoSize)
                .into(ivPhoto, new Callback() {
                    @Override
                    public void onSuccess() {
                        ivPhoto.animate()
                                .scaleX(1.f).scaleY(1.f)
                                .setInterpolator(new OvershootInterpolator())
                                .setDuration(400)
                                .setStartDelay(200)
                                .start();
                    }

                    @Override
                    public void onError() {
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_publish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_publish) {
            Log.d(TAG, "saveImage: now send email through composeEmail");
            String description = editTextDescription.getText().toString();
            //take action to data base here
            addPostToDatabase(photoUri,description);
//            sendGmail(photoUri, description);
            bringMainActivityToTop();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void bringMainActivityToTop() {
        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        intent.setAction(MainActivity.ACTION_SHOW_LOADING_ITEM);
        intent.putExtra(PATH, photoUri.toString());
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_TAKEN_PHOTO_URI, photoUri);
    }

    @OnCheckedChanged(R.id.tbFollowers)
    public void onFollowersCheckedChange(boolean checked) {
        if (!propagatingToggleState) {
            propagatingToggleState = true;
            tbDirect.setChecked(!checked);
            propagatingToggleState = false;
        }
    }

    @OnCheckedChanged(R.id.tbDirect)
    public void onDirectCheckedChange(boolean checked) {
        if (!propagatingToggleState) {
            propagatingToggleState = true;
            tbFollowers.setChecked(!checked);
            propagatingToggleState = false;
        }
    }

    @SuppressWarnings("unused")
    public void composeEmail(final String[] addresses, final String subject, final Uri attachment) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, addresses);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                emailIntent.putExtra(Intent.EXTRA_STREAM, attachment);
                emailIntent.putExtra(Intent.EXTRA_TEXT, "From My App");
                try {
                    if (emailIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(emailIntent);
                    }
                } catch (android.content.ActivityNotFoundException ex) {
                }
            }
        }).start();
    }


    private void sendGmail(final Uri photoUri, final String description) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender(SHARKNAPALESTINE_GMAIL_COM,
                            PASSWORD);
//                    Log.d(TAG, "run: ------------------------"+photoUri.getEncodedPath());
                    sender.addAttachment(photoUri.getEncodedPath());
                    sender.sendMail("Sharkna", description + "\n\n" + currentAccount.getUserName() + "\n" + currentAccount.getUserEmail(),
                            SHARKNAPALESTINE_GMAIL_COM, RECIPIENTS);
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }

        }).start();
    }

    private void addPostToDatabase(Uri photoUri, String description) {
        Bitmap bitmap=null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);
        } catch (IOException e) {
            Log.e(TAG, "addPostToDatabase: IOException", e);
        }
        Post post = new Post();
        post.setImage(getStringImage(bitmap));
        post.setDescription(description);
        post.setUserId(currentAccount.getUserId());
        post.setMunicipalityId(0);
        post.setPublicPost(1);

        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", String.valueOf(post.getUserId()));
        params.put("municipality_id", String.valueOf(post.getMunicipalityId()));
        params.put("description", post.getDescription());
        params.put("image", post.getImage());
        params.put("public_post", String.valueOf(post.getPublicPost()));
        //Calling the create hero API
        PerformNetworkRequestForResult request = new PerformNetworkRequestForResult(DBHelper.URL_CREATE_POST, params, CODE_POST_REQUEST);
        request.setiOnPostAddedToDatabaseListener(this);
        request.execute();
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
    @Override
    public void onPostAddedtoDatabase(String string) {
        Log.d(TAG, "onPostAddedtoDatabase: "+string);
    }
}

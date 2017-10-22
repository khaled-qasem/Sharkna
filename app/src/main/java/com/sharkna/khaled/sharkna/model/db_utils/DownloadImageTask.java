package com.sharkna.khaled.sharkna.model.db_utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Khaled on 10/22/2017.
 * Assumptions
 * Descriptions
 */

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private static final String TAG = DownloadImageTask.class.getName();
    ImageView imageView;

    public DownloadImageTask(ImageView imageView) {
        this.imageView = imageView;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];

        return getBitmapImage(urldisplay);
    }

    protected void onPostExecute(Bitmap result) {
        imageView.setImageBitmap(result);
    }

    private static Bitmap getBitmapImage(String imageUrl){
        URL url = null;
        Bitmap image = null;
        try {
            url = new URL(imageUrl);
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            Log.e(TAG, "getBitmapImage: IOException",e );
        }
        return image;
    }

}
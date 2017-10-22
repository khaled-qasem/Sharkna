package com.sharkna.khaled.sharkna.model.db_utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
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

public class DownloadRoundImageTask extends AsyncTask<String, Void, Bitmap> {
    private static final String TAG = DownloadRoundImageTask.class.getName();
    ImageView imageView;

    public DownloadRoundImageTask(ImageView imageView) {
        this.imageView = imageView;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];

        return getRoundedShape(getBitmapImage(urldisplay));
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

    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 70;
        int targetHeight = 70;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }

}
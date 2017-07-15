package com.hongtao.weather.util;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

/**
 * author：Administrator on 2017/7/14/014 20:33
 * email：935245421@qq.com
 */
public class UpdateImageViewTask extends AsyncTask<Void, Void, Void> {
    private Bitmap mBitmap;
    private ImageView mImageView;

    public UpdateImageViewTask(Bitmap bitmap, ImageView imageView) {
        this.mBitmap = bitmap;
        this.mImageView = imageView;
    }

    @Override
    protected Void doInBackground(Void... params) {
        publishProgress();
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        mImageView.setImageBitmap(mBitmap);
        super.onProgressUpdate(values);
    }
}
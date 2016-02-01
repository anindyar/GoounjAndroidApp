package com.orgware.polling.utils;

/**
 * Created by Nandagopal on 31-Jan-16.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Environment;

import com.orgware.polling.interfaces.Appinterface;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * GraphicsUtil an utility class which convert the image in circular shape
 * Author : Mukesh Yadav
 */
public class GraphicsUtil implements Appinterface {

    /*
     * Draw image in circular shape Note: change the pixel size if you want
     * image small or large
     */
    public Bitmap getCircleBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = null;
        try {
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xffff0000;
            final Paint paint = new Paint();

            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);

            paint.setAntiAlias(true);
            paint.setDither(true);
            paint.setFilterBitmap(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawOval(rectF, paint);
            paint.setColor(Color.BLUE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth((float) 4);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return output;
    }

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    public static File getCacheFolder(Context context) {
        File cacheDir = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheDir = new File(Environment.getExternalStorageDirectory(), "cachefolder");
            if (!cacheDir.isDirectory()) {
                cacheDir.mkdirs();
            }
        }

        if (!cacheDir.isDirectory()) {
            cacheDir = context.getCacheDir(); //get system cache folder
        }

        return cacheDir;
    }


    public static File getDataFolder(Context context) {
        File dataDir = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            dataDir = new File(Environment.getExternalStorageDirectory(), "myappdata");
            if (!dataDir.isDirectory()) {
                dataDir.mkdirs();
            }
        }

        if (!dataDir.isDirectory()) {
            dataDir = context.getFilesDir();
        }

        return dataDir;
    }

    public static void WriteProfileImage(Context context) {
        new WriteImage().execute(context);
    }


    private static class WriteImage extends AsyncTask<Context, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Context... params) {

            try {
                Context context = params[0];
                SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_POLLING, context.MODE_PRIVATE);
                URL imgURL = new URL(preferences.getString(ENCODE_IMAGE, ""));
                URLConnection connection = imgURL.openConnection();
                InputStream inputStream = new BufferedInputStream(imgURL.openStream(), 10240);
                File cacheDir = GraphicsUtil.getCacheFolder(context);
                File cacheFile = new File(cacheDir, ENCODE_IMAGE + ".jpg");
                FileOutputStream outputStream = new FileOutputStream(cacheFile);

                byte buffer[] = new byte[1024];
                int dataSize;
                int loadedSize = 0;
                while ((dataSize = inputStream.read(buffer)) != -1) {
                    loadedSize += dataSize;
                    //publishProgress(loadedSize);
                    outputStream.write(buffer, 0, dataSize);
                }

                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {

        }

        @Override
        protected void onPreExecute() {

        }

    }


}


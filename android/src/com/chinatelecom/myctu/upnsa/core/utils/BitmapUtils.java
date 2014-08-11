package com.chinatelecom.myctu.upnsa.core.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;

/**
 * Utility class for Bitmap
 * <p/>
 * User: snowway
 * Date: 10/15/13
 * Time: 11:45 AM
 */
public class BitmapUtils {


    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    /**
     * 将Bitmap转换成byte数组
     *
     * @param bitmap
     * @return
     */
    public static byte[] toByteArray(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }


    /**
     * 将byte数组转换成Bitmap
     *
     * @param content 内容数组
     * @return Bitmap
     */
    public static Bitmap fromByteArray(byte[] content) {
        if (content == null || content.length == 0) {
            return null;
        }
        return BitmapFactory.decodeByteArray(content, 0, content.length);
    }
}

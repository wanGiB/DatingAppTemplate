package com.app.wemeet.utils;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.wemeet.top_level.ApplicationLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * @author Wan Clem
 */

public class UiUtils {

    private static Handler handler = new Handler(Looper.getMainLooper());

    public static void showSafeToast(final String toastMessage) {
        runOnMain(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ApplicationLoader.getInstance(), toastMessage, Toast.LENGTH_LONG).show();
            }
        });
    }


    private static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    private static void runOnMain(final @NonNull Runnable runnable) {
        if (isMainThread()) runnable.run();
        else handler.post(runnable);
    }

    public static void loadImage(final Activity context, final String photoPath, final ImageView imageView) {
        if (imageView != null) {
            if (!TextUtils.isEmpty(photoPath)) {
                if (context != null) {
                    if (Build.VERSION.SDK_INT >= 17) {
                        if (!context.isDestroyed()) {
                            Glide.with(context).load(photoPath).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(imageView);
                            imageView.invalidate();
                        }
                    } else {
                        Glide.with(context).load(photoPath).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(imageView);
                        imageView.invalidate();
                    }
                }
            }
        }
    }

    public static void displayDummyDialog() {
        showSafeToast("Yope!");
    }

}

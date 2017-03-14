package com.app.shixelsdating.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.shixelsdating.top_level.ApplicationLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by wan on 3/13/17.
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

    public static void runOnMain(final @NonNull Runnable runnable) {
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

    public static void sorryFontsAndIconsDontTally(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(Html.fromHtml("<b>Hi</b>, please pardon me on the majority of blurred icons. " +
                "\n\nIt was pretty hard to get the real icons and fonts used in the referenced arts." +
                "\n\nI basically snapshot the majority of the icons from the referenced art and stripped out the backgrounds."));

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

}

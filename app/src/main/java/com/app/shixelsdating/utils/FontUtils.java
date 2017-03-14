package com.app.shixelsdating.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Wan Clem
 */
public class FontUtils {

    private static Map<String, Typeface> sCachedFonts = new HashMap<>();

    public static Typeface getTypeface(Context context, String assetPath) {
        if (!sCachedFonts.containsKey(assetPath)) {
            try {
                Typeface t = Typeface.createFromAsset(context.getAssets(), "fonts/" + assetPath);
                sCachedFonts.put(assetPath, t);
                return t;
            } catch (Exception e) {
                return null;
            }
        }
        return sCachedFonts.get(assetPath);
    }
}

package com.github.warren_bank.rtsp_ipcam_viewer.common.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.inputmethod.InputMethodManager;
import android.view.View;
import android.view.ViewGroup;

import java.nio.charset.Charset;
import java.util.UUID;

public final class Utils {

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideKeyboard(Activity activity) {
        Context context = (Context) activity;
        View view       = activity.getWindow().getDecorView().findViewById(android.R.id.content);

        hideKeyboard(context, view);
    }

    public static void resizeViewByPercentOfScreenWidth(Context context, View view, float percent) {
        int width_px;
        ViewGroup.LayoutParams params;

        width_px     = context.getResources().getDisplayMetrics().widthPixels;
        width_px     = (int)(width_px * (percent/100));

        params       = (ViewGroup.LayoutParams) view.getLayoutParams();
        params.width = width_px;

        view.setLayoutParams(params);
    }

    public static long getUniqueLongFromString(String value) {
        Charset charset;
        byte[] input;
        long uniqueID;

        try {
            charset = Charset.forName("UTF-8");
            input   = value.getBytes(charset);
        }
        catch(Exception e) {
            input   = value.getBytes();
        }
        uniqueID = UUID.nameUUIDFromBytes(input).getMostSignificantBits();

        return uniqueID;
    }

    public static enum AspectRatio {
        W16H9, W3H2, W4H3, W5H4
    }

    public static int getHeightForAspectRatio(int width, AspectRatio ratio) {
        switch (ratio) {
            case W16H9:
                return (int) (width / 1.77);  // 16/9 = 1.77
            case W3H2:
                return (int) (width / 1.50);  //  3/2 = 1.50
            case W4H3:
                return (int) (width / 1.33);  //  4/3 = 1.33
            case W5H4:
                return (int) (width / 1.25);  //  5/4 = 1.25
            default:
                return 0;
        }
    }

    public static int getHeightForColumns(int columns) {
        int screen_width  = Resources.getSystem().getDisplayMetrics().widthPixels;
        int width_per_col = (int) (screen_width / columns);

        return getHeightForAspectRatio(width_per_col, AspectRatio.W16H9);
    }

}

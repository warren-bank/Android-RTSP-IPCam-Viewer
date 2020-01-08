package com.github.warren_bank.rtsp_ipcam_viewer.common.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.github.warren_bank.rtsp_ipcam_viewer.mock.data.MockData;

import java.util.ArrayList;

public final class SharedPrefs {

    public static final String PREFS_FILENAME = "PREFS_FILE";
    public static final String PREF_VIDEOS    = "VIDEOS";
    public static final String PREF_GRID_COLS = "GRID_COLS";

    private static SharedPreferences getPrefs(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    private static SharedPreferences.Editor getPrefsEditor(Context context) {
        SharedPreferences sharedPreferences   = getPrefs(context);
        SharedPreferences.Editor prefs_editor = sharedPreferences.edit();
        return prefs_editor;
    }

    private static void putString(Context context, String key, String value) {
        SharedPreferences.Editor prefs_editor = getPrefsEditor(context);
        prefs_editor.putString(key, value);
        prefs_editor.apply();
    }

    private static void putInt(Context context, String key, int value) {
        SharedPreferences.Editor prefs_editor = getPrefsEditor(context);
        prefs_editor.putInt(key, value);
        prefs_editor.apply();
    }

    private static String getString(Context context, String key, String defValue) {
        SharedPreferences sharedPreferences = getPrefs(context);
        return sharedPreferences.getString(key, defValue);
    }

    private static int getInt(Context context, String key, int defValue) {
        SharedPreferences sharedPreferences = getPrefs(context);
        return sharedPreferences.getInt(key, defValue);
    }

    public static void setVideos(Context context, String jsonVideos) {
        putString(context, PREF_VIDEOS, jsonVideos);
    }

    public static ArrayList<VideoType> getVideos(Context context) {
        return getVideos(context, false);
    }

    public static ArrayList<VideoType> getVideos(Context context, boolean allow_mock_data) {
        String jsonVideos = getString(context, PREF_VIDEOS, null);

        if ((jsonVideos == null) && allow_mock_data) {
            jsonVideos = MockData.getJsonVideos();

            setVideos(context, jsonVideos);
        }

        ArrayList<VideoType> videos = ((jsonVideos == null) || jsonVideos.isEmpty())
          ? new ArrayList<VideoType>()
          : VideoType.fromJson(jsonVideos)
        ;
        return videos;
    }

    public static void setGridColumns(Context context, int columns) {
        putInt(context, PREF_GRID_COLS, columns);
    }

    public static int getGridColumns(Context context, int defValue) {
        return getInt(context, PREF_GRID_COLS, defValue);
    }

}

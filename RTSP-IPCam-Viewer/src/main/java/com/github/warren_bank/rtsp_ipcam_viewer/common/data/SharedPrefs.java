package com.github.warren_bank.rtsp_ipcam_viewer.common.data;

import com.github.warren_bank.rtsp_ipcam_viewer.mock.data.MockData;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public final class SharedPrefs {

    public static final String PREFS_FILENAME = "PREFS_FILE";
    public static final String PREF_VIDEOS    = "VIDEOS";

    public static void setVideos(Context context, String jsonVideos) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefs_editor = sharedPreferences.edit();
        prefs_editor.putString(PREF_VIDEOS, jsonVideos);
        prefs_editor.apply();
    }

    public static ArrayList<VideoType> getVideos(Context context) {
        return getVideos(context, false);
    }

    public static ArrayList<VideoType> getVideos(Context context, boolean allow_mock_data) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE);
        String jsonVideos = sharedPreferences.getString(PREF_VIDEOS, null);

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

}

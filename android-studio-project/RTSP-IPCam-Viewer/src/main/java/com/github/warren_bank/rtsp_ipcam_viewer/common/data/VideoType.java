package com.github.warren_bank.rtsp_ipcam_viewer.common.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public final class VideoType {
    public String title;
    public String URL_low_res;
    public String URL_high_res;
    public boolean is_enabled;

    public VideoType(String title, String URL_low_res, String URL_high_res, boolean is_enabled) {
        this.title        = title;
        this.URL_low_res  = URL_low_res;
        this.URL_high_res = URL_high_res;
        this.is_enabled   = is_enabled;
    }

    @Override
    public String toString() {
        return ((title != null) && (title.length() > 0)) ? title : URL_low_res;
    }

    public boolean equals(VideoType that) {
        return (
            (that != null)
         && equals(that.URL_low_res)
        );
    }

    public boolean equals(String that_URL_low_res) {
        return (this.URL_low_res.equals(that_URL_low_res));
    }

    public boolean strict_equals(VideoType that) {
        return (
            (that != null)
         && this.title.equals(that.title)
         && this.URL_low_res.equals(that.URL_low_res)
         && ((this.URL_high_res == null)
                ? (that.URL_high_res == null)
                : this.URL_high_res.equals(that.URL_high_res)
            )
         && (this.is_enabled == that.is_enabled)
        );
    }

    // helpers

    public static ArrayList<VideoType> fromJson(String jsonVideos) {
        ArrayList<VideoType> videos;
        Gson gson = new Gson();
        videos = gson.fromJson(jsonVideos, new TypeToken<ArrayList<VideoType>>(){}.getType());
        return videos;
    }

    public static String toJson(ArrayList<VideoType> videos) {
        if (videos == null) return "";

        return new Gson().toJson(videos);
    }

    public static boolean contains(ArrayList<VideoType> items, VideoType item) {
        VideoType matchingItem = find(items, item);
        return (matchingItem != null);
    }

    public static boolean contains(ArrayList<VideoType> items, String URL_low_res) {
        VideoType matchingItem = find(items, URL_low_res);
        return (matchingItem != null);
    }

    public static VideoType find(ArrayList<VideoType> items, VideoType item) {
        if ((items == null) || (item == null)) return null;

        return find(items, item.URL_low_res);
    }

    public static VideoType find(ArrayList<VideoType> items, String URL_low_res) {
        if ((items == null) || (URL_low_res == null)) return null;

        for (int i=0; i < items.size(); i++) {
            VideoType nextItem = items.get(i);
            if (nextItem.equals(URL_low_res)) return nextItem;
        }
        return null;
    }

    public static ArrayList<VideoType> filterByEnabled(ArrayList<VideoType> items) {
        if (items == null) return null;

        ArrayList<VideoType> filtered = new ArrayList<VideoType>();
        for (int i=0; i < items.size(); i++) {
            VideoType nextItem = items.get(i);
            if (nextItem.is_enabled) filtered.add(nextItem);
        }
        return filtered;
    }
}

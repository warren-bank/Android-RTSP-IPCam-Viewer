package com.github.warren_bank.rtsp_ipcam_viewer.mock.data;

import com.github.warren_bank.rtsp_ipcam_viewer.common.helpers.FileUtils;

import android.content.Context;

public final class MockData {

    public static String getJsonVideos(Context context) {
        try {
            return FileUtils.getFileContents(
                context.getAssets().open("video_streams.json")
            );
        }
        catch(Exception e) {}
        return null;
    }
}

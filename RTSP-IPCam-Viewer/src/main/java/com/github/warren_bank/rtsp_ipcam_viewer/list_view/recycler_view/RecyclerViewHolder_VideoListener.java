package com.github.warren_bank.rtsp_ipcam_viewer.list_view.recycler_view;

import androidx.recyclerview.widget.RecyclerView;
import android.content.res.Resources;
import android.view.ViewGroup;

import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.video.VideoListener;

public class RecyclerViewHolder_VideoListener implements VideoListener {

    private static final float default_video_aspect_ratio = (float) (16 / 9);  // common ratios: (16:9), (5:4)

    private PlayerView view;
    private RecyclerView recyclerView;

    public RecyclerViewHolder_VideoListener(PlayerView view, RecyclerView recyclerView) {
        this.view         = view;
        this.recyclerView = recyclerView;
    }

    public void bind() {
        resize(default_video_aspect_ratio);
    }

    private void resize(float video_aspect_ratio) {
        int video_width  = (int) Resources.getSystem().getDisplayMetrics().widthPixels;
        int video_height = (int) (video_width / video_aspect_ratio);

        resize(video_height);
    }

    private void resize(int video_height) {
        boolean is_dirty = false;
        ViewGroup.LayoutParams params;

        if (video_height > 0) {
            params = view.getLayoutParams();
            if (params.height != video_height) {
                params.height  = video_height;
                view.setLayoutParams(params);
                is_dirty = true;
            }
        }

        if (is_dirty && !recyclerView.isComputingLayout()) {
            recyclerView.requestLayout();
        }
    }

    @Override
    public void onRenderedFirstFrame() {}

    @Override
    public void onSurfaceSizeChanged(int width, int height) {
        resize(height);
    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
     // resize(pixelWidthHeightRatio);
        resize(height);
    }

}

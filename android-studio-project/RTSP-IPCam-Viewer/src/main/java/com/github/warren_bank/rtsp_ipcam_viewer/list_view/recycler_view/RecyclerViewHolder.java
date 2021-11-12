package com.github.warren_bank.rtsp_ipcam_viewer.list_view.recycler_view;

import com.github.warren_bank.rtsp_ipcam_viewer.R;
import com.github.warren_bank.rtsp_ipcam_viewer.common.data.VideoType;
import com.github.warren_bank.rtsp_ipcam_viewer.common.helpers.ExoPlayerUtils;
import com.github.warren_bank.rtsp_ipcam_viewer.fullscreen_view.activities.VideoActivity;

import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;

public final class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private PlayerView view;
    private ExoPlayer  exoPlayer;
    private VideoType  data;

    public RecyclerViewHolder(View view) {
        this(view, 0);
    }

    public RecyclerViewHolder(View view, int defaultHeight) {
        super(view);

        this.view = (PlayerView) view;

        TextView title = (TextView) view.findViewById(R.id.exo_error_message);  // https://github.com/google/ExoPlayer/blob/r2.16.0/library/ui/src/main/res/layout/exo_player_view.xml#L45
        title.setGravity(Gravity.TOP);

        if (defaultHeight > 0) {
            this.view.setMinimumHeight(defaultHeight);
            title.setMaxHeight(defaultHeight);

            if (title.getTextSize() > defaultHeight) {
                title.setTextSize(
                    (defaultHeight > 10)
                      ? (float) (defaultHeight - 2)
                      : 0f
                );
            }
        }

        Context context = view.getContext();
        this.exoPlayer  = ExoPlayerUtils.initializeExoPlayer(context);

        this.exoPlayer.setVolume(0f);  // mute all videos in list view

        this.view.setControllerAutoShow(false);
        this.view.setUseController(false);
        this.view.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
        this.view.setPlayer(this.exoPlayer);

        this.view.setOnTouchListener(new GestureDetectorOnTouchListener(context) {
            @Override
            public void onClick() {
                super.onClick();
                doOnClick();
            }

            @Override
            public void onDoubleClick() {
                super.onDoubleClick();
                doOnClick();
            }

            @Override
            public void onLongClick() {
                super.onLongClick();
                doOnLongClick();
            }
        });
    }

    public void bind(VideoType data) {
        this.data = data;
        this.view.setCustomErrorMessage(data.title);

        stop();
        ExoPlayerUtils.prepareExoPlayer(exoPlayer, data.URL_low_res);
        play();
    }

    public void play() {
        try {
            exoPlayer.setPlayWhenReady(true);
        }
        catch (Exception e){}
    }

    public void pause() {
        try {
            exoPlayer.setPlayWhenReady(false);
        }
        catch (Exception e){}
    }

    public void stop() {
        try {
            exoPlayer.stop(true);
        }
        catch (Exception e){}
    }

    public void release() {
        try {
            exoPlayer.release();
        }
        catch (Exception e){}
    }

    // open selected video in fullscreen view
    private void doOnClick() {
        VideoActivity.open(
            view.getContext(),
            (data.URL_high_res != null) ? data.URL_high_res : data.URL_low_res
        );
    }

    // toggle play/pause of selected video
    private void doOnLongClick() {
        try {
            exoPlayer.setPlayWhenReady(
                !exoPlayer.getPlayWhenReady()
            );
        }
        catch (Exception e){}
    }
}

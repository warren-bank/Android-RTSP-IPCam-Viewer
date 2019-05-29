package com.github.warren_bank.rtsp_ipcam_viewer.list_view.recycler_view;

import com.github.warren_bank.rtsp_ipcam_viewer.common.data.VideoType;
import com.github.warren_bank.rtsp_ipcam_viewer.fullscreen_view.activities.VideoActivity;

import androidx.recyclerview.widget.RecyclerView;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.widget.VideoView;

public final class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private MediaPlayer.OnPreparedListener listener;
    private VideoView view;
    private boolean isPaused;

    private VideoType data;

    public RecyclerViewHolder(View view) {
        super(view);

        this.listener = new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mp) {
                RecyclerViewHolder.this.play();
            }
        };
        this.view = (VideoView) view;
        this.isPaused = false;

        this.view.setOnClickListener(this);
        this.view.setOnLongClickListener(this);
        this.view.setOnPreparedListener(this.listener);
    }

    public void bind(VideoType data) {
        this.data = data;

        release();

        view.setVideoURI(
            Uri.parse(data.URL_low_res)
        );
    }

    public void play() {
        try {
            if (isPaused)
                view.resume();
            else
                view.start();
        }
        catch (Exception e){}

        isPaused = false;
    }

    public void pause() {
        try {
            view.pause();
        }
        catch (Exception e){}

        isPaused = true;
    }

    public void stop() {
        try {
            view.stopPlayback();
        }
        catch (Exception e){}

        isPaused = false;
    }

    public void release() {
        try {
            stop();
            view.suspend();
        }
        catch (Exception e){}

        isPaused = false;
    }

    @Override
    public void onClick(View view) {
        VideoActivity.open(
            view.getContext(),
            (data.URL_high_res != null) ? data.URL_high_res : data.URL_low_res
        );
    }

    @Override
    public boolean onLongClick(View v) {
        if (view.isPlaying())
            pause();
        else
            play();

        return true;
    }

}

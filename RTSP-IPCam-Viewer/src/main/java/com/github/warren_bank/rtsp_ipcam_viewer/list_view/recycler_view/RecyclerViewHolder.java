package com.github.warren_bank.rtsp_ipcam_viewer.list_view.recycler_view;

import com.github.warren_bank.rtsp_ipcam_viewer.R;
import com.github.warren_bank.rtsp_ipcam_viewer.common.data.VideoType;
import com.github.warren_bank.rtsp_ipcam_viewer.fullscreen_view.activities.VideoActivity;

import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.rtsp.RtspDefaultClient;
import com.google.android.exoplayer2.source.rtsp.RtspMediaSource;
import com.google.android.exoplayer2.source.rtsp.core.Client;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public final class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {

    private PlayerView view;
    private SimpleExoPlayer exoPlayer;
    private DefaultHttpDataSourceFactory dataSourceFactory;

    private VideoType data;

    public RecyclerViewHolder(View view) {
        super(view);

        this.view = (PlayerView) view;

        Context context = view.getContext();
        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
        RenderersFactory renderersFactory = new DefaultRenderersFactory(context);
        this.exoPlayer = ExoPlayerFactory.newSimpleInstance(context, renderersFactory, trackSelector);

        String userAgent = context.getResources().getString(R.string.user_agent);
        this.dataSourceFactory = new DefaultHttpDataSourceFactory(userAgent);

        this.view.setOnTouchListener(this);
        this.view.setUseController(false);
        this.view.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
        this.view.setPlayer(this.exoPlayer);

        this.exoPlayer.setVolume(0f);  // mute all videos in list view
    }

    public void bind(VideoType data) {
        this.data = data;

        stop();

        Uri uri = Uri.parse(data.URL_low_res);
        MediaSource source;

        if (Util.isRtspUri(uri)) {
            source = new RtspMediaSource.Factory(RtspDefaultClient.factory()
                .setFlags(Client.FLAG_ENABLE_RTCP_SUPPORT)
                .setNatMethod(Client.RTSP_NAT_DUMMY))
                .createMediaSource(uri);
        } else {
            source = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
        }

        exoPlayer.prepare(source);

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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            long click_duration = event.getEventTime() - event.getDownTime();  // milliseconds
            int max_duration, max_distance;

            max_duration = ViewConfiguration.getLongPressTimeout();
            if (click_duration <= max_duration) {
                doOnClick();
                return true;
            }

            max_duration = max_duration * 10;
            max_distance = ViewConfiguration.getTouchSlop();
            if (
                (click_duration <= max_duration)
             && (getDistance(event, max_distance) <= max_distance)
            ) {
                doOnLongClick();
                return true;
            }
        }

        return false;
    }

    private static float getDistance(MotionEvent ev, int max) {
        float distanceSum = 0;
        final int historySize = ev.getHistorySize();

        if (historySize == 0) return 0f;

        float startX = ev.getHistoricalX(0, 0);
        float startY = ev.getHistoricalY(0, 0);

        for (int h = 1; h < historySize; h++) {
            // historical point
            float hx = ev.getHistoricalX(0, h);
            float hy = ev.getHistoricalY(0, h);

            // distance between startX,startY and historical point
            float dx = (hx - startX);
            float dy = (hy - startY);
            distanceSum += Math.sqrt(dx * dx + dy * dy);

            // short-circuit test
            if ((max > 0) && (distanceSum > max))
                return distanceSum;

            // make historical point the start point for next loop iteration
            startX = hx;
            startY = hy;
        }

        // add distance from last historical point to event's point
        float dx = (ev.getX(0) - startX);
        float dy = (ev.getY(0) - startY);
        distanceSum += Math.sqrt(dx * dx + dy * dy);

        return distanceSum;
    }

    private void doOnClick() {
        VideoActivity.open(
            view.getContext(),
            (data.URL_high_res != null) ? data.URL_high_res : data.URL_low_res
        );
    }

    private void doOnLongClick() {
        try {
            exoPlayer.setPlayWhenReady(
                !exoPlayer.getPlayWhenReady()
            );
        }
        catch (Exception e){}
    }

}

package com.github.warren_bank.rtsp_ipcam_viewer.fullscreen_view.activities;

import com.github.warren_bank.rtsp_ipcam_viewer.R;
import com.github.warren_bank.rtsp_ipcam_viewer.common.helpers.ExoPlayerUtils;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;

public class VideoActivity extends AppCompatActivity {
    private static final String EXTRA_URL = "URL";

    private PlayerView view;
    private ExoPlayer exoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fullscreen_view_activities_videoactivity);

        this.view = (PlayerView) findViewById(R.id.player_view);

        Context context = (Context) this;
        this.exoPlayer  = ExoPlayerUtils.initializeExoPlayer(context);

        this.view.setUseController(true);
        this.view.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
        this.view.setPlayer(this.exoPlayer);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_URL)) {
            String url = intent.getStringExtra(EXTRA_URL);

            ExoPlayerUtils.prepareExoPlayer(exoPlayer, url);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        play();
    }

    @Override
    protected void onPause() {
        super.onPause();

        pause();
    }

    @Override
    protected void onStop() {
        super.onStop();

        view.setPlayer(null);
        stop();
        release();

        exoPlayer = null;
        view      = null;

        setContentView(new View(this));
        System.gc();
        finish();
    }

    private void play() {
        try {
            exoPlayer.setPlayWhenReady(true);
        }
        catch (Exception e){}
    }

    private void pause() {
        try {
            exoPlayer.setPlayWhenReady(false);
        }
        catch (Exception e){}
    }

    private void stop() {
        try {
            exoPlayer.stop(true);
        }
        catch (Exception e){}
    }

    private void release() {
        try {
            exoPlayer.release();
        }
        catch (Exception e){}
    }

    public static void open(Context context, String url) {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        intent.putExtra(EXTRA_URL, url);
        context.startActivity(intent);
    }
}

package com.github.warren_bank.rtsp_ipcam_viewer.fullscreen_view.activities;

import com.github.warren_bank.rtsp_ipcam_viewer.R;
import com.github.warren_bank.rtsp_ipcam_viewer.common.helpers.ExoPlayerUtils;
import com.github.warren_bank.rtsp_ipcam_viewer.fullscreen_view.utils.PipUtils;

import androidx.annotation.RequiresApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.AspectRatioFrameLayout;
import androidx.media3.ui.PlayerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class VideoActivity extends Activity implements PlayerView.FullscreenButtonClickListener {
    private static final String EXTRA_URL = "URL";

    private PlayerView view;
    private ExoPlayer exoPlayer;
    private boolean enterPipMode;
    private boolean isPipMode;

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

        this.enterPipMode = false;
        this.isPipMode = false;

        if (PipUtils.supportsPictureInPictureMode(this))
            this.view.setFullscreenButtonClickListener(this);

        processIntent(getIntent());
    }

    @Override
    public void onNewIntent (Intent intent) {
      super.onNewIntent(intent);

      processIntent(intent);
    }

    private void processIntent(Intent intent) {
        if (intent.hasExtra(EXTRA_URL)) {
            String url = intent.getStringExtra(EXTRA_URL);

            stop();
            ExoPlayerUtils.prepareExoPlayer(exoPlayer, url);
            play();
        }
    }

    @Override
    @RequiresApi(24)
    public void onPictureInPictureModeChanged (boolean isInPictureInPictureMode) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode);

        isPipMode = isInPictureInPictureMode;
    }

    private void updatePictureInPictureMode(Intent intent) {
        if (PipUtils.supportsPictureInPictureMode(this))
            view.setFullscreenButtonState(!enterPipMode);

        if (!isPipMode && enterPipMode) {
            enterPipMode = false;
            isPipMode = true; // redundant to avoid race condition: will also be set by onPictureInPictureModeChanged(), but needs to be set early for onPause()
            PipUtils.enterPictureInPictureMode(this);
        }
        else if (isPipMode && !enterPipMode && (intent != null)) {
            isPipMode = false;
            PipUtils.exitPictureInPictureMode(this, intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        updatePictureInPictureMode(null);
        play();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (!enterPipMode && !isPipMode)
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
            exoPlayer.stop();
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
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(EXTRA_URL, url);
        context.startActivity(intent);
    }

    // Event handler interfaces.

    // PlayerView.FullscreenButtonClickListener
    @Override
    public void onFullscreenButtonClick(boolean isFullscreen) {
        if (!isFullscreen) {
            enterPipMode = true;
            updatePictureInPictureMode(null);
        }
    }
}

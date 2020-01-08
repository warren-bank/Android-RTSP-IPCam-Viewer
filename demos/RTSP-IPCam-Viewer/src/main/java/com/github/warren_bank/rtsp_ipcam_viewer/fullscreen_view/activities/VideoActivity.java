package com.github.warren_bank.rtsp_ipcam_viewer.fullscreen_view.activities;

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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.github.warren_bank.rtsp_ipcam_viewer.R;

import androidx.appcompat.app.AppCompatActivity;

public class VideoActivity extends AppCompatActivity {
    private static final String EXTRA_URL = "URL";

    private PlayerView view;
    private SimpleExoPlayer exoPlayer;
    private DefaultHttpDataSourceFactory dataSourceFactory;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fullscreen_view_activities_videoactivity);

        this.view = (PlayerView) findViewById(R.id.player_view);

        Context context = (Context) this;
        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
        RenderersFactory renderersFactory = new DefaultRenderersFactory(context);
        this.exoPlayer = ExoPlayerFactory.newSimpleInstance(context, renderersFactory, trackSelector);

        String userAgent = context.getResources().getString(R.string.user_agent);
        this.dataSourceFactory = new DefaultHttpDataSourceFactory(userAgent);

        this.view.setUseController(true);
        this.view.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
        this.view.setPlayer(this.exoPlayer);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_URL)) {
            this.url = intent.getStringExtra(EXTRA_URL);

            prepare();
        }
    }

    @Override
    public void onBackPressed() {
        stop();
        release();
        finish();

        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();

        play();
    }

    @Override
    protected void onPause() {
        super.onPause();

        stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        release();
    }

    private void prepare() {
        Uri uri = Uri.parse(this.url);
        MediaSource source;

        if (Util.isRtspUri(uri)) {
            source = new RtspMediaSource.Factory(RtspDefaultClient.factory()
                .setFlags(Client.FLAG_ENABLE_RTCP_SUPPORT)
                .setNatMethod(Client.RTSP_NAT_DUMMY)
                .setPlayer(exoPlayer))
                .createMediaSource(uri);
        } else {
            source = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
        }

        exoPlayer.prepare(source);
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
        intent.putExtra(EXTRA_URL, url);
        context.startActivity(intent);
    }
}

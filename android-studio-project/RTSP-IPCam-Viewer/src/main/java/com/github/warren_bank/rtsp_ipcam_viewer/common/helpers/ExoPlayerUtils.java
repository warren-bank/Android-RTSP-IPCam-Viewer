package com.github.warren_bank.rtsp_ipcam_viewer.common.helpers;

import com.github.warren_bank.rtsp_ipcam_viewer.R;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.analytics.AnalyticsCollector;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceFactory;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.rtsp.RtspMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.util.Clock;

public final class ExoPlayerUtils {

  private static String USER_AGENT;
  private static DefaultHttpDataSource.Factory httpDataSourceFactory;

  private static RenderersFactory renderersFactory;
  private static DefaultExtractorsFactory extractorsFactory;
  private static MediaSourceFactory mediaSourceFactory;
  private static DefaultTrackSelector trackSelector;
  private static DefaultLoadControl loadControl;
  private static BandwidthMeter bandwidthMeter;

  private static synchronized void setUserAgent(Context context) {
    setUserAgent(
      context.getString(R.string.user_agent)
    );
  }

  private static synchronized void setUserAgent(String userAgent) {
    USER_AGENT = userAgent;

    if (httpDataSourceFactory != null) {
      httpDataSourceFactory.setUserAgent(USER_AGENT);
    }
  }

  private static synchronized DefaultHttpDataSource.Factory getHttpDataSourceFactory() {
    if (httpDataSourceFactory == null) {
      httpDataSourceFactory = new DefaultHttpDataSource.Factory();

      if (USER_AGENT != null) {
        httpDataSourceFactory.setUserAgent(USER_AGENT);
      }
    }
    return httpDataSourceFactory;
  }

  private static RenderersFactory getRenderersFactory(Context context) {
    if (renderersFactory == null) {
      renderersFactory = new DefaultRenderersFactory(context);
    }
    return renderersFactory;
  }

  private static DefaultExtractorsFactory getExtractorsFactory() {
    if (extractorsFactory == null) {
      extractorsFactory = new DefaultExtractorsFactory();
    }
    return extractorsFactory;
  }

  private static MediaSourceFactory getMediaSourceFactory(Context context) {
    if (mediaSourceFactory == null) {
      mediaSourceFactory = new DefaultMediaSourceFactory(context, getExtractorsFactory());
    }
    return mediaSourceFactory;
  }

  private static DefaultTrackSelector getTrackSelector(Context context) {
    if (trackSelector == null) {
      trackSelector = new DefaultTrackSelector(context);
    }
    return trackSelector;
  }

  private static DefaultLoadControl getLoadControl() {
    if (loadControl == null) {
      loadControl = new DefaultLoadControl.Builder().build();
    }
    return loadControl;
  }

  private static BandwidthMeter getBandwidthMeter(Context context) {
    if (bandwidthMeter == null) {
      bandwidthMeter = DefaultBandwidthMeter.getSingletonInstance(context);
    }
    return bandwidthMeter;
  }

  // not a singleton; reuse causes fatal exception
  private static AnalyticsCollector getAnalyticsCollector() {
    return new AnalyticsCollector(Clock.DEFAULT);
  }

  public static ExoPlayer initializeExoPlayer(Context context) {
    context = context.getApplicationContext();

    if (USER_AGENT == null)
      setUserAgent(context);

    return new ExoPlayer.Builder(
      context,
      getRenderersFactory(context),
      getMediaSourceFactory(context),
      getTrackSelector(context),
      getLoadControl(),
      getBandwidthMeter(context),
      getAnalyticsCollector()
    ).build();
  }

  public static void prepareExoPlayer(ExoPlayer player, String video_url) {
    Uri uri = Uri.parse(video_url);
    boolean isRtsp = uri.getScheme().toLowerCase().startsWith("rtsp");
    MediaItem mediaItem = MediaItem.fromUri(uri);
    MediaSource mediaSource;

    if (isRtsp) {
      mediaSource = new RtspMediaSource.Factory().createMediaSource(mediaItem);
    }
    else {
      mediaSource = new ProgressiveMediaSource.Factory(getHttpDataSourceFactory()).createMediaSource(mediaItem);
    }

    player.setMediaSource(mediaSource);
    player.prepare();
  }

}

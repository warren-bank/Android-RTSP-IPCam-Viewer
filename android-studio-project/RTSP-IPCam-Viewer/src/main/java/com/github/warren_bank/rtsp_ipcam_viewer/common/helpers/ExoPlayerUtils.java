package com.github.warren_bank.rtsp_ipcam_viewer.common.helpers;

import com.github.warren_bank.rtsp_ipcam_viewer.R;

import android.content.Context;
import android.net.Uri;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;

import androidx.media3.common.MediaItem;
import androidx.media3.common.util.Clock;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.datasource.rtmp.RtmpDataSource;
import androidx.media3.exoplayer.DefaultLoadControl;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.DefaultRenderersFactory;
import androidx.media3.exoplayer.RenderersFactory;
import androidx.media3.exoplayer.analytics.DefaultAnalyticsCollector;
import androidx.media3.exoplayer.rtsp.RtspMediaSource;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.MediaSourceFactory;
import androidx.media3.exoplayer.source.ProgressiveMediaSource;
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector;
import androidx.media3.exoplayer.upstream.BandwidthMeter;
import androidx.media3.exoplayer.upstream.DefaultBandwidthMeter;
import androidx.media3.extractor.DefaultExtractorsFactory;

public final class ExoPlayerUtils {

  private static RtmpDataSource.Factory rtmpDataSourceFactory;
  private static DefaultHttpDataSource.Factory httpDataSourceFactory;
  private static String USER_AGENT;

  private static RenderersFactory renderersFactory;
  private static DefaultExtractorsFactory extractorsFactory;
  private static MediaSourceFactory mediaSourceFactory;
  private static DefaultLoadControl loadControl;
  private static BandwidthMeter bandwidthMeter;
  private static int playerId = 0;
  private static Looper playbackLooper;

  private static synchronized RtmpDataSource.Factory getRtmpDataSourceFactory() {
    if (rtmpDataSourceFactory == null) {
      rtmpDataSourceFactory = new RtmpDataSource.Factory();
    }
    return rtmpDataSourceFactory;
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

  // not a singleton; reuse causes fatal exception
  private static DefaultTrackSelector getTrackSelector(Context context) {
    return new DefaultTrackSelector(context);
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
  private static DefaultAnalyticsCollector getAnalyticsCollector() {
    return new DefaultAnalyticsCollector(Clock.DEFAULT);
  }

  private static String getDistinctPlayerName() {
    return String.valueOf(++playerId);
  }

  private static Looper getPlaybackLooper() {
    if (playbackLooper == null) {
      HandlerThread internalPlaybackThread = new HandlerThread("ExoPlayer:Playback", Process.THREAD_PRIORITY_AUDIO);
      internalPlaybackThread.start();
      playbackLooper = internalPlaybackThread.getLooper();
    }
    return playbackLooper;
  }

  public static ExoPlayer initializeExoPlayer(Context context) {
    context = context.getApplicationContext();

    if (USER_AGENT == null)
      setUserAgent(context);

    ExoPlayer.Builder builder = new ExoPlayer.Builder(
      context,
      getRenderersFactory(context),
      getMediaSourceFactory(context),
      getTrackSelector(context),
      getLoadControl(),
      getBandwidthMeter(context),
      getAnalyticsCollector()
    );

    // https://github.com/androidx/media/releases/tag/1.4.0
    //   changelog: Add PlayerId to most methods of LoadControl to enable LoadControl implementations to support multiple players.
    // https://github.com/androidx/media/blob/1.4.0/libraries/exoplayer/src/main/java/androidx/media3/exoplayer/ExoPlayer.java#L1293
    //   builder.setName(String playerName)
    // https://github.com/androidx/media/blob/1.4.0/libraries/exoplayer/src/main/java/androidx/media3/exoplayer/ExoPlayerImpl.java#L354
    //   PlayerId playerId = new PlayerId(builder.playerName)
    // https://github.com/androidx/media/blob/1.4.0/libraries/exoplayer/src/main/java/androidx/media3/exoplayer/ExoPlayerImpl.java#L362
    //   internalPlayer = new ExoPlayerImplInternal(..., playerId, ...)
    builder.setName(
      getDistinctPlayerName()
    );

    // https://github.com/androidx/media/blob/1.4.0/libraries/exoplayer/src/main/java/androidx/media3/exoplayer/ExoPlayer.java#L1275
    // https://github.com/androidx/media/blob/1.4.0/libraries/exoplayer/src/main/java/androidx/media3/exoplayer/ExoPlayerImplInternal.java#L317-L322
    //   Players that share the same LoadControl must share the same playback thread.
    builder.setPlaybackLooper(
      getPlaybackLooper()
    );

    return builder.build();
  }

  public static void prepareExoPlayer(ExoPlayer player, String video_url) {
    boolean isRtsp      = false;
    boolean isRtmp      = false;
    MediaItem mediaItem = null;
    MediaSource mediaSource;

    try {
      Uri uri       = Uri.parse(video_url);
      String scheme = uri.getScheme();
      if (scheme == null) throw new Exception("invalid URL");

      scheme = scheme.toLowerCase();
      isRtsp = scheme.startsWith("rtsp");
      isRtmp = scheme.startsWith("rtmp");

      mediaItem = MediaItem.fromUri(uri);
      if (mediaItem == null) throw new Exception("invalid media URI");
    }
    catch(Exception e) {
      return;
    }

    if (isRtsp) {
      mediaSource = new RtspMediaSource.Factory().createMediaSource(mediaItem);
    }
    else if (isRtmp) {
      mediaSource = new ProgressiveMediaSource.Factory(getRtmpDataSourceFactory()).createMediaSource(mediaItem);
    }
    else {
      mediaSource = new ProgressiveMediaSource.Factory(getHttpDataSourceFactory()).createMediaSource(mediaItem);
    }

    player.setMediaSource(mediaSource);
    player.prepare();
  }

}

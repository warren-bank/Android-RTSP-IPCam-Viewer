package com.github.warren_bank.rtsp_ipcam_viewer.fullscreen_view.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.warren_bank.rtsp_ipcam_viewer.R
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.RenderersFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.rtsp.RtspDefaultClient
import com.google.android.exoplayer2.source.rtsp.RtspMediaSource
import com.google.android.exoplayer2.source.rtsp.core.Client
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util

class VideoActivity : AppCompatActivity() {
    private var view: PlayerView? = null
    private var exoPlayer: SimpleExoPlayer? = null
    private var dataSourceFactory: DefaultHttpDataSourceFactory? =
        null
    private var url: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fullscreen_view_activities_videoactivity)
        view =
            findViewById<View>(R.id.player_view) as PlayerView
        val context = this as Context
        val trackSelector =
            DefaultTrackSelector()
        val renderersFactory: RenderersFactory =
            DefaultRenderersFactory(context)
        exoPlayer = ExoPlayerFactory.newSimpleInstance(
            context,
            renderersFactory,
            trackSelector
        )
        val userAgent = context.resources
            .getString(R.string.user_agent)
        dataSourceFactory =
            DefaultHttpDataSourceFactory(userAgent)
        view!!.useController = true
        view!!.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
        view!!.setPlayer(exoPlayer)
        val intent = intent
        if (intent.hasExtra(EXTRA_URL)) {
            url = intent.getStringExtra(EXTRA_URL)
            prepare()
        }
    }

    override fun onResume() {
        super.onResume()
        play()
    }

    override fun onPause() {
        super.onPause()
        stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        release()
    }

    private fun prepare() {
        val uri = Uri.parse(url)
        val source: MediaSource
        source = if (Util.isRtspUri(uri)) {
            RtspMediaSource.Factory(
                RtspDefaultClient.factory()
                    .setFlags(Client.FLAG_ENABLE_RTCP_SUPPORT)
                    .setNatMethod(Client.RTSP_NAT_DUMMY)
            )
                .createMediaSource(uri)
        } else {
            ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri)
        }
        exoPlayer!!.prepare(source)
    }

    private fun play() {
        try {
            exoPlayer!!.playWhenReady = true
        } catch (e: Exception) {
        }
    }

    private fun pause() {
        try {
            exoPlayer!!.playWhenReady = false
        } catch (e: Exception) {
        }
    }

    private fun stop() {
        try {
            exoPlayer!!.stop(true)
        } catch (e: Exception) {
        }
    }

    private fun release() {
        try {
            exoPlayer!!.release()
        } catch (e: Exception) {
        }
    }

    companion object {
        private const val EXTRA_URL = "URL"
        @JvmStatic
        fun open(context: Context, url: String?) {
            val intent =
                Intent(context, VideoActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME)
            intent.putExtra(EXTRA_URL, url)
            context.startActivity(intent)
        }
    }
}
package com.github.warren_bank.rtsp_ipcam_viewer.fullscreen_view.activities;

import com.github.warren_bank.rtsp_ipcam_viewer.R;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {
    private static final String EXTRA_URL = "URL";

    private VideoView video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fullscreen_view_activities_videoactivity);

        this.video = (VideoView) findViewById(R.id.video_view);

        Intent intent = getIntent();
        String url;

        if (intent.hasExtra(EXTRA_URL)) {
            url = intent.getStringExtra(EXTRA_URL);

            video.setVideoURI(
                Uri.parse(url)
            );
        }
    }

    public static void open(Context context, String url) {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        intent.putExtra(EXTRA_URL, url);
        context.startActivity(intent);
    }
}

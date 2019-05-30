package com.github.warren_bank.rtsp_ipcam_viewer.grid_view.activities;

import com.github.warren_bank.rtsp_ipcam_viewer.R;
import com.github.warren_bank.rtsp_ipcam_viewer.common.data.SharedPrefs;
import com.github.warren_bank.rtsp_ipcam_viewer.common.data.VideoType;
// TO DO: replace with grid view
import com.github.warren_bank.rtsp_ipcam_viewer.list_view.recycler_view.RecyclerViewInit;
import com.github.warren_bank.rtsp_ipcam_viewer.list_view.recycler_view.RecyclerViewAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public final class GridActivity extends AppCompatActivity {
    private static final String EXTRA_JSON_VIDEOS = "JSON_VIDEOS";

    private static ArrayList<VideoType> videos;

    private RecyclerView        recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_view_activities_listactivity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();

        this.videos = (intent.hasExtra(EXTRA_JSON_VIDEOS))
            ? VideoType.fromJson(intent.getStringExtra(EXTRA_JSON_VIDEOS))
            : VideoType.filterByEnabled(SharedPrefs.getVideos(this))
        ;
        this.recyclerView        = (RecyclerView) findViewById(R.id.recycler_view);
        this.recyclerViewAdapter = RecyclerViewInit.adapter(this, this.recyclerView, this.videos);
    }

    @Override
    public void onResume() {
        super.onResume();

        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();

        recyclerView.setAdapter(null);
    }

    public static void open(Context context, String jsonVideos) {
        Intent intent = new Intent(context, GridActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        if ((jsonVideos != null) && (!jsonVideos.isEmpty()))
            intent.putExtra(EXTRA_JSON_VIDEOS, jsonVideos);
        context.startActivity(intent);
    }

}

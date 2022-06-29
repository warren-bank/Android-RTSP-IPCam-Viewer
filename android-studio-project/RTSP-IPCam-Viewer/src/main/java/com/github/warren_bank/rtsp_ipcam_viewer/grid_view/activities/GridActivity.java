package com.github.warren_bank.rtsp_ipcam_viewer.grid_view.activities;

import com.github.warren_bank.rtsp_ipcam_viewer.R;
import com.github.warren_bank.rtsp_ipcam_viewer.common.data.SharedPrefs;
import com.github.warren_bank.rtsp_ipcam_viewer.common.data.VideoType;
import com.github.warren_bank.rtsp_ipcam_viewer.grid_view.recycler_view.RecyclerViewInit;
import com.github.warren_bank.rtsp_ipcam_viewer.list_view.recycler_view.RecyclerViewAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import java.util.ArrayList;

public final class GridActivity extends AppCompatActivity {
    private static final String EXTRA_JSON_VIDEOS  = "JSON_VIDEOS";
    private static final String EXTRA_GRID_COLUMNS = "GRID_COLUMNS";

    private static ArrayList<VideoType> videos;
    private static int columns;

    private RecyclerView        recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_view_activities_listactivity);

        Intent intent = getIntent();

        this.videos = (intent.hasExtra(EXTRA_JSON_VIDEOS))
            ? VideoType.fromJson(intent.getStringExtra(EXTRA_JSON_VIDEOS))
            : VideoType.filterByEnabled(SharedPrefs.getVideos(this))
        ;
        this.columns             = intent.getIntExtra(EXTRA_GRID_COLUMNS, 2);
        this.recyclerView        = (RecyclerView) findViewById(R.id.recycler_view);
        this.recyclerViewAdapter = RecyclerViewInit.adapter(this, this.recyclerView, this.videos, this.columns);
    }

    @Override
    public void onPause() {
        super.onPause();

        // deallocate all resources that consume memory
        if (recyclerView != null) {
            recyclerView.setAdapter(null);
            recyclerViewAdapter.release();
            recyclerViewAdapter = null;
            recyclerView = null;
            videos = null;
            setContentView(new View(this));
            System.gc();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (recyclerView == null) {
            // give the garbage collector a little time to recover unused memory
            new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // display a fresh instance of this Activity
                        recreate();
                    }
            }, 1000);
        }
    }

    public static void open(Context context, String jsonVideos, int columns) {
        Intent intent = new Intent(context, GridActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        if ((jsonVideos != null) && (!jsonVideos.isEmpty()))
            intent.putExtra(EXTRA_JSON_VIDEOS, jsonVideos);
        if (columns > 1)
            intent.putExtra(EXTRA_GRID_COLUMNS, columns);
        context.startActivity(intent);
    }

}

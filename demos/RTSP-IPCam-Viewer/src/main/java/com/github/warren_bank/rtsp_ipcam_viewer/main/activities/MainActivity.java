package com.github.warren_bank.rtsp_ipcam_viewer.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.github.warren_bank.rtsp_ipcam_viewer.R;
import com.github.warren_bank.rtsp_ipcam_viewer.common.activities.ExitActivity;
import com.github.warren_bank.rtsp_ipcam_viewer.common.activities.FilePicker;
import com.github.warren_bank.rtsp_ipcam_viewer.common.data.SharedPrefs;
import com.github.warren_bank.rtsp_ipcam_viewer.common.data.VideoType;
import com.github.warren_bank.rtsp_ipcam_viewer.common.helpers.FileUtils;
import com.github.warren_bank.rtsp_ipcam_viewer.grid_view.activities.GridActivity;
import com.github.warren_bank.rtsp_ipcam_viewer.list_view.activities.ListActivity;
import com.github.warren_bank.rtsp_ipcam_viewer.main.dialogs.add_video.VideoDialog;
import com.github.warren_bank.rtsp_ipcam_viewer.main.dialogs.grid_view_columns.GridColumnsDialog;
import com.github.warren_bank.rtsp_ipcam_viewer.main.recycler_view.RecyclerViewAdapter;
import com.github.warren_bank.rtsp_ipcam_viewer.main.recycler_view.RecyclerViewInit;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

/*
 * -------------------------------------
open 'add video' dialog
open list
open grid
read file
  import
  open list
  open grid

list items
  check: toggle video 'enabled' field
  click: open 'edit video' dialog
 * -------------------------------------
 */

public class MainActivity extends AppCompatActivity {
    private static ArrayList<VideoType> videos;

    private RecyclerView        recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activities_mainactivity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(null);

        this.videos              = SharedPrefs.getVideos(this, /* allow_mock_data= */ true);
        this.recyclerView        = (RecyclerView) findViewById(R.id.recycler_view);
        this.recyclerViewAdapter = RecyclerViewInit.adapter(this, this.recyclerView, this.videos);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activities_mainactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.action_add_video:
                VideoDialog.add(
                    MainActivity.this,
                    recyclerView,
                    new VideoDialog.ResultListener() {
                        @Override
                        public void onResult(VideoType new_video) {
                            if (new_video != null) {
                                if (videos.add(new_video)) {
                                    int position = videos.size() - 1;
                                    recyclerViewAdapter.notifyItemInserted(position);
                                    RecyclerViewAdapter.saveVideos(recyclerViewAdapter);
                                }
                            }
                        }
                    }
                );
                return true;
            case R.id.action_open_list:
                ListActivity.open(MainActivity.this, null);
                return true;
            case R.id.action_open_grid_2col:
                GridActivity.open(MainActivity.this, null, 2);
                return true;
            case R.id.action_open_grid_Ncol:
                GridColumnsDialog.show(
                    MainActivity.this,
                    recyclerView,
                    new GridColumnsDialog.ResultListener() {
                        @Override
                        public void onResult(int columns) {
                            if (columns > 1) {
                                GridActivity.open(MainActivity.this, null, columns);
                            }
                        }
                    }
                );
                return true;
            case R.id.action_read_file:
                FilePicker.open(
                    /* activity= */ MainActivity.this,
                    /* listener= */ new FilePicker.ResultListener() {
                        @Override
                        public void onResult(String filepath) {
                            try {
                                MainActivity self = MainActivity.this;
                                String jsonVideos = FileUtils.getFileContents(filepath);

                                // import
                                ArrayList<VideoType> new_videos = VideoType.fromJson(jsonVideos);
                                int positionStart = self.videos.size();
                                int itemCount     = new_videos.size();
                                self.videos.addAll(new_videos);
                                self.recyclerViewAdapter.notifyItemRangeInserted(positionStart, itemCount);
                                jsonVideos = VideoType.toJson(self.videos);
                                SharedPrefs.setVideos(self, jsonVideos);
                            }
                            catch(Exception e) {}
                        }
                    },
                    /* showHiddenFiles=   */ true,
                    /* filterPattern=     */ ".*\\.(?:json|js|txt)$",
                    /* filterDirectories= */ false
                );
                return true;
            case R.id.action_exit:
                ExitActivity.open(MainActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FilePicker.ResultHandler.onActivityResult(requestCode, resultCode, data);
    }
}

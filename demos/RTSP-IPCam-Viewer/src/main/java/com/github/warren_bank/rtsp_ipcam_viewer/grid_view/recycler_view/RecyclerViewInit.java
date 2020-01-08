package com.github.warren_bank.rtsp_ipcam_viewer.grid_view.recycler_view;

import android.content.Context;

import com.github.warren_bank.rtsp_ipcam_viewer.R;
import com.github.warren_bank.rtsp_ipcam_viewer.common.data.VideoType;
import com.github.warren_bank.rtsp_ipcam_viewer.common.helpers.Utils;
import com.github.warren_bank.rtsp_ipcam_viewer.list_view.recycler_view.RecyclerViewAdapter;

import java.util.ArrayList;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public final class RecyclerViewInit {

    public static RecyclerViewAdapter adapter(Context context, RecyclerView recyclerView, ArrayList<VideoType> videos, int columns) {
        GridLayoutManager   layoutmgr = new GridLayoutManager(context, columns);
        RecyclerViewAdapter adapter   = new RecyclerViewAdapter(context, videos, Utils.getHeightForColumns(columns));

        recyclerView.setLayoutManager(layoutmgr);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        // add spacing between grid cells
        RecyclerViewItemDecoration itemDecoration = new RecyclerViewItemDecoration(context, R.dimen.grid_cell_spacing);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setClipToPadding(false);
        recyclerView.setPadding(itemDecoration.grid_cell_spacing, itemDecoration.grid_cell_spacing, itemDecoration.grid_cell_spacing, itemDecoration.grid_cell_spacing);

        return adapter;
    }

}

package com.github.warren_bank.rtsp_ipcam_viewer.list_view.recycler_view;

import android.content.Context;

import com.github.warren_bank.rtsp_ipcam_viewer.common.data.VideoType;

import java.util.ArrayList;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public final class RecyclerViewInit {

    public static RecyclerViewAdapter adapter(Context context, RecyclerView recyclerView, ArrayList<VideoType> videos) {
        LinearLayoutManager layoutmgr = new LinearLayoutManager(context);
        RecyclerViewAdapter adapter   = new RecyclerViewAdapter(context, videos, 150);
        RecyclerViewCallback callback = new RecyclerViewCallback(adapter);
        ItemTouchHelper helper        = new ItemTouchHelper(callback);

        recyclerView.setLayoutManager(layoutmgr);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        helper.attachToRecyclerView(recyclerView);

        // add divider between list items
        recyclerView.addItemDecoration(
            new DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        );

        return adapter;
    }

}

package com.github.warren_bank.rtsp_ipcam_viewer.main.recycler_view;

import com.github.warren_bank.rtsp_ipcam_viewer.common.data.VideoType;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;

import java.util.ArrayList;

public final class RecyclerViewInit {

    public static RecyclerViewAdapter adapter(Context context, RecyclerView recyclerView, ArrayList<VideoType> videos) {
        LinearLayoutManager layoutmgr = new LinearLayoutManager(context);
        RecyclerViewAdapter adapter   = new RecyclerViewAdapter(context, videos);
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

package com.github.warren_bank.rtsp_ipcam_viewer.main.recycler_view;

import com.github.warren_bank.rtsp_ipcam_viewer.common.data.C;
import com.github.warren_bank.rtsp_ipcam_viewer.common.data.VideoType;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public final class RecyclerViewCallback extends ItemTouchHelper.SimpleCallback {

    private RecyclerViewAdapter  adapter;
    private ArrayList<VideoType> videos;

    private int draggingFromPosition;
    private int draggingToPosition;

    public RecyclerViewCallback(RecyclerViewAdapter adapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.START | ItemTouchHelper.END);

        this.adapter              = adapter;
        this.videos               = adapter.videos;
        this.draggingFromPosition = C.INDEX_UNSET;
        this.draggingToPosition   = C.INDEX_UNSET;
    }

    @Override
    public boolean onMove(RecyclerView list, RecyclerView.ViewHolder origin, RecyclerView.ViewHolder target) {
        int fromPosition = origin.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        if (draggingFromPosition == C.INDEX_UNSET) {
            draggingFromPosition = fromPosition;
        }
        draggingToPosition = toPosition;
        adapter.notifyItemMoved(fromPosition, toPosition);
        RecyclerViewAdapter.saveVideos(adapter);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();

        if (position >= videos.size()) return;

        videos.remove(position);
        adapter.notifyItemRemoved(position);
        RecyclerViewAdapter.saveVideos(adapter);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        int fromPos = draggingFromPosition;
        int toPos   = draggingToPosition;

        draggingFromPosition = C.INDEX_UNSET;
        draggingToPosition   = C.INDEX_UNSET;

        if (
            (fromPos != C.INDEX_UNSET) &&
            (toPos   != C.INDEX_UNSET) &&
            (fromPos < videos.size())  &&
            (toPos   < videos.size())  &&
            (fromPos != toPos)
        ) {
            videos.add(toPos, videos.remove(fromPos));
            RecyclerViewAdapter.saveVideos(adapter);

            if (!recyclerView.isComputingLayout() && (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE)) {
                adapter.notifyDataSetChanged();
            }
            else {
                recyclerView.post(new Runnable(){
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }
}

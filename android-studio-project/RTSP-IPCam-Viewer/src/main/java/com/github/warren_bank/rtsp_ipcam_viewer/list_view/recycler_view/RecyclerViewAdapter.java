package com.github.warren_bank.rtsp_ipcam_viewer.list_view.recycler_view;

import com.github.warren_bank.rtsp_ipcam_viewer.R;
import com.github.warren_bank.rtsp_ipcam_viewer.common.data.VideoType;
import com.github.warren_bank.rtsp_ipcam_viewer.common.helpers.Utils;

import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public final class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    public Context context;
    public ArrayList<VideoType> videos;
    private int minHeight;
    private ArrayList<RecyclerViewHolder> holders;

    public RecyclerViewAdapter(Context context, ArrayList<VideoType> videos, int minHeight) {
        super();

        this.context   = context;
        this.videos    = videos;
        this.minHeight = minHeight;
        this.holders   = new ArrayList<RecyclerViewHolder>();

        setHasStableIds(true);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_recycler_view_holder, parent, false);

        RecyclerViewHolder holder = new RecyclerViewHolder(view, minHeight);
        holders.add(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        if (position >= getItemCount()) return;

        VideoType video = videos.get(position);

        holder.bind(video);
    }

    // deallocate all resources that consume memory
    public void release() {
        if (holders != null) {
            for (RecyclerViewHolder holder : holders) {
              holder.release();
            }
            holders.clear();

            context = null;
            videos  = null;
            holders = null;

            System.gc();
        }
    }

    @Override
    public int getItemCount() {
        return (videos == null) ? 0 : videos.size();
    }

    @Override
    public long getItemId(int position) {
        VideoType video = videos.get(position);
        String url      = video.URL_low_res;
        long uniqueID   = Utils.getUniqueLongFromString(url);

        return uniqueID;
    }

}

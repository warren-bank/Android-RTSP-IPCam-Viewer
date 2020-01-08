package com.github.warren_bank.rtsp_ipcam_viewer.main.recycler_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.warren_bank.rtsp_ipcam_viewer.R;
import com.github.warren_bank.rtsp_ipcam_viewer.common.data.SharedPrefs;
import com.github.warren_bank.rtsp_ipcam_viewer.common.data.VideoType;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public final class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    public Context context;
    public ArrayList<VideoType> videos;

    public RecyclerViewAdapter(Context context, ArrayList<VideoType> videos) {
        super();

        this.context = context;
        this.videos  = videos;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recycler_view_holder, parent, false);
        return new RecyclerViewHolder(view, parent, RecyclerViewAdapter.this);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        if (position >= getItemCount()) return;

        VideoType video = videos.get(position);

        holder.bind(video);
    }

    @Override
    public int getItemCount() {
        return (videos == null) ? 0 : videos.size();
    }

    // helper

    public static void saveVideos(RecyclerViewAdapter adapter) {
        String jsonVideos = VideoType.toJson(adapter.videos);
        SharedPrefs.setVideos(adapter.context, jsonVideos);
    }

}

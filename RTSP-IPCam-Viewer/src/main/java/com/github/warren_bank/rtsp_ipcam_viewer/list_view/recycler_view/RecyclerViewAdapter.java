package com.github.warren_bank.rtsp_ipcam_viewer.list_view.recycler_view;

import com.github.warren_bank.rtsp_ipcam_viewer.R;
import com.github.warren_bank.rtsp_ipcam_viewer.common.data.VideoType;

import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public final class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    public Context context;
    public ArrayList<VideoType> videos;

    private static final float video_aspect_ratio = (float) (16 / 9);  // common ratios: (16:9), (5:4)

    public RecyclerViewAdapter(Context context, ArrayList<VideoType> videos) {
        super();

        this.context = context;
        this.videos = videos;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_recycler_view_holder, parent, false);

        int video_width  = (int) parent.getWidth();
        int video_height = (int) (video_width / video_aspect_ratio);
        ViewGroup.LayoutParams params;

        params = view.getLayoutParams();
        params.width  = video_width;
        params.height = video_height;
        view.setLayoutParams(params);

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        if (position >= getItemCount()) return;

        VideoType video = videos.get(position);

        holder.bind(video);
    }

    @Override
    public void onViewDetachedFromWindow (RecyclerViewHolder holder) {
        holder.stop();
    }

    @Override
    public int getItemCount() {
        return (videos == null) ? 0 : videos.size();
    }

}
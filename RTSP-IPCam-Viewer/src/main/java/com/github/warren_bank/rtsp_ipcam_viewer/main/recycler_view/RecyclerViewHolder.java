package com.github.warren_bank.rtsp_ipcam_viewer.main.recycler_view;

import com.github.warren_bank.rtsp_ipcam_viewer.R;
import com.github.warren_bank.rtsp_ipcam_viewer.common.data.VideoType;
import com.github.warren_bank.rtsp_ipcam_viewer.main.dialogs.add_video.VideoDialog;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

public final class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private View     view;
    private CheckBox check;
    private TextView text;

    private ViewGroup viewGroup;
    private RecyclerViewAdapter adapter;

    private VideoType data;

    public RecyclerViewHolder(View view, ViewGroup viewGroup, RecyclerViewAdapter adapter) {
        super(view);

        this.view  = view;
        this.check = view.findViewById(R.id.check1);
        this.text  = view.findViewById(R.id.text1);

        this.viewGroup = viewGroup;
        this.adapter   = adapter;

        check.setOnClickListener(this);
        text.setOnClickListener(this);
    }

    public void bind(VideoType data) {
        this.data = data;

        check.setChecked(data.is_enabled);
        text.setText(data.title, TextView.BufferType.NORMAL);
    }

    @Override
    public void onClick(View view) {
        int position = getAdapterPosition();

        if (view == check) {
            data.is_enabled = (!data.is_enabled);
            check.setChecked(data.is_enabled);

            adapter.notifyItemChanged(position);
            RecyclerViewAdapter.saveVideos(adapter);
            return;
        }

        if (view == text) {
            VideoDialog.edit(
                adapter.context,
                viewGroup,
                data,
                new VideoDialog.ResultListener() {
                    @Override
                    public void onResult(boolean is_edited) {
                        if (is_edited) {
                            adapter.notifyItemChanged(position);
                            RecyclerViewAdapter.saveVideos(adapter);
                        }
                    }
                }
            );
            return;
        }
    }

}

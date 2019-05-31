package com.github.warren_bank.rtsp_ipcam_viewer.grid_view.recycler_view;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;

public final class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration {
    protected int grid_cell_spacing;

    public RecyclerViewItemDecoration(int itemOffset) {
        grid_cell_spacing = itemOffset;
    }

    public RecyclerViewItemDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
        this(context.getResources().getDimensionPixelSize(itemOffsetId));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.set(grid_cell_spacing, grid_cell_spacing, grid_cell_spacing, grid_cell_spacing);
    }
}

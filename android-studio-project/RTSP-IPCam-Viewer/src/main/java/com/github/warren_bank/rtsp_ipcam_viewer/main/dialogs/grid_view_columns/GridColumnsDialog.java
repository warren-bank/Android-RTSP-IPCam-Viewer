package com.github.warren_bank.rtsp_ipcam_viewer.main.dialogs.grid_view_columns;

import com.github.warren_bank.rtsp_ipcam_viewer.R;
import com.github.warren_bank.rtsp_ipcam_viewer.common.data.SharedPrefs;
import com.github.warren_bank.rtsp_ipcam_viewer.common.dialogs.PersistentAlertDialogBuilder;
import com.github.warren_bank.rtsp_ipcam_viewer.common.helpers.Utils;

import com.google.android.material.textfield.TextInputEditText;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public final class GridColumnsDialog {

    public static class ResultListener {
        public void onResult(int columns) {}
    }

    public static void show(Context context, ViewGroup parent, ResultListener listener) {
        final View view = (View) LayoutInflater.from(context).inflate(R.layout.main_dialogs_custom_grid_columns, parent, false);

        final TextInputEditText columns = (TextInputEditText) view.findViewById(R.id.grid_columns);

        columns.setText(
            String.valueOf(
                SharedPrefs.getGridColumns(context, 3)
            ),
            TextView.BufferType.EDITABLE
        );

        final AlertDialog alertDialog = new PersistentAlertDialogBuilder(context)
            .setTitle(R.string.dialog_title_custom_grid)
            .setView(view)
            .setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Utils.hideKeyboard(context, view);
                    dialogInterface.dismiss();
                }
            })
            .setPositiveButton(R.string.dialog_button_open, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        int result = Integer.parseInt(
                            String.valueOf(columns.getText()),
                            10
                        );

                        Utils.hideKeyboard(context, view);
                        dialogInterface.dismiss();

                        SharedPrefs.setGridColumns(context, result);

                        listener.onResult(result);
                    }
                    catch(Exception e) {}
                }
            })
            .show();
    }
}

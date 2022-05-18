package com.github.warren_bank.rtsp_ipcam_viewer.main.dialogs.add_video;

import com.github.warren_bank.rtsp_ipcam_viewer.R;
import com.github.warren_bank.rtsp_ipcam_viewer.common.data.VideoType;
import com.github.warren_bank.rtsp_ipcam_viewer.common.dialogs.PersistentAlertDialogBuilder;
import com.github.warren_bank.rtsp_ipcam_viewer.common.helpers.Utils;

import com.google.android.material.textfield.TextInputEditText;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

public final class VideoDialog {

    public static class ResultListener {
        public void onResult(VideoType result) {}

        public void onResult(boolean is_edited) {}
    }

    public static void add(Context context, ViewGroup parent, ResultListener listener) {
        show(context, parent, null, listener);
    }

    public static void edit(Context context, ViewGroup parent, VideoType video, ResultListener listener) {
        ResultListener editListener = new ResultListener() {
            @Override
            public void onResult(VideoType result) {
                boolean is_edited;

                is_edited = (result == null)
                    ? false
                    : !result.strict_equals(video)
                ;

                if (is_edited) {
                    video.title        = result.title;
                    video.URL_low_res  = result.URL_low_res;
                    video.URL_high_res = result.URL_high_res;
                    video.is_enabled   = result.is_enabled;
                }

                listener.onResult(is_edited);
            }
        };

        show(context, parent, video, editListener);
    }

    private static void show(Context context, ViewGroup parent, VideoType video, ResultListener listener) {
        final View view = (View) LayoutInflater.from(context).inflate(R.layout.main_dialogs_add_video, parent, false);

        final TextInputEditText title        = (TextInputEditText) view.findViewById(R.id.video_title);
        final TextInputEditText URL_low_res  = (TextInputEditText) view.findViewById(R.id.video_url_low_res);
        final TextInputEditText URL_high_res = (TextInputEditText) view.findViewById(R.id.video_url_high_res);
        final CheckBox          is_enabled   = (CheckBox)          view.findViewById(R.id.video_is_enabled);

        if (video != null) {
            title.setText(        video.title,        TextView.BufferType.EDITABLE);
            URL_low_res.setText(  video.URL_low_res,  TextView.BufferType.EDITABLE);
            URL_high_res.setText( video.URL_high_res, TextView.BufferType.EDITABLE);
            is_enabled.setChecked(video.is_enabled);
        }

        final AlertDialog alertDialog = new PersistentAlertDialogBuilder(context)
            .setTitle(
                (video == null)
                    ? R.string.dialog_title_add_video
                    : R.string.dialog_title_edit_video
            )
            .setView(view)
            .setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Utils.hideKeyboard(context, view);
                    dialogInterface.dismiss();

                    listener.onResult((VideoType) null);
                }
            })
            .setPositiveButton(R.string.dialog_button_save, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // normalize user input
                    String video_title        = normalize_string(String.valueOf(title.getText()));
                    String video_URL_low_res  = normalize_string(String.valueOf(URL_low_res.getText()));
                    String video_URL_high_res = normalize_string(String.valueOf(URL_high_res.getText()));
                    boolean video_is_enabled  = is_enabled.isChecked();

                    if ((video_URL_low_res == null) && (video_URL_high_res != null)) {
                        video_URL_low_res  = video_URL_high_res;
                        video_URL_high_res = null;
                    }

                    if ((video_URL_low_res != null) && (video_URL_high_res != null) && video_URL_low_res.equals(video_URL_high_res)) {
                        video_URL_high_res = null;
                    }

                    // validate user input
                    if ((video_title == null) || (video_URL_low_res == null)) return;

                    Utils.hideKeyboard(context, view);
                    dialogInterface.dismiss();

                    VideoType result = new VideoType(video_title, video_URL_low_res, video_URL_high_res, video_is_enabled);

                    listener.onResult(result);
                }

                private String normalize_string(String val) {
                    if (val == null) return null;
                    val = val.trim();
                    if (val.isEmpty()) return null;
                    return val;
                }
            })
            .show();
    }
}

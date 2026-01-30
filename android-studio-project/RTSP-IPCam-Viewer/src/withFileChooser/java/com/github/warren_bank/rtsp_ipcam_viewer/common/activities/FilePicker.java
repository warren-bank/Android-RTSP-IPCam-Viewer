package com.github.warren_bank.rtsp_ipcam_viewer.common.activities;

import android.app.Activity;
import android.content.Intent;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.util.ArrayList;
import java.util.regex.Pattern;

public final class FilePicker {

    private static int nonce = 0;

    private static ArrayList<ResultListener> listeners = new ArrayList<ResultListener>();

    public static class ResultListener {
        public void onResult(String filepath) {}
    }

    public static class ResultHandler {
        public static void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode == Activity.RESULT_OK) {
                String filepath;
                ResultListener listener;

                filepath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                listener = listeners.get(requestCode);

                if ((filepath != null) && (!filepath.isEmpty()) && (listener != null)) {
                    listener.onResult(filepath);
                }
            }
            listeners.set(requestCode, null);
        }
    }

    public static void open(Activity activity, ResultListener listener) {
        open(activity, listener, /* showHiddenFiles= */ true, /* filterPattern= */ (Pattern) null, /* filterDirectories= */ false);
    }

    public static void openJson(Activity activity, ResultListener listener) {
        open(activity, listener, /* showHiddenFiles= */ true, /* filterPattern= */ ".*\\.(?:json|js|txt)$", /* filterDirectories= */ false);
    }

    private static void open(Activity activity, ResultListener listener, boolean showHiddenFiles, String filterPattern, boolean filterDirectories) {
        Pattern mFileFilter = ((filterPattern == null) || (filterPattern.isEmpty()))
          ? null
          : Pattern.compile(filterPattern)
        ;

        if (filterDirectories && (mFileFilter == null))
            filterDirectories = false;

        open(activity, listener, showHiddenFiles, mFileFilter, filterDirectories);
    }

    private static void open(Activity activity, ResultListener listener, boolean showHiddenFiles, Pattern mFileFilter, boolean filterDirectories) {
        int requestCode = nonce++;

        listeners.add(requestCode, listener);

        new MaterialFilePicker()
            .withActivity(activity)
            .withRequestCode(requestCode)
            .withRootPath("/")
            .withPath("/storage")
            .withHiddenFiles(showHiddenFiles)
            .withFilter(mFileFilter)
            .withFilterDirectories(filterDirectories)
            .start();
    }
}

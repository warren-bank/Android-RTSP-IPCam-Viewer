package com.github.warren_bank.rtsp_ipcam_viewer.common.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import java.util.ArrayList;

public final class FilePicker {

    private static int nonce = 0;

    private static ArrayList<ResultListener> listeners = new ArrayList<ResultListener>();

    public static class ResultListener {
        public void onResult(String filepath) {}
    }

    public static class ResultHandler {
        public static void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode == Activity.RESULT_OK) {
                String filepath = null;
                ResultListener listener;

                if (data != null) {
                    Uri uri = data.getData();

                    if (uri != null) {
                        filepath = uri.toString();
                    }
                }

                listener = listeners.get(requestCode);

                if ((filepath != null) && (!filepath.isEmpty()) && (listener != null)) {
                    listener.onResult(filepath);
                }
            }
            listeners.set(requestCode, null);
        }
    }

    public static void open(Activity activity, ResultListener listener) {
        open(activity, listener, /* mimeTypes= */ null);
    }

    public static void openJson(Activity activity, ResultListener listener) {
        String[] mimeTypes = {"application/json", "text/javascript", "text/plain"};

        open(activity, listener, mimeTypes);
    }

    private static void open(Activity activity, ResultListener listener, String[] mimeTypes) {
        int requestCode = nonce++;

        listeners.add(requestCode, listener);

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");

        if ((mimeTypes != null) && (mimeTypes.length > 0)) {
          intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        }

        activity.startActivityForResult(intent, requestCode);
    }
}

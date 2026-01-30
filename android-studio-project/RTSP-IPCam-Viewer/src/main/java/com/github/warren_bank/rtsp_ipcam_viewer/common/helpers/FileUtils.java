package com.github.warren_bank.rtsp_ipcam_viewer.common.helpers;

import android.content.Context;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class FileUtils {

    public static String getFileContents(final Context context, final String filepath) throws Exception {
        final InputStream inputStream = getInputStream(context, filepath);

        return (inputStream == null)
            ? null
            : getFileContents(inputStream);
    }

    public static InputStream getInputStream(final Context context, String filepath) throws Exception {
        if ((filepath != null) && !filepath.isEmpty()) {
            if (filepath.startsWith("file:")) {
                filepath = filepath.replaceFirst("file:/*", "/");
            }

            if (filepath.startsWith("/")) {
                File file = new File(filepath);
                return new FileInputStream(file);
            }

            if (filepath.startsWith("content:")) {
                Uri uri = Uri.parse(filepath);
                return context.getContentResolver().openInputStream(uri);
            }
        }
        return null;
    }

    public static String getFileContents(final InputStream inputStream) throws Exception {
        final BufferedReader reader       = new BufferedReader(new InputStreamReader(inputStream));
        final StringBuilder stringBuilder = new StringBuilder();

        boolean done = false;
        String line;

        while (!done) {
            line = reader.readLine();
            done = (line == null);

            if (!done) {
                stringBuilder.append(line);
            }
        }

        reader.close();
        inputStream.close();

        return stringBuilder.toString();
    }
}

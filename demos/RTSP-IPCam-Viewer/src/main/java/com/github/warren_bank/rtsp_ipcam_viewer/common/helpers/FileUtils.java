package com.github.warren_bank.rtsp_ipcam_viewer.common.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class FileUtils {

    public static String getFileContents(final String filepath) throws IOException {
        final File file = new File(filepath);

        return getFileContents(file);
    }

    public static String getFileContents(final File file) throws IOException {
        final InputStream inputStream     = new FileInputStream(file);
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

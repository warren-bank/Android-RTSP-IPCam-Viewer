/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.exoplayer2.source.sdp.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;

public final class ProtoVersion {
    private static final Pattern regexSDPVersion = Pattern.compile("(\\d)",
            Pattern.CASE_INSENSITIVE);

    private int protoVersion;

    ProtoVersion(int protoVersion) {
        this.protoVersion = protoVersion;
    }

    public int getProtoVersion() {
        return protoVersion;
    }

    @Nullable
    public static ProtoVersion parse(String line) {
        try {

            Matcher matcher = regexSDPVersion.matcher(line);

            if (matcher.find()) {
                return new ProtoVersion(Integer.parseInt(matcher.group(1).trim()));
            }

        } catch (Exception ex) {

        }

        return null;
    }
}

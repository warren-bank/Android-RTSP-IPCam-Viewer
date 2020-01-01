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
package com.google.android.exoplayer2.source.rtp.upstream;

import com.google.android.exoplayer2.source.rtp.RtpPacket;

public final class RtpSamplesHolder {

    private volatile RtpSamplesQueue samplesQueue;

    private boolean opened;

    public RtpSamplesHolder() {
    }

    public void open(int clockrate) {
        samplesQueue = new RtpSamplesQueue(clockrate);
        opened = true;
    }

    public void put(RtpPacket packet) {
        if (opened) {
            if (packet != null) {
                samplesQueue.offer(packet);
            }
        }
    }

    public void close() {
        if (opened) {
            samplesQueue.clear();
            opened = false;
        }
    }

    public RtpSamplesQueue samples() {
        return samplesQueue;
    }
}

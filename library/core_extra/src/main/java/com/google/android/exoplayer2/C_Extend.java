/*
 * Copyright (C) 2016 The Android Open Source Project
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
package com.google.android.exoplayer2;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/**
 * Defines constants used by the library.
 */
@SuppressWarnings("InlinedApi")
public class C_Extend {

    private C_Extend() {
    }

    /**
     * Represents an unset or unknown length.
     */
    public static final int LENGTH_UNSET = -1;

    /**
     * Represents an unset or unknown port.
     */
    public static final int PORT_UNSET = -1;

    /**
     * Transport protocol. One of {@link #TCP}, {@link #UDP}.
     */
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            TCP,
            UDP
    })
    public @interface TransportProtocol {
    }

    /**
     * Indicates TCP transport protocol.
     */
    public static final int TCP = 0;

    /**
     * Indicates UDP transport protocol.
     */
    public static final int UDP = 1;
}

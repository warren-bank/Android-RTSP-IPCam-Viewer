package com.github.warren_bank.rtsp_ipcam_viewer.mock.data;

public final class MockData {

    public static String getJsonVideos() {
        return "["
          /*
             + "  {"
             + "    \"title\":        \"---\","
             + "    \"URL_low_res\":  \"---\","
             + "    \"URL_high_res\": \"---\","
             + "    \"is_enabled\":   true"
             + "  },"
          */
             + "  {"
             + "    \"title\":        \"Big Buck Bunny (RTSP, 240x160, VOD)\","
             + "    \"URL_low_res\":  \"rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mp4\","
             + "    \"URL_high_res\": null,"
             + "    \"is_enabled\":   true"
             + "  },"
             + "  {"
             + "    \"title\":        \"Big Buck Bunny (RTMP, 240x160, VOD)\","
             + "    \"URL_low_res\":  \"rtmp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mp4\","
             + "    \"URL_high_res\": null,"
             + "    \"is_enabled\":   true"
             + "  },"
             + "  {"
             + "    \"title\":        \"Big Buck Bunny (RTSP, 512x290, Live)\","
             + "    \"URL_low_res\":  \"rtsp://wowzaec2demo.streamlock.net/live/bigbuckbunny\","
             + "    \"URL_high_res\": null,"
             + "    \"is_enabled\":   true"
             + "  },"
             + "  {"
             + "    \"title\":        \"Big Buck Bunny (RTMP, 512x290, Live)\","
             + "    \"URL_low_res\":  \"rtmp://wowzaec2demo.streamlock.net/live/bigbuckbunny\","
             + "    \"URL_high_res\": null,"
             + "    \"is_enabled\":   true"
             + "  }"
             + "]";
    }
}

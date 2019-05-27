### [RTSP IPCam Viewer](https://github.com/warren-bank/Android-RTSP-IPCam-Viewer)

Android app to watch RTSP video streams; this format is typical of inexpensive IP security cams.

#### Background

* [tinyCam PRO](https://play.google.com/store/apps/details?id=com.alexvas.dvr.pro) by [Tiny Solutions LLC](https://tinycammonitor.com/)
  * is great
  * is the defacto standard for this kind of app
  * is inexpensive ($4 MSRP, occasionally $1 on sale)
* [IP Cam Viewer](https://hit-mob.com/ip-cam-viewer-android/) by [Robert Chou](mailto:robert.chou@gmail.com)
  * has pretty good reviews
  * is the go-to _free_ option for this kind of app
  * available variations:
    * limited functionality w/ ads
      * [_Basic_](https://play.google.com/store/apps/details?id=com.rcreations.ipcamviewerBasic)
        * uses Google's in-app purchase to upgrade
      * [_Lite_](https://play.google.com/store/apps/details?id=com.rcreations.ipcamviewer)
        * uses an unlock code to upgrade
    * full functionality w/o ads
      * [_Pro_](https://play.google.com/store/apps/details?id=com.rcreations.WebCamViewerPaid)
        * $4 MSRP
* there are no (good) open-source options

#### Goals

* an extremely light-weight open-source app
  * minimal features
  * minimal UI
* ability to add video streams
  * data structure:
    * required fields:
      * name
      * low-res video stream URL
    * optional fields:
      * high-res video stream URL
      * is enabled?
  * data import methods:
    * manual entry via dialog
    * import via text file (JSON format)
* display of enabled video streams:
  * vertical list view of low-res video streams
  * (TBD) vertical grid view of low-res video streams
    * number of columns is configurable
  * full-screen view of a single high-res video stream
    * (TBD) ability to zoom/pan
    * (TBD) ability to record to external SD card

#### Notes

* for simplicity/performance, each video stream is played in a native [`VideoView`](https://developer.android.com/reference/android/widget/VideoView)
  * consequences:
    * supports a limited set of [media formats](https://developer.android.com/guide/appendix/media-formats.html)
    * cannot zoom/pan
* may decide to display the full-screen view in a [`TextureView`](https://developer.android.com/reference/android/view/TextureView.html)
  * considerations:
    * performance would suffer on lower-end hardware
    * would gain the ability to zoom/pan
* vertical grid-view is a double-edge sword
  * considerations:
    * the bitrate of each low-res video stream isn't reduced by displaying the video in a smaller area of the screen
    * this display option would only make sense if the resolution of each low-res video stream was so low that:
      * at full scale, the width of each video is significantly less than the full width of the screen
    * on a small screen (ex: phone):
      * poor resolution
      * difficult to see
    * on a large screen (ex: tablet, TV):
      * this display option would certainly be of benefit

#### Legal

* copyright: [Warren Bank](https://github.com/warren-bank)
* license: [GPL-2.0](https://www.gnu.org/licenses/old-licenses/gpl-2.0.txt)

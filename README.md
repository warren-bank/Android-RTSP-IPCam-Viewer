### [RTSP IPCam Viewer](https://github.com/warren-bank/Android-RTSP-IPCam-Viewer)

Android app to watch RTSP video streams; this format is typical of inexpensive IP security cams.

#### Background

* [tinyCam PRO](https://play.google.com/store/apps/details?id=com.alexvas.dvr.pro) by [Tiny Solutions LLC](https://tinycammonitor.com/)
  * is great
  * is the defacto standard for this kind of app
  * is inexpensive ($4 MSRP, occasionally $1 on sale)
  * cons:
    * none
      * this a personal quirk..
        * I prefer to not add Google account(s) to most of my Android devices
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
  * cons:
    * size of APK is over 25MB
    * requires __a lot__ of permissions
    * most buttons/features are crippled
      * open a prompt to purchase pro license
    * basic functionality is (imho) not very impressive
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
    * import via text file ([JSON format](https://github.com/warren-bank/Android-RTSP-IPCam-Viewer/blob/master/.etc/sample_file_import_data/video_streams.json))
* display of enabled video streams:
  * list view of low-res video streams
  * grid view of low-res video streams
    * number of columns is configurable
  * full-screen view of a single high-res video stream
    * (TBD) ability to zoom/pan
    * (TBD) ability to record to external SD card

#### Notes

* minimum supported version of Android:
  * Android 4.1 Jelly Bean (API 16)
* when videos are displayed in list/grid views:
  * audio is mute
  * click to open video in full-screen view
  * long click to toggle pause/play
* when a video is displayed in full-screen view:
  * audio is not mute
  * controls are visible

#### Screenshots

<!-- portrait -->
![MainActivity](./.etc/screenshots/01-main.png)
![MainActivity](./.etc/screenshots/02-main-menu.png)
![MainActivity](./.etc/screenshots/03-main-menu.png)
![MainActivity](./.etc/screenshots/04-main-menu-edit.png)
![FilePicker](./.etc/screenshots/10-import.png)
![ListActivity](./.etc/screenshots/05-list.png)
![GridActivity](./.etc/screenshots/06-grid-2col.png)
<br>
<!-- landscape -->
![GridActivity](./.etc/screenshots/07-grid-3col.png)
![GridActivity](./.etc/screenshots/08-grid-4col.png)
![VideoActivity](./.etc/screenshots/09-fullscreen.png)

#### Credits

* [ExoPlayer](https://github.com/google/ExoPlayer)
  * video player library that supports RTSP streams
* [MaterialFilePicker](https://github.com/nbsp-team/MaterialFilePicker)
  * library to browse the file system and select a file

#### Legal

* copyright: [Warren Bank](https://github.com/warren-bank)
* license: [GPL-2.0](https://www.gnu.org/licenses/old-licenses/gpl-2.0.txt)

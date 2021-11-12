package com.github.warren_bank.rtsp_ipcam_viewer.list_view.recycler_view;

import androidx.core.view.GestureDetectorCompat;
import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class GestureDetectorOnTouchListener implements View.OnTouchListener {

    private static final String TAG = "GestureDetectorOnTouchListener";

    private final GestureDetectorCompat mDetector;

    public GestureDetectorOnTouchListener(Context context) {
        mDetector = new GestureDetectorCompat(context, new GestureListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }

    public void onSwipeRight() {
        Log.i(TAG, "onSwipeRight: Swiped to the RIGHT");
    }

    public void onSwipeLeft() {
        Log.i(TAG, "onSwipeLeft: Swiped to the LEFT");
    }

    public void onSwipeTop() {
        Log.i(TAG, "onSwipeTop: Swiped to the TOP");
    }

    public void onSwipeBottom() {
        Log.i(TAG, "onSwipeBottom: Swiped to the BOTTOM");
    }

    public void onClick() {
        Log.i(TAG, "onClick: Clicking in the screen");
    }

    public void onDoubleClick() {
        Log.i(TAG, "onClick: Clicking TWO TIMES in the screen");
    }

    public void onLongClick() {
        Log.i(TAG, "onLongClick: LONG click in the screen");
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY  = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            onClick();
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            onDoubleClick();
            return super.onDoubleTap(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            onLongClick();
            super.onLongPress(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                        result = true;
                    }
                } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                    result = true;
                }
            } catch (Exception e) {
                Log.e(TAG, "onFling:", e);
            }
            return result;
        }
    }
}

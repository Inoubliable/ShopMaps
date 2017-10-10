package com.janzelj.tim.shopmaps;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);
    }

    class MyGLSurfaceView extends GLSurfaceView {

        private final MyGLRenderer mRenderer;

        private float mPreviousX;
        private float mPreviousY;
        private ScaleGestureDetector mScaleDetector;
        private float mScaleFactor = 15.0f;

        private boolean scaling = false;

        @Override
        public boolean onTouchEvent(MotionEvent e) {
            // MotionEvent reports input details from the touch screen
            // and other input controls. In this case, you are only
            // interested in events where the touch position changed.

            float x = e.getX();
            float y = e.getY();

            // get other touch events, eg. pinch
            mScaleDetector.onTouchEvent(e);

            switch (e.getActionMasked()) {
                case MotionEvent.ACTION_UP:
                    scaling = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(!scaling) {
                        float dx = x - mPreviousX;
                        float dy = y - mPreviousY;

                        mRenderer.setX(mRenderer.getX() - (dx * 0.0001f * (100.0f - mScaleFactor))); // ne stekam zakaj minus, vrjetno android screen koordinate?
                        mRenderer.setY(mRenderer.getY() + (dy * 0.0001f * (100.0f - mScaleFactor)));

                        requestRender();
                    }
                    break;
            }

            mPreviousX = x;
            mPreviousY = y;
            return true;
        }

        public MyGLSurfaceView(Context context){
            super(context);

            // Create an OpenGL ES 2.0 context
            setEGLContextClientVersion(2);

            mRenderer = new MyGLRenderer();

            // Set the Renderer for drawing on the GLSurfaceView
            setRenderer(mRenderer);
            setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

            mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        }

        private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                scaling = true;

                mScaleFactor *= detector.getScaleFactor();

                // Don't let the object get too small or too large.
                mScaleFactor = Math.max(0.0f, Math.min(mScaleFactor, 85.0f));

                mRenderer.setZoom(mScaleFactor);
                requestRender();

                invalidate();
                return true;
            }
        }
    }
}

package com.janzelj.tim.shopmaps;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MyGLRenderer mRenderer;
    private DBHelper mydb;

    private GLSurfaceView mGLView;

    public static ArrayList<ArrayList<String>> model;
    public float[] productCoords = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);

        Bundle extras = getIntent().getExtras();
        int shop_id = 0;
        if (extras != null) {
            shop_id = extras.getInt("id");
        }

        mydb = new DBHelper(this);
        model = mydb.getModelByShop(shop_id);

        onSearchRequested();

        //handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // show search
                onSearchRequested();

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void handleIntent(Intent intent) {
        //if (Intent.ACTION_VIEW.equals(intent.getAction())) {
        Uri uri = intent.getData();
        int productId = 0;

        if (uri != null) {
            productId = Integer.parseInt(uri.getLastPathSegment());
        }

        productCoords = mydb.getProductCoordinates(productId);

        if(productCoords != null) {
            mRenderer.changeProductMark(productCoords);
        }
        productCoords = null;

    }

    class MyGLSurfaceView extends GLSurfaceView {

        //private final MyGLRenderer mRenderer;

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
            //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

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

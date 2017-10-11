package com.janzelj.tim.shopmaps;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by TJ on 5.10.2017.
 */

public class MyGLRenderer implements GLSurfaceView.Renderer {

    public static final int TYPE_PROSTOR = 1;
    public static final int TYPE_STENA = 2;
    public static final int TYPE_REGAL = 3;

    private ArrayList<ArrayList<String>> model;

    private List<Cuboid> cuboids;
    private List<Circle> circles;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.43671875f, 0.56953125f, 0.32265625f, 1.0f);

        // initialize cuboids
        cuboids = new ArrayList<Cuboid>();
        circles = new ArrayList<Circle>();
        model = MainActivity.model;
        Log.d("timi", "" + model);

        for (ArrayList<String> object: model) {

            int type_id = Integer.parseInt(object.get(0));
            float x = Float.parseFloat(object.get(1));
            float y = Float.parseFloat(object.get(2));
            float width = Float.parseFloat(object.get(3));
            float height = Float.parseFloat(object.get(4));

            float prostorColor[] = {0f, 0f, 0f, 1.0f};
            float stenaColor[] = {0.9f, 0.9f, 0.9f, 1.0f};
            float regalColor[] = {0.65f, 0.65f, 0.65f, 1.0f};
            float prostorDepth = 0;
            float stenaDepth = 10f;
            float regalDepth = 5f;

            switch (type_id) {
                case MyGLRenderer.TYPE_PROSTOR:
                    cuboids.add(new Cuboid(x, y, width, height, prostorDepth, prostorColor));
                    break;
                case MyGLRenderer.TYPE_STENA:
                    cuboids.add(new Cuboid(x, y, width, height, stenaDepth, stenaColor));
                    break;
                case MyGLRenderer.TYPE_REGAL:
                    cuboids.add(new Cuboid(x, y, width, height, regalDepth, regalColor));
                    break;
            }

        }


        //float circleCoords[] = {-2f, -2f};
        //float circleColor[] = { 0.8f, 0f, 0f, 1.0f };

        //circles.add(new Circle(circleCoords, 0.5f, circleColor));
    }

    public void onDrawFrame(GL10 unused) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, mX, mY, mZoom, mX, mY, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        // Draw shapes
        for (Cuboid cuboid : cuboids) {
            cuboid.draw(mMVPMatrix);
        }
        for (Circle circle : circles) {
            circle.draw(mMVPMatrix);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 10, 100);
    }

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public volatile float mX;
    public volatile float mY;
    public volatile float mZoom = 85.0f;

    public float getX() {
        return mX;
    }

    public void setX(float x) {
        mX = x;
    }

    public float getY() {
        return mY;
    }

    public void setY(float y) {
        mY = y;
    }

    public void setZoom(float zoom) {
        mZoom = 100.0f - zoom;
    }
}

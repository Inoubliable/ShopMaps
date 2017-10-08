package com.janzelj.tim.shopmaps;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by TJ on 5.10.2017.
 */

public class MyGLRenderer implements GLSurfaceView.Renderer {

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

        float prostorCoords[] = {-10f, -5f};
        float prostorColor[] = { 0f, 0f, 0f, 1.0f };

        float steneCoords1[] = {-11f, -6f};
        float steneCoords2[] = {-10f, -6f};
        float steneCoords3[] = {10f, -6f};
        float steneCoords4[] = {-10f, 5f};
        float steneColor[] = {0.9f, 0.9f, 0.9f, 1.0f};
        float steneDepth = 10f;

        float cuboidCoords1[] = {-8f, -4f};
        float cuboidCoords2[] = {-5f, -4f};
        float cuboidCoords3[] = {-2f, -4f};
        float cuboidCoords4[] = {1f, -4f};
        float cuboidCoords5[] = {-8f, 2f};
        float cuboidCoords6[] = {4f, -4f};
        float cuboidCoords7[] = {7f, -4f};
        float cuboidColor[] = {0.65f, 0.65f, 0.65f, 1.0f};
        float cuboidDepth = 5f;

        cuboids.add(new Cuboid(prostorCoords, 20, 10, 0, prostorColor));
        cuboids.add(new Cuboid(steneCoords1, 1, 12, steneDepth, steneColor));
        cuboids.add(new Cuboid(steneCoords2, 20, 1, steneDepth, steneColor));
        cuboids.add(new Cuboid(steneCoords3, 1, 12, steneDepth, steneColor));
        cuboids.add(new Cuboid(steneCoords4, 20, 1, steneDepth, steneColor));

        cuboids.add(new Cuboid(cuboidCoords1, 1, 5, cuboidDepth, cuboidColor));
        cuboids.add(new Cuboid(cuboidCoords2, 1, 5, cuboidDepth, cuboidColor));
        cuboids.add(new Cuboid(cuboidCoords3, 1, 5, cuboidDepth, cuboidColor));
        cuboids.add(new Cuboid(cuboidCoords4, 1, 5, cuboidDepth, cuboidColor));
        cuboids.add(new Cuboid(cuboidCoords5, 10, 1.5f, cuboidDepth, cuboidColor));
        cuboids.add(new Cuboid(cuboidCoords6, 1.5f, 8, cuboidDepth, cuboidColor));
        cuboids.add(new Cuboid(cuboidCoords7, 1.5f, 8, cuboidDepth, cuboidColor));

        float circleCoords[] = {-2f, -2f};
        float circleColor[] = { 0.8f, 0f, 0f, 1.0f };

        circles.add(new Circle(circleCoords, 0.5f, circleColor));
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

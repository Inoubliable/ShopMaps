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

    private List<Rectangle> rectangles;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        // initialize rectangles
        rectangles = new ArrayList<Rectangle>();

        float prostorCoords[] = {-10f, -5f};
        float prostorColor[] = { 0f, 0f, 0f, 1.0f };

        float rectangleCoords1[] = {-8f, -4f};
        float rectangleCoords2[] = {-5f, -4f};
        float rectangleCoords3[] = {-2f, -4f};
        float rectangleCoords4[] = {1f, -4f};
        float rectangleCoords5[] = {-8f, 2f};
        float rectangleCoords6[] = {4f, -4f};
        float rectangleCoords7[] = {7f, -4f};
        float rectangleColor[] = { 0.43671875f, 0.56953125f, 0.32265625f, 1.0f };

        rectangles.add(new Rectangle(prostorCoords, 20, 10, prostorColor));
        rectangles.add(new Rectangle(rectangleCoords1, 1, 5, rectangleColor));
        rectangles.add(new Rectangle(rectangleCoords2, 1, 5, rectangleColor));
        rectangles.add(new Rectangle(rectangleCoords3, 1, 5, rectangleColor));
        rectangles.add(new Rectangle(rectangleCoords4, 1, 5, rectangleColor));
        rectangles.add(new Rectangle(rectangleCoords5, 10, 1.5f, rectangleColor));
        rectangles.add(new Rectangle(rectangleCoords6, 1.5f, 8f, rectangleColor));
        rectangles.add(new Rectangle(rectangleCoords7, 1.5f, 8f, rectangleColor));
    }

    public void onDrawFrame(GL10 unused) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, mX, mY, mZoom, mX, mY, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        // Draw shape
        for (Rectangle rectangle : rectangles) {
            rectangle.draw(mMVPMatrix);
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

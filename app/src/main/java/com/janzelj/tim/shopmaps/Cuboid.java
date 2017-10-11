package com.janzelj.tim.shopmaps;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by TJ on 7.10.2017.
 */

public class Cuboid {

    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    private float squareCoords[] = new float[36*3];

    /*
    private short drawOrder[] = {
            0, 1, 2,
            0, 2, 3,
            0, 4, 5, 0, 5, 1,
            1, 5, 6, 1, 6, 2,
            2, 6, 7, 2, 7, 3,
            3, 7, 4, 3, 4, 0,
            4, 5, 6,
            4, 6, 3
    }; // order to draw vertices
    */

    private float squareColor[] = new float[4];

    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    // Use to access and set the view transformation
    private int mMVPMatrixHandle;

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private final int mProgram;

    public Cuboid(float blX, float blY, float width, float height, float depth, float[] color) {

        float tlX = blX;
        float tlY = blY + height;
        float trX = tlX + width;
        float trY = tlY;
        float brX = trX;
        float brY = blY;

        /*
        float[] coords = {
                tlX, tlY, depth,   // top left
                blX, blY, depth,   // bottom left
                brX, brY, depth,   // bottom right
                trX, trY, depth,   // top right
                tlX, tlY, 0.0f,   // top left
                blX, blY, 0.0f,   // bottom left
                brX, brY, 0.0f,   // bottom right
                trX, trY, 0.0f,   // top right
        };
        */

        float[] coords = {
                tlX, tlY, depth, // triangle 1 : begin
                blX, blY, depth,
                brX, brY, depth, // triangle 1 : end
                tlX, tlY, depth, // triangle 2 : begin
                brX, brY, depth,
                trX, trY, depth, // triangle 2 : end
                tlX, tlY, depth,
                tlX, tlY, 0.0f,
                blX, blY, 0.0f,
                tlX, tlY, depth,
                blX, blY, 0.0f,
                blX, blY, depth,
                blX, blY, depth,
                blX, blY, 0.0f,
                brX, brY, 0.0f,
                blX, blY, depth,
                brX, brY, 0.0f,
                brX, brY, depth,
                brX, brY, depth,
                brX, brY, 0.0f,
                trX, trY, 0.0f,
                brX, brY, depth,
                trX, trY, 0.0f,
                trX, trY, depth,
                trX, trY, depth,
                trX, trY, 0.0f,
                tlX, tlY, 0.0f,
                trX, trY, depth,
                tlX, tlY, 0.0f,
                tlX, tlY, depth,
                tlX, tlY, 0.0f,
                blX, blY, 0.0f,
                brX, brY, 0.0f,
                tlX, tlY, 0.0f,
                brX, brY, 0.0f,
                trX, trY, depth,
        };

        squareCoords = coords;
        squareColor = color;

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        /*
        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);
        */

        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);
    }

    private int mPositionHandle;
    private int mColorHandle;

    private final int vertexCount = squareCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    public void draw(float[] mvpMatrix) { // pass in the calculated transformation matrix
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, squareColor, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the triangles
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}

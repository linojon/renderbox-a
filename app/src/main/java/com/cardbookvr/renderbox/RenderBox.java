package com.cardbookvr.renderbox;

import android.app.Activity;
import android.opengl.GLES20;
import android.opengl.GLU;
import android.util.Log;

import com.cardbookvr.renderbox.components.Camera;
import com.cardbookvr.renderbox.components.Cube;
import com.cardbookvr.renderbox.components.RenderObject;
import com.cardbookvr.renderbox.materials.SolidColorMaterial;
import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;

/**
 * RenderBox class
 * Abstracts Render functions so we don't have to clutter the Main Activity
 */
public class RenderBox implements CardboardView.StereoRenderer {
    private static final String TAG = "RenderBox";

    public static RenderBox instance;
    public Activity mainActivity;
    IRenderBox callbacks;

    public List<RenderObject> renderObjects = new ArrayList<RenderObject>();

    public static Camera mainCamera;

    public RenderBox(Activity mainActivity, IRenderBox callbacks){
        instance = this;
        this.mainActivity = mainActivity;
        this.callbacks = callbacks;
        mainCamera = new Camera();
    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {
        mainCamera.onNewFrame();
    }

    @Override
    public void onDrawEye(Eye eye) {
        callbacks.preDraw();
        mainCamera.onDrawEye(eye);
        callbacks.postDraw();
    }

    @Override
    public void onFinishFrame(Viewport viewport) {

    }

    @Override
    public void onSurfaceChanged(int i, int i1) {

    }

    @Override
    public void onSurfaceCreated(EGLConfig eglConfig) {
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 0.5f);
        Cube.allocateBuffers();
        SolidColorMaterial.setupProgram();
        checkGLError("onSurfaceCreated");
        callbacks.setup();
    }

    @Override
    public void onRendererShutdown() {

    }

    /**
     * Checks if we've had an error inside of OpenGL ES, and if so what that error is.
     *
     * @param label Label to report in case of error.
     */
    public static void checkGLError(String label) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            String errorText = String.format("%s: glError %d, %s", label, error, GLU.gluErrorString(error));
            Log.e(TAG, errorText);
            throw new RuntimeException(errorText);
        }
    }

}

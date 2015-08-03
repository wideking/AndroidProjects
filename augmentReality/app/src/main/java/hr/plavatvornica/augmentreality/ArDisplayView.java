package hr.plavatvornica.augmentreality;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

/**
 * Created by widek on 1.8.2015..
 * View for drawing objects
 */
public class ArDisplayView extends SurfaceView implements SurfaceHolder.Callback {
    Camera camera;
    SurfaceHolder mHolder;
    private Activity mActivity;

    public ArDisplayView(Context context, Activity activity) {
        super(context);

        mActivity = activity;
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    /**
     * This is called immediately after the surface is first created.
     * Implementations of this should start up whatever rendering code
     * they desire.  Note that only one thread can ever draw into
     * a {@link Surface}, so you should not draw into the Surface here
     * if your normal rendering will be in another thread.
     *
     * @param holder The SurfaceHolder whose surface is being created.
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, cameraInfo);
        int rotation = mActivity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        camera.setDisplayOrientation((cameraInfo.orientation - degrees + 360) % 360);

        try {
            camera.setPreviewDisplay(mHolder);
        } catch (IOException e) {
            Log.e("TAG_cameraPreview", "can display preview inside surface", e);
        }


    }

    /**
     * This is called immediately after any structural changes (format or
     * size) have been made to the surface.  You should at this point update
     * the imagery in the surface.  This method is always called at least
     * once, after {@link #surfaceCreated}.
     *
     * @param holder The SurfaceHolder whose surface has changed.
     * @param format The new PixelFormat of the surface.
     * @param width  The new width of the surface.
     * @param height The new height of the surface.
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Camera.Parameters params = camera.getParameters();//Get parameters from cam
        List<Camera.Size> prevSizes = params.getSupportedPreviewSizes();//get all supported sizes.
        for (Camera.Size s : prevSizes) {
            if ((s.height <= height) && (s.width <= width)) {
                params.setPreviewSize(s.width, s.height);//if size of preview matches the size of surface set parameters size.
                break;
            }
        }

        camera.setParameters(params);
        camera.startPreview();

    }

    /**
     * This is called immediately before a surface is being destroyed. After
     * returning from this call, you should no longer try to access this
     * surface.  If you have a rendering thread that directly accesses
     * the surface, you must ensure that thread is no longer touching the
     * Surface before returning from this function.
     *
     * @param holder The SurfaceHolder whose surface is being destroyed.
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();

    }
}

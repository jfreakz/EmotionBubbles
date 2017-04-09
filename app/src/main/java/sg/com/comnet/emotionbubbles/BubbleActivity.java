package sg.com.comnet.emotionbubbles;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BubbleActivity extends AppCompatActivity {

    private static final Uri IMAGE_PROVIDER_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    Camera cam, camr;
    //a variable to store a reference to the Image View at the main.xml file
    private ImageView iv_image;
    private Button btnClick;
    private File folder = null;

    private static Uri getImageContentUri(Context context, String imagePath) {
        String[] projection = new String[]{MediaStore.Images.Media._ID};
        String selection = MediaStore.Images.Media.DATA + "=? ";
        String[] selectionArgs = new String[]{imagePath};
        Cursor cursor = context.getContentResolver().query(IMAGE_PROVIDER_URI, projection,
                selection, selectionArgs, null);

        if (cursor != null && cursor.moveToFirst()) {
            int imageId = cursor.getInt(0);
            cursor.close();

            return Uri.withAppendedPath(IMAGE_PROVIDER_URI, Integer.toString(imageId));
        }

        if (new File(imagePath).exists()) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, imagePath);

            return context.getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        }

        return null;
    }

    private static int getOrientationFromMediaStore(Context context, String imagePath) {
        Uri imageUri = getImageContentUri(context, imagePath);
        if (imageUri == null) {
            return -1;
        }

        String[] projection = {MediaStore.Images.ImageColumns.ORIENTATION};
        Cursor cursor = context.getContentResolver().query(imageUri, projection, null, null, null);

        int orientation = -1;
        if (cursor != null && cursor.moveToFirst()) {
            orientation = cursor.getInt(0);
            cursor.close();
        }

        return orientation;
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble);

        String ext_storage_state = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        folder = new File(ext_storage_state + File.separator + "BGCamPicture");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        btnClick = (Button) findViewById(R.id.btnClick);
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePictureNoPreview(BubbleActivity.this);
            }
        });
        iv_image = (ImageView) findViewById(R.id.imageView);

    }

    public void takePictureNoPreview(Context context) { // open back facing
        // camera by default

        cam = openFrontFacingCameraGingerbread();

        if (cam != null) {
            try { // set camera parameters if you want to

                // here, the unused surface view and holder

                SurfaceView dummy = new SurfaceView(context);
                cam.setPreviewDisplay(dummy.getHolder());
                cam.startPreview();
                cam.takePicture(null, null, getJpegCallback());

            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
        } else {
            camr = Camera.open();
            if (camr != null) {
                try { // set camera parameters if you want to

                    // here, the unused surface view and holder

                    SurfaceView dummy = new SurfaceView(context);
                    camr.setPreviewDisplay(dummy.getHolder());
                    camr.startPreview();
                    camr.takePicture(null, null, getJpegCallback());

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                }
            }
        }
    }

    private Camera openFrontFacingCameraGingerbread() {
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    cam = Camera.open(camIdx);
                    break;
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }
        return cam;
    }

    private Camera.PictureCallback getJpegCallback() {
        Camera.PictureCallback jpeg = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream fos;
                try {
                    File image = new File(folder.getAbsolutePath()
                            + File.separator + "BGCamPict.jpg");
                    image.createNewFile();
                    fos = new FileOutputStream(image);
                    fos.write(data);
                    fos.close();

                    getContentResolver().notifyChange(Uri.parse(image.getAbsolutePath()), null);

                    BitmapFactory.Options bounds = new BitmapFactory.Options();
                    bounds.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(image.getAbsolutePath(), bounds);

                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    Bitmap bm = BitmapFactory.decodeFile(image.getAbsolutePath(), opts);
                    ExifInterface exif = new ExifInterface(image.getAbsolutePath());
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    orientation = getOrientationFromMediaStore(BubbleActivity.this, image.getAbsolutePath());
                    int rotationAngle = 0;
                    if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
                    if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
                    if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

                    Matrix matrix = new Matrix();
//                    matrix.postRotate(rotationAngle);
//                    Bitmap rotatedBitmap =  Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
//                            matrix, true);
                    matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);

                    iv_image.setImageBitmap(rotatedBitmap);

                } catch (IOException e) {
                    // do something about it
                    e.printStackTrace();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                } finally {
                    if (cam != null) {
                        cam.release();
                    }

                    if (camr != null) {
                        camr.release();
                    }
                }

            }
        };
        return jpeg;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cam != null) {
            cam.release();
        }

        if (camr != null) {
            camr.release();
        }
    }
}
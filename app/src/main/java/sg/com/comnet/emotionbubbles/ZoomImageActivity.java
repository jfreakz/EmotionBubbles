package sg.com.comnet.emotionbubbles;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class ZoomImageActivity extends AppCompatActivity {

    ImageView ivBG;
    boolean isSameView = false;
    private ZoomImageHelper imageZoomHelper;
    private View zoomableView = null;
    View.OnTouchListener zoomTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent ev) {

            switch (ev.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    Log.e("Touch Event :", "Down" + ev.getPointerCount() + " " + ev.getAction() + " " + ev.getActionIndex());
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    Log.e("Touch Event :", "Pointer_Down" + ev.getPointerCount() + " " + ev.getAction() + " " + ev.getActionIndex());

                    isSameView = isPointInsideView(ev.getRawX(), ev.getRawY(), view);
//                    isSameView = mycheck(ev,view);

                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.e("Touch Event :", "Move" + ev.getPointerCount() + " " + ev.getAction() + " " + ev.getActionIndex());
                    if (ev.getPointerCount() == 2 && zoomableView == null && isSameView)
                        zoomableView = view;

                    break;
            }
            return false;
        }
    };

    public static boolean isPointInsideView(float x, float y, View view) {
        int location[] = new int[2];
        view.getLocationOnScreen(location);
        int viewX = location[0];
        int viewY = location[1];

        // point is inside view bounds
        return ((x > viewX && x < (viewX + view.getWidth())) &&
                (y > viewY && y < (viewY + view.getHeight())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);

        ivBG = (ImageView) findViewById(R.id.ivBG);
        ivBG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ivBG.setOnTouchListener(zoomTouchListener);

        imageZoomHelper = new ZoomImageHelper(this);
        imageZoomHelper.addOnZoomListener(new ZoomImageHelper.OnZoomListener() {
            @Override
            public void onImageZoomStarted(View view) {

            }

            @Override
            public void onImageZoomEnded(View view) {
                zoomableView = null;
                isSameView = false;
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return imageZoomHelper.onDispatchTouchEvent(ev, zoomableView) || super.dispatchTouchEvent(ev);
    }

    public boolean mycheck(MotionEvent event, View view) {

        MotionEvent.PointerCoords pointerCoords1 = new MotionEvent.PointerCoords();
        event.getPointerCoords(0, pointerCoords1);

        MotionEvent.PointerCoords pointerCoords2 = new MotionEvent.PointerCoords();
        event.getPointerCoords(1, pointerCoords2);

        Rect visibleRect = new Rect();
        int location[] = new int[2];
        view.getLocationOnScreen(location);
        visibleRect.left = location[0];
        visibleRect.top = location[1];
        visibleRect.right = visibleRect.left + view.getWidth();
        visibleRect.bottom = visibleRect.top + view.getHeight();

        if (visibleRect.contains((int) pointerCoords1.x, (int) pointerCoords1.y) &&
                visibleRect.contains((int) pointerCoords2.x, (int) pointerCoords2.y)) {
            return true;
        } else
            return false;

    }
}

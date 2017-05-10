package sg.com.comnet.emotionbubbles;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JCB on 07-May-2017.
 */


public class ZoomImageHelper {
    TextView textView;
    private View zoomableView = null;
    private ViewGroup parentOfZoomableView;
    private ViewGroup.LayoutParams zoomableViewLP;
    private FrameLayout.LayoutParams zoomableViewFrameLP;
    private Dialog dialog;
    private View placeholderView;
    private int viewIndex;
    private View darkView;
    private double originalDistance;
    private int[] twoPointCenter;
    private int[] originalXY;
    private WeakReference<Activity> activityWeakReference;

    private boolean isAnimatingDismiss = false;

    private List<OnZoomListener> zoomListeners = new ArrayList<>();

    public ZoomImageHelper(Activity activity, TextView tvParam) {
        this.activityWeakReference = new WeakReference<>(activity);
        textView = tvParam;
    }

    public boolean onDispatchTouchEvent(MotionEvent ev, View view) {
        Activity activity;
        if ((activity = activityWeakReference.get()) == null)
            return false;

        if (ev.getPointerCount() == 2) {
            if (zoomableView == null) {
//                View view = findZoomableView(ev,
//                        activity.findViewById(android.R.id.content));
                if (view != null) {
                    zoomableView = view;

                    // get view's original location relative to the window
                    originalXY = new int[2];
                    view.getLocationInWindow(originalXY);

                    // this FrameLayout will be the zoomableView's temporary parent
                    FrameLayout frameLayout = new FrameLayout(view.getContext());

                    // this view is to gradually darken the backdrop as user zooms
                    darkView = new View(view.getContext());
                    darkView.setBackgroundColor(Color.BLACK);
                    darkView.setAlpha(0f);

                    // adding darkening backdrop to the frameLayout
                    frameLayout.addView(darkView, new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT));

                    // the Dialog that will hold the FrameLayout
                    dialog = new Dialog(activity,
                            android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
//                    dialog = new Dialog(activity,
//                            R.style.MyTranslucentTheme);
                    dialog.addContentView(frameLayout,
                            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT));
                    dialog.show();

                    // get the parent of the zoomable view and get it's index and layout param
                    parentOfZoomableView = (ViewGroup) zoomableView.getParent();
                    viewIndex = parentOfZoomableView.indexOfChild(zoomableView);
                    this.zoomableViewLP = zoomableView.getLayoutParams();

                    // this is the new layout param for the zoomableView
                    zoomableViewFrameLP = new FrameLayout.LayoutParams(
                            view.getWidth(), view.getHeight());
                    zoomableViewFrameLP.leftMargin = originalXY[0];
                    zoomableViewFrameLP.topMargin = originalXY[1];

                    // this view will hold the zoomableView's position temporarily
                    placeholderView = new View(activity);

                    // setting placeholderView's background to zoomableView's drawingCache
                    // this avoids flickering when adding/removing views
                    zoomableView.setDrawingCacheEnabled(true);

                    BitmapDrawable placeholderDrawable = new BitmapDrawable(
                            activity.getResources(),
                            Bitmap.createBitmap(zoomableView.getDrawingCache()));
                    if (Build.VERSION.SDK_INT >= 16) {
                        placeholderView.setBackground(placeholderDrawable);
                    } else {
                        placeholderView.setBackgroundDrawable(placeholderDrawable);
                    }

                    // placeholderView takes the place of zoomableView temporarily
                    parentOfZoomableView.addView(placeholderView, zoomableViewLP);

                    // zoomableView has to be removed from parent view before being added to it's
                    // new parent
                    parentOfZoomableView.removeView(zoomableView);
                    frameLayout.addView(zoomableView, zoomableViewFrameLP);
                    // using a post to remove placeholder's drawing cache
                    zoomableView.post(new Runnable() {
                        @Override
                        public void run() {
                            if (dialog != null) {
                                if (Build.VERSION.SDK_INT >= 16) {
                                    placeholderView.setBackground(null);
                                } else {
                                    placeholderView.setBackgroundDrawable(null);
                                }

                                zoomableView.setDrawingCacheEnabled(false);
                            }
                        }
                    });

                    // Pointer variables to store the original touch positions
                    MotionEvent.PointerCoords pointerCoords1 = new MotionEvent.PointerCoords();
                    ev.getPointerCoords(0, pointerCoords1);

                    MotionEvent.PointerCoords pointerCoords2 = new MotionEvent.PointerCoords();
                    ev.getPointerCoords(1, pointerCoords2);

                    // storing distance between the two positions to be compared later on for
                    // zooming
                    originalDistance = (int) getDistance(pointerCoords1.x, pointerCoords2.x,
                            pointerCoords1.y, pointerCoords2.y);

                    // storing center point of the two pointers to move the view according to the
                    // touch position
                    twoPointCenter = new int[]{
                            (int) ((pointerCoords2.x + pointerCoords1.x) / 2),
                            (int) ((pointerCoords2.y + pointerCoords1.y) / 2)
                    };

                    textView.setText("first : " + twoPointCenter[0] + " | " + twoPointCenter[1]);
                    sendZoomEventToListeners(zoomableView, true);
                    return true;
                }
            } else {
                MotionEvent.PointerCoords pointerCoords1 = new MotionEvent.PointerCoords();
                ev.getPointerCoords(0, pointerCoords1);

                MotionEvent.PointerCoords pointerCoords2 = new MotionEvent.PointerCoords();
                ev.getPointerCoords(1, pointerCoords2);

                int[] newCenter = new int[]{
                        (int) ((pointerCoords2.x + pointerCoords1.x) / 2),
                        (int) ((pointerCoords2.y + pointerCoords1.y) / 2)
                };

                int currentDistance = (int) getDistance(pointerCoords1.x, pointerCoords2.x,
                        pointerCoords1.y, pointerCoords2.y);
                double pctIncrease = (currentDistance - originalDistance) / originalDistance;

                zoomableView.setScaleX((float) (1 + pctIncrease));
                zoomableView.setScaleY((float) (1 + pctIncrease));

                updateZoomableViewMargins(newCenter[0] - twoPointCenter[0] + originalXY[0],
                        newCenter[1] - twoPointCenter[1] + originalXY[1]);


                textView.setText(ev.getRawX() + " | " + ev.getRawY() + "\nsecond : " + newCenter[0] + " | " + newCenter[1] + "\n" + twoPointCenter[0] + " | " + twoPointCenter[1] + "\n" + originalXY[0] + " | " + originalXY[1] + "\n" + (newCenter[0] - twoPointCenter[0] + originalXY[0]) + " | " + (newCenter[1] - twoPointCenter[1] + originalXY[1]));

                darkView.setAlpha((float) (pctIncrease / 8));

                return true;
            }
        } else {
            if (zoomableView != null && !isAnimatingDismiss) {
                isAnimatingDismiss = true;
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f);
                valueAnimator.setDuration(activity.getResources()
                        .getInteger(android.R.integer.config_shortAnimTime));
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    float scaleYStart = zoomableView.getScaleY();
                    float scaleXStart = zoomableView.getScaleX();
                    int leftMarginStart = zoomableViewFrameLP.leftMargin;
                    int topMarginStart = zoomableViewFrameLP.topMargin;
                    float alphaStart = darkView.getAlpha();

                    float scaleYEnd = 1f;
                    float scaleXEnd = 1f;
                    int leftMarginEnd = originalXY[0];
                    int topMarginEnd = originalXY[1];
                    float alphaEnd = 0f;

                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        float animatedFraction = valueAnimator.getAnimatedFraction();
                        if (animatedFraction < 1) {
                            zoomableView.setScaleX(((scaleXEnd - scaleXStart) * animatedFraction) +
                                    scaleXStart);
                            zoomableView.setScaleY(((scaleYEnd - scaleYStart) * animatedFraction) +
                                    scaleYStart);

                            updateZoomableViewMargins(
                                    ((leftMarginEnd - leftMarginStart) * animatedFraction) +
                                            leftMarginStart,
                                    ((topMarginEnd - topMarginStart) * animatedFraction) +
                                            topMarginStart);

                            darkView.setAlpha(((alphaEnd - alphaStart) * animatedFraction) +
                                    alphaStart);
                        } else {
                            dismissDialogAndViews();
                        }
                    }
                });
                valueAnimator.start();

                return true;
            }
        }

        return false;
    }

    void updateZoomableViewMargins(float left, float top) {
        if (zoomableView != null && zoomableViewFrameLP != null) {
            zoomableViewFrameLP.leftMargin = (int) left;
            zoomableViewFrameLP.topMargin = (int) top;
            zoomableView.setLayoutParams(zoomableViewFrameLP);
        }
    }

    /**
     * Dismiss dialog and set views to null for garbage collection
     */
    private void dismissDialogAndViews() {
        sendZoomEventToListeners(zoomableView, false);

        if (zoomableView != null) {
            zoomableView.setVisibility(View.VISIBLE);
            zoomableView.setDrawingCacheEnabled(true);

            BitmapDrawable placeholderDrawable = new BitmapDrawable(
                    zoomableView.getResources(),
                    Bitmap.createBitmap(zoomableView.getDrawingCache()));
            if (Build.VERSION.SDK_INT >= 16) {
                placeholderView.setBackground(placeholderDrawable);
            } else {
                placeholderView.setBackgroundDrawable(placeholderDrawable);
            }

            ViewGroup parent = (ViewGroup) zoomableView.getParent();
            parent.removeView(zoomableView);
            this.parentOfZoomableView.addView(zoomableView, viewIndex, zoomableViewLP);
            this.parentOfZoomableView.removeView(placeholderView);

            zoomableView.setDrawingCacheEnabled(false);
            zoomableView.post(new Runnable() {
                @Override
                public void run() {
                    dismissDialog();
                }
            });

//            zoomableView = null;
        } else {
            dismissDialog();
        }

        isAnimatingDismiss = false;
    }

    public void addOnZoomListener(OnZoomListener onZoomListener) {
        zoomListeners.add(onZoomListener);
    }

    public void removeOnZoomListener(OnZoomListener onZoomListener) {
        zoomListeners.remove(onZoomListener);
    }

    private void sendZoomEventToListeners(View zoomableView, boolean zoom) {
        for (OnZoomListener onZoomListener : zoomListeners) {
            if (zoom)
                onZoomListener.onImageZoomStarted(zoomableView);
            else
                onZoomListener.onImageZoomEnded(zoomableView);
        }
    }

    private void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }

        darkView = null;
        resetOriginalViewAfterZoom();
    }

    private void resetOriginalViewAfterZoom() {
        zoomableView.invalidate();
        zoomableView = null;
    }

    /**
     * Get distance between two points
     *
     * @param x1
     * @param x2
     * @param y1
     * @param y2
     * @return distance
     */
    private double getDistance(double x1, double x2, double y1, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public interface OnZoomListener {

        void onImageZoomStarted(View view);

        void onImageZoomEnded(View view);

    }
}

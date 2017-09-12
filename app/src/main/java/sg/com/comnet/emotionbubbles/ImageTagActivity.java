package com.codermagnet.imagetagging;
//https://stackoverflow.com/questions/27757099/android-detect-doubletap-and-tripletap-on-view
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import static android.R.attr.radius;

public class ImageTagActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

    ImageView ivSample;
    Bitmap original;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivSample = (ImageView) findViewById(R.id.ivSample);
        Glide.with(this).load("https://lh3.googleusercontent.com/mlLzYuFqIVlPHOYI1EhNeDXV9UasN0FYpfBhN3Ihp_6DqcKzgm5Uf5t5JGelQ8NNdSf9J307VzLQfbv2QQ").override(500, 500).into(ivSample).getSize(new SizeReadyCallback() {
            @Override
            public void onSizeReady(int width, int height) {
                Toast.makeText(MainActivity.this, width + "|" + height,Toast.LENGTH_LONG).show();
            }
        });
        ivSample.setOnTouchListener(this);
        ivSample.setOnClickListener(this);
        original = ((BitmapDrawable)ivSample.getDrawable()).getBitmap();
    }

    private Bitmap drawNamesOnBitmap(Bitmap originalBitmap, String[] names, int x, int y) {
        Bitmap bitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        int stokeWidth = 2;
        paint.setStrokeWidth(stokeWidth);
        if (names != null) {
            for (String name : names) {
//                int xPos = layoutTextViewContainer.getLeft();
//                int yPos = layoutTextViewContainer.getTop();
//                xPos = (int) (xPos / getResources().getDisplayMetrics().density);
//                yPos = (int) (yPos / getResources().getDisplayMetrics().density);
                int projectedX = (int)((double)x * ((double)bitmap.getWidth()/(double)ivSample.getWidth()));
                int projectedY = (int)((double)y * ((double)bitmap.getHeight()/(double)ivSample.getHeight()));

                Paint.FontMetrics fm = new Paint.FontMetrics();
                paint.getFontMetrics(fm);
                int margin = 5;
                String emoji = new String(Character.toChars(0x1F369));
//                canvas.drawRect(projectedX - margin, projectedY + fm.top - margin,
//                        projectedX + paint.measureText(name+emoji) + margin, projectedY + fm.bottom
//                                + margin, paint);

                drawRoundRect(projectedX - margin, projectedY + fm.top - margin,
                        projectedX + paint.measureText(name+emoji) + margin, projectedY + fm.bottom
                                + margin,paint,canvas);
                paint.setColor(Color.WHITE);

                canvas.drawText(name+emoji,projectedX,projectedY, paint);

                /*Paint mTxtPaint = new Paint();
                String str = "I Like Android Learning";
                Paint.FontMetrics fm = new Paint.FontMetrics();
                mTxtPaint.setColor(Color.BLACK);
                mTxtPaint.setTextSize(18.0f);

                mTxtPaint.getFontMetrics(fm);

                int margin = 5;

                canvas.drawRect(100 - margin, 100 + fm.top - margin,
                        100 + mTxtPaint.measureText(str) + margin, 100 + fm.bottom
                                + margin, mTxtPaint);

                mTxtPaint.setColor(Color.WHITE);

                canvas.drawText(str, 100, 100, mTxtPaint);*/


                ivSample.invalidate();
            }
        }
        return bitmap;
    }

    private void drawRoundRect(float left, float top, float right, float bottom, Paint paint, Canvas canvas) {
        Path path =RoundedRect(left,top,right,bottom,15,15,false);
//        path.moveTo(left, top);
//        path.lineTo(right, top);
//        path.lineTo(right, bottom);
//        path.lineTo(left + 15, bottom);
//        path.quadTo(left, bottom, left, bottom - 15);
//        path.lineTo(left, top + 15);
//        path.quadTo(left, top, left + 15, top);
        canvas.drawPath(path, paint);
    }

    public Path RoundedRect(float left, float top, float right, float bottom, float rx, float ry, boolean conformToOriginalPost) {
        Path path = new Path();
        if (rx < 0) rx = 0;
        if (ry < 0) ry = 0;
        float width = right - left;
        float height = bottom - top;
        if (rx > width/2) rx = width/2;
        if (ry > height/2) ry = height/2;
        float widthMinusCorners = (width - (2 * rx));
        float heightMinusCorners = (height - (2 * ry));

        path.moveTo(right, top + ry);
        path.rQuadTo(0, -ry, -rx, -ry);//top-right corner
        path.rLineTo(-widthMinusCorners, 0);
        path.rQuadTo(-rx, 0, -rx, ry); //top-left corner
        path.rLineTo(0, heightMinusCorners);

        if (conformToOriginalPost) {
            path.rLineTo(0, ry);
            path.rLineTo(width, 0);
            path.rLineTo(0, -ry);
        }
        else {
            path.rQuadTo(0, ry, rx, ry);//bottom-left corner
            path.rLineTo(widthMinusCorners, 0);
            path.rQuadTo(rx, 0, rx, -ry); //bottom-right corner
        }

        path.rLineTo(0, -heightMinusCorners);

        path.close();//Given close, last lineto can be removed.

        return path;
    }

    int x,y;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = (int)event.getX();
                y = (int)event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                // do something
                break;
            case MotionEvent.ACTION_UP:
                // do somethig
                break;
        }
        return false;
    }

    boolean onoff = true;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivSample:
                if(onoff) {
                    onoff = false;
                    ivSample.setImageBitmap(drawNamesOnBitmap(((BitmapDrawable) ivSample.getDrawable()).getBitmap(), new String[]{"#JItesh"}, x, y));
                }
                else {
                    onoff =true;
                    ivSample.setImageBitmap(original);
                }

                break;
        }
    }
}

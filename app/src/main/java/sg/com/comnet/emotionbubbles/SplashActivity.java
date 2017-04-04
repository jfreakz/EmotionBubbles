package sg.com.comnet.emotionbubbles;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.media.Image;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.constraint.solver.widgets.Animator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nineoldandroids.view.ViewHelper;

import java.util.Random;

import static java.security.AccessController.getContext;

public class SplashActivity extends AppCompatActivity {

    private static String[] labels = {"Love","Joy","Surprise","Anger","Sadness","Anxiety","Fear","Anticipation","Hope","Grief","Pleasure"};
    BubbleLayout layout;
    Random random;
    private int[] colors = {
            R.color.colorPrimary,
            R.color.colorAccent,
            R.color.blueStart,
            R.color.blueEnd,
            R.color.purpleStart,
            R.color.purpleEnd,
            R.color.roseStart,
            R.color.roseEnd,
            R.color.greenStart,
            R.color.greenEnd,
            R.color.amber
    };
    int cnt = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        random = new Random();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        getWindowManager().getDefaultDisplay().getRectSize(mDisplaySize);
        mScale = displayMetrics.density;

        layout = (BubbleLayout) findViewById(R.id.bubble_layout);

        int col = 0;
        for (String label : labels) {
            BubbleView bubbleView = new BubbleView(this);

//            bubbleView.setCircleColor(colors[new Random().nextInt(colors.length)]);
            bubbleView.setCircleColor(colors[col]);
            bubbleView.setTextColor(Color.WHITE);
            bubbleView.setTextSize((float)random.nextInt(20-12) + 12);
            bubbleView.setRotation(random.nextInt(90));
            bubbleView.setText(label);
            bubbleView.setGravity(Gravity.CENTER);
            bubbleView.setPadding(10, 10, 10, 10);
//            bubbleView.setTextColor(Color.parseColor("#000000"));
//            layout.addViewSortByWidth(bubbleView);


            bubbleView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        Toast.makeText(SplashActivity.this, ((BubbleView) view).getText().toString(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SplashActivity.this, ImageActivity.class));
                    }
                    return true;
                }
            });

//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(150, 150);

//            ViewHelper.setX(bubbleView, random.nextInt(width/2));
//            ViewHelper.setY(bubbleView, random.nextInt(height/2));

            bubbleView.setVisibility(View.INVISIBLE);
            layout.addView(bubbleView,150,150);
            col++;

        }


        cnt = layout.getChildCount();
        ct = new CountDownTimer(Long.MAX_VALUE ,700) { // interval 1s

            // This is called every interval. (Every 1 seconds in this example)
            public void onTick(long millisUntilFinished) {
                if(cnt == 1) cancelTimer();
                View v = layout.getChildAt(cnt-1);
                v.setVisibility(View.VISIBLE);
                v.setAnimation(AnimationUtils.loadAnimation(SplashActivity.this,R.anim.slide_in_up));
//                startAnimation(v);
                cnt--;
            }
            public void onFinish() {
                System.out.println("finished");
            }
        }.start();

        /*for (int i = 0; i < layout.getChildCount(); i++) {
            final View v = layout.getChildAt(i);
            if (v instanceof BubbleView) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {

                                v.setVisibility(View.VISIBLE);

                                v.setAnimation(AnimationUtils.loadAnimation(SplashActivity.this,android.R.anim.slide_in_left));

//                    }
//                });
            }
        },1000);
            }
        }*/
        /*col = 0;
        for(int i=0; i<labels.length/2; i++){
            BubbleView bubbleView = new BubbleView(this);

            bubbleView.setCircleColor(colors[col]);
            int hw = new Random().nextInt(125-75)+75;
            layout.addView(bubbleView, hw, hw);
            col++;
        }*/
    }

    //cancel timer
    void cancelTimer() {
        if(ct!=null) {
            System.out.println("cancelled");
            ct.cancel();
        }
    }
    CountDownTimer ct;
    private Rect mDisplaySize = new Rect();
    private float mScale;
    public void startAnimation(final View leafImageView) {

        leafImageView.setPivotX(leafImageView.getWidth()/2);
        leafImageView.setPivotY(leafImageView.getHeight()/2);

        long delay = new Random().nextInt(6000);

        final ValueAnimator animator = ValueAnimator.ofFloat(1, 0);
        animator.setDuration(10000);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setStartDelay(delay);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            int movex = new Random().nextInt(mDisplaySize.right);
            int angle = 50 + (int)(Math.random() * 101);

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = ((Float) (animation.getAnimatedValue())).floatValue();

                leafImageView.setRotation(angle*value);
                leafImageView.setTranslationX((movex-40)*value);
                leafImageView.setTranslationY((mDisplaySize.top + (150*mScale))*value);
            }
        });

        animator.start();
    }

}
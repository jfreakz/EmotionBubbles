package sg.com.comnet.emotionbubbles;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import java.util.Random;

public class SplashActivity extends AppCompatActivity {

    private static String[] labels = {"Love", "Joy", "Surprise", "Anger", "Sadness", "Anxiety", "Fear", "Anticipation", "Hope", "Grief", "Pleasure"};
    BubbleLayout layout;
    Random random;
    int cnt = 0;
    CountDownTimer ct;
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
            R.color.amber,
            R.color.colorPrimary,
            R.color.colorAccent,
            R.color.blueStart,
            R.color.blueEnd,
            R.color.purpleStart
    };
    private Rect mDisplaySize = new Rect();
    private float mScale;

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


        BubbleView bubbleViewLogo = new BubbleView(this);
        bubbleViewLogo.setTag("logo");
        bubbleViewLogo.setCircleColor(android.R.color.transparent);
        bubbleViewLogo.setBackgroundResource(R.mipmap.ic_launcher);

        layout.addView(bubbleViewLogo, 150, 150);

        int col = 0;
        for (String label : labels) {
            BubbleView bubbleView = new BubbleView(this);

//            bubbleView.setCircleColor(colors[new Random().nextInt(colors.length)]);
            bubbleView.setCircleColor(colors[col]);
            bubbleView.setTag("c_color" + colors[col]);
            bubbleView.setTextColor(Color.WHITE);
            bubbleView.setBackgroundResource(R.drawable.shadow_circle);
            bubbleView.setTextSize((float)random.nextInt(20-12) + 12);
//            bubbleView.setRotation(random.nextInt(90));
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
                        startActivity(new Intent(SplashActivity.this, FlowActivity.class));
                    }
                    return true;
                }
            });

//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(150, 150);

//            ViewHelper.setX(bubbleView, random.nextInt(width/2));
//            ViewHelper.setY(bubbleView, random.nextInt(height/2));

            bubbleView.setVisibility(View.INVISIBLE);
            int size = random.nextInt(150 - 125) + 125;
            layout.addView(bubbleView, size, size);
            col++;

        }


        cnt = 1;
        ct = new CountDownTimer(Long.MAX_VALUE, 1000) { // interval 1s

            // This is called every interval. (Every 1 seconds in this example)
            public void onTick(long millisUntilFinished) {
                if (cnt == layout.getChildCount())
                    cancelTimer();
                else {
                    View v = layout.getChildAt(cnt);
                    v.setVisibility(View.VISIBLE);
                    v.setAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.slide_in_up));
//                startAnimation(v);
                    cnt++;
                }
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
                float value = (Float) (animation.getAnimatedValue());

                leafImageView.setRotation(angle*value);
                leafImageView.setTranslationX((movex-40)*value);
                leafImageView.setTranslationY((mDisplaySize.top + (150*mScale))*value);
            }
        });

        animator.start();
    }

}
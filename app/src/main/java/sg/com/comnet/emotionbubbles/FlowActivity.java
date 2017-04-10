package sg.com.comnet.emotionbubbles;

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
import android.view.ViewGroup;
import android.widget.Toast;

import com.wefika.flowlayout.FlowLayout;

import java.util.Random;

public class FlowActivity extends AppCompatActivity {

    private static String[] labels = {"Mood1", "Mood2", "Mood3", "Mood4", "Mood5", "Love", "Joy", "Surprise", "Anger", "Sadness", "Anxiety", "Fear", "Anticipation", "Hope", "Grief", "Pleasure"};
    Random random;
    int cnt = 0;
    CountDownTimer ct;
    FlowLayout flowLayout;
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
        setContentView(R.layout.activity_flow);

        random = new Random();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        getWindowManager().getDefaultDisplay().getRectSize(mDisplaySize);
        mScale = displayMetrics.density;
        flowLayout = (FlowLayout) findViewById(R.id.flowLayout);

        int col = 0;
        for (String label : labels) {
            BubbleView bubbleView = new BubbleView(this);

            bubbleView.setCircleColor(colors[col]);
            bubbleView.setTextColor(Color.WHITE);
            bubbleView.setTextSize(15.0f);
            bubbleView.setText(label);
            bubbleView.setGravity(Gravity.CENTER);
            bubbleView.setPadding(20, 20, 20, 20);

            bubbleView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        Toast.makeText(FlowActivity.this, ((BubbleView) view).getText().toString(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(FlowActivity.this, SubFeelingActivity.class));
                    }
                    return true;
                }
            });

            bubbleView.setVisibility(View.INVISIBLE);
            FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(random.nextInt(20), random.nextInt(20), random.nextInt(20), random.nextInt(20));
            flowLayout.addView(bubbleView, params);
            col++;

        }

        cnt = 0;
        ct = new CountDownTimer(Long.MAX_VALUE, 700) { // interval 1s

            // This is called every interval. (Every 1 seconds in this example)
            public void onTick(long millisUntilFinished) {
                if (cnt == flowLayout.getChildCount())
                    cancelTimer();
                else {
                    View v = flowLayout.getChildAt(cnt);
                    v.setVisibility(View.VISIBLE);
                    cnt++;
                }
            }

            public void onFinish() {
                System.out.println("finished");
            }
        }.start();

    }

    //cancel timer
    void cancelTimer() {
        if (ct != null) {
            System.out.println("cancelled");
            ct.cancel();
        }
    }
}

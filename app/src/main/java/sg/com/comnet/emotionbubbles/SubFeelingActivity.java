package sg.com.comnet.emotionbubbles;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SubFeelingActivity extends AppCompatActivity {
    private static String[] labels = {"Affection", "Longing", "Crush"};
    private LinearLayout llSubFeelingContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_feeling);

        initializeUI();
    }

    private void initializeUI() {
        llSubFeelingContainer = (LinearLayout) findViewById(R.id.llSubFeelingContainer);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        for (String label : labels) {
            BubbleView bubbleView = new BubbleView(this);

//            bubbleView.setCircleColor(colors[new Random().nextInt(colors.length)]);
            bubbleView.setCircleColor(R.color.roseEnd);
            bubbleView.setTextColor(Color.WHITE);
            bubbleView.setTextSize(22.0f);
            bubbleView.setText(label);
            bubbleView.setGravity(Gravity.CENTER);
            bubbleView.setPadding(10, 10, 10, 10);

            int size = (height - 150) / 3;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);

            params.setMargins(10, 10, 10, 10);

            bubbleView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        Toast.makeText(SubFeelingActivity.this, ((BubbleView) view).getText().toString(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SubFeelingActivity.this, ImageActivity.class));
                    }
                    return true;
                }
            });


            llSubFeelingContainer.addView(bubbleView, params);

        }
    }
}

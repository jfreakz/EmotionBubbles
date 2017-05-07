package sg.com.comnet.emotionbubbles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.List;

public class ImageActivity extends AppCompatActivity {
    int h, w;
    boolean firstTime = false;
    private SeekbarWithIntervals SeekbarWithIntervals = null;
    private ImageView imageview;
    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            imageview.getLayoutParams().width = w + 10 * progress;
            imageview.getLayoutParams().height = h + 10 * progress;
            imageview.requestLayout();
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            if (!firstTime) {
                w = imageview.getMeasuredWidth();
                h = imageview.getMeasuredWidth();
                firstTime = true;
            }
        }

        public void onStopTrackingTouch(SeekBar seekBar) {

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        List<String> seekbarIntervals = getIntervals();
        getSeekbarWithIntervals().setIntervals(seekbarIntervals);
        getSeekbarWithIntervals().setOnSeekBarChangeListener(seekBarChangeListener);

        this.imageview = (ImageView) this.findViewById(R.id.imageview1);
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ImageActivity.this, ZoomImageActivity.class));
            }
        });
//        Bitmap finalBitmap = Bitmap.createScaledBitmap(bitmap,
//                imageview.getWidth(), imageview.getHeight(), false);

    }

    private List<String> getIntervals() {
        return new ArrayList<String>() {{
            add("1");
            add("2");
            add("3");
            add("4");
            add("5");
            add("6");
            add("7");
            add("8");
            add("9");
            add("10");
        }};
    }

    private SeekbarWithIntervals getSeekbarWithIntervals() {
        if (SeekbarWithIntervals == null) {
            SeekbarWithIntervals = (SeekbarWithIntervals) findViewById(R.id.seekbarWithIntervals);
        }

        return SeekbarWithIntervals;
    }
}
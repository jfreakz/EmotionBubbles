package sg.com.comnet.emotionbubbles;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import java.util.ArrayList;
import java.util.List;

public class ImageActivity extends AppCompatActivity {
    private SeekbarWithIntervals SeekbarWithIntervals = null;
    private ImageView imageview;
    int h,w;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        List<String> seekbarIntervals = getIntervals();
        getSeekbarWithIntervals().setIntervals(seekbarIntervals);
        getSeekbarWithIntervals().setOnSeekBarChangeListener(seekBarChangeListener);

        this.imageview = (ImageView) this.findViewById(R.id.imageview1);

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

    boolean firstTime = false;
    SeekBar.OnSeekBarChangeListener seekBarChangeListener	= new SeekBar.OnSeekBarChangeListener() {

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            imageview.getLayoutParams().width = w + 10*progress;
            imageview.getLayoutParams().height = h + 10*progress;
            imageview.requestLayout();
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            if(!firstTime) {
                w = imageview.getMeasuredWidth();
                h = imageview.getMeasuredWidth();
                firstTime = true;
            }
        }

        public void onStopTrackingTouch(SeekBar seekBar) {

        }

    };
}
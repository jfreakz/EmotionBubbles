package sg.com.comnet.emotionbubbles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

/**
 * Created by Mastan on 06-07-2015.
 */
public class BouncingBallView extends View {
    private final int FRAME_RATE=15;
    private Paint paint;
    private Handler h;
    Ball myball;
    Ball greenBall;
    Ball redBall;

    private Runnable r=new Runnable(){
        public void run()
        {
            invalidate();
        }
    };
    public BouncingBallView(Context context,AttributeSet attrs)
    {
        super(context,attrs);

        h=new Handler();
        paint=new Paint();
        paint.setColor(Color.BLUE);
        myball=new Ball(100,100,Color.BLUE,80);
        greenBall=new Ball(200,200,Color.GREEN,80);
        redBall=new Ball(50,400,Color.RED,80);

        myball.setDX(1);
        myball.setDY(-3);

        greenBall.setDX(2);
        greenBall.setDY(-2);
        redBall.setDX(3);
        redBall.setDY(-1);
    }
    protected void onDraw(Canvas c)
    {
        myball.bounce(c);
        greenBall.bounce(c);
        redBall.bounce(c);

//        myball.bounceoff(greenBall);
//        myball.bounceoff(redBall);
//        greenBall.bounceoff(redBall);
//        greenBall.bounceoff(myball);
//        redBall.bounceoff(myball);
//        redBall.bounceoff(greenBall);

        c.drawCircle(myball.getX(),myball.getY(),myball.getRadius(),myball.getPaint());

        c.drawCircle(greenBall.getX(),greenBall.getY(),greenBall.getRadius(),greenBall.getPaint());

        c.drawCircle(redBall.getX(),redBall.getY(),redBall.getRadius(),redBall.getPaint());

        h.postDelayed(r,FRAME_RATE);
    }

}
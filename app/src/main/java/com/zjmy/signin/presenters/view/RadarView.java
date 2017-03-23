package com.zjmy.signin.presenters.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by utopia on 17-3-23.
 */

public class RadarView extends FrameLayout {
    private Context mContext;
    private int viewSize = 400;
    private Paint mPaintLine;
    private Paint mPaintCircle;
    private Paint mPaintSector;
    public boolean isstart = false;
    private ScanThread mThread;
    private Paint mPaintPoint;
    //旋转效果起始角度
    private int start = 0;

    //private int[] point_x;
    //private int[] point_y;

    private Shader mShader;

    private Matrix matrix;

    public final static int CLOCK_WISE = 1;
    public final static int ANTI_CLOCK_WISE = -1;

    @IntDef({CLOCK_WISE, ANTI_CLOCK_WISE})
    public @interface RADAR_DIRECTION {

    }

    //默认为顺时针呢
    private final static int DEFAULT_DIERCTION = CLOCK_WISE;

    //设定雷达扫描方向
    private int direction = DEFAULT_DIERCTION;

    private boolean threadRunning = true;

    public RadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        mContext = context;
        initPaint();
    }

    public RadarView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        mContext = context;
        initPaint();

    }

    private void initPaint() {
        // TODO Auto-generated method stub
        setBackgroundColor(Color.TRANSPARENT);

        //宽度=5，抗锯齿，描边效果的白色画笔
        mPaintLine = new Paint();
        mPaintLine.setStrokeWidth(5);
        mPaintLine.setAntiAlias(true);
        mPaintLine.setStyle(Style.STROKE);
        mPaintLine.setColor(Color.WHITE);

        //宽度=5，抗锯齿，描边效果的浅绿色画笔
        mPaintCircle = new Paint();
        mPaintCircle.setStrokeWidth(5);
        mPaintCircle.setAntiAlias(true);
        mPaintCircle.setStyle(Style.FILL);
        mPaintCircle.setColor(0x99000000);

        //暗绿色的画笔
        mPaintSector = new Paint();
        mPaintSector.setColor(0x9D00ff00);
        mPaintSector.setAntiAlias(true);
        mShader = new SweepGradient(viewSize / 2, viewSize / 2, Color.TRANSPARENT, Color.GREEN);
        mPaintSector.setShader(mShader);

        //白色实心画笔
        mPaintPoint = new Paint();
        mPaintPoint.setColor(Color.WHITE);
        mPaintPoint.setStyle(Style.FILL);

        //随机生成的点，模拟雷达扫描结果
        //point_x = Getrandomarray(1, 120);
        //point_y = Getrandomarray(1, 120);

    }

    /**
     * @param log 数组长度
     * @param top 随机数上限
     * @return 生成随机数数组，内容为[-top,top]
     */
    public int[] Getrandomarray(int log, int top) {
        int[] result = new int[log];
        for (int i = 0; i < log; i++) {
            int random = (int) (top * (2 * Math.random() - 1));
            result[i] = random;
        }
        return result;
    }

    public void setViewSize(int size) {
        this.viewSize = size;
        setMeasuredDimension(viewSize, viewSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        setMeasuredDimension(viewSize, viewSize);
    }

    public void start() {
        mThread = new ScanThread(this);
        mThread.setName("radar");
        mThread.start();
        threadRunning = true;
        isstart = true;
    }

    public void stop() {
        if (isstart) {
            threadRunning = false;
            isstart = false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        canvas.drawCircle(viewSize / 2, viewSize / 2, 180, mPaintCircle);
        canvas.drawCircle(viewSize / 2, viewSize / 2, 120, mPaintLine);
        canvas.drawCircle(viewSize / 2, viewSize / 2, 60, mPaintLine);
        canvas.drawCircle(viewSize / 2, viewSize / 2, 180, mPaintLine);
        //绘制两条十字线
        canvas.drawLine(viewSize / 2, 0, viewSize / 2, viewSize, mPaintLine);
        canvas.drawLine(0, viewSize / 2, viewSize, viewSize / 2, mPaintLine);


        //这里在雷达扫描过制定圆周度数后，将随机绘制一些白点，模拟搜索结果
       /* if (start > 120) {
            for (int i = 0; i < 2; i++) {
                //canvas.drawCircle(viewSize / 2 + point_x[i], viewSize / 2 + point_y[i], 10, mPaintPoint);
            }
        }*/

        //根据matrix中设定角度，不断绘制shader,呈现出一种扇形扫描效果
        canvas.concat(matrix);
        canvas.drawCircle(viewSize / 2, viewSize / 2, 180, mPaintSector);
        super.onDraw(canvas);
    }

    public void setDirection(@RADAR_DIRECTION int direction) {
        if (direction != CLOCK_WISE && direction != ANTI_CLOCK_WISE) {
            throw new IllegalArgumentException("Use @RADAR_DIRECTION constants only!");
        }
        this.direction = direction;
    }

    protected class ScanThread extends Thread {

        private RadarView view;

        public ScanThread(RadarView view) {
            // TODO Auto-generated constructor stub
            this.view = view;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (threadRunning) {
                if (isstart) {
                    view.post(new Runnable() {
                        public void run() {
                            start = start + 1;
                            matrix = new Matrix();
                            //设定旋转角度,制定进行转转操作的圆心
//                            matrix.postRotate(start, viewSize / 2, viewSize / 2);
//                            matrix.setRotate(start,viewSize/2,viewSize/2);
                            matrix.preRotate(direction * start, viewSize / 2, viewSize / 2);
                            view.invalidate();

                        }
                    });
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
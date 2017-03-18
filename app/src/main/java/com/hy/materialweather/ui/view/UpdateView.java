package com.hy.materialweather.ui.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.hy.materialweather.R;

/**
 * 定义一个View，旋转更新
 */
public class UpdateView extends View {

    private Context context;

    //资源
    private Paint picturePaint;
    private Paint textPaint;
//    private Bitmap rawBitmap;

    public UpdateView(Context context) {
        this(context, null);
    }

    public UpdateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UpdateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        //读取属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.UpdateView);
        init(ta);
        ta.recycle();
    }

    //Attrs
    protected boolean isShowing;
    protected String showText;

    public static final int TEXT_SIZE = 20;

    /**
     * 初始化数据
     *
     * @param ta
     */
    private void init(TypedArray ta) {
        if (ta != null) {
            isShowing = ta.getBoolean(R.styleable.UpdateView_isShowing, false);
            showText = ta.getString(R.styleable.UpdateView_tellText);
        }
        if (showText == null) {
            showText = "加载中";
        }
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(TEXT_SIZE);

        picturePaint = new Paint();
        picturePaint.setStrokeWidth(10);
        picturePaint.setAntiAlias(true);
        picturePaint.setColor(Color.BLACK);
        picturePaint.setStyle(Paint.Style.STROKE);

//        rawBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.loading);
        deltaRadius = eachTime * 360 / duration;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //尺寸自适应成100dp
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(150, 150);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(150, heightSize);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, 150);
        }
        cx = 150 / 2;
        cy = 150 / 2;
    }

    //valid
    float cx, cy;
    float radius = 0;
    int deltaRadius;
    int duration = 2000;
    int eachTime = 15;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画文字
        canvas.drawText(showText, 0, getHeight() - TEXT_SIZE, textPaint);

        drawTrack(canvas);
        postInvalidateDelayed(eachTime);
    }

    protected void drawTrack(Canvas canvas) {
        RectF rectF = new RectF(0 + 10, 0 + 10, getWidth() - 10, getHeight() - 10);
        canvas.drawArc(rectF, 180, radius, false, picturePaint);
        radius += deltaRadius;
        if (radius >= 360) {
            radius = 0;
        }
    }

    /**
     * 展示进度条
     */
    public void show() {
        setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏进度条
     */
    public void hide() {
        setVisibility(View.INVISIBLE);
    }

//        canvas.drawBitmap(rawBitmap, 0, 0, picturePaint);

}

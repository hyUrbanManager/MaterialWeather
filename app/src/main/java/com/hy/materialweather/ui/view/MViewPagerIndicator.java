package com.hy.materialweather.ui.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.hy.materialweather.R;
import com.hy.materialweather.uitls.Utils;

/**
 * ViewPager 自定义指示器
 */
public class MViewPagerIndicator extends View {
    private static final String TAG = MViewPagerIndicator.class.getName() + " ";

    private Context context;

    //资源
    private Paint textPaint;
    private Paint picturePaint;
    private int accent_color = Color.BLUE;
    private int text_color = Color.BLACK;

    public MViewPagerIndicator(Context context) {
        this(context, null);
    }

    public MViewPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        //读取属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MViewPagerIndicator);
        init(ta);
        ta.recycle();
    }

    //Attrs 属性
    protected int num;

    //初始常量
    public final int TEXT_SIZE = 48;
    public final int RADIUS_PADDING = 25;
    public final int radius = 15;
    public final int HEIGHT = 60;
    public final int WIDTH = TEXT_SIZE * 2 + RADIUS_PADDING * 4;

    public final String[] indicators = new String[]{"今天", "明天", "后天"};

    //运行时变量
    private int page;

    /**
     * 初始化数据
     *
     * @param ta
     */
    private void init(TypedArray ta) {
        if (ta != null) {
            num = ta.getInt(R.styleable.MViewPagerIndicator_num, 3);
            text_color = ta.getColor(R.styleable.MViewPagerIndicator_textColor, Color.BLUE);
            accent_color = ta.getColor(R.styleable.MViewPagerIndicator_circleColor, Color.BLACK);
        }
        textPaint = new Paint();
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.setTypeface(Typeface.MONOSPACE);
        textPaint.setColor(text_color);

        picturePaint = new Paint();
        picturePaint.setAntiAlias(true);
        picturePaint.setColor(accent_color);
        picturePaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //固定尺寸
        setMeasuredDimension(WIDTH, HEIGHT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Utils.d(TAG + "onDraw 调用");

        //根据page来显示不同的界面，不具有通用性
        switch (page) {
            case 0: {
                canvas.drawText(indicators[page], 0, HEIGHT - (HEIGHT - TEXT_SIZE) / 2, textPaint);
                canvas.drawCircle(RADIUS_PADDING + TEXT_SIZE * 2, HEIGHT / 2, radius, picturePaint);
                canvas.drawCircle(RADIUS_PADDING * 3 + TEXT_SIZE * 2, HEIGHT / 2, radius, picturePaint);
                break;
            }
            case 1: {
                canvas.drawText(indicators[page], RADIUS_PADDING * 2, HEIGHT - (HEIGHT - TEXT_SIZE) / 2, textPaint);
                canvas.drawCircle(RADIUS_PADDING, HEIGHT / 2, radius, picturePaint);
                canvas.drawCircle(RADIUS_PADDING * 3 + TEXT_SIZE * 2, HEIGHT / 2, radius, picturePaint);
                break;
            }
            case 2: {
                canvas.drawText(indicators[page], RADIUS_PADDING * 4, HEIGHT - (HEIGHT - TEXT_SIZE) / 2, textPaint);
                canvas.drawCircle(RADIUS_PADDING, HEIGHT / 2, radius, picturePaint);
                canvas.drawCircle(RADIUS_PADDING * 3, HEIGHT / 2, radius, picturePaint);
                break;
            }
        }
    }

    private void drawCircle(int cnt) {

    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
        invalidate();
    }
}

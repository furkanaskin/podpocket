package com.furkanaskin.app.podpocket.utils.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.content.ContextCompat;
import com.furkanaskin.app.podpocket.R;

/**
 * Created by Furkan on 29.04.2019
 */
public class GMMarqueTextView extends View {

    static final int DEFAULT_SPEED = 3;
    static final int DEFAULT_EDGE_EFFECT_WIDTH = 10;
    static final int DEFAULT_EDGE_EFFECT_COLOR = Color.WHITE;
    static int DEFAULT_PAUSE_DURATION = 1000;
    boolean marqueeEnabled = true;
    boolean forceMarquee = false;
    int textColor = Color.BLACK;
    float textSize = getResources().getDisplayMetrics().scaledDensity * 20.0f;
    int pauseDuration = DEFAULT_PAUSE_DURATION;
    int speed = DEFAULT_SPEED;

    boolean edgeEffectEnabled = false;
    int edgeEffectWidth = DEFAULT_EDGE_EFFECT_WIDTH;
    int edgeEffectColor = DEFAULT_EDGE_EFFECT_COLOR;

    CharSequence text;

    double wrapAroundPoint;
    boolean animationRunning = false;
    boolean paused = false;
    boolean wrapped = false;
    boolean centerText = true; // only applies to "unwrapped" text

    TextPaint textPaint;
    Paint leftPaint;
    Paint rightPaint;


    Rect textBounds;
    RectF leftRect;
    RectF rightRect;

    int topOffset;
    int xOffset;

    Typeface customTypeface;
    Runnable pauseRunnable = new Runnable() {
        @Override
        public void run() {
            paused = false;
            invalidate();
        }
    };
    Runnable invalidateRunnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    public GMMarqueTextView(Context context) {
        super(context);
        init(null);
    }

    public GMMarqueTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public GMMarqueTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public static void setGlobalDefaultPauseDuration(int pauseDuration) {
        DEFAULT_PAUSE_DURATION = pauseDuration;
    }

    void init(AttributeSet attrs) {

        if (attrs != null) {
            readAttrs(attrs);
        }

        textBounds = new Rect();

        setText(text);

    }

    void readAttrs(AttributeSet attrs) {

        int[] attrsArray = new int[]{
                android.R.attr.textSize,
                android.R.attr.textColor,
                android.R.attr.text,
                R.attr.marqueeEnabled,
                R.attr.edgeEffectEnabled,
                R.attr.edgeEffectWidth,
                R.attr.edgeEffectColor,
                R.attr.pauseDuration,
                R.attr.forceMarquee,
                R.attr.centerText,
        };

        TypedArray ta = getContext().obtainStyledAttributes(attrs, attrsArray);

        textSize = ta.getDimension(0, textSize); // 2 is the index in the array of the textSize attribute
        textColor = ta.getColor(1, textColor); // 3 is the index of the array of the textColor attribute
        text = ta.getText(2);
        marqueeEnabled = ta.getBoolean(3, marqueeEnabled);
        edgeEffectEnabled = ta.getBoolean(4, edgeEffectEnabled);
        edgeEffectWidth = ta.getInt(5, edgeEffectWidth);
        edgeEffectColor = ta.getColor(6, edgeEffectColor);
        pauseDuration = ta.getInt(7, pauseDuration);
        forceMarquee = ta.getBoolean(8, forceMarquee);
        centerText = ta.getBoolean(9, centerText);

        ta.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (text != null) {

            float viewWidth = this.getWidth();

            int textWidth = textBounds.width();

            if (!animationRunning) {

                xOffset = 0;
                wrapAroundPoint = -(textWidth + (textWidth * 0.05));
                animationRunning = true;
                wrapped = true;
                paused = false;

            }

            canvas.drawText(text.toString(), xOffset, topOffset, textPaint);

            if (edgeEffectEnabled) {

                if (xOffset < 0 || pauseDuration <= 0) {
                    canvas.drawRect(leftRect, leftPaint);
                }

                canvas.drawRect(rightRect, rightPaint);

            }

            if (!paused) {

                xOffset -= speed;

                if (xOffset < wrapAroundPoint) {
                    xOffset = (int) viewWidth;
                    wrapped = true;
                }

                if (wrapped && xOffset <= 0) {
                    wrapped = false;

                    if (pauseDuration > 0) {
                        xOffset = 0;
                        pause();
                    }
                }

                invalidateAfter(20);

            }
        }
    }

    synchronized void pause() {
        paused = true;
        removeCallbacks(pauseRunnable);
        postDelayed(pauseRunnable, pauseDuration);
    }

    void invalidateAfter(long delay) {
        removeCallbacks(invalidateRunnable);
        postDelayed(invalidateRunnable, delay);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            // Parent has told us how big to be. So be it.
            width = widthSize;
        } else {
            width = this.getWidth();
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            // Parent has told us how big to be. So be it.
            height = heightSize;
        } else {

            TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            paint.density = getResources().getDisplayMetrics().density;
            paint.setTextSize(textSize);

            if (customTypeface != null) {
                paint.setTypeface(customTypeface);
            }

            height = (int) (Math.abs(paint.ascent()) + Math.abs(paint.descent()));
        }

        setMeasuredDimension(width, height);
        renewPaint();
    }

    void renewPaint() {

        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.density = getResources().getDisplayMetrics().density;
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);

        if (customTypeface != null) {
            textPaint.setTypeface(customTypeface);
        }

        int absEdgeEffectWidth = (getMeasuredWidth() / 100) * edgeEffectWidth;

        Shader leftShader = new LinearGradient(
                0,
                0,
                absEdgeEffectWidth,
                0,
                edgeEffectColor,
                Color.TRANSPARENT,
                Shader.TileMode.CLAMP);

        leftPaint = new Paint();
        leftPaint.setShader(leftShader);

        int rightOffset = getMeasuredWidth() - absEdgeEffectWidth;

        Shader rightShader = new LinearGradient(
                rightOffset,
                0,
                getMeasuredWidth(),
                0,
                Color.TRANSPARENT,
                edgeEffectColor,
                Shader.TileMode.CLAMP);

        rightPaint = new Paint();
        rightPaint.setShader(rightShader);

        leftRect = new RectF(0, 0, absEdgeEffectWidth, getMeasuredHeight());
        rightRect = new RectF(rightOffset, 0, getMeasuredWidth(), getMeasuredHeight());

        textPaint.getTextBounds(text.toString(), 0, text.length(), textBounds);

        int viewheight = getMeasuredHeight();
        topOffset = (int) (viewheight / 2 - ((textPaint.descent() + textPaint.ascent()) / 2));
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public CharSequence getText() {
        return text;
    }

    public void setText(CharSequence text) {

        if (text == null)
            text = "";

        this.text = text;
        animationRunning = false;
        requestLayout();
    }

    public void setTextColor(int color) {
        textColor = color;
        renewPaint();
        invalidate();
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        renewPaint();
        animationRunning = false;
        requestLayout();
    }


    public void setEdgeEffectColorRes(int coloRes) {
        setEdgeEffectColor(ContextCompat.getColor(getContext(), coloRes));
    }

    public void setEdgeEffectColor(int edgeEffectColor) {
        this.edgeEffectColor = edgeEffectColor;

        renewPaint();

        if (paused)
            invalidate();
    }

    public boolean isEdgeEffectEnabled() {
        return edgeEffectEnabled;
    }

    public void setEdgeEffectEnabled(boolean edgeEffectEnabled) {
        this.edgeEffectEnabled = edgeEffectEnabled;

        if (paused)
            invalidate();
    }

    public void setCustomTypeface(Typeface customTypeface) {
        this.customTypeface = customTypeface;
    }

}
package com.example.simple.simpleandroidpractice.loading;

import android.animation.AnimatorSet;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;

import com.example.simple.simpleandroidpractice.R;

/**
 * 描述：
 * author：LiaoYangSen
 * email：lyssenlove@sina.com
 * create：2018/9/26
 */
public class LoadingView extends View {

    private static final String TAG = "LoadingView";

    private float mRadius;
    private float mArcWidth;
    private float mLineWidth;
    private float mLineStartY;
    private float mLineEndY;
    private Paint mBackGroundCiclePaint;
    private Paint mLinePaint;
    private Paint mPointPaint;
    private ValueAnimator mVerticalAnimator;

    private boolean isInitValue;
    private float mLineLength;
    private ValueAnimator mHideArrowAnimator;
    private float mFormatRadius;
    private AnimatorSet mAnimatorSet;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {


        mBackGroundCiclePaint = new Paint();
        mBackGroundCiclePaint.setColor(getResources().getColor(R.color.color_4594de));
        mBackGroundCiclePaint.setAntiAlias(true);
        mBackGroundCiclePaint.setStyle(Paint.Style.STROKE);
        mArcWidth = getResources().getDimension(R.dimen.dp_2);
        mBackGroundCiclePaint.setStrokeWidth(mArcWidth);

        mLinePaint = new Paint();
        mLinePaint.setColor(getResources().getColor(R.color.color_ffffff));
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLineWidth = getResources().getDimension(R.dimen.dp_2);
        mLinePaint.setStrokeWidth(mLineWidth);

        mPointPaint = new Paint();
        mPointPaint.setColor(getResources().getColor(R.color.colorAccent));
        mLinePaint.setAntiAlias(true);

        mVerticalAnimator = new ValueAnimator();
        mVerticalAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mVerticalAnimator.setEvaluator(new TypeEvaluator() {
            @Override
            public Object evaluate(float fraction, Object startValue, Object endValue) {
                return null;
            }
        });
        mVerticalAnimator.setDuration(100);
        mVerticalAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mVerticalAnimator.setRepeatCount(1);
        mVerticalAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLineStartY = (float) animation.getAnimatedValue();
                mLineEndY = mLineStartY + mLineLength;
                invalidate();
            }
        });

        mHideArrowAnimator = new ValueAnimator();
        mHideArrowAnimator.setDuration(100);
        mHideArrowAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PropertyValuesHolder[] values = animation.getValues();
//                values[0].
//                animation.get
//                Float[] verticalAnimatedValues = (Float[]) animation.getAnimatedValue();
//                mLineStartY = verticalAnimatedValues[0];
//                mLineEndY = verticalAnimatedValues[1];
//                invalidate();
            }
        });

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!isInitValue){
            isInitValue = true;
            mRadius = getWidth() / 2;
            mFormatRadius = mRadius - mArcWidth/2;
            mLineLength = getHeight()/2;
            mLineStartY = mRadius - mLineLength /2;
            mLineEndY = mLineStartY + mLineLength;
        }
        drawBackgroundCircle(canvas);
        drawArrow(canvas);
    }

    private void drawBackgroundCircle(Canvas canvas) {
        float circleLeft = getWidth() / 2 - mFormatRadius;
        float circleTop = getHeight() / 2 - mFormatRadius;
        float circleRight = getWidth() / 2 + mFormatRadius;
        float circleBottom = getHeight() / 2 + mFormatRadius;
        RectF cicleRectF = new RectF(circleLeft, circleTop, circleRight, circleBottom);
        canvas.drawArc(cicleRectF, 0, 360, false, mBackGroundCiclePaint);
    }

    private void drawArrow(Canvas canvas) {
        float lineStartX = mRadius;
        float lineStartY = mLineStartY;
        float lineEndX = lineStartX;
        float lineEndY = mLineEndY;
        canvas.drawLine(lineStartX, lineStartY, lineEndX, lineEndY, mLinePaint);

//        float leftLineStartX = mRadius - mRadius/2;
//        float leftLineStartY = mLineStartY + mRadius/2;
//        float leftLineEndX = lineEndX;
//        float leftLineEndY = lineEndY;
//        canvas.drawLine(leftLineStartX, leftLineStartY, leftLineEndX, leftLineEndY, mLinePaint);
//
//        float RightLineStartX = mRadius + mRadius/2;
//        float RightLineStartY = mLineStartY + mRadius/2;
//        float RightLineEndX = lineEndX;
//        float RightLineEndY = lineEndY;
//        canvas.drawLine(RightLineStartX, RightLineStartY, RightLineEndX, RightLineEndY, mLinePaint);
    }

    public void start(){
        if(null != mAnimatorSet && mAnimatorSet.isRunning()){
            mAnimatorSet.end();
        }
        mVerticalAnimator.setFloatValues(mLineStartY, mLineStartY *1.2f);
        mHideArrowAnimator.setFloatValues(mLineStartY, 0, mLineEndY, 0);

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playSequentially(mVerticalAnimator, mHideArrowAnimator);
        mAnimatorSet.start();
    }

}

package com.example.simple.simpleandroidpractice.loading;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.example.simple.simpleandroidpractice.R;

/**
 * 描述：
 * author：LiaoYangSen
 * email：lyssenlove@sina.com
 * create：2018/9/26
 */
public class LoadingView extends View {

    private static final String TAG = "LoadingView";

    private float mMaxValue = 100;

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
    private boolean isDrawPercentCircle;
    private boolean isFinishAnimator;
    private float mLineLength;
    private ValueAnimator mHideArrowAnimator;
    private float mFormatRadius;
    private AnimatorSet mAnimatorSet;
    private float mLeftLineStartX;
    private float mLeftLineStartY;
    private float mRightLineStartX;
    private float mRightLineStartY;
    private float mLeftLineEndX;
    private float mLeftLineEndY;
    private float mRightLineEndX;
    private float mRightLineEndY;
    private float mLeftLineInitX;
    private float mLeftLineInitY;
    private float mRightLineInitX;
    private float mRightLineInitY;
    private float mPercentAngle;

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

        mAnimatorSet = new AnimatorSet();

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
                mLeftLineStartY = mLineStartY + mRadius/2;
                mLeftLineEndY = mLineEndY;
                mRightLineStartY = mLineStartY + mRadius/2;
                mRightLineEndY = mLineEndY;

                invalidate();
            }
        });

        mHideArrowAnimator = new ValueAnimator();
        mHideArrowAnimator.setDuration(1000);
        mHideArrowAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLineStartY = (float) animation.getAnimatedValue();
                mLineEndY = mLineLength *(1 - animation.getAnimatedFraction());
                mLeftLineStartX = mLeftLineInitX + animation.getAnimatedFraction()*mRadius/2;
                mLeftLineStartY = mLeftLineInitY + animation.getAnimatedFraction()*mRadius/2;
                mRightLineStartX = mRightLineInitX - animation.getAnimatedFraction()*mRadius/2;
                mRightLineStartY = mRightLineInitY + animation.getAnimatedFraction()*mRadius/2;

                invalidate();
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
            mLineLength = mRadius;
            mLineStartY = mRadius - mLineLength /2;
            mLineEndY = mLineStartY + mLineLength;
            mLeftLineStartX = mRadius - mRadius/2;
            mLeftLineStartY = mLineStartY + mRadius/2;
            mLeftLineEndX = mRadius;
            mLeftLineEndY = mLineEndY;
            mRightLineStartX = mRadius + mRadius/2;
            mRightLineStartY = mLineStartY + mRadius/2;
            mRightLineEndX = mRadius;
            mRightLineEndY = mLineEndY;

            mLeftLineInitX = mLeftLineStartX;
            mLeftLineInitY = mLeftLineStartY;
            mRightLineInitX = mRightLineStartX;
            mRightLineInitY = mRightLineStartY;

        }
        drawBackgroundCircle(canvas);

        if(isDrawPercentCircle){
            drawPercentCircle(canvas);
        } else {
            drawArrow(canvas);
        }
    }

    private void drawBackgroundCircle(Canvas canvas) {
        mBackGroundCiclePaint.setColor(getResources().getColor(R.color.color_4594de));

        RectF cicleRectF = initCircleRectF();
        canvas.drawArc(cicleRectF, 0, 360, false, mBackGroundCiclePaint);
    }

    @NonNull
    private RectF initCircleRectF() {
        float circleLeft = getWidth() / 2 - mFormatRadius;
        float circleTop = getHeight() / 2 - mFormatRadius;
        float circleRight = getWidth() / 2 + mFormatRadius;
        float circleBottom = getHeight() / 2 + mFormatRadius;
        return new RectF(circleLeft, circleTop, circleRight, circleBottom);
    }

    private void drawArrow(Canvas canvas) {
        canvas.drawLine(mRadius, mLineStartY, mRadius, mLineEndY, mLinePaint);
        canvas.drawLine(mLeftLineStartX, mLeftLineStartY, mLeftLineEndX, mLeftLineEndY, mLinePaint);
        canvas.drawLine(mRightLineStartX, mRightLineStartY, mRightLineEndX, mRightLineEndY, mLinePaint);
    }

    private void drawPercentCircle(Canvas canvas) {
        mBackGroundCiclePaint.setColor(getResources().getColor(R.color.color_ffffff));
        RectF cicleRectF = initCircleRectF();
        canvas.drawArc(cicleRectF, -90, mPercentAngle, false, mBackGroundCiclePaint);
    }


    public void start(){
        if(null != mAnimatorSet && mAnimatorSet.isRunning()){
            mAnimatorSet.end();
        }
        mVerticalAnimator.setFloatValues(mLineStartY, mLineStartY *1.2f);
        mHideArrowAnimator.setFloatValues(mLineStartY, 0);

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playSequentially(mVerticalAnimator, mHideArrowAnimator);
        mAnimatorSet.start();
    }

    public void progress(float value){
        if(mAnimatorSet.isRunning()){
            return;
        }

        if(!isFinishAnimator){
            isDrawPercentCircle = false;
            mVerticalAnimator.setFloatValues(mLineStartY, mLineStartY *1.2f);
            mHideArrowAnimator.setFloatValues(mLineStartY, 0);

            mAnimatorSet.playSequentially(mVerticalAnimator, mHideArrowAnimator);
            mAnimatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    isFinishAnimator = true;
                }
            });
            mAnimatorSet.start();
            return;
        }

        isDrawPercentCircle = true;
        if(value < 0){
            value = 0;
        }

        if(value > mMaxValue){
            value = mMaxValue;
        }
        float progress = value / mMaxValue;
        mPercentAngle = 360 * progress;
        invalidate();
    }

    public void setMaxValue(float maxValue) {
        mMaxValue = maxValue;
    }
}

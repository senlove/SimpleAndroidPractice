package com.example.simple.simpleandroidpractice.loading;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
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
    private boolean isDrawArrow = true;
    private boolean isDrawPercentCircle;
    private boolean isDrawPoint;
    private boolean isDrawSuccessTag;
    private boolean isNeedStartPoinAnimator;
    private boolean isFinishAnimator;
    private float mLineLength;
    private ValueAnimator mHideArrowAnimator;
    private ValueAnimator mPointAnimator;
    private ValueAnimator mSuccessLeftLineAnimator;
    private ValueAnimator mSuccessRightLineAnimator;
    private float mFormatRadius;
    private AnimatorSet mAnimatorSet;
    private AnimatorSet mPointSuccessAnimatorSet;
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
    private float mPointAngle = 270;
    private float mSuccessPointOneX;
    private float mSuccessPointOneY;
    private float mLeftPointArcCrossX;
    private float mLeftPointArcCrossY;
    private float mRightPointArcCrossX;
    private float mRightPointArcCrossY;
    private float mRightSuccessLineK;
    private float mRightSuccessLineB;
    private float mSuccessPointTwoX;
    private float mSuccessPointTwoY;
    private float mSuccessPointThreeX;
    private float mSuccessPointThreeY;
    private Path mSuccessPath;
    private float mArcPointX;
    private float mArcPointY;
    private float mLeftSuccessLineK;
    private float mLeftSuccessLineB;


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

        mSuccessPath = new Path();

        mAnimatorSet = new AnimatorSet();
        mPointSuccessAnimatorSet = new AnimatorSet();

        mVerticalAnimator = new ValueAnimator();
        mVerticalAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
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

        mPointAnimator = new ValueAnimator();
        mPointAnimator.setDuration(500);
        mPointAnimator.setFloatValues(mPointAngle, mPointAngle - 60);
        mPointAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPointAngle = (float) animation.getAnimatedValue();
                mArcPointX = mRadius + (float) (mRadius * Math.cos(mPointAngle*Math.PI/180));
                mArcPointY = mRadius + (float) (mRadius * Math.sin(mPointAngle*Math.PI/180));
                if(1 == animation.getAnimatedFraction()){
                    mSuccessPointOneX = mArcPointX;
                    mSuccessPointOneY = mArcPointY;
                }
                invalidate();
            }
        });

        mSuccessLeftLineAnimator = new ValueAnimator();
        mSuccessLeftLineAnimator.setDuration(500);
        mSuccessLeftLineAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mSuccessLeftLineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                isDrawPoint = false;
                isDrawSuccessTag = true;
                mSuccessPointOneX = (float) animation.getAnimatedValue();
                mSuccessPointOneY = mSuccessPointOneX*mLeftSuccessLineK + mLeftSuccessLineB;
                invalidate();
            }
        });
        mSuccessRightLineAnimator = new ValueAnimator();
        mSuccessRightLineAnimator.setDuration(500);
        mSuccessRightLineAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mSuccessRightLineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mSuccessPointThreeX = (float) animation.getAnimatedValue();
                mSuccessPointThreeY = mSuccessPointThreeX*mRightSuccessLineK + mRightSuccessLineB;
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

            //点初始位置
            mPointAngle = 270;

            //勾三点的位置
            //勾折点的位置
            float successLineCrossX = mRadius - getResources().getDimension(R.dimen.dp_12);
            float successLineCrossY = mRadius + getResources().getDimension(R.dimen.dp_20);

            //勾左边点与弧线交叉的位置
            mLeftPointArcCrossX = mRadius + (float) (mRadius * Math.cos(210*Math.PI/180));
            mLeftPointArcCrossY = mRadius + (float) (mRadius * Math.sin(210*Math.PI/180));
            mLeftSuccessLineK = (successLineCrossY - mLeftPointArcCrossY)/(successLineCrossX - mLeftPointArcCrossX);
            mLeftSuccessLineB = mLeftPointArcCrossY - mLeftSuccessLineK* mLeftPointArcCrossX;

            //勾右边点与弧线交叉的位置
            mRightPointArcCrossX = mRadius+(float) (mRadius * Math.cos(330*Math.PI/180));
            mRightPointArcCrossY = mRadius+(float) (mRadius * Math.sin(330*Math.PI/180));
            mRightSuccessLineK = (mRightPointArcCrossY - successLineCrossY)/(mRightPointArcCrossX - successLineCrossX);
            mRightSuccessLineB = mRightPointArcCrossY - mRightSuccessLineK*mRightPointArcCrossX;

            mSuccessPointOneX = mLeftPointArcCrossX;
            mSuccessPointOneY = mLeftPointArcCrossY;
            mSuccessPointTwoX = successLineCrossX;
            mSuccessPointTwoY = successLineCrossY;
            mSuccessPointThreeX = mRightPointArcCrossX - getResources().getDimension(R.dimen.dp_15);
            mSuccessPointThreeY = mRightSuccessLineK*mRightPointArcCrossX + mRightSuccessLineB;
        }
        drawBackgroundCircle(canvas);
        if(isDrawArrow){
            drawArrow(canvas);
        }

        if(isDrawPercentCircle){
            drawPercentCircle(canvas);
        }

        if(isNeedStartPoinAnimator){
            isNeedStartPoinAnimator = false;
            isDrawPercentCircle = false;
            isDrawPoint = true;

            mSuccessLeftLineAnimator.setFloatValues(mLeftPointArcCrossX, mLeftPointArcCrossX + getResources().getDimension(R.dimen.dp_25),
                    mLeftPointArcCrossX + getResources().getDimension(R.dimen.dp_15));
            mSuccessRightLineAnimator.setFloatValues(mRightPointArcCrossX - getResources().getDimension(R.dimen.dp_15), mRightPointArcCrossX,
                    mRightPointArcCrossX - getResources().getDimension(R.dimen.dp_10));
            mPointSuccessAnimatorSet. play(mSuccessLeftLineAnimator).with(mSuccessRightLineAnimator).after(mPointAnimator);
            mPointSuccessAnimatorSet.start();
        }

        if(isDrawPoint){
            drawPoint(canvas);
        }

        if(isDrawSuccessTag) {
            drawSuccessTag(canvas);
        }

//        drawFailedTag(canvas);
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


    private void drawPoint(Canvas canvas) {
        canvas.drawPoint(mArcPointX, mArcPointY, mLinePaint);
    }

    private void drawSuccessTag(Canvas canvas) {

        mSuccessPath.reset();
        mSuccessPath.moveTo(mSuccessPointOneX, mSuccessPointOneY);
        mSuccessPath.lineTo(mSuccessPointTwoX, mSuccessPointTwoY);
        mSuccessPath.lineTo(mSuccessPointThreeX, mSuccessPointThreeY);

        canvas.drawPath(mSuccessPath, mLinePaint);
    }

    private void drawFailedTag(Canvas canvas) {
        Path pathOne = new Path();
        Path pathTwo = new Path();
        float pointOneX = mRadius - getResources().getDimension(R.dimen.dp_25);
        float pointOneY = mRadius - getResources().getDimension(R.dimen.dp_25);
        float pointTwoX = mRadius + getResources().getDimension(R.dimen.dp_25);
        float pointTwoY = mRadius + getResources().getDimension(R.dimen.dp_25);
        float pointThreeX = mRadius + getResources().getDimension(R.dimen.dp_25);
        float pointThreeY = mRadius - getResources().getDimension(R.dimen.dp_25);
        float pointFourX = mRadius - getResources().getDimension(R.dimen.dp_25);
        float pointFourY = mRadius + getResources().getDimension(R.dimen.dp_25);

        pathOne.moveTo(pointOneX, pointOneY);
        pathOne.lineTo(pointTwoX, pointTwoY);
        pathTwo.moveTo(pointThreeX, pointThreeY);
        pathTwo.lineTo(pointFourX, pointFourY);
        canvas.drawPath(pathOne, mLinePaint);
        canvas.drawPath(pathTwo, mLinePaint);
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
        isDrawArrow = false;
        if(value < 0){
            value = 0;
        }

        if(value > mMaxValue){
            value = mMaxValue;
        }
        float progress = value / mMaxValue;
        mPercentAngle = 360 * progress;

        if(mMaxValue == value){
            isNeedStartPoinAnimator = true;
        }
        invalidate();
    }

    public void setMaxValue(float maxValue) {
        mMaxValue = maxValue;
    }
}

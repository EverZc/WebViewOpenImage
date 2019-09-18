package me.everzc.webviewimage;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.blankj.utilcode.util.LogUtils;

import com.nineoldandroids.view.ViewHelper;

import org.simple.eventbus.EventBus;

/**
 * Created by Laughing on 2017/7/4.
 * <p/>
 * 只有y轴运动才能放大缩小屏幕，往y轴方向 向上提是放大或者不变，往y轴方向 向下拉是缩小，或者缩小到极限
 */
public class NewScaleViewPager extends BaseAnimCloseViewPager {

    public static final int STATUS_NORMAL = 0;
    public static final int STATUS_MOVING = 1;
    public static final int STATUS_REBACK = 2;
    public static final String TAG = "ScaleViewPager";
    //最多可缩小比例
    public static final float MIN_SCALE_WEIGHT = 0.25f;
    public static final int REBACK_DURATION = 300;//ms
    public static final int DRAG_GAP_PX = 50;
    public static final int DRAG_GAP_PX_FU = -50;
    private int currentStatus = STATUS_NORMAL;
    float mDownX;
    float mDownY;

    public NewScaleViewPager(Context context) {
        super(context);
    }

    public NewScaleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                LogUtils.e("ScaleView dispatchTouchEvent ACTION_DOWN");
                mDownX = ev.getRawX();
                mDownY = ev.getRawY();
                LogUtils.e("mDownX : " + mDownX);
                LogUtils.e("mDownY : " + mDownY);

            case MotionEvent.ACTION_MOVE:
                LogUtils.e("ev.getPointerCount()" + ev.getPointerCount());
                if (ev.getPointerCount() >= 2) {
                    LogUtils.e("ev.getPointerCount() > =2  不处理给子View");
                    return false;
                    //如果还没结束缩放模式，但是第一个点抬起了，那么让第二个点和第三个点作为缩放控制点
                }
                LogUtils.e("ev.getRawY() : " + ev.getRawY());
                LogUtils.e("ev.getRawX() : " + ev.getRawX());
                //计算手指移动距离，大于0表示手指往屏幕下方移动
                int deltaY = (int) (ev.getRawY() - mDownY);
                int deltaX = (int) (ev.getRawX() - mDownX);
                LogUtils.e("Math.abs(deltaY) : " + Math.abs(deltaY));
                LogUtils.e("Math.abs(deltaX) : " + Math.abs(deltaX));
                if (Math.abs(deltaY) > Math.abs(deltaX) + 100) {
                    LogUtils.e("Y>X");
                    if (!MyConstant.bigbig) {
                        LogUtils.e("如果没有被放大直接拦截");
                        return true;
                    } else {
                        LogUtils.e("如果放大不拦截");
                        return super.onInterceptTouchEvent(ev);
                    }

                } else {
                    LogUtils.e("不用直接拦截");
                    return super.onInterceptTouchEvent(ev);
                }


        }

        return super.onInterceptTouchEvent(ev);

    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (currentStatus == STATUS_REBACK) {
            LogUtils.e("ScaleView  STATUS_REBACK");
            return false;

        }

        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                LogUtils.e("ScaleView  ACTION_DOWN");
                mDownX = ev.getRawX();
                mDownY = ev.getRawY();
                addIntoVelocity(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                addIntoVelocity(ev);
                //计算手指移动距离，大于0表示手指往屏幕下方移动
                int deltaY = (int) (ev.getRawY() - mDownY);
                if (currentPageStatus != SCROLL_STATE_DRAGGING &&
                        deltaY > DRAG_GAP_PX || currentStatus == STATUS_MOVING) {
                    //如果往下移动，或者目前状态是缩放移动状态，那么传入移动坐标，进行对ImageView的操作
                    LogUtils.e("ACTION_MOVE下", "下滑动");
                    if (!MyConstant.bigbig) {
                        setupMoving(ev.getRawX(), ev.getRawY());
                        EventBus.getDefault().post("", "framloadinggone");
                    }
                    return true;

                }
                if (currentPageStatus != SCROLL_STATE_DRAGGING &&
                        deltaY < DRAG_GAP_PX_FU) {
                    //如果往下移动，或者目前状态是缩放移动状态，那么传入移动坐标，进行对ImageView的操作
                    if (!MyConstant.bigbig) {
                        setupMoving(ev.getRawX(), ev.getRawY());
                        EventBus.getDefault().post("", "framloadinggone");
                    }
                }
                if (deltaY <= DRAG_GAP_PX && currentStatus != STATUS_MOVING)
                    return super.onTouchEvent(ev);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (currentStatus != STATUS_MOVING)
                    return super.onTouchEvent(ev);
                final float mUpX = ev.getRawX();//->mDownX
                final float mUpY = ev.getRawY();//->mDownY
                float vY = computeYVelocity();
                if (vY >= 1500 || Math.abs(mUpY - mDownY) > screenHeight / 4) {
                    //速度有一定快，或者移动位置超过屏幕一半，那么释放
                    if (iAnimClose != null)
                        iAnimClose.onPictureRelease(currentShowView);
                } else {
                    setupReback(mUpX, mUpY);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void setupReback(final float mUpX, final float mUpY) {
        currentStatus = STATUS_REBACK;
        if (mUpY != mDownY) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(mUpY, mDownY);
            valueAnimator.setDuration(REBACK_DURATION);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float mY = (float) animation.getAnimatedValue();
                    float percent = (mY - mDownY) / (mUpY - mDownY);
                    float mX = percent * (mUpX - mDownX) + mDownX;
                    setupMoving(mX, mY);
                    if (mY == mDownY) {
                        mDownY = 0;
                        mDownX = 0;
                        currentStatus = STATUS_NORMAL;
                    }
                }
            });
            valueAnimator.start();
        } else if (mUpX != mDownX) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(mUpX, mDownX);
            valueAnimator.setDuration(REBACK_DURATION);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float mX = (float) animation.getAnimatedValue();
                    float percent = (mX - mDownX) / (mUpX - mDownX);
                    float mY = percent * (mUpY - mDownY) + mDownY;
                    setupMoving(mX, mY);
                    if (mX == mDownX) {
                        mDownY = 0;
                        mDownX = 0;
                        currentStatus = STATUS_NORMAL;
                    }
                }
            });
            valueAnimator.start();
        } else if (iAnimClose != null)
            iAnimClose.onPictureClick();
    }


    private void setupMoving(float movingX, float movingY) {
        if (currentShowView == null)
            return;
        currentStatus = STATUS_MOVING;

        // float deltaX = movingX - mDownX;
        float deltaY = movingY - mDownY;
        float scale = 1f;
        float alphaPercent = 1f;
        if (deltaY > 0) {
            // scale = 1 - Math.abs(deltaY) / screenHeight;
            //这里是设置背景的透明度，我这是设置移动屏幕一半高度的距离就全透明了。
            alphaPercent = 1 - Math.abs(deltaY) / (screenHeight / 2);
        } else {
            // scale = 1 - Math.abs(deltaY) / screenHeight;
            //这里是设置背景的透明度，我这是设置移动屏幕一半高度的距离就全透明了。
            alphaPercent = 1 - Math.abs(deltaY) / (screenHeight / 2);
        }
        //ViewHelper.setTranslationX(currentShowView, deltaX);
        ViewHelper.setTranslationY(currentShowView, deltaY);
        setupScale(scale);
        setupBackground(alphaPercent);
    }

    private void setupScale(float scale) {
        scale = Math.min(Math.max(scale, MIN_SCALE_WEIGHT), 1);
        ViewHelper.setScaleX(currentShowView, scale);
        ViewHelper.setScaleY(currentShowView, scale);
    }
}

/**
 * 
 */
package com.etao.test.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import android.widget.EdgeEffect;
import android.widget.FrameLayout;
import android.widget.OverScroller;

/**
 * @author YadiYan 2015年7月31日
 *
 */
@SuppressLint("NewApi")
public class BounceView extends FrameLayout {
	/**
	 * 
	 */
	private static final String TAG = BounceView.class.getName();
	private int mMinVerticaFillinglVelocity;
	private int mMaxVerticalFillingVelocity;
	private OverScroller mScroller;
	private EdgeEffect mEdgeTop;
	private VelocityTracker mVelocity;
	
	private Paint mOverPaint;

	private Drawable mIcon;
	/**
	 * 默认的 反弹动画 持续时间
	 */
	private static final int DEFAULT_OVERSCROLL_DURATION = 600;// ms

	private int mFirstDownX;
	private int mLastDownY;
	private int mTouchSlop;

	private static Interpolator mBounceInterpolator = new Interpolator() {

		public float easeIn(float t, float b, float c, float d) {
			return -c * (float) Math.cos(t / d * (Math.PI / 2)) + c + b;
		}

		@Override
		public float getInterpolation(float input) {
			// if(input < 0.55)
			// return easeIn(input, 0, 1, 1);
			// else
			// return getInterpolationBounce(input);
			// 流体力学
			// return AnimateUtils.viscousFluid(input);

			// 反弹
			// return getBounceInterpolation(input);
			return getElasticInterpolation(input);
		}

		public float getElasticInterpolation(float t) {
			return elasticEaseOut(t, 0, 1, 1);
		}

		/**
		 * 松紧带插值器
		 * 
		 * @param t
		 *            time
		 * @param b
		 *            start
		 * @param c
		 *            end
		 * @param d
		 *            duration
		 * @return
		 */
		private float elasticEaseOut(float t, float b, float c, float d) {
			if (t == 0)
				return b;
			if ((t /= d) == 1)
				return b + c;
			float p = d * .4f;
			float a = c;
			float s = p / 4;
			return (float) (a * Math.pow(2, -10 * t) * Math.sin((t * d - s) * (2 * Math.PI) / p) + c + b);
		}
	};

	/**
	 * @param context
	 * @param attrs
	 */
	public BounceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		mScroller = new OverScroller(context, mBounceInterpolator);
		// 设置摩擦力
		mScroller.setFriction(0.005F);
		ViewConfiguration vc = ViewConfiguration.get(getContext());
		mTouchSlop = vc.getScaledTouchSlop();
		mMinVerticaFillinglVelocity = vc.getScaledMinimumFlingVelocity();
		mMaxVerticalFillingVelocity = vc.getScaledMaximumFlingVelocity();
		setOnClickListener(mClick);

		setWillNotDraw(false);

		mIcon = context.getResources().getDrawable(com.etao.test.R.drawable.ic_launcher);
		mIcon.setBounds(0, 0, 100, 100);
		//初始化 overscroll 时的渐变图像
		mEdgeTop = new EdgeEffect(getContext());
		
		mOverPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mOverPaint.setColor(Color.BLUE);
		mOverPaint.setStyle(Style.FILL);
	}

	private OnClickListener mClick = new OnClickListener() {
		@Override
		public void onClick(View v) {

		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#computeVerticalScrollOffset()
	 */
	@Override
	protected int computeVerticalScrollOffset() {
		// TODO Auto-generated method stub
		return super.computeVerticalScrollOffset();
	}

	/* (non-Javadoc)
	 * @see android.widget.FrameLayout#draw(android.graphics.Canvas)
	 */
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if (mEdgeTop != null) {
			int scrollY = getScrollY();
			if (!mEdgeTop.isFinished()) {

				final int restroeCount = canvas.save();
				final int mPaddingLeft = getPaddingLeft();
				final int width = getWidth() - mPaddingLeft - getPaddingRight();

				canvas.translate(mPaddingLeft, Math.min(0, scrollY));

				mEdgeTop.setSize(width, getHeight());

				if (mEdgeTop.draw(canvas)) {
					mIcon.draw(canvas);
					ViewCompat.postInvalidateOnAnimation(this);
				}

				canvas.restoreToCount(restroeCount);
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int scrollTop = getScrollY();
		if(scrollTop < 0){
			int saveCount = canvas.getSaveCount();
			canvas.save();
			canvas.translate(0, scrollTop);
			canvas.drawRect(0, 0, getWidth(), -scrollTop,  mOverPaint);
			
			
			canvas.restoreToCount(saveCount);
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#computeScroll()
	 */
	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			int oldScrollY = getScrollY();
			int oldScrollX = getScrollX();

			int scrollY = mScroller.getCurrY();
			int scrollX = mScroller.getCurrX();

			if (oldScrollY != scrollY)
				this.overScrollBy(scrollX - oldScrollX, scrollY - oldScrollY, oldScrollX, oldScrollY, 0,
						// scrollRange 
						/**
						 * |----- me -----| |---------- child ------------|
						 * |---- range ---|
						 */
						getScrollVerticalRange(), 0,
						// overscrollY 
						/**
						 * 
						 */
						getHeight() / 2, false);

			if (scrollY < 0)
				mEdgeTop.onAbsorb((int) mScroller.getCurrVelocity());

			ViewCompat.postInvalidateOnAnimation(this);
		} else {
			//
		}
		awakenScrollBars();
	}

	public void startScroll() {
		mScroller.startScroll(0, getScrollY(), 0, Math.abs(getScrollY()), 600);
		// mScroller.springBack(0, getScrollY(), 0, 0, 0, 0);
		ViewCompat.postInvalidateOnAnimation(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.FrameLayout#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
		ViewGroup.LayoutParams lp = child.getLayoutParams();

		int childWidthMeasureSpec;
		int childHeightMeasureSpec;

		int mPaddingLeft = getPaddingLeft();
		int mPaddingRight = getPaddingRight();
		childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec, mPaddingLeft + mPaddingRight, lp.width);

		childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

		child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
	}

	@Override
	protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
		final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

		int mPaddingLeft = getPaddingLeft();
		int mPaddingRight = getPaddingRight();
		final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec, mPaddingLeft + mPaddingRight + lp.leftMargin + lp.rightMargin + widthUsed, lp.width);
		final int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(lp.topMargin + lp.bottomMargin, MeasureSpec.UNSPECIFIED);

		child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
	}

	@SuppressWarnings({ "UnusedParameters" })

	/**
	 * 
	 */
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY,
			boolean isTouchEvent) {
		final int overScrollMode = getOverScrollMode();
		final boolean canScrollHorizontal = computeHorizontalScrollRange() > computeHorizontalScrollExtent();
		final boolean canScrollVertical = computeVerticalScrollRange() > computeVerticalScrollExtent();
		final boolean overScrollHorizontal = overScrollMode == OVER_SCROLL_ALWAYS || (overScrollMode == OVER_SCROLL_IF_CONTENT_SCROLLS && canScrollHorizontal);
		final boolean overScrollVertical = overScrollMode == OVER_SCROLL_ALWAYS || (overScrollMode == OVER_SCROLL_IF_CONTENT_SCROLLS && canScrollVertical);

		int newScrollX = scrollX + deltaX;
		if (!overScrollHorizontal) {
			maxOverScrollX = 0;
		}

		int newScrollY = scrollY + deltaY;
		if (!overScrollVertical) {
			maxOverScrollY = 0;
		}

		// Clamp values if at the limits and record
		final int left = -maxOverScrollX;
		final int right = maxOverScrollX + scrollRangeX;
		final int top = -maxOverScrollY;
		final int bottom = maxOverScrollY + scrollRangeY;

		boolean clampedX = false;
		if (newScrollX > right) {
			newScrollX = right;
			clampedX = true;
		} else if (newScrollX < left) {
			newScrollX = left;
			clampedX = true;
		}

		boolean clampedY = false;
		if (newScrollY > bottom) {
			if (!isTouchEvent)
				newScrollY = bottom;
			clampedY = true;
		} else if (newScrollY < top) {
			if (!isTouchEvent)
				newScrollY = top;
			clampedY = true;
		}

		onOverScrolled(newScrollX, newScrollY, clampedX, clampedY);

		return clampedX || clampedY;
	}

	/**
	 * 获取 纵向 可滚动的距离
	 */
	private int getScrollVerticalRange() {
		if (getChildCount() > 0) {
			return Math.max(0, getChildAt(0).getHeight() - (getHeight() - getPaddingBottom() - getPaddingTop()));
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onOverScrolled(int, int, boolean, boolean)
	 */
	@Override
	protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {

		if (!mScroller.isFinished()) {
			// final int oldX = getScrollX();
			// final int oldY = getScrollY();
			scrollTo(scrollX, scrollY);
			// invalidateParentIfNeeded();
			// onScrollChanged(mScrollX, mScrollY, oldX, oldY);
			// if (clampedY) {
			// mScroller.springBack(scrollX, scrollY, 0, 0, 30, 0);
			// }
		} else {
			super.scrollTo(scrollX, scrollY);
		}

		awakenScrollBars();

	}

	/**
	 * <p>
	 * The scroll range of a scroll view is the overall height of all of its
	 * children.
	 * </p>
	 */
	@Override
	protected int computeVerticalScrollRange() {
		final int count = getChildCount();
		final int contentHeight = getHeight() - getPaddingBottom() - getPaddingTop();
		if (count == 0) {
			return contentHeight;
		}

		int scrollRange = getChildAt(0).getBottom();
		final int scrollY = getScrollY();
		final int overscrollBottom = Math.max(0, scrollRange - contentHeight);
		if (scrollY < 0) {
			scrollRange -= scrollY;
		} else if (scrollY > overscrollBottom) {
			scrollRange += scrollY - overscrollBottom;
		}

		return scrollRange;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#computeHorizontalScrollExtent()
	 */
	@Override
	protected int computeHorizontalScrollExtent() {
		// TODO Auto-generated method stub

		return super.computeHorizontalScrollExtent();
	}

	public void setPostion() {
		scrollBy(0, -100);
	}

	private boolean mIsBeginDragged;

	// /**
	// * 返回可滚动的范围
	// *
	// * @return
	// */
	// private int getScrollRange() {
	//
	// }

	private void initVolicityTrackerIfNotExist() {
		if (mVelocity == null) {
			mVelocity = VelocityTracker.obtain();
		}
	}

	private void recycleVolicity() {
		if (mVelocity != null) {
			mVelocity.recycle();
			mVelocity = null;
		}
	}
	
	/* 当前 激活的 pointerID **/
	private int mActivePointerId;

	/*
	 * 
	 * 
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		initVolicityTrackerIfNotExist();
		int currentAction = event.getActionMasked();
		
		
		
		switch (currentAction) {
		case MotionEvent.ACTION_DOWN:
			mLastDownY = (int) event.getY();
			
            mActivePointerId = event.getPointerId(0);
			Log.i(TAG, "first action_down  actionIndex = " + event.getActionIndex());
			break;
		case MotionEvent.ACTION_MOVE: {
			
			int pointerIndex =	event.findPointerIndex(mActivePointerId);
			int downY = (int) event.getY(pointerIndex);
			int deltaY = mLastDownY - downY;
			if (!mIsBeginDragged && Math.abs(deltaY) > mTouchSlop) {
				mIsBeginDragged = true;

				final ViewParent parent = getParent();
				if (parent != null) {
					parent.requestDisallowInterceptTouchEvent(true);
				}

				// 如果是向上移动
				if (deltaY > 0) {
					deltaY -= mTouchSlop;
				} else {
					deltaY += mTouchSlop;
				}
			}

			if (mIsBeginDragged) {
//				Log.i(Tag, " touch move =" + downY);
				if(overScrollBy(0, deltaY, 0, getScrollY(), 0, getScrollVerticalRange(), 0, getHeight() / 2, false)){
					//如果 已经溢出边界，清除  velocity  
					mVelocity.clear();
				}
				mLastDownY = downY;
				
				final int pulledToY = getScrollY() + deltaY;
				if(pulledToY < 0){
					float edgePullRatio = (float)deltaY / getHeight();
					mEdgeTop.onPull(edgePullRatio);
					
					ViewCompat.postInvalidateOnAnimation(this);
				}
			}

		}
		
//			Log.i(TAG, " action move actionIndex = "+ event.getActionIndex() + "  ");
			break;

		case MotionEvent.ACTION_POINTER_DOWN:
			// 取出 action 的索引
			 
			int actIndex = event.getActionIndex();
			// 获取 pointerId
			mActivePointerId = event.getPointerId(actIndex);
			
			mLastDownY = (int)event.getY(actIndex);
			Log.i(TAG, " pointer down actionIndex = "+ event.getActionIndex() + "  ");
			break;
		case MotionEvent.ACTION_POINTER_UP:
			
			int upIndex = event.getActionIndex();
			int upId = event.getPointerId(upIndex);
			// 监听滑动的 手指离开屏幕
			if(upId == mActivePointerId)
			{
				int newIndex = upIndex == 0 ? 1 : 0;
				mActivePointerId = event.getPointerId(newIndex);
				mLastDownY = (int)event.getY(newIndex);
				
				if(mVelocity!=null)
					mVelocity = null;
			}
				Log.i(TAG, " pointer UP actionIndex = "+ event.getActionIndex() + "  "  + upId);
			break;
		case MotionEvent.ACTION_UP:
			if(mIsBeginDragged){
			mVelocity.computeCurrentVelocity(1000, mMaxVerticalFillingVelocity);
			final int velocity = (int)mVelocity.getYVelocity();
			int scrollY = getScrollY();
			//如果 滑动的速度，大于最小可滑动的最小速度
			if(Math.abs(velocity) > mMinVerticaFillinglVelocity){
				fling(-velocity);
			}else if (scrollY < 0 || scrollY > getScrollVerticalRange()) {
				int realScrollY = clamp(scrollY, getHeight(), getChildAt(0).getHeight());
				int realYpos = realScrollY - scrollY;
				mScroller.startScroll(0, scrollY, 0, realYpos, DEFAULT_OVERSCROLL_DURATION);
			}
			endDrag();
			}
			
			Log.i(TAG, " action UP actionIndex = "+ event.getActionIndex() + "  ");
			break;
		}
		
		if(mVelocity!=null)
			mVelocity.addMovement(event);
		return true;
	}

	private void fling(int velocity) {
		if (getChildCount() > 0) {
			int height = getHeight() - getPaddingTop() - getPaddingBottom();
			int bottom = getChildAt(0).getHeight();
			int scrollY = getScrollY();

			mScroller.fling(
					//起始滚动的 x 坐标
					0,
					//起始滚动的 y 坐标
					scrollY,
					// x 速度
					0,
					// y 速度
					velocity,
					// 最小 x ，最大 x
					0, 0,
					// 最小 y, 
					/**
					 * |------ me ------| 
					 * |--- child ----|
					 * |--最小为 0
					 */
					0,
					/**
					 * 最大 y
					 *  |------ me ------|
					 *  |----------child---------------| 
					 * 					 |--	maxY --|
					 */
					Math.max(0, bottom - height), 0, height / 4);

			postInvalidateOnAnimation();
		}
	}

	private void endDrag() {
		//释放顶部特效
		if (mEdgeTop != null) {
			mEdgeTop.onRelease();
		}
		mIsBeginDragged = false;
		// mScroller.springBack(0, getScrollY(), 0, 0, 0, 0);
		ViewCompat.postInvalidateOnAnimation(this);
	}

	/**
	 * 
	 * @param n
	 * @param my
	 * @param child
	 * @return
	 */
	private static int clamp(int n, int my, int child) {
		if (my >= child || n < 0) {
			/* my >= child is this case:
			 *                    |--------------- me ---------------|
			 *     |------ child ------|
			 * or
			 *     |--------------- me ---------------|
			 *            |------ child ------|
			 * or
			 *     |--------------- me ---------------|
			 *                                  |------ child ------|
			 *
			 * n < 0 is this case:
			 *     |------ me ------|
			 *                    |-------- child --------|
			 *     |-- mScrollX --|
			 */
			return 0;
		}
		if ((my + n) > child) {
			/* this case:
			 *                    |------ me ------|
			 *     |------ child ------|
			 *     |-- mScrollX --|
			 */
			return child - my;
		}
		return n;
	}
}

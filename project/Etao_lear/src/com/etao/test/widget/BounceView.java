/**
 * 
 */
package com.etao.test.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.OverScroller;
import android.widget.Scroller;

/**
 * @author YadiYan 2015年7月31日
 *
 */
@SuppressLint("NewApi")
public class BounceView extends FrameLayout {

	private OverScroller mScroller;
	
	private static Interpolator mBounceInterpolator = new BounceInterpolator();

	/**
	 * @param context
	 * @param attrs
	 */
	public BounceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initScroller(context);
		
	}

	private void initScroller(Context context) {
		mScroller = new OverScroller(context, mBounceInterpolator);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#computeScroll()
	 */
	@Override
	public void computeScroll() {
		if(mScroller.computeScrollOffset()){
			int scrollY = mScroller.getCurrY();
			
			scrollTo(0, scrollY);
			ViewCompat.postInvalidateOnAnimation(this);
		}
		
	}

	public void startScroll() {
//		mScroller.startScroll(0, getScrollY(), 0, Math.abs(getScrollY()));
		mScroller.springBack(0, getScrollY(), 0, 0,  30, getScrollY()-100);
		invalidate();
	}
	/* (non-Javadoc)
	 * @see android.view.View#onOverScrolled(int, int, boolean, boolean)
	 */
	@Override
	protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
		super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
		
		System.out.println("overscroll  "+ scrollY);
	}

	/* (non-Javadoc)
	 * @see android.view.View#computeVerticalScrollRange()
	 */
	@Override
	protected int computeVerticalScrollRange() {
		return super.computeVerticalScrollRange();
	}
	/* (non-Javadoc)
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

}

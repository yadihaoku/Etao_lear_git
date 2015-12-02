package com.etao.test.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * �������� �ؼ�
    * @ClassName: VerticalPager
    * @author YanYadi
    * @date 2015��12��2��
    *
 */
public class VerticalPager extends ViewGroup {
	private int mChildHeight;
	public VerticalPager(Context context) {
		this(context, null);
	}
	public VerticalPager(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	public VerticalPager(Context context, AttributeSet attrs, int style) {
		super(context, attrs, style);
	}

	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		doVerticalLayout();
	}
	
	private int getChildHeight(){
		return mChildHeight;
	}
	public void toScreen(int index){
		scrollTo(0, (mChildHeight * index));
	}
	
	private void doVerticalLayout(){
		int childCount = getChildCount();
		mChildHeight = getHeight() - getPaddingBottom() - getPaddingTop();
		int maxHeight = childCount * (getHeight() - getPaddingBottom() - getPaddingTop());
		
		int mPaddingLeft = getPaddingLeft();
		int mPaddingRight = getPaddingRight();
		int mPaddingTop = getPaddingTop();
		int mPaddingBottom = getPaddingBottom();
		
		int mWidth = getWidth();
		for(int i=0; i < childCount; i ++){
			View child = getChildAt(i);
			child.layout(mPaddingLeft, mPaddingTop + (i * mChildHeight), mWidth-mPaddingRight, mPaddingTop + ((i + 1) * mChildHeight));
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		measureChildren( widthMeasureSpec, heightMeasureSpec);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

}

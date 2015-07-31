/**
 * 
 */
package com.etao.test;

import com.etao.test.widget.BounceView;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

/**
 * @author YadiYan 2015年7月31日
 *
 */
public class ScrollerActivity extends Activity implements OnClickListener {

	private BounceView mBounce;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.bound_layout);

		findViewById(R.id.btn_reset).setOnClickListener(this);
		findViewById(R.id.btn_scroll).setOnClickListener(this);

		mBounce = (BounceView) findViewById(R.id.bv_container);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_reset:
			mBounce.startScroll();
			break;
		case R.id.btn_scroll:
			mBounce.setPostion();
			break;
		}
	}

}

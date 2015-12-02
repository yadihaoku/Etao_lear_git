package com.etao.test.widget;

import com.etao.test.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class VerticalPagerActivity extends Activity implements OnClickListener{

	VerticalPager vp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_vertical_pager);
		vp = (VerticalPager) findViewById(R.id.verticalPager1);
		findViewById(R.id.tv_to_next_screen).setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		vp.toScreen(1);
	}
}

/**
 * 
 */
package com.etao.test;

import java.lang.Thread.UncaughtExceptionHandler;

import com.etao.app.UncaughtHandler;
import com.etao.test.widget.BounceView;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

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

		//		setContentView(R.layout.sys_scrollview);
		setContentView(R.layout.bound_layout);
		MobclickAgent.updateOnlineConfig(this);
		
		UncaughtHandler.init(this.getApplicationContext());

		findViewById(R.id.btn_reset).setOnClickListener(this);
		findViewById(R.id.btn_scroll).setOnClickListener(this);

		mBounce = (BounceView) findViewById(R.id.bv_container);
		
		findViewById(R.id.btn_clk).setOnClickListener(this);
		
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
//		MobclickAgent.onResume(this);
	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
//		MobclickAgent.onPause(this);
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
		case R.id.btn_clk:
			System.out.println(((ImageView)v).getDrawable() );
			break;
		}
	}
	
	public static String getDeviceInfo(Context context) {
	    try{
	      org.json.JSONObject json = new org.json.JSONObject();
	      android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
	          .getSystemService(Context.TELEPHONY_SERVICE);

	      String device_id = tm.getDeviceId();

	      android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

	      String mac = wifi.getConnectionInfo().getMacAddress();
	      json.put("mac", mac);

	      if( TextUtils.isEmpty(device_id) ){
	        device_id = mac;
	      }

	      if( TextUtils.isEmpty(device_id) ){
	        device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
	      }

	      json.put("device_id", device_id);

	      return json.toString();
	    }catch(Exception e){
	      e.printStackTrace();
	    }
	  return null;
	}

}

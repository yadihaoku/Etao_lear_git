/**
 * 
 */
package com.etao.app;

import java.lang.Thread.UncaughtExceptionHandler;

import com.etao.test.ReportErrorActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;
import android.widget.Toast;

/**
 * @author YadiYan 2015年8月11日
 *
 */
public class UncaughtHandler implements UncaughtExceptionHandler {

	private Context mContext;

	private HandlerThread mSubThread;
	private Handler mSubThreadHandler;

	private UncaughtExceptionHandler mDefaultExcetpionHandler;

	/**
	 * 
	 */
	public UncaughtHandler(Context context) {
		this.mContext = context;
		//		Thread.currentThread().setUncaughtExceptionHandler(this);
		mDefaultExcetpionHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	public static final void init(Context context) {
		new UncaughtHandler(context);
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handlerException(ex) && mDefaultExcetpionHandler != null) {
			mDefaultExcetpionHandler.uncaughtException(thread, ex);
		} else {
			Process.killProcess(Process.myPid());
		}
	}

	private boolean handlerException(Throwable ex) {

		//弹出 toast
		toast();
		//打开 error report activity
		showErrorReport();
		return true;
	}

	/**
	 * 打开异常信息收集 report
	 */
	private void showErrorReport() {
		Intent toReport = new Intent(mContext, ReportErrorActivity.class);
		toReport.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(toReport);
	}

	/**
	 * 弹出程序 异常的 toast
	 */
	private void toast() {

		mSubThread = new HandlerThread("error_handler_thread", Process.THREAD_PRIORITY_DISPLAY);
		mSubThread.start();

		mSubThreadHandler = new Handler(mSubThread.getLooper());

		mSubThreadHandler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(mContext, "抱歉！程序出现异常.即将退出！", Toast.LENGTH_SHORT).show();
			}
		});

		try {
			//休眠2秒, 让子线程有足够的时间弹出 toast
			Thread.sleep(1000);

			//释放子线程
			mSubThread.quit();
			
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}

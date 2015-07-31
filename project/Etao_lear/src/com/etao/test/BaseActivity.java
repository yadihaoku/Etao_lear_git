package com.etao.test;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class BaseActivity extends Activity {

	/**
	 * 代码参考
	 * http://www.fampennings.nl/maarten/android/09keyboard/index.htm
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.custom_keyboard);

		Keyboard mKeyboard = new Keyboard(this, R.xml.custom_keyboard);

//		ViewGroup
		// Lookup the KeyboardView
		KeyboardView mKeyboardView = (KeyboardView) findViewById(R.id.keyboard);
		// Attach the keyboard to the view
		mKeyboardView.setKeyboard(mKeyboard);
		// Do not show the preview balloons
		mKeyboardView.setPreviewEnabled(false);

		mKeyboardView.setOnKeyboardActionListener(mKeyListener);

		// Hide the standard keyboard initially
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		
		EditText edit=(EditText)findViewById(R.id.editText1);  
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
        imm.hideSoftInputFromWindow(edit.getWindowToken(),0); 
        hideKeyPad(edit);
	}
	/**
	 * 在 editText 获取焦点时，不弹出 软键盘
	 * @param mVerifyCodeET
	 */
	 private void hideKeyPad(EditText mVerifyCodeET) {
	        if (android.os.Build.VERSION.SDK_INT <= 10) {
	            mVerifyCodeET.setInputType(InputType.TYPE_NULL);
	        } else {
	            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	            try {
	                Class<EditText> cls = EditText.class;
	                Method setShowSoftInputOnFocus;
	                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
	                setShowSoftInputOnFocus.setAccessible(true);
	                setShowSoftInputOnFocus.invoke(mVerifyCodeET, false);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    }
	private OnKeyboardActionListener mKeyListener = new OnKeyboardActionListener() {

		@Override
		public void swipeUp() {

		}

		@Override
		public void swipeRight() {

		}

		@Override
		public void swipeLeft() {
			// TODO Auto-generated method stub

		}

		@Override
		public void swipeDown() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onText(CharSequence text) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onRelease(int primaryCode) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPress(int primaryCode) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onKey(int primaryCode, int[] keyCodes) {
			View focusCurrent = BaseActivity.this.getWindow().getCurrentFocus();
			if (focusCurrent == null || focusCurrent.getClass() != EditText.class)
				return;
			EditText edittext = (EditText) focusCurrent;
			Editable editable = edittext.getText();
			int start = edittext.getSelectionStart();
			// Handle key

			editable.insert(start, Character.toString((char) primaryCode));
		}
	};

}

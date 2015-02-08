package com.example.webview;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	private boolean isBackPressed = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_smart_ola_splash);
		mHandler.sendEmptyMessageDelayed(0, 3000);
	}
	static class RunnableHandler extends Handler {
		private Runnable mRunnable;

		public RunnableHandler(Runnable runnable) {
			mRunnable = runnable;
		}

		@Override
		public void handleMessage(Message msg) {
			mRunnable.run();
		};
	}
	private RunnableHandler mHandler = new RunnableHandler(new Runnable() {
		@Override
		public void run() {
			if (!isBackPressed) {
				Intent home = new Intent(MainActivity.this, MapLocation.class);
				startActivity(home);
			}
			finish();
		}
	});

	

	
}

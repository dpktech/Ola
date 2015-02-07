package com.example.webview;




import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class MainActivity extends Activity implements OnCancelListener,
OnDismissListener{
	private WebView webView;
	private ProgressDialog progressBar; 
	private boolean isRecieveError;
	public static String web_url = "http://www.google.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = (WebView) findViewById(R.id.webView);

		// if (RuntimeValues.checkNetworkConnectivity(Web_view.this)) {
		// loadWebView();
		// } else {
		// finish();
		// }
		loadWebView();
	}

	/*
	 * loading FB fan page on webview
	 */
	private void loadWebView() {

		webView.setKeepScreenOn(true);
		webView.setWebViewClient(new AppWebViewClients());
		webView.getSettings().setJavaScriptEnabled(true);
		webView.clearCache(true);
		try {
			webView.loadUrl(web_url);
		} catch (Exception e) {
			Log.e("webView : ", "" + e);
		}
	}
    
@Override
public void onBackPressed() {
	// TODO Auto-generated method stub
	super.onBackPressed();
//	webView.setKeepScreenOn(false);
	finish();

}
@Override
protected void onRestart() {
	// TODO Auto-generated method stub
	super.onRestart();
	Log.d("Facebook_fanPage", "onRestart");
	if (isRecieveError) {
		// reconnect();
		isRecieveError = false;
	}
}

@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
	// Check if the key event was the Back button and if there's history
	if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
		webView.goBack();
		return true;
	}
	// If it wasn't the Back key or there's no web page history, bubble up
	// to the default
	// system behavior (probably exit the activity)
	return super.onKeyDown(keyCode, event);
}

/*
 * creating progress bar
 */
private ProgressDialog progress() {
	progressBar = ProgressDialog.show(this, "", "Loading...");
	progressBar.setOnCancelListener(this);
	progressBar.setOnDismissListener(this);
	return progressBar;
}

private class AppWebViewClients extends WebViewClient {

	private boolean loadingFinished = true;

	public AppWebViewClients() {
		progressBar = progress();
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		view.loadUrl(url);
		return true;
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);

		if (progressBar != null && !loadingFinished) {
			progressBar.dismiss();
			progressBar = null;
			loadingFinished = true;
		}
	}

	@Override
	public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {

		return true;

	}

	@Override
	public void onLoadResource(WebView view, String url) {
		super.onLoadResource(view, url);

	}

	@Override
	public void onPageStarted(android.webkit.WebView view,
			java.lang.String url, android.graphics.Bitmap favicon) {
		super.onPageStarted(view, url, favicon);
		if (progressBar == null) {
			progressBar = progress();
		}
		loadingFinished = false;
	}

	@Override
	public void onReceivedError(android.webkit.WebView view, int errorCode,
			java.lang.String description, java.lang.String failingUrl) {
		super.onReceivedError(view, errorCode, description, failingUrl);

		if (progressBar != null) {
			progressBar.dismiss();
			progressBar = null;
		}
		isRecieveError = true;
	}
}

@Override
public void onCancel(DialogInterface dialog) {
	if (progressBar != null) {
		progressBar.dismiss();
		progressBar = null;
	}
}

@Override
public void onDismiss(DialogInterface dialog) {
	if (progressBar != null) {
		progressBar.dismiss();
		progressBar = null;
	}
}



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

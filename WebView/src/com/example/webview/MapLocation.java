package com.example.webview;




import java.io.InputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.GeolocationPermissions;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.WebChromeClient;
import android.webkit.WebIconDatabase;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;


@SuppressLint("SetJavaScriptEnabled") public class MapLocation extends Activity implements OnCancelListener,
OnDismissListener,GeolocationPermissions.Callback{
	private WebView webView;
	private ProgressDialog progressBar; 
	private boolean isRecieveError;
	public static String web_url = "http://www.olacabs.com";
	private static AlertDialog alertDialog = null;
	
	LocationManager locationManager = null;
    Boolean isGpsEnabled = false;
    
    InputStream is = null;
    byte[] buffer = null;
    String data = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        webView = (WebView) findViewById(R.id.webView);
       final TextView mTextField = (TextView) findViewById(R.id.textView1);
        locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        
        Button btn = (Button) findViewById(R.id.button1);
        btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent home = new Intent(MapLocation.this, VoiceRecognition.class);
				startActivity(home);
				
			}
		});
        
        Button btn2 = (Button) findViewById(R.id.button2);
        btn2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new CountDownTimer(30000, 1000) {

				     public void onTick(long millisUntilFinished) {
				         mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
				     }

				     public void onFinish() {
				         mTextField.setText("done!");
				     }
				  }.start();
					
					
				
			}
		});

		// if (RuntimeValues.checkNetworkConnectivity(Web_view.this)) {
		// loadWebView();
		// } else {
		// finish();
		// }
        if(!isGpsEnabled){
        	Log.d("Main","isGps= "+isGpsEnabled);
			showAlert("Turn On Location Settings");
		}
		loadWebView();
	}

    @Override
    protected void onResume() {
    	//webView.loadUrl( "https://google-developers.appspot.com/maps/documentation/javascript/examples/full/control-custom" ); 
    	webView.loadDataWithBaseURL(web_url, data, "text/html", "UTF-8", null);
    	super.onResume();
    }
   
    public void showAlert(String title) {
		/*Dismissing the showing alert dialogs.*/
		
		AlertDialog.Builder	alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle(title);
		alertDialogBuilder
		.setCancelable(false)
		.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
			public void onClick(
					DialogInterface dialog,int id) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				MapLocation.this.startActivity(intent);
				dialog.cancel();
				
			}
		});
		alertDialog = alertDialogBuilder.create();
		alertDialog.show();
		
	}
	/*
	 * loading FB fan page on webview
	 */
	private void loadWebView() {

		webView.setKeepScreenOn(true);
		webView.setWebViewClient(new AppWebViewClients());
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.getSettings().setGeolocationEnabled(true);
		//GeolocationPermissions geo = new GeolocationPermissions();
		GeoClient geoClient = new GeoClient();
		webView.setWebChromeClient(geoClient);
		webView.addJavascriptInterface(new WebAppInterface(this),"Android");
		
		webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		if (Build.VERSION.SDK_INT >= 19) {
			   webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
			}       
			else {
			   webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
			}
		
		webView.setInitialScale(1);
		webView.getSettings().setBuiltInZoomControls(false);
		
		String origin ="";
		geoClient.onGeolocationPermissionsShowPrompt(origin, this);
		webView.clearCache(true);
		
		try {
			//String dataUrl = "file:///android_asset/index.html";
			is = getAssets().open("index.html");
			int size = is.available();

			 buffer = new byte[size];
			is.read(buffer);
			is.close();

			data = new String(buffer);
			webView.loadUrl("https://google-developers.appspot.com/maps/documentation/javascript/examples/full/control-custom");
			//webView.loadDataWithBaseURL(web_url, data, "text/html", "UTF-8", null);
		} catch (Exception e) {
			Log.e("webView : ", "" + e);
		}
	}
	
	final class GeoClient extends WebChromeClient{
		@Override
		public void onGeolocationPermissionsShowPrompt(String origin, Callback callback){
			super.onGeolocationPermissionsShowPrompt(origin, callback);
			callback.invoke(origin, true, false);
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

	@Override
	public void invoke(String origin, boolean allow, boolean retain) {
		// TODO Auto-generated method stub
		
	}
	
}


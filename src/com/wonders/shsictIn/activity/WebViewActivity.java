package com.wonders.shsictIn.activity;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.wonders.shsictIn.BuildConfig;
import com.wonders.shsictIn.R;
import com.wonders.shsictIn.service.ScheduleService;
import com.wonders.shsictIn.utils.ConfigUtil;
import com.wonders.shsictIn.utils.ScheduleUtil;
import com.wonders.shsictIn.utils.WebAppInterface;

public class WebViewActivity extends Activity {
	private final static int INTERVAL = 60;
	public static boolean isFavouriteUpdate = false;
	public final static String GOTO_Favourite = "gotoFavourite";
	protected int checkedItem = -1;
	protected String[] urls = { "", "" };
	private final String error_html = "<html><body style=\"backgroud:#F1F1F1; text-align:center\"><h4>网络连接失败，请连接网络后再试</h4></body></html>";
	protected WebView webview;
	//current url
	protected static String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (WebViewActivity.url == null)
			WebViewActivity.url = ConfigUtil.getCachedShsictURL(this) + "/Portal";

		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.activity_fullscreen);
		initWebview();
		ScheduleUtil.startSchedule(this, INTERVAL, ScheduleService.class, ScheduleService.ACTION);
		
		// 接收Intent传过来的数据  
		Intent intent = getIntent(); 
		Boolean gotoFavourite = intent.getBooleanExtra(GOTO_Favourite, false); // 接收Intent的数据  
		if(gotoFavourite){
			NotificationManager mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			mManager.cancel(0);
			isFavouriteUpdate = false;
			gotoFavourite();
		}
	}

	/**
	 * 跳转favourite界面,
	 * 用于配合notification
	 */
	private void gotoFavourite() {
		String favourite = ConfigUtil.getCachedShsictURL(this) + "/MechanicalError";
		url = favourite;
		webview.loadUrl(url);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//當前界面是首頁
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (webview.getUrl().toLowerCase(Locale.US).indexOf("portal") > -1) {

				new AlertDialog.Builder(this).setTitle("确认退出吗？").setIcon(android.R.drawable.ic_dialog_info).setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 点击“确认”后的操作,退出整個程序 
						// android.os.Process.killProcess(android.os.Process.myPid()); 
						finish();
					}
				}).setNegativeButton("返回", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 点击“返回”后的操作,这里不设置没有任何操作  
					}
				}).show();
			} else if (webview.getUrl().toLowerCase(Locale.US).indexOf("detail") > -1) {
				webview.goBack();
			} else {
				//否则返回首页id
				webview.loadUrl(ConfigUtil.getCachedShsictURL(WebViewActivity.this) + "/Portal");
			}
			return true;
		}

		return false;
	}

	/**
	 * 初始化webview
	 */
	@SuppressLint("SetJavaScriptEnabled")
	private void initWebview() {
		webview = (WebView) findViewById(R.id.webview);
		webview.getSettings().setSupportZoom(false);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setDefaultTextEncodingName("UTF-8");
		webview.requestFocus();
		//是否堵塞网络图片
		webview.getSettings().setBlockNetworkImage(false);
		//使用内部的滚动条
		webview.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
		webview.getSettings().setLoadWithOverviewMode(true);
		webview.setWebViewClient(new webViewClient());
		final Activity activity = WebViewActivity.this;
		webview.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				// Activities and WebViews measure progress with different
				// scales.
				// The progress meter will automatically disappear when we reach
				// 100%PREFS_NAME
				activity.setProgress(progress * 1000);
			}
		});
		webview.setWebViewClient(new WebViewClient() {
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//				webview.loadData(error_html, "text/html", "UTF-8");
				webview.loadDataWithBaseURL(null, error_html, "text/html", "UTF-8", null);
				//				if (errorCode == WebViewClient.ERROR_HOST_LOOKUP && description.equals("net::ERR_ADDRESS_UNREACHABLE")) {
			}
		});
		//暴露给javascript接口
		webview.addJavascriptInterface(new WebAppInterface(WebViewActivity.this), "Mobile");
		if(BuildConfig.DEBUG)
			Toast.makeText(activity, "Goto "+url, Toast.LENGTH_LONG).show();
		
		webview.loadUrl(url);
		//		setWebViewUrl(WebViewActivity.this);
	}

	class webViewClient extends WebViewClient {
		// 重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			// 如果不需要其他对点击链接事件的处理返回true，否则返回false
			return true;
		}
		
		@Override
		 public void onPageFinished(WebView view, String url) {
	            CookieManager cookieManager = CookieManager.getInstance();
	            String CookieStr = cookieManager.getCookie(url);
	            Log.e("ninja", "Cookies = " + CookieStr + " , url = " + url);
	            super.onPageFinished(view, url);
        }
	}

	/*
	 * actionBar's menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
	
	

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if(isFavouriteUpdate){
		MenuItem mi = menu.findItem(R.id.favourite_item);
		mi.setIcon(R.drawable.f1_2);
		}else{
			MenuItem mi = menu.findItem(R.id.favourite_item);
			mi.setIcon(R.drawable.f1);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onStart() {
		super.onStart();
		refreshWebView(this);
	}

	/*public class RadioDialogBuilder extends Builder{

		public RadioDialogBuilder(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Builder setSingleChoiceItems(CharSequence[] items, int checkedItem, OnClickListener listener) {
			// TODO Auto-generated method stub
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				checkedItem = which;
			}
			return this;
		}
		
	}*/

	public void refreshWebView(Activity activity) {
		if (url != null)
			webview.loadUrl(url);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.main_page_item:
			
			WebViewActivity.url = ConfigUtil.getCachedShsictURL(this) + "/Portal";
			if(BuildConfig.DEBUG)
				Toast.makeText(getApplicationContext(), "click main page, url "+ WebViewActivity.url, Toast.LENGTH_LONG).show();
			webview.loadUrl(WebViewActivity.url);
			break;
		case R.id.favourite_item:
			isFavouriteUpdate = false;
			WebViewActivity.url = ConfigUtil.getCachedShsictURL(this) + "/MechanicalError";
			if(BuildConfig.DEBUG)
				Toast.makeText(getApplicationContext(), "click main page, url "+ WebViewActivity.url, Toast.LENGTH_LONG).show();
			
			webview.loadUrl(WebViewActivity.url);
			break;
//		case R.id.system_notice_item:
//			WebViewActivity.url = ConfigUtil.cacheShsictURL(this) + "/notice.aspx";
//			webview.loadUrl(WebViewActivity.url);
//			break;
		case R.id.account_manage_item:
			WebViewActivity.url = ConfigUtil.getCachedShsictURL(this) + "/Login/Phone";
			webview.loadUrl(WebViewActivity.url);
			break;
		case R.id.setting_item:
			ConfigUtil.showDialog(this);
			return true;
		case R.id.about:
//			showAbout();
			Toast.makeText(getApplicationContext(), ConfigUtil.getUIDFromCookie(ConfigUtil.getCachedShsictURL(this)), Toast.LENGTH_SHORT).show();
//			playRingTone(getApplicationContext());
//			Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
//			vibrator.vibrate(400);
//			vibrator = null;
			break;
		default:
			WebViewActivity.url = ConfigUtil.getCachedShsictURL(this) + "/Portal";
			webview.loadUrl(WebViewActivity.url);
			break;
		}
		return true;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		WebViewActivity.url = webview.getUrl();
	}

	/**
	 * @param activity
	 * @return
	 */
	public AlertDialog showAbout() {
		Activity activity = this;
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(activity.getString(R.string.vertion_title));
		LayoutInflater inflater = activity.getLayoutInflater();
		View customer_layout = inflater.inflate(R.layout.about_view, null);
		// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
		builder.setView(customer_layout);

		// Set up the buttons
		builder.setPositiveButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
//		builder.setNegativeButton(activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.cancel();
//			}
//		});
		return builder.show();
	}
	
	public static void playRingTone(Context ctx){
//		Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//		Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		//alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
		Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
							Ringtone r = RingtoneManager.getRingtone(ctx, notification);
							r.play(); 
	}
	
	public void enableFavouriteUpdate(){
		isFavouriteUpdate = true;
		getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
	}
	
	
	public void disableFavouriteUpdate(){
		isFavouriteUpdate = false;
		getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
	}
	
}
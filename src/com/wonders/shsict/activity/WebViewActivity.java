package com.wonders.shsict.activity;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.wonders.shsict.R;
import com.wonders.shsict.utils.ConfigUtil;

public class WebViewActivity extends Activity {


	protected int checkedItem = -1;
	protected String [] urls = {"",""};
	
	protected WebView webview;
	protected String url ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.activity_fullscreen);
		initWebview();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//當前界面是首頁
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (webview.getUrl().toLowerCase(Locale.US).indexOf("portal.aspx") > -1) {

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
				//否则返回首页
				webview.loadUrl(ConfigUtil.getShsictServiceURLString(WebViewActivity.this) + "/Portal.aspx");
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
				Toast.makeText(activity, "Oh no! " + " failingUrl:" + failingUrl + description, Toast.LENGTH_SHORT).show();
			}
		});
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
	protected void onStart() {
		super.onStart();
//		url = ConfigUtil.getShsictServiceURLString(this) +  "/Portal.aspx";
//		refreshWebView(this);
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
	
	public void refreshWebView(Activity activity){
		if(url != null)
			webview.loadUrl(url);
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		 switch (item.getItemId()) {
	        case R.id.main_page_item:
	        	webview.loadUrl(ConfigUtil.getShsictServiceURLString(this) + "/Portal.aspx");
	        	break;
	        case R.id.seach_item:
	        	webview.loadUrl(ConfigUtil.getShsictServiceURLString(this) + "/favourite.aspx");
	        	break;
	        case R.id.system_notice_item:
	        	webview.loadUrl(ConfigUtil.getShsictServiceURLString(this) + "/notice.aspx");
	        	break;
	        case R.id.account_manage_item:
	        	webview.loadUrl(ConfigUtil.getShsictServiceURLString(this) + "/login.aspx");
	        	break;
	        case R.id.setting_item:
	        	ConfigUtil.showDialog(this);
				
				return true;
	        default:
	        	webview.loadUrl(ConfigUtil.getShsictServiceURLString(this) + "/Portal.aspx");
	        	break;
	    }
		 return true;
	}
}
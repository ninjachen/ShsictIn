package com.wonders.shsict;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class FullscreenActivity extends Activity {

	private WebView webview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.activity_fullscreen);
		initWebview();
	}

	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	  if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
		  webview.goBack();// 返回前一个页面
	   return true;
	  }
	  return super.onKeyDown(keyCode, event);
	 }
	 
	@SuppressLint("SdCardPath")
	private String getShsictServiceURLString() {
		String configPath = "/sdcard/Shsict.config";
		String ip = getIP(configPath);
		StringBuffer sb = new StringBuffer();
		sb.append("http://").append(ip);
		return sb.toString();
	}

	private String getIP(String path) {
		try {BufferedReader br = null;
		
			br = new BufferedReader(new FileReader(path));
			String ip = br.readLine();
			br.close();
			return ip;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "baidu.com";

	}

	/**
	 * 初始化webview
	 */
	@SuppressLint("SetJavaScriptEnabled")
	private void initWebview(){
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
		final Activity activity = this;
		webview.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				// Activities and WebViews measure progress with different
				// scales.
				// The progress meter will automatically disappear when we reach
				// 100%
				activity.setProgress(progress * 1000);
			}
		});
		webview.setWebViewClient(new WebViewClient() {
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(activity, "Oh no! " + description,
						Toast.LENGTH_SHORT).show();
			}
		});
		webview.loadUrl(getShsictServiceURLString());
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
}
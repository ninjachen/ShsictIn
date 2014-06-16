package com.wonders.shsictIn.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.wonders.shsictIn.R;


public class WebviewSectionFragment extends Fragment {

	private WebView webview;
	
	public static final String WEBVIEW_URL = "WEBVIEW_URL";
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_webview, container, false);
            Bundle args = getArguments();
            webview = ((WebView) rootView.findViewById(R.id.webview)); 
            initWebview(webview);
            webview.loadUrl(args.getString(WEBVIEW_URL));
            return rootView;
        }
	
	/**
	 * 初始化webview
	 */
	@SuppressLint("SetJavaScriptEnabled")
	private void initWebview(WebView webview){
		webview.getSettings().setSupportZoom(false);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.requestFocus();
		//是否堵塞网络图片
		webview.getSettings().setBlockNetworkImage(false);
		//使用内部的滚动条
		webview.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
		webview.getSettings().setLoadWithOverviewMode(true);
		webview.setWebViewClient(new MWebViewClient()); 
		webview.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				// Activities and WebViews measure progress with different
				// scales.
				// The progress meter will automatically disappear when we reach
				// 100%
				getActivity().setProgress(progress * 1000);
			}
		});
		
	}
	
	public class MWebViewClient extends WebViewClient {

		// 重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			// 如果不需要其他对点击链接事件的处理返回true，否则返回false
			return true;

		}
		
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			Toast.makeText(getActivity (), "Oh no! " + description,
					Toast.LENGTH_SHORT).show();
		}
	}
}

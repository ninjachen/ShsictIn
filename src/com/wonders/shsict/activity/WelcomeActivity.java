/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wonders.shsict.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.wonders.shsict.R;
import com.wonders.shsict.utils.ConfigUtil;

public class WelcomeActivity extends Activity implements OnClickListener {
	//	private AlertDialog alertDialog = null;
	//	private Thread loopThread;
	public static final int START_WITH_OUT_GO_TO_MAINPAGE = 777;
//	public static boolean first_create = true;
	private WebView webview;
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);
		ImageView iv = (ImageView)findViewById(R.id.welcomeView);
		iv.setOnClickListener(this);
		
//		Thread delayToMainPageThread = new Thread() {
//
//			@Override
//			public void run() {
//				try {
//					super.run();
//					sleep(1000); //Delay of 5 seconds
//					goToMainPage(WelcomeActivity.this);
//				} catch (Exception e) {
//					//sleep error
//					e.printStackTrace();
//				}
//			}
//		};
		
		webview = new WebView(WelcomeActivity.this);
		
		webview.setWebViewClient(new WebViewClient() {
			
			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			}

			
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				goToMainPage(WelcomeActivity.this);
			}
			
		});
		String url = ConfigUtil.cacheShsictURL(this);
		//假如是第一次进入
		if(url == null){
			url = getString(R.string.default_server_ip);
			ConfigUtil.storeUrl(getApplicationContext(), getString(R.string.default_server_ip));
		}
			url += "/Portal.aspx";
		webview.loadUrl(url);
		
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		String url = ConfigUtil.cacheShsictURL(this) + "/Portal.aspx";
		webview.loadUrl(url);
	}

	public void goToMainPage(Activity activity) {
		Intent i = new Intent(activity, WebViewActivity.class);
		activity.startActivity(i);
		activity.finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		}
}

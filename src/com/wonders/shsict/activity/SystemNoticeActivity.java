package com.wonders.shsict.activity;

import android.widget.Toast;

import com.wonders.shsict.utils.ConfigUtil;

public class SystemNoticeActivity extends WebViewActivity {
	protected String url = ConfigUtil.getShsictServiceURLString() + "/notice.aspx";

		@Override
		public void reLoad() {
			// TODO Auto-generated method stub
//			Toast.makeText(getApplicationContext(), "call in reload(), "+url, Toast.LENGTH_SHORT).show();;
			webview.loadUrl(url);
		}
		
	}
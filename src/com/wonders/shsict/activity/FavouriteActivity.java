package com.wonders.shsict.activity;

import com.wonders.shsict.utils.ConfigUtil;

public class FavouriteActivity extends WebViewActivity {
	protected String url = ConfigUtil.getShsictServiceURLString(this) + "/favourite.aspx";

//	@Override
//	public void reLoad() {
//		// TODO Auto-generated method stub
////		Toast.makeText(getApplicationContext(), "call in reload(), "+url, Toast.LENGTH_SHORT).show();;
//		webview.loadUrl(url);
//	}
	
}
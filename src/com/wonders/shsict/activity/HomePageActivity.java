package com.wonders.shsict.activity;

import android.content.Intent;
import android.view.MenuItem;

import com.wonders.shsict.R;
import com.wonders.shsict.utils.ConfigUtil;

public class HomePageActivity extends WebViewActivity {
	protected String url = ConfigUtil.getShsictServiceURLString() + "/Portal.aspx";
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
//		Intent i;
		 switch (item.getItemId()) {
	        case R.id.main_page_item:
	        	webview.loadUrl(ConfigUtil.getShsictServiceURLString() + "/Portal.aspx");
	        	break;
	        case R.id.seach_item:
	        	webview.loadUrl(ConfigUtil.getShsictServiceURLString() + "/favourite.aspx");
	        	break;
	        case R.id.system_notice_item:
	        	webview.loadUrl(ConfigUtil.getShsictServiceURLString() + "/notice.aspx");
	        	break;
	        case R.id.account_manage_item:
	        	webview.loadUrl(ConfigUtil.getShsictServiceURLString() + "/login.aspx");
	        	break;
	        default:
	        	webview.loadUrl(ConfigUtil.getShsictServiceURLString() + "/Portal.aspx");
	        	break;
	    }
//		 finish();
		 return true;
	}
}
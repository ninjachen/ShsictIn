package com.wonders.shsictIn.utils;

import android.content.Context;
import android.webkit.JavascriptInterface;

/**
 * 暴露给javascript的接口
 * @author rabbie
 *
 */
public class WebAppInterface {
	
	public static String user = null;
	
	Context mContext;

    /** Instantiate the interface and set the context */
    public WebAppInterface(Context c) {
        mContext = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void setUser(String toast) {
    	
    	WebAppInterface.user = toast;
    	
//        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }
}

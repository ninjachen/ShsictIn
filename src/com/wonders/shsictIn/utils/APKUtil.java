package com.wonders.shsictIn.utils;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class APKUtil {
	public static void installAPkFromFile(File file, Context ctx) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		ctx.startActivity(intent);
	}

	public static void installAPKFromUrl(String url, Context ctx) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		Uri data = Uri.parse(url);
		intent.setData(data);
//		intent.setClassName("com.android.browser","com.android.browser.BrowserActivity");
		ctx.startActivity(intent);
	}
}

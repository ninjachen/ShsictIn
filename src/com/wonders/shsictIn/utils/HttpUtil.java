package com.wonders.shsictIn.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.webkit.CookieManager;

public class HttpUtil {
	private Context context;
	private RequestQueue requestQueue;

	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	public static String getUIDFromCookie(String url) {
		String uid = null;

		try {
			CookieManager cm = CookieManager.getInstance();
			String cookies = cm.getCookie(url);
			String[] cookieArray = cookies.split(";");
			for (String cookie : cookieArray) {
				if (cookie.contains("uid")) {
					int indexOfValue = cookie.indexOf("=") + 1;
					uid = cookie.substring(indexOfValue);
					break;
				}
			}
			if (uid == null || uid.trim().equals(""))
				return null;

			return uid;
		} catch (Exception e) {
			Log.i("Ninja", "getCookieErr");
			return null;
		}
	}

	public static File download(String sUrl) {
		File extDir = Environment.getExternalStorageDirectory();
		File file = new File(extDir, "test.apk");
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		InputStream is = null;
		OutputStream os = null;
		try {
			URL url = new URL(sUrl);
			URLConnection conn = url.openConnection();
			conn.connect();
			// if (conn.getResponseCode() == 200) {
			is = conn.getInputStream();
			byte[] buffer = new byte[1024];
			int len;
			int offset;
			os = new FileOutputStream(file);
			while ((len = is.read(buffer)) != -1) {
				os.write(buffer, 0, len);
				// offset += len;
				// os.write(buffer, offset, len);
			}
			os.close();
			is.close();
			return file;
			// }
			// conn = null;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

//	private HttpUtil(){
//	}
//	
//	private HttpUtil(Context ctx){
//		this.context = ctx;
//	}
	
}

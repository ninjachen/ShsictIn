package com.wonders.shsict.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.wonders.shsict.R;

public class ConfigUtil {
	protected static final String PREFS_NAME = "ShsictSetting";
	protected static final String SHSICT_URL = "ShsictUrl";
	protected static final String SHSICT_URL_DEFAULT = "http://10.1.25.21";

	/**
	 * 
	 * @return eg http://1.1.1.1:8080
	 */
	public static String getShsictServiceURLString(Activity activity) {
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		String url = settings.getString(SHSICT_URL, "ERROR");
		if (url.equals("ERROR")) {
			return null;
		} else {
			return url;
		}
	}

	/*public static boolean initShsictServiceURLString(){
		String configPath = Environment.getExternalStorageDirectory().getPath()+"/Shsict.config";
		try {
			BufferedReader br = null;
			br = new BufferedReader(new FileReader(configPath));
			String ip = br.readLine();
			br.close();
			if(ip.trim().isEmpty()){
				return false;
			}else{
				defaultUrl =  "http://"+ip;
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	*/
	public static AlertDialog showDialog(final Activity activity) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(activity.getString(R.string.input_server_url));
		LayoutInflater inflater = activity.getLayoutInflater();
		View customer_layout = inflater.inflate(R.layout.setting_view, null);
		final EditText input = (EditText) customer_layout.findViewById(R.id.url_setting);
		// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
		builder.setView(customer_layout);

		// Set up the buttons
		builder.setPositiveButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String url = input.getText().toString();
				storeUrl(activity.getApplicationContext(), url);
				Toast.makeText(activity.getApplicationContext(), url + ", 设置完毕", Toast.LENGTH_SHORT).show();
			}
		});
		builder.setNegativeButton(activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		return builder.show();
	}

	public static void storeUrl(Context ctx, String url) {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(SHSICT_URL, url);
		editor.commit();
	}

	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
}

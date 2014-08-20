package com.wonders.shsictIn.task;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;

import com.wonders.shsictIn.utils.APKUtil;
import com.wonders.shsictIn.utils.ScheduleUtil;

public class CheckUpdateTask extends AsyncTask<Integer, Integer, String>{
	
	
	private final String versionUrl;
	private final int localVersion;
	private boolean isNeedToUpdate;
	private final Activity activity;
	private String APKUrl = null;

	public CheckUpdateTask(String versionUrl, Activity activity){
		this.activity = activity;
		this.versionUrl = versionUrl;
		this.localVersion = getVersionCode(activity);
	}
	//该方法并不运行在UI线程内，所以在方法内不能对UI当中的控件进行设置和修改
    //主要用于进行异步操作
    @Override
    protected String doInBackground(Integer... params) {
//    	super.
    	this.isNeedToUpdate = checkUpdate(versionUrl, localVersion);
    	Log.i("Ninja", "isNeedToUpdate: "+isNeedToUpdate);
    	return "";
    }

    //该方法运行在Ui线程内，可以对UI线程内的控件设置和修改其属性
    @Override
    protected void onPreExecute() {
    	super.onPreExecute();
    }

    //在doInBackground方法当中，每次调用publishProgrogress()方法之后，都会触发该方法
    @Override
    protected void onProgressUpdate(Integer... values) {
    	super.onProgressUpdate(values);
    }
    
    //在doInBackground方法执行结束后再运行，并且运行在UI线程当中
    //主要用于将异步操作任务执行的结果展示给用户
    @Override
    protected void onPostExecute(String result) {
//        异步操作执行结束
    	AlertDialog.Builder builder = new AlertDialog.Builder(activity).setTitle("检测到有更新是否更新？").setIcon(android.R.drawable.ic_dialog_info).setPositiveButton("更新", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				APKUtil.installAPKFromUrl(APKUrl, activity);
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
    	if(isNeedToUpdate)
    		builder.show();
    }
    
    /**
	 * 检查是否有更新
	 * @param jsonUrl
	 */
	public boolean checkUpdate(String jsonUrl,int localVersion) {
    	try {
		 URL aURL = new URL(jsonUrl);
		 URLConnection conn = aURL.openConnection();
    	 conn.connect();
    	 InputStream is = conn.getInputStream();
    	 String s = ScheduleUtil.inputStreamToString(is, "UTF-8");
    	 Log.i("isNeedUpdate-result: ", s);
    	 JSONObject obj = new JSONObject(s);
    	 String versionAndUrl =  (String) obj.get("IsNeedToUpadateResult");
    	 if(versionAndUrl.contains(",")){
    		 final String[] versionAndUrls = versionAndUrl.split(",");
    		 int newestVersion = Integer.valueOf(versionAndUrls[0]);
    		 boolean isNeedToUpdate = newestVersion > localVersion;
    		 if(isNeedToUpdate){
				this.APKUrl = versionAndUrls[1];
    		 }
    		 return isNeedToUpdate;
    	 }
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return false;
	}
	
	//获取版本号(内部识别号)
	public static int getVersionCode(Context context) {  
	    try {  
	        PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);  
	        return pi.versionCode;  
	    } catch (NameNotFoundException e) {  
	        e.printStackTrace();  
	        return Integer.MAX_VALUE;  
	    }  
	}  
}

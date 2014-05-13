package com.wonders.shsict.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.wonders.shsict.BuildConfig;

/**
 * 计划任务工具包
 * @author rabbie
 *
 */
public class ScheduleUtil {

	//context leak!!TODO fix it
	private static Map<String, Context>contexts = new HashMap<String, Context>();
	
//	public static boolean isShowNotiFication = false;
	public final static String favouriteUrl = ConfigUtil.SHSICT_URL + "/Service/MessagePushService.svc/uid/";

	//开启轮询服务(计划任务)  
	public static void startSchedule(Context context, int seconds, Class<?> cls, String action) {
		ScheduleUtil.contexts.put(action, context);
		
		//获取AlarmManager系统服务  
		AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

		//包装需要执行Service的Intent  
		Intent intent = new Intent(context, cls);
		intent.setAction(action);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		//触发服务的起始时间  
		long triggerAtTime = SystemClock.elapsedRealtime();

		//使用AlarmManger的setRepeating方法设置定期执行的时间间隔（seconds秒）和需要执行的Service  
		manager.setRepeating(AlarmManager.ELAPSED_REALTIME, triggerAtTime, seconds * 1000, pendingIntent);
	}

	//停止轮询服务(计划任务) 
	public static void stopSchedule(Class<?> cls, String action) {
		if(BuildConfig.DEBUG)
			Log.i("Ninja", "stopSchedule");
		
		Context context = contexts.get(action); 
		AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, cls);
		intent.setAction(action);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		//取消正在执行的服务  
		manager.cancel(pendingIntent);
	}

	public synchronized static int queryFavouriteChangeNum(String uid){
    	try {
		 URL aURL = new URL(favouriteUrl + uid);
		 URLConnection conn = aURL.openConnection();
    	 conn.connect();
    	 InputStream is = conn.getInputStream();
    	 String s = inputStreamToString(is, "UTF-8");
    	 if(BuildConfig.DEBUG){
    		 Log.i("tangningfeng-result: ", s);
    	 }
    	 JSONObject obj = new JSONObject(s);
    	 int num = (Integer) obj.get("GetMsgStatueByUIDResult");
    	 return num ;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
    }

	/**  
	 * 将InputStream转换成某种字符编码的String  
	 * @param in  
	 * @param encoding  
	 * @return  
	 * @throws Exception  
	 */
	public static String inputStreamToString(InputStream in, String encoding) throws Exception {
		final int BUFFER_SIZE = 4096;

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;
		while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
			outStream.write(data, 0, count);

		return new String(outStream.toByteArray(), encoding);
	}
}
